package io.mikupush

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileOutputStream
import kotlin.concurrent.thread

class ChunkAppendRequest(
    val id: String,
    val chunk: ByteArray
)

private val logger = LoggerFactory.getLogger("Upload")
val chunkChannel = Channel<ChunkAppendRequest>(100)

fun ChunkAppendRequest.appendChunk() {
    val file = File("data/$id")
    val directory = file.parentFile

    if (!directory.exists()) {
        logger.debug("data directory not exists, creating it")
        directory.mkdirs()
    }

    try {
        FileOutputStream(file, true).use { stream ->
            stream.write(chunk)
            logger.debug("chunk appended on file $id")
        }
    } catch (exception: IOException) {
        logger.warn("failed to append chunk on file $id", exception)
    }
}

fun CoroutineScope.chunkWorker() = launch(Dispatchers.IO) {
    for (chunk in chunkChannel) {
        chunk.appendChunk()
    }
}

private var workersStarted = false

fun startChunkAppenderWorkers() {
    if (workersStarted) return
    logger.debug("starting chunk appender workers")

    val job = CoroutineScope(Job()).launch {
        chunkWorker()
    }

    Runtime.getRuntime().addShutdownHook(thread(start = false) {
        job.cancel()
    })

    logger.debug("chunk appender workers started")
    workersStarted = true
}
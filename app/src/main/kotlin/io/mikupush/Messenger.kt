package io.mikupush

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.file.*
import java.util.*
import kotlin.concurrent.thread
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.exists

private val logger = LoggerFactory.getLogger("Messenger")
private val messagesDirectory = Path(System.getProperty("java.io.tmpdir"), "mikupush")
val messageFlow = MutableSharedFlow<String>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

fun listenToMessages() {
    if (!messagesDirectory.exists()) {
        messagesDirectory.createDirectories()
    }

    logger.debug("starting listening to incoming messages")
    val watchService = FileSystems.getDefault().newWatchService()
    messagesDirectory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE)

    val job = CoroutineScope(Job() + Dispatchers.IO).launch {
        while (isActive) {
            waitForFileEvent(watchService)
        }
    }

    Runtime.getRuntime().addShutdownHook(thread(start = false) {
        job.cancel()
    })

    logger.debug("listening to incoming messages")
}

private fun waitForFileEvent(watchService: WatchService) {
    try {
        val key = watchService.take()
        logger.debug("file watch event received: {}", key)

        for (event in key.pollEvents()) {
            val kind = event.kind()
            logger.debug("file watch event kind: {}", kind)

            if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                val path = (event as WatchEvent<Path>).context()
                logger.debug("received new message: {}", path)
                val file = Path(messagesDirectory.toString(), path.toString()).toFile()
                logger.debug("reading received new message: {}", file.path)
                val message = file.readText()

                if (!messageFlow.tryEmit(message)) {
                    logger.warn("failed to emit message: {}", file.path)
                }

                file.delete()
            }
        }

        key.reset()
    } catch (exception: IOException) {
        logger.warn("failed reading incoming message", exception)
    }
}

fun emitMessage(message: String) {
    Path(messagesDirectory.toString(), UUID.randomUUID().toString()).run {
        logger.debug("Writing file message at {}", this)
        val file = toFile()
        file.parentFile.mkdirs()
        file.writeText(message)
    }
}
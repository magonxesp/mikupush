package io.mikupush

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import io.ktor.client.request.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.apache.tika.Tika
import org.slf4j.LoggerFactory
import java.io.File
import java.util.*
import java.util.concurrent.atomic.AtomicLong
import kotlin.concurrent.fixedRateTimer
import kotlin.concurrent.thread


private val logger = LoggerFactory.getLogger("Upload")
private val uploadsInProgress = mutableListOf<String>()

suspend fun upload(filePath: String) {
    if (uploadsInProgress.contains(filePath)) {
        return
    }

    uploadsInProgress.add(filePath)
    val file = File(filePath)

    notificationFlow.emit(Notification(
        title = "Uploading file",
        message = "Uploading file ${file.name} please wait"
    ))

    if (!file.exists()) {
        error("File $filePath not exists")
    }

    val uuid = UUID.randomUUID().toString()
    val uploadState = file.createUploadState(uuid)
    val lastUploadedBytes = AtomicLong(0)
    val totalUploadedBytes = AtomicLong(0)

    val timer = fixedRateTimer(uuid, period = 1000) {
        val speedRate = totalUploadedBytes.get() - lastUploadedBytes.get()
        logger.debug("Speed rate for file $uuid is $speedRate B/s")
        lastUploadedBytes.set(totalUploadedBytes.get())
        uploadState.notifyBytesUploadedRate(speedRate)
    }

    uploadState.addToUploadsList()
    backendHttpClient.use { client ->
        val reader = file.inputStream().buffered(100 * 1000)
        val buffer = ByteArray(100 * 1000) // 100kb

        while (reader.read(buffer) != -1) {
            logger.debug("sending new chunk to file $uuid")
            client.post("/upload/$uuid/chunk") {
                setBody(buffer)
            }

            totalUploadedBytes.set(totalUploadedBytes.get() + buffer.size)
            val progress = totalUploadedBytes.get() / file.length().toFloat()
            logger.debug("updating upload progress of file $uuid: $progress")
            uploadState.updateProgress(progress)
        }
    }

    timer.cancel()
    uploadState.removeFromUploadsList()
    copyToClipboard("$baseUrl/$uuid")
    uploadsInProgress.remove(filePath)

    notificationFlow.emit(Notification(
        title = "File uploaded :D",
        message = "The file url copied to clipboard"
    ))
}

fun MutableStateFlow<UploadState>.updateProgress(progress: Float) {
    update { state ->
        state.copy(progress = progress)
    }
}

fun MutableStateFlow<UploadState>.notifyBytesUploadedRate(bytes: Long) {
    update { state ->
        state.copy(bytesUploadedRate = bytes)
    }
}

fun File.createUploadState(fileId: String): MutableStateFlow<UploadState> {
    val upload = UploadState(
        fileId = fileId,
        fileName = name,
        fileMimeType = Tika().detect(this),
        fileSizeBytes = length(),
    )

    return MutableStateFlow(upload)
}

class UploadRequestCommand : CliktCommand(name = "upload") {
    private val filePath: String by argument()

    override fun run() {
        logger.debug("Requesting file upload for $filePath")
        emitMessage(filePath)
    }
}

fun listenToUploadsRequests() {
    val job = CoroutineScope(Job()).launch {
        logger.debug("start listening to upload requests")

        messageFlow.collect { filePath ->
            logger.debug("Incoming upload request: $filePath")
            launch(Dispatchers.IO) { upload(filePath) }
        }
    }

    Runtime.getRuntime().addShutdownHook(thread(start = false) {
        job.cancel()
    })
}

package io.mikupush.upload

import io.ktor.client.request.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import io.mikupush.http.backendHttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.apache.tika.Tika
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicLong
import kotlin.concurrent.fixedRateTimer

class FileUploader(
    private val filesToUpload: ReceiveChannel<UploadRequest>,
    private val uploadsProgress: SendChannel<UploadProgress>,
    private val uploadsFinished: SendChannel<UploadFinished>
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private var isStarted = false

    fun startUploadingFiles(workers: Int = 1) = CoroutineScope(Job()).launch {
        if (isStarted) return@launch

        repeat(workers) { workerNumber ->
            logger.debug("Starting worker $workerNumber to listen to new pending files to upload")
            worker(filesToUpload)
        }

        isStarted = true
    }

    private fun CoroutineScope.worker(filesToUpload: ReceiveChannel<UploadRequest>) = launch(Dispatchers.IO) {
        for (file in filesToUpload) {
            upload(file)
        }
    }

    private suspend fun upload(request: UploadRequest) {
        logger.debug("processing upload request: {}", request)
        val file = request.path.toFile()
        val mimeType = Tika().detect(file)
        var state: UploadFinishState

        try {
            backendHttpClient.use { client ->
                client.post("/upload/${request.fileId}") {
                    setBody(UploadStream(request, uploadsProgress))
                }
            }

            state = UploadFinishState.SUCCESS
        } catch (exception: Exception) {
            logger.warn("failed to upload file ${file.path}", exception)
            state = UploadFinishState.FAILED
        }

        uploadsFinished.send(
            UploadFinished(
                fileId = request.fileId,
                path = request.path,
                fileName = file.name,
                fileSizeBytes = file.length(),
                fileMimeType = mimeType,
                state = state
            )
        )

        logger.debug("upload request finished: {}", request)
    }

    private class UploadStream(
        private val request: UploadRequest,
        private val uploadsProgress: SendChannel<UploadProgress>,
    ) : OutgoingContent.WriteChannelContent() {
        private val logger = LoggerFactory.getLogger(this::class.java)
        private val progress = MutableStateFlow(UploadProgress(request.fileId))
        private val lastUploadedBytes = AtomicLong(0)
        private val totalUploadedBytes = AtomicLong(0)
        private val file = request.path.toFile()

        override suspend fun writeTo(channel: ByteWriteChannel) {
            val job = notifyOnProgressUpdate()
            val timer = observeUploadSpeedRate()
            val reader = file.inputStream().buffered(100 * 1000)
            val buffer = ByteArray(100 * 1000) // 100kb

            while (reader.read(buffer) != -1) {
                logger.debug("sending new chunk to file {}", request.fileId)
                channel.writeByteArray(buffer)
                updateProgress(buffer.size)
            }

            job.cancel()
            timer.cancel()
        }

        private fun updateProgress(bytesUploaded: Int) {
            progress.update { state ->
                totalUploadedBytes.set(totalUploadedBytes.get() + bytesUploaded)
                val progress = totalUploadedBytes.get() / file.length().toFloat()

                logger.debug("updating upload progress of file {}: {}", request.fileId, progress)
                state.copy(
                    progress = progress,
                    totalBytesUploaded = totalUploadedBytes.get()
                )
            }
        }

        private fun observeUploadSpeedRate() = fixedRateTimer(request.fileId.toString(), period = 500) {
            val speedRate = totalUploadedBytes.get() - lastUploadedBytes.get()
            logger.debug("Speed rate for file {} is {} B/s", request.fileId, speedRate)
            lastUploadedBytes.set(totalUploadedBytes.get())
            progress.update { state -> state.copy(bytesRate = speedRate) }
        }

        private fun notifyOnProgressUpdate() = CoroutineScope(Job()).launch {
            progress.collect { progress ->
                uploadsProgress.trySend(progress)
            }
        }
    }
}
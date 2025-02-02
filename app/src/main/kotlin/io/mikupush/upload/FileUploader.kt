package io.mikupush.upload

import io.ktor.client.request.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import io.mikupush.http.backendHttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import org.apache.tika.Tika
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicLong
import kotlin.concurrent.fixedRateTimer

class FileUploader(private val workers: Int) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun CoroutineScope.uploadFiles(
        uploadRequestChannel: ReceiveChannel<UploadRequest>,
        uploadStateChannel: SendChannel<UploadState>
    ) = launch {
        repeat(workers) { workerNumber ->
            logger.debug("Starting worker $workerNumber to listen to new pending files to upload")
            for (fileToUpload in uploadRequestChannel) {
                doUpload(fileToUpload, uploadStateChannel)
            }
        }
    }

    private fun CoroutineScope.doUpload(
        uploadRequest: UploadRequest,
        progressChannel: SendChannel<UploadState>
    ) = launch(Dispatchers.IO) {
        logger.debug("processing upload request: {}", uploadRequest)
        var progress = uploadRequest.createState()
        progressChannel.trySend(progress)

        try {
            val uploadStream = UploadStream(uploadRequest, progress) { updated ->
                progress = updated
                progressChannel.trySend(progress)
            }

            backendHttpClient.use { client ->
                client.post("/upload/${uploadRequest.fileId}") {
                    setBody(uploadStream)
                }
            }

            progressChannel.trySend(progress.copy(finishState = UploadFinishState.SUCCESS))
        } catch (exception: Exception) {
            logger.warn("failed to upload file ${uploadRequest.path}", exception)
            progressChannel.trySend(progress.copy(finishState = UploadFinishState.FAILED))
        }

        logger.debug("upload request finished: {}", uploadRequest)
    }

    private fun UploadRequest.createState(): UploadState {
        val file = path.toFile()

        return UploadState(
            fileId = fileId,
            fileName = file.name,
            fileSizeBytes = file.length(),
            fileMimeType = Tika().detect(file),
        )
    }

    private class UploadStream(
        private val request: UploadRequest,
        private val progress: UploadState,
        private val onProgressUpdate: (UploadState) -> Unit,
    ) : OutgoingContent.WriteChannelContent() {
        private val logger = LoggerFactory.getLogger(this::class.java)
        private val lastUploadedBytes = AtomicLong(0)
        private val totalUploadedBytes = AtomicLong(0)
        private val file = request.path.toFile()

        override suspend fun writeTo(channel: ByteWriteChannel) {
            val timer = observeUploadSpeedRate()
            val reader = file.inputStream().buffered(100 * 1000)
            val buffer = ByteArray(100 * 1000) // 100kb

            while (reader.read(buffer) != -1) {
                logger.debug("sending new chunk to file {}", request.fileId)
                channel.writeByteArray(buffer)
                updateProgress(buffer.size)
            }

            timer.cancel()
        }

        private fun updateProgress(bytesUploaded: Int) {
            totalUploadedBytes.set(totalUploadedBytes.get() + bytesUploaded)
            logger.debug("updating upload progress of file {}: {}", request.fileId, progress)

            onProgressUpdate(
                progress.copy(
                    progress = totalUploadedBytes.get() / file.length().toFloat(),
                )
            )
        }

        private fun observeUploadSpeedRate() = fixedRateTimer(request.fileId.toString(), period = 500) {
            val speedRate = totalUploadedBytes.get() - lastUploadedBytes.get()
            logger.debug("Speed rate for file {} is {} B/s", request.fileId, speedRate)
            lastUploadedBytes.set(totalUploadedBytes.get())

            onProgressUpdate(
                progress.copy(bytesUploadedRate = speedRate)
            )
        }
    }
}
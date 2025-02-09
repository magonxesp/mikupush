package io.mikupush.upload

import io.ktor.client.request.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import io.mikupush.http.backendHttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.slf4j.LoggerFactory
import java.io.File
import java.util.*
import kotlin.concurrent.fixedRateTimer

class FileUploader {
    private val logger = LoggerFactory.getLogger(this::class.java)

    suspend fun upload(
        fileId: UUID,
        file: File,
        onProgress: (Float) -> Unit,
        onByteRate: (Long) -> Unit
    ) {
        logger.debug("processing upload request for file id: {}: {}", fileId, file.path)

        val streamWriter = createWriteStream(
            file = file,
            fileId = fileId,
            onProgress = onProgress,
            onByteRate = onByteRate
        )

        backendHttpClient.use { client ->
            client.post("/upload/$fileId") {
                setBody(streamWriter)
            }
        }

        logger.debug("upload request finished for file id: {}: {}", fileId, file.path)
    }

    private fun createWriteStream(
        file: File,
        fileId: UUID,
        onProgress: (Float) -> Unit,
        onByteRate: (Long) -> Unit
    ) = object : OutgoingContent.WriteChannelContent() {
        @get:Synchronized
        @set:Synchronized
        private var lastUploadedBytes = 0L
        @get:Synchronized
        @set:Synchronized
        private var totalUploadedBytes = 0L

        override suspend fun writeTo(channel: ByteWriteChannel) {
            val rateUpdater = updateByteRate()

            try {
                val reader = file.inputStream().buffered(100 * 1000)
                val buffer = ByteArray(100 * 1000) // 100kb

                while (reader.read(buffer) != -1) {
                    channel.writeByteArray(buffer)
                    updateProgress(buffer.size)
                }
            } finally {
                logger.debug("canceling bytes rate timer")
                rateUpdater.cancel()
            }
        }

        private fun updateProgress(bytes: Int) {
            totalUploadedBytes += bytes
            val progress = totalUploadedBytes / file.length().toFloat()
            logger.debug("updating upload progress of file {}: {}", fileId, progress)
            onProgress(progress)
        }

        private fun updateByteRate() = fixedRateTimer(period = 500) {
            val speedRate = totalUploadedBytes - lastUploadedBytes
            logger.debug("speed rate for file {} is {} B/s", fileId, speedRate)
            lastUploadedBytes = totalUploadedBytes
            onByteRate(speedRate)
        }
    }
}
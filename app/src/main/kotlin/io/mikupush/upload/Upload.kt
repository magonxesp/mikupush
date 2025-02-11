package io.mikupush.upload

import io.mikupush.serialization.UUIDSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import java.nio.file.Path
import java.util.*

data class Upload(
    val details: UploadDetails,
    val path: Path,
    val progress: Float = 0f,
    val bytesUploadedRate: Long = 0,
    val error: String = "",
    val finished: Boolean = false
) {
    fun isFinished() = finished && error.isBlank()
    fun isInProgress() = !finished
    fun isFinishedWithError() = finished && error.isNotBlank()
}

@Serializable
data class UploadDetails(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val fileName: String,
    val fileMimeType: String,
    val fileSizeBytes: Long,
    val uploadedAt: Instant,
)
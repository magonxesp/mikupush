package io.mikupush.upload

import io.mikupush.serialization.UUIDSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import java.util.*

data class Upload(
    val details: UploadDetails,
    val progress: Float = 0f,
    val bytesUploadedRate: Long = 0,
) {
    fun isFinished() = progress >= 1f
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
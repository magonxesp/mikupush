package io.mikupush.upload

import io.mikupush.serialization.UUIDSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class UploadDetails(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val fileName: String,
    val fileMimeType: String,
    val fileSizeBytes: Long,
    val uploadedAt: Instant
)

package io.mikupush.upload

import kotlinx.datetime.Instant
import java.util.*

data class Upload(
    val id: UUID,
    val fileName: String,
    val fileMimeType: String,
    val fileSizeBytes: Long,
    val uploadedAt: Instant
)
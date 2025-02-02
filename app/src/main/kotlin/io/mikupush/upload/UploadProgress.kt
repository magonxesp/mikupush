package io.mikupush.upload

import java.util.UUID

data class UploadProgress(
    val fileId: UUID,
    val progress: Float = 0f,
    val totalBytesUploaded: Long = 0,
    val bytesRate: Long = 0
)
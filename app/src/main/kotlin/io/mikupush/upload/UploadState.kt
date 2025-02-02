package io.mikupush.upload

import java.util.*

data class UploadState(
    val fileId: UUID,
    val fileName: String,
    val fileMimeType: String,
    val fileSizeBytes: Long,
    val progress: Float = 0f,
    val bytesUploadedRate: Long = 0,
    val finishState: UploadFinishState? = null
) {
    fun isFinished() = progress >= 1f
}
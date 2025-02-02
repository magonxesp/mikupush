package io.mikupush.upload

import java.nio.file.Path
import java.util.UUID

data class UploadFinished(
    val fileId: UUID,
    val path: Path,
    val fileName: String,
    val fileSizeBytes: Long,
    val fileMimeType: String,
    val state: UploadFinishState
)
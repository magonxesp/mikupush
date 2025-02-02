package io.mikupush.upload

import java.nio.file.Path
import java.util.UUID

data class UploadRequest(
    val fileId: UUID,
    val path: Path
)
package io.mikupush.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun FileIcon(mimeType: String) {
    val icon = when {
        mimeType.startsWith("image/") -> "file-image.svg"
        mimeType.startsWith("audio/") -> "file-audio.svg"
        mimeType.startsWith("video/") -> "file-video.svg"
        mimeType.startsWith("video/") -> "file-video.svg"
        arrayOf(
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "application/vnd.oasis.opendocument.spreadsheet"
        ).contains(mimeType) -> "file-excel.svg"
        arrayOf(
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/rtf",
            "application/vnd.oasis.opendocument.tex"
        ).contains(mimeType) -> "file-word.svg"
        arrayOf(
            "application/vnd.ms-powerpoint",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation",
            "application/vnd.oasis.opendocument.presentation"
        ).contains(mimeType) -> "file-powerpoint.svg"
        mimeType == "application/pdf" -> "file-pdf.svg"
        mimeType == "text/plain" -> "file-lines.svg"
        arrayOf(
            "text/x-java-source",
            "text/x-c",
            "text/x-c++",
            "text/x-csharp",
            "text/x-python",
            "application/javascript",
            "application/x-typescript",
            "text/html",
            "text/css",
            "application/x-httpd-php",
            "text/x-ruby",
            "text/x-perl",
            "application/x-sh",
            "application/x-powershell",
            "application/sql",
            "text/x-go",
            "text/x-rust",
            "text/x-swift",
            "text/x-kotlin",
            "text/x-lua",
            "text/x-r-source",
            "text/x-matlab"
        ).contains(mimeType) -> "file-code.svg"
        arrayOf(
            "application/zip",
            "application/x-7z-compressed",
            "application/x-rar-compressed",
            "application/gzip",
            "application/x-tar",
            "application/x-bzip2",
            "application/x-xz",
            "application/x-lzma",
            "application/x-apple-diskimage"
        ).contains(mimeType) -> "file-zipper.svg"
        else -> "file.svg"
    }

    Image(
        painter = painterResource("/assets/icons/$icon"),
        contentDescription = "File $mimeType",
        contentScale = ContentScale.Fit
    )
}
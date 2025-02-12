package io.mikupush.ui.compose

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun UploadingListItem(
    fileName: String,
    fileMimeType: String,
    progress: Float,
    uploadSpeedBytes: Long,
    onCancel: () -> Unit = { }
) {
    ListItem(
        leadingContent = {
            FileIcon(mimeType = fileMimeType)
        },
        headlineContent = {
            Text(
                text = fileName,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        },
        supportingContent = {
            BytesSpeedFormatted(bytes = uploadSpeedBytes)
        },
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(
                    progress = progress,
                    modifier = Modifier.size(24.dp)
                )
                IconButton(onClick = onCancel) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Cancel upload of $fileName",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(start = 5.dp)
                    )
                }
            }
        }
    )
}

@Composable
@Preview
fun UploadProgressPreview() {
    UploadingListItem(
        fileName = "alya.jpg",
        fileMimeType = "image/jpeg",
        progress = 0.8f,
        uploadSpeedBytes = Random.nextLong(1, 1024 * 1024 * 1024) // until 1gb
    )
}

@Composable
fun BytesSpeedFormatted(bytes: Long, modifier: Modifier = Modifier) {
    val kb = bytes / 1024.0
    val mb = kb / 1024.0
    val gb = mb / 1024.0
    val speed = when {
        gb >= 1 -> String.format("%.2f GB/s", gb)
        mb >= 1 -> String.format("%.2f MB/s", mb)
        kb >= 1 -> String.format("%.2f KB/s", kb)
        else -> "$bytes B/s"
    }

    Text(
        text = speed,
        modifier = modifier.fillMaxWidth(),
        style = MaterialTheme.typography.bodyMedium
    )
}

package io.mikupush.ui.compose

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.mikupush.upload.Upload
import kotlin.random.Random

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UploadingListItem(
    fileName: String,
    fileMimeType: String,
    progress: Float,
    uploadSpeedBytes: Long,
    onCancel: () -> Unit = { }
) {
    ListItem(
        icon = {
            FileIcon(
                mimeType = fileMimeType,
                modifier = Modifier.height(40.dp)
            )
        },
        text = {
            Text(
                text = fileName,
                style = MaterialTheme.typography.subtitle2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        },
        secondaryText = {
            BytesSpeedFormatted(bytes = uploadSpeedBytes)
        },
        trailing = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(
                    progress = progress,
                    modifier = Modifier.size(24.dp)
                )
                IconButton(onClick = onCancel) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Cancel upload of $fileName",
                        tint = MaterialTheme.colors.error,
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
        style = MaterialTheme.typography.body2
    )
}

package io.mikupush.ui.compose

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.mikupush.upload.UploadState
import java.util.*
import kotlin.random.Random

@Composable
fun UploadsList(
    items: List<UploadState>,
    onCancel: (fileId: UUID) -> Unit = { }
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        itemsIndexed(items) { index, item ->
            UploadingListItem(
                upload = item,
                onCancel = { onCancel(item.fileId) }
            )

            if (index < items.size - 1) {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun UploadsListPreview() {
    UploadsList(items = (0..5).map {
        UploadState(
            fileId = UUID.randomUUID(),
            fileName = "alya.jpeg",
            fileMimeType = "image/jpeg",
            fileSizeBytes = 1024,
        )
    })
}

@Composable
fun UploadingListItem(
    upload: UploadState,
    onCancel: () -> Unit = { }
) {
    UploadProgress(
        fileName = upload.fileName,
        fileMimeType = upload.fileMimeType,
        uploadSpeedBytes = upload.bytesUploadedRate,
        progress = upload.progress,
        onCancel = onCancel
    )
}

@Composable
fun UploadProgress(
    fileName: String,
    fileMimeType: String,
    progress: Float,
    uploadSpeedBytes: Long,
    modifier: Modifier = Modifier,
    onCancel: () -> Unit = { }
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FileIcon(
            mimeType = fileMimeType,
            modifier = Modifier.height(45.dp)
                .padding(end = 13.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = fileName,
                style = MaterialTheme.typography.subtitle2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 10.dp)
            )
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 3.dp)
            )
            BytesSpeedFormatted(bytes = uploadSpeedBytes)
        }
        IconButton(onClick = onCancel) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Cancel upload of $fileName",
                tint = MaterialTheme.colors.error
            )
        }
    }
}

@Composable
@Preview
fun UploadProgressPreview() {
    UploadProgress(
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

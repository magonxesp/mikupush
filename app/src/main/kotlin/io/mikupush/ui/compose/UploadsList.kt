package io.mikupush.ui.compose

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.mikupush.upload.Upload
import io.mikupush.upload.UploadDetails
import kotlinx.datetime.Clock
import java.util.*

@Composable
fun UploadsList(
    items: List<Upload>,
    onCancel: (fileId: UUID) -> Unit = { }
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        val inProgress = items.filter { !it.isFinished() }
        val finished = items.filter { it.isFinished() }

        itemsIndexed(inProgress) { index, upload ->
            if (index == 0) {
                Text(
                    text = "Uploading (${inProgress.size})",
                    style = MaterialTheme.typography.subtitle1
                )
            }

            if (index < items.size && index != 0) {
                Divider(modifier = Modifier.fillMaxWidth())
            }

            UploadingListItem(
                fileName = upload.details.fileName,
                fileMimeType = upload.details.fileMimeType,
                uploadSpeedBytes = upload.bytesUploadedRate,
                progress = upload.progress,
                onCancel = { onCancel(upload.details.id) }
            )
        }

        itemsIndexed(finished) { index, upload ->
            if (index == 0) {
                Text(
                    text = "Uploads (${finished.size})",
                    style = MaterialTheme.typography.subtitle1
                )
            }

            if (index < items.size && index != 0) {
                Divider(modifier = Modifier.fillMaxWidth())
            }

            UploadListItem(
                fileName = upload.details.fileName,
                fileMimeType = upload.details.fileMimeType,
                uploadedAt = upload.details.uploadedAt,
            )
        }
    }
}

@Preview
@Composable
fun UploadsListPreview() {
    UploadsList(items = (0..5).map {
        Upload(
            details = UploadDetails(
                id = UUID.randomUUID(),
                fileName = "alya.jpeg",
                fileMimeType = "image/jpeg",
                fileSizeBytes = 1024,
                uploadedAt = Clock.System.now()
            ),
            progress = if (it % 2 == 0) 0.8f else 1f
        )
    })
}
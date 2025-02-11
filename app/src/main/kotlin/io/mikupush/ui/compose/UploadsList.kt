package io.mikupush.ui.compose

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.mikupush.upload.Upload
import io.mikupush.upload.UploadDetails
import kotlinx.datetime.Clock
import java.nio.file.Path
import java.util.*
import kotlin.io.path.Path

@Composable
fun UploadsList(
    items: List<Upload>,
    onCancel: (fileId: UUID) -> Unit = { },
    onGetLink: (fileId: UUID) -> Unit = { },
    onShowInExplorer: (path: Path) -> Unit = { },
    onRetry: (path: Path, fileId: UUID) -> Unit = { _, _ -> },
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        val inProgress = items.filter { it.isInProgress() }
        val finished = items.filter { it.isFinished() }
        val finishedWithError = items.filter { it.isFinishedWithError() }

        itemsIndexed(inProgress) { index, upload ->
            if (index == 0) {
                Text(
                    text = "Uploading (${inProgress.size})",
                    style = MaterialTheme.typography.titleMedium
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

        itemsIndexed(finishedWithError) { index, upload ->
            if (index == 0) {
                Text(
                    text = "Failed (${finishedWithError.size})",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            if (index < items.size && index != 0) {
                Divider(modifier = Modifier.fillMaxWidth())
            }

            UploadFailedListItem(
                fileName = upload.details.fileName,
                fileMimeType = upload.details.fileMimeType,
                error = upload.error,
                onRetry = { onRetry(upload.path, upload.details.id) }
            )
        }

        itemsIndexed(finished) { index, upload ->
            if (index == 0) {
                Text(
                    text = "Uploads (${finished.size})",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            if (index < items.size && index != 0) {
                Divider(modifier = Modifier.fillMaxWidth())
            }

            UploadListItem(
                fileName = upload.details.fileName,
                fileMimeType = upload.details.fileMimeType,
                uploadedAt = upload.details.uploadedAt,
                onOpenInFileExplorer = { onShowInExplorer(upload.path) },
                onGetLink = { onGetLink(upload.details.id) }
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
            progress = if (it % 2 == 0) 0.8f else 1f,
            path = Path("")
        )
    })
}
package io.mikupush.ui.compose

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.mikupush.upload.UploadState
import io.mikupush.upload.Upload
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

@Composable
fun UploadedList(items: List<Upload>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        itemsIndexed(items) { index, item ->
            UploadListItem(item)

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
fun UploadListPreview() {
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
fun UploadListItem(upload: Upload) {
    UploadedFile(
        fileName = upload.fileName,
        fileMimeType = upload.fileMimeType,
        uploadedAt = upload.uploadedAt,
    )
}

@Composable
fun UploadedFile(
    fileName: String,
    fileMimeType: String,
    uploadedAt: Instant,
    modifier: Modifier = Modifier,
    onGetLink: () -> Unit = {},
    onOpenInFileExplorer: () -> Unit = {},
) {
    Row(
        modifier = modifier,
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
                    .padding(bottom = 3.dp)
            )
            Text(
                text = uploadedAt.toLocalDateTime(TimeZone.currentSystemDefault())
                    .toJavaLocalDateTime()
                    .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)),
                style = MaterialTheme.typography.body2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
        IconButton(
            onClick = onOpenInFileExplorer,
            modifier = Modifier.padding(start = 5.dp)
        ) {
            Icon(
                painter = painterResource("/assets/icons/open_in_explorer.svg"),
                contentDescription = "Open $fileName in file explorer"
            )
        }
        IconButton(onClick = onGetLink) {
            Icon(
                painter = painterResource("/assets/icons/link.svg"),
                contentDescription = "Copy link of $fileName"
            )
        }
    }
}

@Composable
@Preview
fun UploadedFilePreview() {
    UploadedFile(
        fileName = "alya.jpg",
        fileMimeType = "image/jpeg",
        uploadedAt = Clock.System.now()
    )
}

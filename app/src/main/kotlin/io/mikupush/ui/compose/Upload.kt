package io.mikupush.ui.compose

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.datetime.*
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun UploadListItem(
    fileName: String,
    fileMimeType: String,
    uploadedAt: Instant,
    onGetLink: () -> Unit = {},
    onOpenInFileExplorer: () -> Unit = {},
    onDelete: () -> Unit = {},
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
            Text(
                text = uploadedAt.toLocalDateTime(TimeZone.currentSystemDefault())
                    .toJavaLocalDateTime()
                    .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        },
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onOpenInFileExplorer,
                    modifier = Modifier.padding(start = 5.dp)
                ) {
                    Icon(
                        painter = painterResource("/assets/icons/folder.svg"),
                        contentDescription = "Open $fileName in file explorer"
                    )
                }
                IconButton(onClick = onGetLink) {
                    Icon(
                        painter = painterResource("/assets/icons/link.svg"),
                        contentDescription = "Copy link of $fileName"
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete $fileName",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    )
}

@Composable
@Preview
fun UploadedFilePreview() {
    UploadListItem(
        fileName = "alya.jpg",
        fileMimeType = "image/jpeg",
        uploadedAt = Clock.System.now()
    )
}

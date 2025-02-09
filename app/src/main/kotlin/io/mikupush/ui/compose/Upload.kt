package io.mikupush.ui.compose

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.mikupush.upload.Upload
import io.mikupush.upload.UploadDetails
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UploadListItem(
    fileName: String,
    fileMimeType: String,
    uploadedAt: Instant,
    onGetLink: () -> Unit = {},
    onOpenInFileExplorer: () -> Unit = {},
) {
    ListItem(
        icon = {
            FileIcon(mimeType = fileMimeType)
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
            Text(
                text = uploadedAt.toLocalDateTime(TimeZone.currentSystemDefault())
                    .toJavaLocalDateTime()
                    .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)),
                style = MaterialTheme.typography.body2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        },
        trailing = {
            Row(verticalAlignment = Alignment.CenterVertically) {
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

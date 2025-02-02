package io.mikupush.ui.compose

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun UploadedFile(
    fileName: String,
    filePath: String,
    fileMimeType: String,
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
            modifier = Modifier.height(60.dp)
                .padding(end = 13.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = fileName,
                style = MaterialTheme.typography.subtitle2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 5.dp)
            )
            Text(
                text = filePath,
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
        filePath = "C:\\Users\\example\\Images\\alya.jpg",
        fileMimeType = "image/jpeg",
    )
}

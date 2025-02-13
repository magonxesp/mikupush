package io.mikupush.ui.compose

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.*
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.net.URI
import java.nio.file.Path
import kotlin.io.path.isDirectory

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun UploadDropInput(onUpload: (List<Path>) -> Unit = {}) {
    var isFileChooserOpen by remember { mutableStateOf(false) }
    var dropStarted by remember { mutableStateOf(false) }
    val dragAndDropTarget = remember {
        object: DragAndDropTarget {
            override fun onStarted(event: DragAndDropEvent) {
                if (event.isFileEvent()) {
                    dropStarted = true
                }
            }

            override fun onEnded(event: DragAndDropEvent) {
                dropStarted = false
            }

            override fun onDrop(event: DragAndDropEvent): Boolean {
                if (event.isFileEvent()) {
                    val filesData = event.dragData() as DragData.FilesList
                    val filePaths = filesData.readFiles().map { Path.of(URI.create(it)) }
                    onUpload(filePaths)
                }

                return true
            }

            private fun DragAndDropEvent.isFileEvent(): Boolean {
                val isMoveOrCopy = action == DragAndDropTransferAction.Move || action == DragAndDropTransferAction.Copy
                val dragData = dragData()
                val isFileData = dragData is DragData.FilesList
                var hasOnlyFiles = true

                if (dragData is DragData.FilesList) {
                    hasOnlyFiles = !dragData.readFiles().any { Path.of(URI.create(it)).isDirectory() }
                }

                return isMoveOrCopy && isFileData && hasOnlyFiles
            }
        }
    }

    val dropColor = if (dropStarted) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .drawBehind {
                drawRoundRect(
                    color = dropColor,
                    style = Stroke(
                        width = 5f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f),
                    ),
                    cornerRadius = CornerRadius(8.dp.toPx())
                )
            }
            .clickable { isFileChooserOpen = !isFileChooserOpen }
            .dragAndDropTarget(
                shouldStartDragAndDrop = { true },
                target = dragAndDropTarget
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(20.dp)
        ) {
            Image(
                painter = painterResource("/assets/icons/upload_large.svg"),
                contentDescription = null,
                colorFilter = ColorFilter.tint(dropColor),
                modifier = Modifier
            )
            Text(
                text = "Drop your file here to upload it, or click to select a file.",
                style = MaterialTheme.typography.bodyLarge,
                color = dropColor,
                textAlign = TextAlign.Center
            )
        }
    }

    if (isFileChooserOpen) {
        FileDialog(
            onCloseRequest = {
                isFileChooserOpen = false

                if (it.isNotEmpty()) {
                    onUpload(it.map { file -> file.toPath() })
                }
            }
        )
    }
}

@Preview
@Composable
fun UploadDropInputPreview() {
    UploadDropInput()
}
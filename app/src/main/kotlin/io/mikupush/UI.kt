package io.mikupush

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.github.ajalt.clikt.core.CliktCommand
import kotlinx.coroutines.flow.*
import org.slf4j.LoggerFactory
import java.awt.MouseInfo
import java.awt.Toolkit
import java.util.UUID
import kotlin.random.Random


private val logger = LoggerFactory.getLogger("Main")

data class UploadState(
    val fileId: String,
    val fileName: String,
    val fileMimeType: String,
    val fileSizeBytes: Long,
    val progress: Float = 0f,
    val bytesUploadedRate: Long = 0,
)

class UICommand : CliktCommand(name = "ui") {
    private fun launchUI() = application {
        MikuPushTheme {
            trayIcon()
        }
    }

    override fun run() {
        listenToMessages()
        listenToUploadsRequests()
        launchUI()
    }
}

val uploads = MutableStateFlow(listOf<StateFlow<UploadState>>())

fun StateFlow<UploadState>.addToUploadsList() {
    uploads.update { state ->
        listOf(this) + state
    }
}

fun StateFlow<UploadState>.removeFromUploadsList() {
    uploads.update { state ->
        state.filter { item -> item.value.fileId != value.fileId }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadsWindow(
    show: Boolean = false,
    onCloseRequest: () -> Unit = {}
) {
    if (!show) return

    Window(
        onCloseRequest = onCloseRequest,
        state = uploadWindowState(),
        alwaysOnTop = true,
        resizable = false,
        title = uploadsWindowTitle
    ) {

        var tab by remember { mutableStateOf(0) }
        val tabTitles = listOf("Uploads", "Uploaded")

        Column {
            PrimaryTabRow(selectedTabIndex = tab) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = tab == index,
                        onClick = { tab = index },
                        text = { Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis) }
                    )
                }
            }

            when (tab) {
                0 -> UploadsTab()
                1 -> UploadedTab()
            }
        }
    }
}

@Composable
fun UploadsTab() {
    val state = uploads.collectAsState()
    UploadsList(state.value)
}

@Composable
fun UploadedTab() {

}

@Composable
fun ApplicationScope.trayIcon() {
    var openUploadsWindow by rememberSaveable { mutableStateOf(false) }
    val trayState = rememberTrayState()

    LaunchedEffect(trayState) {
        trayState.listenToNotifications(notificationFlow)
    }

    Tray(
        icon = painterResource("/icon.png"),
        state = trayState,
        tooltip = appName,
        onAction = {
            openUploadsWindow = true
        },
        menu = {
            Item(
                text = "Open",
                onClick = {
                    openUploadsWindow = true
                }
            )
            Item(
                text = "Exit",
                onClick = ::exitApplication
            )
        }
    )

    UploadsWindow(
        show = openUploadsWindow,
        onCloseRequest = { openUploadsWindow = false }
    )
}

@Composable
fun UploadsList(items: List<StateFlow<UploadState>>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        itemsIndexed(items) { index, item ->
            UploadingListItem(item)

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
        MutableStateFlow(UploadState(
            fileId = UUID.randomUUID().toString(),
            fileName = "alya.jpeg",
            fileMimeType = "image/jpeg",
            fileSizeBytes = 1024,
        ))
    })
}

@Composable
fun UploadingListItem(stateFlow: StateFlow<UploadState>) {
    val state = stateFlow.collectAsState()
    val upload = state.value

    UploadProgress(
        fileName = upload.fileName,
        fileMimeType = upload.fileMimeType,
        uploadSpeedBytes = upload.bytesUploadedRate,
        progress = upload.progress
    )
}

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

@Composable
fun FileIcon(mimeType: String, modifier: Modifier = Modifier) {
    val icon = when {
        mimeType.startsWith("image/") -> "file-image.svg"
        mimeType.startsWith("audio/") -> "file-audio.svg"
        mimeType.startsWith("video/") -> "file-video.svg"
        mimeType.startsWith("video/") -> "file-video.svg"
        arrayOf(
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "application/vnd.oasis.opendocument.spreadsheet"
        ).contains(mimeType) -> "file-excel.svg"
        arrayOf(
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/rtf",
            "application/vnd.oasis.opendocument.tex"
        ).contains(mimeType) -> "file-word.svg"
        arrayOf(
            "application/vnd.ms-powerpoint",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation",
            "application/vnd.oasis.opendocument.presentation"
        ).contains(mimeType) -> "file-powerpoint.svg"
        mimeType == "application/pdf" -> "file-pdf.svg"
        mimeType == "text/plain" -> "file-lines.svg"
        arrayOf(
            "text/x-java-source",
            "text/x-c",
            "text/x-c++",
            "text/x-csharp",
            "text/x-python",
            "application/javascript",
            "application/x-typescript",
            "text/html",
            "text/css",
            "application/x-httpd-php",
            "text/x-ruby",
            "text/x-perl",
            "application/x-sh",
            "application/x-powershell",
            "application/sql",
            "text/x-go",
            "text/x-rust",
            "text/x-swift",
            "text/x-kotlin",
            "text/x-lua",
            "text/x-r-source",
            "text/x-matlab"
        ).contains(mimeType) -> "file-code.svg"
        arrayOf(
            "application/zip",
            "application/x-7z-compressed",
            "application/x-rar-compressed",
            "application/gzip",
            "application/x-tar",
            "application/x-bzip2",
            "application/x-xz",
            "application/x-lzma",
            "application/x-apple-diskimage"
        ).contains(mimeType) -> "file-zipper.svg"
        else -> "file.svg"
    }

    Image(
        painter = painterResource("/assets/icons/$icon"),
        contentDescription = "File $mimeType",
        modifier = modifier
    )
}

fun uploadWindowState(): WindowState {
    val width = 300
    val height = 600
    val mouseLocation = MouseInfo.getPointerInfo().location
    val screenSize = Toolkit.getDefaultToolkit().screenSize

    val windowY = if (mouseLocation.y > screenSize.height / 2) {
        mouseLocation.y - height
    } else {
        mouseLocation.y + height
    }

    val windowX = mouseLocation.x - (width / 2)

    return WindowState(
        size = DpSize(
            width = width.dp,
            height = height.dp
        ),
        position = WindowPosition(
            x = windowX.dp,
            y = windowY.dp
        )
    )
}

suspend fun TrayState.listenToNotifications(notificationFlow: SharedFlow<Notification>) {
    notificationFlow.collect { notification ->
        logger.debug("Incoming notification {}", notification)
        sendNotification(notification.toTrayNotification())
    }
}

@Composable
fun MikuPushTheme(block: @Composable () -> Unit) {
    MaterialTheme {
        block()
    }
}
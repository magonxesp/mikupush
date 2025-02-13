package io.mikupush.ui.window

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import io.mikupush.appTitle
import io.mikupush.ui.compose.AppWindow
import io.mikupush.ui.compose.UploadDropInput
import io.mikupush.ui.compose.UploadListEmptyState
import io.mikupush.ui.compose.UploadsList
import io.mikupush.ui.fredokaFamily
import io.mikupush.upload.UploadViewModel
import org.koin.java.KoinJavaComponent.inject
import java.awt.Dimension
import java.awt.MouseInfo
import java.awt.Toolkit

private val uploadViewModel by inject<UploadViewModel>(UploadViewModel::class.java)

private val MinimumWindowWidth = 300.dp
private val MinimumWindowHeight = 400.dp

@Composable
fun MainWindow(
    show: Boolean = true,
    onCloseRequest: () -> Unit = {}
) {
    if (!show) return

    val state = remember {
        WindowState(
            size = DpSize(
                width = MinimumWindowWidth,
                height = MinimumWindowHeight
            ),
            position = WindowPosition(alignment = Alignment.Center)
        )
    }

    AppWindow(
        onCloseRequest = onCloseRequest,
        state = state,
        title = appTitle
    ) {
        with(LocalDensity.current) {
            window.minimumSize = Dimension(
                MinimumWindowWidth.toPx().toInt(),
                MinimumWindowHeight.toPx().toInt()
            )
        }

        UploadsWindowContent()
    }
}

@Composable
fun UploadsWindowContent() {
    var tabIndex by rememberSaveable { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        uploadViewModel.loadUploads()
    }

    Column {
        MainWindowHeader()
        TabRow(selectedTabIndex = tabIndex) {
            Tab(
                selected = tabIndex == 0,
                onClick = { tabIndex = 0 },
                text = { Text("Upload file") },
                icon = {
                    Icon(
                        painter = painterResource("/assets/icons/upload.svg"),
                        contentDescription = null
                    )
                }
            )
            Tab(
                selected = tabIndex == 1,
                onClick = { tabIndex = 1 },
                text = { Text("Uploaded files") },
                icon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.List,
                        contentDescription = null
                    )
                }
            )
        }

        when(tabIndex) {
            0 -> UploadFileInputTab()
            1 -> UploadedFilesTab()
        }
    }
}

@Composable
fun UploadedFilesTab() {
    val uploads = uploadViewModel.uploads.collectAsState()

    Column {
        if (uploads.value.isNotEmpty()) {
            UploadsList(
                items = uploads.value,
                onCancel = { fileId -> uploadViewModel.cancel(fileId) },
                onGetLink = { fileId -> uploadViewModel.copyLinkToClipboard(fileId) },
                onShowInExplorer = { path -> uploadViewModel.showInFileExplorer(path) },
                onRetry = { path, fileId -> uploadViewModel.startUpload(path.toString(), fileId) },
                onDelete = { fileId -> uploadViewModel.delete(fileId) }
            )
        } else {
            UploadListEmptyState(onUpload = { path ->
                uploadViewModel.startUpload(path.toString())
            })
        }
    }
}

@Composable
fun UploadFileInputTab() {
    UploadDropInput(onUpload = { paths ->
        uploadViewModel.startUploadMultiple(paths.map { path -> path.toString() })
    })
}

@Composable
fun MainWindowHeader() {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(5.dp)
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource("/icon.png"),
            contentDescription = null,
            modifier = Modifier.height(80.dp)
                .padding(end = 10.dp)
        )
        Text(
            text = appTitle,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 60.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = fredokaFamily,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

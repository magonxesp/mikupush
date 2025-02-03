package io.mikupush.ui.window

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.runtime.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import io.mikupush.ui.compose.UploadedList
import io.mikupush.ui.compose.UploadsList
import io.mikupush.upload.UploadViewModel
import io.mikupush.uploads.UploadsViewModel
import io.mikupush.uploadsWindowTitle
import org.koin.java.KoinJavaComponent.inject
import java.awt.MouseInfo
import java.awt.Toolkit

private val uploadViewModel by inject<UploadViewModel>(UploadViewModel::class.java)
private val uploadsViewModel by inject<UploadsViewModel>(UploadsViewModel::class.java)

@Composable
fun MainWindow(
    show: Boolean = false,
    onCloseRequest: () -> Unit = {}
) {
    if (!show) return

    Window(
        onCloseRequest = onCloseRequest,
        state = uploadWindowState(),
        alwaysOnTop = true,
        resizable = true,
        title = uploadsWindowTitle
    ) {
        UploadsWindowContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadsWindowContent() {

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

@Composable
fun UploadsTab() {
    val state = uploadViewModel.uiState.collectAsState()
    UploadsList(state.value)
}

@Composable
fun UploadedTab() {
    val state = uploadsViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        uploadsViewModel.showAllUploads()
    }

    UploadedList(state.value)
}

fun uploadWindowState(): WindowState {
    val width = 500
    val height = 800
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
package io.mikupush.ui.window

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import io.mikupush.ui.compose.UploadsList
import io.mikupush.ui.fredokaFamily
import io.mikupush.upload.UploadViewModel
import org.koin.java.KoinJavaComponent.inject
import java.awt.Dimension
import java.awt.MouseInfo
import java.awt.Toolkit

private val uploadViewModel by inject<UploadViewModel>(UploadViewModel::class.java)

private val MinimumWindowWidth = 300.dp
private val MinimumWindowHeight = 600.dp

@Composable
fun MainWindow(
    show: Boolean = false,
    onCloseRequest: () -> Unit = {}
) {
    if (!show) return

    AppWindow(
        onCloseRequest = onCloseRequest,
        state = uploadWindowState(),
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
    val uploads = uploadViewModel.uploads.collectAsState()

    LaunchedEffect(Unit) {
        uploadViewModel.loadUploads()
    }

    Column {
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
        UploadsList(
            uploads.value,
            onCancel = { fileId -> uploadViewModel.cancel(fileId) },
            onGetLink = { fileId -> uploadViewModel.copyLinkToClipboard(fileId) },
            onShowInExplorer = { path -> uploadViewModel.showInFileExplorer(path) },
            onRetry = { path, fileId -> uploadViewModel.startUpload(path.toString(), fileId) }
        )
    }
}

@Composable
fun uploadWindowState(): WindowState {
    val density = LocalDensity.current
    val mouseLocation = MouseInfo.getPointerInfo().location
    val screenSize = Toolkit.getDefaultToolkit().screenSize

    val windowY = with(density) {
        if (mouseLocation.y > screenSize.height / 2) {
            mouseLocation.y - MinimumWindowHeight.toPx()
        } else {
            mouseLocation.y + MinimumWindowHeight.toPx()
        }
    }

    val windowX = with(density) {
        mouseLocation.x - (MinimumWindowWidth.toPx() / 2)
    }

    return WindowState(
        size = DpSize(
            width = MinimumWindowWidth,
            height = MinimumWindowHeight
        ),
        position = WindowPosition(
            x = windowX.dp,
            y = windowY.dp
        )
    )
}
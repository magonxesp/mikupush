package io.mikupush

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.github.ajalt.clikt.core.CliktCommand
import kotlinx.coroutines.flow.SharedFlow
import org.slf4j.LoggerFactory
import java.awt.Dimension
import java.awt.MouseInfo
import java.awt.Toolkit


private val logger = LoggerFactory.getLogger("Main")

class UICommand : CliktCommand(name = "ui") {
    private fun launchUI() = application {
        MikuPushTheme {
            trayIcon()
        }
    }

    override fun run() {
        startHttpServer()
        launchUI()
    }
}

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
        Column {
            Text("Hello world")
        }
    }
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
        }
    )

    UploadsWindow(
        show = openUploadsWindow,
        onCloseRequest = { openUploadsWindow = false }
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
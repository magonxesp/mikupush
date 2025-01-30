package io.mikupush

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.*
import com.github.ajalt.clikt.core.CliktCommand
import kotlinx.coroutines.flow.SharedFlow
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("Main")

class UICommand : CliktCommand(name = "ui") {
    private fun launchUI() = application {
        trayIcon()
    }

    override fun run() {
        startHttpServer()
        launchUI()
    }
}

@Composable
fun ApplicationScope.trayIcon() {
    val trayState = rememberTrayState()

    LaunchedEffect(trayState) {
        trayState.listenToNotifications(notificationFlow)
    }

    Tray(
        icon = painterResource("/icon.png"),
        state = trayState,
        menu = {
            Item(
                text = "Exit",
                onClick = ::exitApplication
            )
        }
    )
}

suspend fun TrayState.listenToNotifications(notificationFlow: SharedFlow<Notification>) {
    notificationFlow.collect { notification ->
        logger.debug("Incoming notification {}", notification)
        sendNotification(notification.toTrayNotification())
    }
}

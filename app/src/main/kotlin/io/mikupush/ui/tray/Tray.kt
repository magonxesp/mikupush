package io.mikupush.ui.tray

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.rememberTrayState
import io.mikupush.notification.Notifier
import io.mikupush.appName
import io.mikupush.notification.toTrayNotification
import io.mikupush.ui.window.MainWindow
import org.koin.java.KoinJavaComponent.inject
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("AppTrayIcon")
private val notifier by inject<Notifier>(Notifier::class.java)

@Composable
fun ApplicationScope.AppTrayIcon(onOpen: () -> Unit) {
    val trayState = rememberTrayState()

    LaunchedEffect(trayState) {
        notifier.notifications.collect { notification ->
            logger.debug("showing notification: {}, {}", notification.title, notification.message)
            trayState.sendNotification(notification.toTrayNotification())
        }
    }

    Tray(
        icon = painterResource("/icon.png"),
        state = trayState,
        tooltip = appName,
        onAction = onOpen,
        menu = {
            Item(
                text = "Open",
                onClick = onOpen
            )
            Item(
                text = "Exit",
                onClick = ::exitApplication
            )
        }
    )
}

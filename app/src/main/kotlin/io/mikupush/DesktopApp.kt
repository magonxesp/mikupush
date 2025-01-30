package io.mikupush

import com.github.ajalt.clikt.core.CliktCommand
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import java.awt.Image
import java.awt.SystemTray
import java.awt.Toolkit
import java.awt.TrayIcon
import java.awt.TrayIcon.MessageType

private val logger = LoggerFactory.getLogger("Main")

class DesktopApplicationCommand : CliktCommand(name = "ui") {
    private fun launchUI() = runBlocking {
        val trayIcon = showTrayIcon()
        launch(Dispatchers.Default) { trayIcon.listenToNotifications() }
    }

    override fun run() {
        startHttpServer()
        launchUI()
    }
}

fun showTrayIcon(): TrayIcon {
    val tray = SystemTray.getSystemTray()
    val image: Image = Toolkit.getDefaultToolkit().createImage(object {}::class.java.getResource("/icon.png"))

    val trayIcon = TrayIcon(image, "Tray Demo").apply {
        isImageAutoSize = true
        toolTip = "System tray icon demo"
    }

    tray.add(trayIcon)
    return trayIcon
}

suspend fun TrayIcon.listenToNotifications() {
    notificationFlow.collect { notification ->
        logger.debug("Incoming notification {}", notification)
        displayMessage(notification.title, notification.message, MessageType.INFO)
    }
}

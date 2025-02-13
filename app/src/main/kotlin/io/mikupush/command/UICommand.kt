package io.mikupush.command

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.application
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import io.mikupush.http.startHttpServer
import io.mikupush.setupDatabase
import io.mikupush.ui.tray.AppTrayIcon
import io.mikupush.ui.window.MainWindow
import org.slf4j.LoggerFactory

class UICommand : CliktCommand(name = "ui") {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val onlyTray by option("-t", "--tray").flag(default = false)

    private fun launchUI() = application {
        var openUploadsWindow by rememberSaveable { mutableStateOf(!onlyTray) }

        AppTrayIcon(onOpen = { openUploadsWindow = true })
        MainWindow(openUploadsWindow, onCloseRequest = { openUploadsWindow = false })

        logger.debug("Application launched")
    }

    override fun run() {
        setupDatabase()
        startHttpServer()
        launchUI()
    }
}
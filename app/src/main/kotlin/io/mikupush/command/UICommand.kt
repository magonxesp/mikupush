package io.mikupush.command

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.window.application
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import io.mikupush.http.startHttpServer
import io.mikupush.setupDatabase
import io.mikupush.ui.tray.AppTrayIcon
import io.mikupush.ui.window.MainWindow
import io.mikupush.upload.UploadViewModel
import org.koin.java.KoinJavaComponent.inject
import org.slf4j.LoggerFactory

class UICommand : CliktCommand(name = "ui") {
    private val viewModel: UploadViewModel by inject(UploadViewModel::class.java)
    private val onlyTray by option("-t", "--tray").flag(default = false)

    private fun launchUI() = application {
        val showWindow = viewModel.showWindow.collectAsState()
        val showWindowAtStartup by rememberSaveable { mutableStateOf(!onlyTray) }

        LaunchedEffect(showWindowAtStartup) {
            if (showWindowAtStartup) {
                viewModel.showWindow()
            }
        }

        AppTrayIcon(onOpen = { viewModel.showWindow() })
        MainWindow(showWindow.value, onCloseRequest = { viewModel.closeWindow() })
    }

    override fun run() {
        setupDatabase()
        startHttpServer()
        launchUI()
    }
}
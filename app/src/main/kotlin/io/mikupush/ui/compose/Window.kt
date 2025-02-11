package io.mikupush.ui.compose

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberWindowState
import io.mikupush.ui.MikuPushTheme
import org.apache.commons.lang3.SystemUtils

@Composable
fun AppWindow(
    onCloseRequest: () -> Unit,
    state: WindowState = rememberWindowState(),
    title: String = "",
    content: @Composable FrameWindowScope.() -> Unit
) {
    when {
        SystemUtils.IS_OS_WINDOWS_11 -> WindowsAppWindowWrapper(onCloseRequest, state, title, content)
        SystemUtils.IS_OS_MAC -> MacOSAppWindowWrapper(onCloseRequest, state, title, content)
        else -> SystemAppWindowWrapper(onCloseRequest, state, title, content)
    }
}

@Composable
fun WindowsAppWindowWrapper(
    onCloseRequest: () -> Unit,
    state: WindowState = rememberWindowState(),
    title: String = "",
    content: @Composable FrameWindowScope.() -> Unit
) {
    Window(
        onCloseRequest = onCloseRequest,
        state = state,
        alwaysOnTop = false,
        resizable = true,
        undecorated = true,
        title = title
    ) {
        MikuPushTheme {
            Surface(
                modifier = Modifier.fillMaxSize()
                    .border(
                        width = 0.1.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = ShapeDefaults.Medium
                    )
            ) {
                Column {
                    AppTitleBar(onCloseRequest)
                    content()
                }
            }
        }
    }
}

@Composable
fun MacOSAppWindowWrapper(
    onCloseRequest: () -> Unit,
    state: WindowState = rememberWindowState(),
    title: String = "",
    content: @Composable FrameWindowScope.() -> Unit
) {
    Window(
        onCloseRequest = onCloseRequest,
        state = state,
        alwaysOnTop = false,
        resizable = true,
        undecorated = false,
        title = title
    ) {
        window.rootPane.putClientProperty("apple.awt.fullWindowContent", true)
        window.rootPane.putClientProperty("apple.awt.transparentTitleBar", true)

        MikuPushTheme {
            Surface(modifier = Modifier.fillMaxSize()) {
                content()
            }
        }
    }
}

@Composable
fun SystemAppWindowWrapper(
    onCloseRequest: () -> Unit,
    state: WindowState = rememberWindowState(),
    title: String = "",
    content: @Composable FrameWindowScope.() -> Unit
) {
    Window(
        onCloseRequest = onCloseRequest,
        state = state,
        alwaysOnTop = false,
        resizable = true,
        undecorated = false,
        title = title
    ) {
        MikuPushTheme {
            Surface(modifier = Modifier.fillMaxSize()) {
                content()
            }
        }
    }
}
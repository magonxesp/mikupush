package io.mikupush.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberWindowState
import io.mikupush.ui.MikuPushTheme
import org.apache.commons.lang3.SystemUtils
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.intui.standalone.theme.IntUiTheme
import org.jetbrains.jewel.intui.standalone.theme.darkThemeDefinition
import org.jetbrains.jewel.intui.standalone.theme.default
import org.jetbrains.jewel.intui.window.decoratedWindow
import org.jetbrains.jewel.intui.window.styling.dark
import org.jetbrains.jewel.ui.ComponentStyling
import org.jetbrains.jewel.window.DecoratedWindow
import org.jetbrains.jewel.window.TitleBar
import org.jetbrains.jewel.window.defaultDecoratedWindowStyle
import org.jetbrains.jewel.window.newFullscreenControls
import org.jetbrains.jewel.window.styling.DecoratedWindowStyle
import org.jetbrains.jewel.window.styling.TitleBarColors
import org.jetbrains.jewel.window.styling.TitleBarStyle
import java.awt.Color

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
    MikuPushTheme {
        IntUiTheme(
            theme = JewelTheme.darkThemeDefinition(),
            styling = ComponentStyling.default().decoratedWindow(
                titleBarStyle = TitleBarStyle.dark(
                    colors = TitleBarColors.dark(
                        backgroundColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.surface,
                        borderColor = MaterialTheme.colorScheme.surface
                    )
                )
            ),
        ) {
            DecoratedWindow(
                onCloseRequest = onCloseRequest,
                state = state,
                title = title
            ) {
                TitleBar(Modifier.newFullscreenControls()) {}
                Surface(modifier = Modifier.fillMaxSize()) {
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
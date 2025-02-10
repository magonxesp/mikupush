package io.mikupush.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.slf4j.LoggerFactory

private val DarkColorScheme = darkColorScheme(
    primary = Verdigris,
    secondary = RichBlack,
    background = Dark,
    surface = Dark,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    primaryContainer = Verdigris,
    onPrimaryContainer = Color.Black,
    outline = Verdigris,
    secondaryContainer = Verdigris,
    onSecondaryContainer = Color.Black,
)

private val LightColorScheme = lightColorScheme(
    primary = CaribbeanCurrent,
    secondary = Night,
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    primaryContainer = CaribbeanCurrent,
    onPrimaryContainer = Color.White,
    secondaryContainer = TiffanyBlue,
    onSecondaryContainer = Color.Black,
    surfaceContainerHighest = AntiFlashWhite
)

private val logger = LoggerFactory.getLogger("Theme")

@Composable
fun MikuPushTheme(content: @Composable () -> Unit) {
    val colorScheme = if (isSystemInDarkTheme()) {
        logger.debug("use dark theme")
        DarkColorScheme
    } else {
        logger.debug("use light theme")
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
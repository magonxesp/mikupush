package io.mikupush.ui.compose

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.mikupush.ui.segoeUIFontFamily

@Composable
fun AppTitleBar(
    onMinimize: () -> Unit = { },
    onMaximize: () -> Unit = { },
    onClose: () -> Unit = { },
    isMaximized: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Top
    ) {
        Spacer(
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
        )
        IconButton(onClick = onMinimize) {
            Text(
                text = "\ue921",
                fontFamily = segoeUIFontFamily,
                style = MaterialTheme.typography.bodySmall
            )
        }
        IconButton(onClick = onMaximize) {
            Text(
                text = if (isMaximized) "\ue923" else "\ue922",
                fontFamily = segoeUIFontFamily,
                style = MaterialTheme.typography.bodySmall
            )
        }
        IconButton(onClick = onClose) {
            Text(
                text = "\ue8bb",
                fontFamily = segoeUIFontFamily,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview
@Composable
fun CustomTitleBarPreview() {
    AppTitleBar()
}
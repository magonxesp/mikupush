package io.mikupush.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun MikuPushTheme(block: @Composable () -> Unit) {
    MaterialTheme {
        block()
    }
}
package io.mikupush

import kotlinx.coroutines.flow.MutableSharedFlow

data class Notification(
    val title: String,
    val message: String
)

fun Notification.toTrayNotification(): androidx.compose.ui.window.Notification {
    return androidx.compose.ui.window.Notification(
        title = title,
        message = message,
        type = androidx.compose.ui.window.Notification.Type.None
    )
}

val notificationFlow = MutableSharedFlow<Notification>()
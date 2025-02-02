package io.mikupush.notification

import androidx.compose.ui.window.Notification as UINotification

enum class NotificationType {
    INFO, ERROR
}

data class Notification(
    val title: String,
    val message: String,
    val type: NotificationType = NotificationType.INFO
)

fun Notification.toTrayNotification() = UINotification(
    title = title,
    message = message,
    type = when (type) {
        NotificationType.ERROR -> UINotification.Type.Error
        else -> UINotification.Type.None
    }
)

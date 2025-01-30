package io.mikupush

import kotlinx.coroutines.flow.MutableSharedFlow
import androidx.compose.ui.window.Notification as UINotification

data class Notification(
    val title: String,
    val message: String
)

fun Notification.toTrayNotification() = UINotification(
    title = title,
    message = message,
    type = UINotification.Type.None
)

val notificationFlow = MutableSharedFlow<Notification>()
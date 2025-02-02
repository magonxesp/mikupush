package io.mikupush.notification

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.slf4j.LoggerFactory

class Notifier {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val notificationFlow = MutableSharedFlow<Notification>()
    val notifications = notificationFlow.asSharedFlow()

    suspend fun notify(title: String, message: String) {
        notificationFlow.emit(
            Notification(
                title = title,
                message = message
            )
        )
        logger.debug("sent notification: {}, {}", title, message)
    }

    suspend fun notifyError(title: String, message: String) {
        notificationFlow.emit(
            Notification(
                title = title,
                message = message,
                type = NotificationType.ERROR
            )
        )
        logger.debug("sent notification error: {}, {}", title, message)
    }
}
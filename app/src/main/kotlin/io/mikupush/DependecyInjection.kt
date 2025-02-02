package io.mikupush

import io.mikupush.messenger.MessengerReceiver
import io.mikupush.messenger.MessengerSender
import io.mikupush.notification.Notifier
import io.mikupush.upload.UploadRepository
import io.mikupush.upload.UploadViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

val services = module {
    single { Notifier() }
    single { MessengerReceiver() }
    single { MessengerSender() }
}

val repositories = module {
    single { UploadRepository() }
}

val viewModels = module {
    single { UploadViewModel(get()) }
}

fun startDependencyInjection() {
    startKoin {
        modules(
            services,
            repositories,
            viewModels
        )
    }
}
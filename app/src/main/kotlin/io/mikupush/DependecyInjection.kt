package io.mikupush

import io.mikupush.notification.Notifier
import io.mikupush.upload.UploadViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

val services = module {
    single { Notifier() }
}

val viewModels = module {
    single { UploadViewModel(get()) }
}

fun startDependencyInjection() {
    startKoin {
        modules(
            services,
            viewModels
        )
    }
}
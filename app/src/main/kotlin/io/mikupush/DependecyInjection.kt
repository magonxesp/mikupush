package io.mikupush

import io.mikupush.notification.Notifier
import io.mikupush.notification.UploadedSignal
import io.mikupush.upload.UploadViewModel
import io.mikupush.upload.UploadsViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

val services = module {
    single { UploadedSignal() }
    single { Notifier() }
}

val viewModels = module {
    single { UploadViewModel(get(), get()) }
    single { UploadsViewModel(get()) }
}

fun startDependencyInjection() {
    startKoin {
        modules(
            services,
            viewModels
        )
    }
}
package io.mikupush.ui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlin.concurrent.thread

abstract class ViewModel {
    protected val viewModelScope = CoroutineScope(Job() + Dispatchers.IO)

    init {
        Runtime.getRuntime().addShutdownHook(thread(start = false) {
            viewModelScope.cancel()
        })
    }
}
package io.mikupush.ui

import kotlinx.coroutines.*
import kotlin.concurrent.thread

abstract class ViewModel {
    protected val viewModelScope = CoroutineScope(Job() + Dispatchers.IO)

    init {
        Runtime.getRuntime().addShutdownHook(thread(start = false) {
            viewModelScope.cancel()
        })
    }
}
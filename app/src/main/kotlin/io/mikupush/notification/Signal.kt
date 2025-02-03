package io.mikupush.notification

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import kotlin.concurrent.thread

abstract class Signal {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val signalScope = CoroutineScope(Job())
    private val signalFlow = MutableSharedFlow<Int>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        Runtime.getRuntime().addShutdownHook(thread(start = false) {
            signalScope.cancel()
        })
    }

    fun onSignal(block: () -> Unit) = signalScope.launch {
        logger.debug("signal receiver listening")

        signalFlow.collect {
            logger.debug("signal received")
            block()
        }
    }

    fun emit() {
        signalFlow.tryEmit(1)
        logger.debug("signal sent")
    }
}
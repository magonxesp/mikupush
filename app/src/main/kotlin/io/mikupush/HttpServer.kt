package io.mikupush

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*

fun startHttpServer() {
    embeddedServer(Netty, host = "127.0.0.1", port = 49152) {
        configureRouting()
    }.start(wait = false)
}

fun Application.configureRouting() {
    routing {
        uploadFileRequestRoute()
    }
}
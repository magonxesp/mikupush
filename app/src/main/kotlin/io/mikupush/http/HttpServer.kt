package io.mikupush.http

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.mikupush.upload.UploadViewModel
import org.koin.java.KoinJavaComponent.inject


fun Routing.configureRoutes() {
    post("/upload") {
        val viewModel: UploadViewModel by inject(UploadViewModel::class.java)
        viewModel.upload(call.receiveText())
    }
}

fun startHttpServer() {
    embeddedServer(Netty, port = 49152, host = "127.0.0.1") {
        routing {
            configureRoutes()
        }
    }.start(wait = false)
}
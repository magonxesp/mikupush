package io.mikupush.http

import io.ktor.http.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.mikupush.upload.UploadViewModel
import org.koin.java.KoinJavaComponent.inject


fun Routing.configureRoutes() {
    get("/ping") {
        call.respondText("pong")
    }
    get("/restore") {
        val viewModel: UploadViewModel by inject(UploadViewModel::class.java)
        viewModel.showWindow()
        call.respond(HttpStatusCode.NoContent)
    }
    post("/upload") {
        val viewModel: UploadViewModel by inject(UploadViewModel::class.java)
        viewModel.startUpload(call.receiveText())
        call.respond(HttpStatusCode.NoContent)
    }
}

fun startHttpServer() {
    embeddedServer(Netty, port = 49152, host = "127.0.0.1") {
        routing {
            configureRoutes()
        }
    }.start(wait = false)
}
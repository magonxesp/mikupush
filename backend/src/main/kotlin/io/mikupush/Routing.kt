package io.mikupush

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import org.apache.tika.Tika
import org.slf4j.LoggerFactory
import java.io.File

private val logger = LoggerFactory.getLogger("Routing")

fun Throwable.log() {
    logger.warn("${this::class} - $message")
}

fun Application.configureRouting() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            cause.log()
            call.respond(HttpStatusCode.InternalServerError)
        }
        exception<IllegalArgumentException> { call, cause ->
            cause.log()
            call.respond(HttpStatusCode.BadRequest)
        }
    }
    routing {
        get("/{fileId}") {
            val fileId = call.parameters["fileId"]
            require(!fileId.isNullOrBlank()) { "File id is required" }
            val file = File("data/$fileId")

            if (!file.exists()) {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }

            val contentType = Tika().detect(file)
            if (contentType.isNullOrBlank()) {
                error("Unable to resolve content type for file $file")
            }

            call.respondBytes(contentType = ContentType.parse(contentType), bytes = file.readBytes())
        }
        post("/upload/{fileId}") {
            val fileId = call.parameters["fileId"]
            require(!fileId.isNullOrBlank()) { "File id is required" }

            val file = File("data/$fileId").apply {
                if (!parentFile.exists()) {
                    parentFile.mkdirs()
                }
            }

            val channel = call.receiveChannel()
            channel.counted().totalBytesRead
            channel.copyAndClose(file.writeChannel())
            call.respond(HttpStatusCode.Created)
        }
    }
}

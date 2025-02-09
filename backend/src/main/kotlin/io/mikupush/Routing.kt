package io.mikupush

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import org.apache.tika.Tika
import org.slf4j.LoggerFactory
import java.io.File
import java.util.*

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
        get("/favicon.ico") {
            val content = object {}::class.java.getResource("/icon.ico")!!.readBytes()

            call.respondBytes(
                contentType = ContentType.Image.XIcon,
                bytes = content
            )
        }
        get("/{fileId}") {
            val fileId = call.parameters["fileId"]
            require(!fileId.isNullOrBlank()) { "File id is required" }

            val upload = findById(UUID.fromString(fileId))
            val file = File("data/$fileId")

            if (!file.exists() || upload == null) {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }

            call.response.headers.append(
                name = "Content-Disposition",
                value = "inline; filename=${upload.fileName}"
            )

            call.respondBytes(
                contentType = ContentType.parse(upload.fileMimeType),
                bytes = file.readBytes()
            )
        }
        post("/upload/{fileId}") {
            val fileId = call.parameters["fileId"]
            require(!fileId.isNullOrBlank()) { "File id is required" }
            logger.debug("Writing file $fileId")

            val file = File("data/$fileId").apply {
                if (!parentFile.exists()) {
                    parentFile.mkdirs()
                }
            }

            val channel = call.receiveChannel()
            channel.copyAndClose(file.writeChannel())
            logger.debug("File $fileId wrote")

            call.respond(HttpStatusCode.Created)
        }
        put("/upload/details") {
            call.receive<UploadDetails>().insertOrUpdate()
            call.respond(HttpStatusCode.NoContent)
        }
        delete("/upload/{fileId}") {
            val fileId = call.parameters["fileId"]
            require(!fileId.isNullOrBlank()) { "File id is required" }

            val upload = findById(UUID.fromString(fileId))
            val file = File("data/$fileId")

            if (file.exists()) {
                val deleted = file.delete()

                if (deleted) {
                    upload?.delete()
                }
            }

            call.respond(HttpStatusCode.OK)
        }
    }
}

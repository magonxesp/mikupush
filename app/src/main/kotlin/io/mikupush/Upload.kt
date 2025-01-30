package io.mikupush

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import java.io.File
import java.util.*


private val logger = LoggerFactory.getLogger("Upload")

suspend fun upload(filePath: String) {
    val file = File(filePath)

    if (!file.exists()) {
        error("File $filePath not exists")
    }

    val uuid = UUID.randomUUID().toString()
    backendHttpClient.use { client ->
        client.post("/upload/$uuid") {
            setBody(file.readBytes())
        }
    }

    copyToClipboard("$baseUrl/$uuid")

    notificationFlow.emit(Notification(
        title = "File uploaded :D",
        message = "The file url copied to clipboard"
    ))
}

class UploadRequestCommand : CliktCommand(name = "upload") {
    val filePath: String by argument()

    override fun run() {
        runBlocking {
            logger.debug("Requesting file upload for $filePath")

            localHttpClient.use { client ->
                client.post("/upload") {
                    setBody(filePath)
                }
            }
        }
    }
}

fun Routing.uploadFileRequestRoute() {
    post("/upload") {
        upload(call.receiveText())
        call.respond(HttpStatusCode.OK)
    }
}
package io.mikupush

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import java.io.File
import java.util.*


private val logger = LoggerFactory.getLogger("Upload")

suspend fun upload(filePath: String) {
    val file = File(filePath)

    notificationFlow.emit(Notification(
        title = "Uploading file",
        message = "Uploading file ${file.name} please wait"
    ))

    if (!file.exists()) {
        error("File $filePath not exists")
    }

    val uuid = UUID.randomUUID().toString()

    backendHttpClient.use { client ->
        val reader = file.inputStream().buffered(100 * 1000)
        val buffer = ByteArray(100 * 1000) // 100kb

        while (reader.read(buffer) != -1) {
            logger.debug("sending new chunk to file $uuid")
            client.post("/upload/$uuid/chunk") {
                setBody(buffer)
            }
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
        val filePath = call.receiveText()

        CoroutineScope(Job() + Dispatchers.IO).launch {
            upload(filePath)
        }

        call.respond(HttpStatusCode.Accepted)
    }
}
package io.mikupush.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import io.ktor.client.request.*
import io.mikupush.http.localHttpClient
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory

class UploadCommand : CliktCommand(name = "upload") {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val filePaths: List<String> by argument().multiple()

    override fun run() = runBlocking {
        localHttpClient.use { client ->
            for (filePath in filePaths) {
                client.post("/upload") {
                    setBody(filePath)
                }

                logger.debug("Requested upload for $filePath")
            }
        }
    }
}
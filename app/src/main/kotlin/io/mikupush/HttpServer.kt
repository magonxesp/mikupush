package io.mikupush

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory
import java.io.IOException
import kotlin.io.path.Path

private val logger = LoggerFactory.getLogger("HttpServer")

fun startHttpServer() {
    logger.debug("Starting http server")
    val ports = (49152..65535).toMutableList()
    var retry: Boolean

    do {
        val port = ports.removeLast()
        retry = false

        try {
            writePort(port)
            embeddedServer(Netty, host = "127.0.0.1", port = port) {
                configureRouting()
            }.start(wait = false)

            logger.info("Http server listening on port $port")
        } catch (exception: IOException) {
            logger.warn("Http server failed to start with port $port, retrying...", exception)
            retry = true
        }
    } while (retry)
}

const val PORT_FILE_NAME = "port"

fun writePort(port: Int) {
    val portFile = Path(appDataDir, PORT_FILE_NAME).toFile()
    portFile.parentFile.mkdirs()

    portFile.writeText(port.toString())
}

fun getHttpServerPort(): Int {
    val portFile = Path(appDataDir, PORT_FILE_NAME).toFile()
    if (!portFile.exists()) {
        error("Port file is missing")
    }

    return portFile.readText().toInt()
}

fun Application.configureRouting() {
    routing {
        uploadFileRequestRoute()
    }
}
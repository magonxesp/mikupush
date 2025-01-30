package io.mikupush

import io.ktor.client.*
import io.ktor.client.plugins.*
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("HttpClient")

val backendHttpClient get() = HttpClient {
    defaultRequest {
        url(baseUrl)
    }

    install(HttpTimeout) {
        requestTimeoutMillis = 30000
    }

    install(HttpRequestRetry) {
        retryOnServerErrors(3)
    }
}

val localHttpClient get() = HttpClient {
    defaultRequest {
        val port = getHttpServerPort()
        val baseUrl = "http://127.0.0.1:$port"

        logger.debug("Configuring target to $baseUrl")
        url(baseUrl)
    }

    install(HttpTimeout) {
        requestTimeoutMillis = 30000
    }

    install(HttpRequestRetry) {
        retryOnServerErrors(3)
    }
}
package io.mikupush.http

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.mikupush.backendBaseUrl

val backendHttpClient get() = HttpClient {
    defaultRequest {
        url(backendBaseUrl)
    }

    install(HttpTimeout) {
        requestTimeoutMillis = 30000
    }

    install(HttpRequestRetry) {
        retryOnServerErrors(3)
    }

    install(ContentNegotiation) {
        json()
    }
}

val localHttpClient get() = HttpClient {
    defaultRequest {
        url("http://127.0.0.1:49152")
    }

    install(HttpTimeout) {
        requestTimeoutMillis = 3000
    }
}

package io.mikupush.http

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.mikupush.baseUrl

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
        url("http://127.0.0.1:49152")
    }

    install(HttpTimeout) {
        requestTimeoutMillis = 3000
    }
}

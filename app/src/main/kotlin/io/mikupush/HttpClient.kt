package io.mikupush

import io.ktor.client.*
import io.ktor.client.plugins.*

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

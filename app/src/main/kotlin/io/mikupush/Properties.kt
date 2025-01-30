package io.mikupush

import java.util.*

val properties = Properties().apply {
    load(object {}::class.java.getResource("/application.properties")!!.openStream())
}

val appName: String = properties.getProperty("app.name")
val uploadsWindowTitle: String = properties.getProperty("app.uploads_window.title")
val baseUrl: String = properties.getProperty("backend.base_url")
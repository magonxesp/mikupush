package io.mikupush

import java.util.*

val properties = Properties().apply {
    load(object {}::class.java.getResource("/application.properties")!!.openStream())
}

val baseUrl: String = properties.getProperty("backend.base_url")
package io.mikupush

import org.apache.commons.lang3.SystemUtils
import java.util.*
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.notExists

val appDataDir = if (SystemUtils.IS_OS_WINDOWS) {
    Path(System.getenv("APPDATA"), "MikuPush")
} else {
    Path(System.getProperty("user.home"), ".mikupush")
}.apply {
    if (notExists()) {
        createDirectories()
    }
}.toString()

val properties = Properties().apply {
    load(object {}::class.java.getResource("/application.properties")!!.openStream())
}

val appName: String = properties.getProperty("app.name")
val appTitle: String = properties.getProperty("app.title")
val backendBaseUrl: String = properties.getProperty("backend.base_url")


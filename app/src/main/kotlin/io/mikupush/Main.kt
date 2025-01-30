package io.mikupush

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import org.apache.commons.lang3.SystemUtils
import kotlin.io.path.Path

class MainApplication : CliktCommand() {
    override fun run() { }
}

val appDataDir = if (SystemUtils.IS_OS_WINDOWS) {
    Path(System.getenv("APPDATA"), "mikupush").toString()
} else {
    Path(System.getProperty("user.home"), ".mikupush").toString()
}

fun main(args: Array<String>) {
    MainApplication()
        .subcommands(
            DesktopApplicationCommand(),
            UploadRequestCommand()
        )
        .main(args)
}

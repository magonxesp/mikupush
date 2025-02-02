package io.mikupush

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import io.mikupush.command.UICommand
import io.mikupush.command.UploadCommand

class MainApplication : CliktCommand() {
    override fun run() { }
}

fun main(args: Array<String>) {
    startDependencyInjection()

    MainApplication()
        .subcommands(
            UICommand(),
            UploadCommand()
        )
        .main(args)
}

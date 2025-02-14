import org.gradle.kotlin.dsl.support.unzipTo
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.net.URL
import java.nio.file.Files
import kotlin.io.path.*

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    kotlin("plugin.serialization")
    id("com.gradleup.shadow")
}

group = "io.github.magonxesp"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://packages.jetbrains.team/maven/p/kpm/public/")
    google()
}

dependencies {
    val ktor_version = "3.0.3"
    val logback_version = "1.4.14"
    val exposed_version = "0.58.0"
    val koin_version = "4.0.2"
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")
    implementation("io.ktor:ktor-server-status-pages:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposed_version")
    implementation("org.xerial:sqlite-jdbc:3.42.0.0")
    implementation("org.apache.tika:tika-core:3.0.0")
    implementation("org.apache.commons:commons-lang3:3.17.0")
    implementation("com.github.ajalt.clikt:clikt:5.0.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    implementation("io.insert-koin:koin-core:$koin_version")
    implementation("org.jetbrains.jewel:jewel-int-ui-standalone-243:0.27.0")
    implementation("org.jetbrains.jewel:jewel-int-ui-decorated-window-243:0.27.0")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "io.mikupush.MainKt"
    }

    archiveBaseName = "mikupush"
}

compose.desktop {
    application {
        mainClass = "io.mikupush.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Exe, TargetFormat.Deb)
            packageName = "MikuPush"
            packageVersion = "1.0.0"
            copyright = "© 2025 MikuPush. All rights reserved."
            vendor = "MagonxESP"
            licenseFile = rootProject.file("LICENSE")
            includeAllModules = true

            windows {
                iconFile.set(project.file("src/main/resources/icon.ico"))
                console = true
                dirChooser = true
            }
        }
    }
}

tasks.register("distWindows") {
    dependsOn("shadowJar")

    doLast {
        val rootProjectDir = rootProject.rootDir.path
        val projectDir = project.projectDir.path
        val windowsDistPath = Path(rootProjectDir, "dist/windows").toString()

        File(windowsDistPath).apply {
            if (exists()) deleteRecursively()
            mkdirs()
        }

        Files.walk(Path(projectDir, "build/libs")).toList()
            .find { jar -> Regex(".*mikupush-.*-all\\.jar").matches(jar.toString()) }
            ?.copyTo(Path(windowsDistPath, "mikupush.jar"))
            ?: error("The jar file is missing")

        // https://github.com/JetBrains/JetBrainsRuntime/releases
        URL("https://cache-redirector.jetbrains.com/intellij-jbr/jbr_jcef-17.0.14-windows-x64-b1367.22.zip").run {
            Path(windowsDistPath, "runtime.zip").toFile().run {
                writeBytes(openStream().readAllBytes())
                val unzippedPath = Path(windowsDistPath, "jbr17")
                unzipTo(unzippedPath.toFile(), this)

                @OptIn(ExperimentalPathApi::class)
                Path(unzippedPath.toString(), "jbr_jcef-17.0.14-windows-x64-b1367.22").copyToRecursively(
                    target = Path(windowsDistPath, "runtime"),
                    followLinks = true,
                    overwrite = true
                )

                @OptIn(ExperimentalPathApi::class)
                unzippedPath.deleteRecursively()
            }
        }

        @OptIn(ExperimentalPathApi::class)
        Path(projectDir, "launcher/windows/curl").copyToRecursively(
            target = Path(windowsDistPath, "curl"),
            followLinks = true,
            overwrite = true
        )
//        Path(projectDir, "launcher/windows/MikuPush.exe").copyTo(Path(windowsDistPath, "MikuPush.exe"))
//        Path(projectDir, "launcher/windows/MikuPush-Requester.exe").copyTo(Path(windowsDistPath, "MikuPush-Requester.exe"))
        Path(projectDir, "launcher/windows/icon.ico").copyTo(Path(windowsDistPath, "icon.ico"))

        arrayOf(
            "powershell.exe", "-file", "$rootProjectDir\\scripts\\compile-windows-launchers.ps1",
            "-icon", "$projectDir\\launcher\\windows\\icon.ico",
            "-script", "$projectDir\\launcher\\windows\\launcher.ps1",
            "-target", "$windowsDistPath\\MikuPush.exe"
        ).run {
            println("Running: ${this.joinToString(" ")}")
            Runtime.getRuntime().exec(this).waitFor()
        }

        arrayOf(
            "powershell.exe", "-file", "$rootProjectDir\\scripts\\compile-windows-launchers.ps1",
            "-icon", "$projectDir\\launcher\\windows\\icon.ico",
            "-script", "$projectDir\\launcher\\windows\\upload-request.ps1",
            "-target", "$windowsDistPath\\MikuPush-Requester.exe"
        ).run {
            println("Running: ${this.joinToString(" ")}")
            Runtime.getRuntime().exec(this).waitFor()
        }
    }
}
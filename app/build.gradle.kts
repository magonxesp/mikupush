import org.gradle.kotlin.dsl.support.unzipTo
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.net.URI
import java.net.URL
import java.nio.file.Files
import kotlin.io.path.*

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.gradleup.shadow")
}

group = "io.github.magonxesp"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    val ktor_version = "3.0.3"
    val logback_version = "1.4.14"
    val exposedVersion = "0.58.0"
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.compose.material3:material3-desktop:1.6.11")
    implementation("com.kohlschutter.junixsocket:junixsocket-core:2.9.0")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposedVersion")
    implementation("org.apache.tika:tika-core:3.0.0")
    implementation("org.apache.commons:commons-lang3:3.17.0")
    implementation("com.github.ajalt.clikt:clikt:5.0.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "io.mikupush.MainKt"
    }

    archiveBaseName = "mikupush"
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

        URL("https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.6%2B7/OpenJDK21U-jre_x64_windows_hotspot_21.0.6_7.zip").run {
            Path(windowsDistPath, "runtime.zip").toFile().run {
                writeBytes(openStream().readAllBytes())
                val unzippedPath = Path(windowsDistPath, "jre21")
                unzipTo(unzippedPath.toFile(), this)

                @OptIn(ExperimentalPathApi::class)
                Path(unzippedPath.toString(), "jdk-21.0.6+7-jre").copyToRecursively(
                    target = Path(windowsDistPath, "runtime"),
                    followLinks = true,
                    overwrite = true
                )

                @OptIn(ExperimentalPathApi::class)
                unzippedPath.deleteRecursively()
            }
        }

        Path(projectDir, "launcher/windows/MikuPush.exe").copyTo(Path(windowsDistPath, "MikuPush.exe"))
        Path(projectDir, "launcher/windows/MikuPush-Requester.exe").copyTo(Path(windowsDistPath, "MikuPush-Requester.exe"))
        Path(projectDir, "launcher/windows/icon.ico").copyTo(Path(windowsDistPath, "icon.ico"))
    }
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

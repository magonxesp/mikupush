import org.gradle.kotlin.dsl.support.unzipTo
import java.net.URL
import java.nio.file.Files
import kotlin.io.path.*

apply(plugin = "org.jetbrains.kotlin.jvm")

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
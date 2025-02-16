package io.mikupush.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import org.rauschig.jarchivelib.ArchiveFormat
import org.rauschig.jarchivelib.ArchiverFactory
import org.rauschig.jarchivelib.CompressionType
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.*
import kotlin.math.log

abstract class BuildDistributionTask : DefaultTask() {
    protected fun DistributionTarget.distributionPath(): Path {
        val rootDirPath = project.rootDir.absolutePath
        logger.info("Root project path is ${project.rootDir.absolutePath}")

        val path = Path(rootDirPath, "dist", name.lowercase())
        logger.info("Distribution path is $path")

        return path
    }

    @OptIn(ExperimentalPathApi::class)
    protected fun DistributionTarget.cleanDistributionDir() {
        val path = distributionPath()
        logger.info("Cleaning distribution directory: $path")

        if (path.exists()) {
            path.deleteRecursively()
        }
    }

    protected fun DistributionTarget.createDistributionDir() {
        val path = distributionPath()
        logger.info("Creating distribution dir if not exists: $path")

        if (!path.exists()) {
            path.createDirectories()
        }
    }

    protected fun copyJarToTarget(target: DistributionTarget) {
        val libs = Files.walk(Path(project.projectDir.path, "build", "libs")).toList()
        val jar = libs.find { jar -> Regex(".*mikupush-.*-all\\.jar").matches(jar.toString()) }
            ?: error("The jar file is missing")

        val destination = Path(target.distributionPath().toString(), "mikupush.jar")
        logger.info("Copy jar to distribution target: $destination")
        jar.copyTo(destination)
    }

    @OptIn(ExperimentalPathApi::class)
    protected fun copyRuntimeToDistribution(target: DistributionTarget) {
        val url = URL(target.jreUrl())
        val compressedRuntimeFile = Path(System.getProperty("java.io.tmpdir"), "runtime.tar.gz").toFile()
        val compressedRuntimeDestinationDirectory = Path(System.getProperty("java.io.tmpdir"), "runtime").toFile()

        url.openStream().use {
            compressedRuntimeFile.writeBytes(it.readAllBytes())
        }

        val archiver = ArchiverFactory.createArchiver(ArchiveFormat.TAR, CompressionType.GZIP)
        archiver.extract(compressedRuntimeFile, compressedRuntimeDestinationDirectory)

        val runtimeDir = Path(compressedRuntimeDestinationDirectory.absolutePath, target.uncompressedDir())
        val targetDir = Path(target.distributionPath().toString(), "runtime")
        runtimeDir.copyToRecursively(targetDir, overwrite = true, followLinks = true)

        compressedRuntimeFile.delete()
        compressedRuntimeDestinationDirectory.delete()
    }

    private fun DistributionTarget.jreUrl(): String = when(this) {
        DistributionTarget.WINDOWS_X64 -> "https://cache-redirector.jetbrains.com/intellij-jbr/jbr-17.0.14-windows-x86-b1367.22.tar.gz"
        DistributionTarget.LINUX_X64 -> "https://cache-redirector.jetbrains.com/intellij-jbr/jbr-17.0.14-linux-x86-b1367.22.tar.gz"
        DistributionTarget.MACOS_ARM64 -> "https://cache-redirector.jetbrains.com/intellij-jbr/jbr-17.0.14-osx-aarch64-b1367.22.tar.gz"
        DistributionTarget.MACOS_X86 -> "https://cache-redirector.jetbrains.com/intellij-jbr/jbr-17.0.14-osx-x64-b1367.22.tar.gz"
    }

    private fun DistributionTarget.uncompressedDir(): String = when(this) {
        DistributionTarget.WINDOWS_X64 -> "jbr-17.0.14-windows-x86-b1367.22"
        DistributionTarget.LINUX_X64 -> "jbr-17.0.14-linux-x86-b1367.22"
        DistributionTarget.MACOS_ARM64 -> "jbr-17.0.14-osx-aarch64-b1367.22"
        DistributionTarget.MACOS_X86 -> "jbr-17.0.14-osx-x64-b1367.22"
    }
}
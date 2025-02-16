package io.mikupush.gradle

import org.gradle.api.tasks.TaskAction
import kotlin.io.path.Path
import kotlin.io.path.copyTo

abstract class BuildWindowsDistributionTask : BuildDistributionTask() {
    @TaskAction
    fun executeTask() {
        val rootProjectDir = project.rootProject.rootDir.path
        val projectDir = project.projectDir.path
        val target = DistributionTarget.WINDOWS_X64

        target.cleanDistributionDir()
        target.createDistributionDir()

        copyJarToTarget(target)
        copyRuntimeToDistribution(target)

        Path(rootProjectDir, "launcher/target/release/MikuPush.exe")
            .copyTo(Path(target.distributionPath().toString(), "MikuPush.exe"))

        Path(rootProjectDir, "launcher/target/release/MikuPush-upload.exe")
            .copyTo(Path(target.distributionPath().toString(), "MikuPush-upload.exe"))

        Path(projectDir, "src/main/resources/icon.ico")
            .copyTo(Path(target.distributionPath().toString(), "icon.ico"))
    }
}
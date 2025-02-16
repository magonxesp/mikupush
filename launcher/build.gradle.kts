tasks.register("buildRelease") {
    doLast {
        exec {
            workingDir = projectDir
            commandLine("cargo", "build", "-r")
        }
    }
}
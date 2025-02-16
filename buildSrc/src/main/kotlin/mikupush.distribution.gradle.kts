import io.mikupush.gradle.BuildWindowsDistributionTask

tasks.register<BuildWindowsDistributionTask>("buildWindowsDistribution") {
    dependsOn("shadowJar", ":launcher:buildRelease")
    group = "distribution"
}
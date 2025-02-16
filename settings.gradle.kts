pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "mikupush"

include(":app")
include(":backend")
include(":launcher")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
        maven { url = uri("https://maven.hq.hydraulic.software") }
    }

}
rootProject.name = "compose-music-sample"

include(":desktop")

plugins {
    //kotlin("jvm")
    id("kotlin-multiplatform")
    id("org.jetbrains.compose")
    id("dev.hydraulic.conveyor")
}

kotlin {
    jvm {
        withJava()
    }
    sourceSets {
        named("jvmMain") {}
    }
}
compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            vendor = "Hydraulic"
            description = "An example of how to package a Compose Desktop app with Conveyor"
        }
    }
}

dependencies {
    linuxAmd64(compose.desktop.linux_x64)
    macAmd64(compose.desktop.macos_x64)
    macAarch64(compose.desktop.macos_arm64)
    windowsAmd64(compose.desktop.windows_x64)
}


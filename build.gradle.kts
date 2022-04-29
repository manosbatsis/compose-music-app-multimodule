plugins {
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.compose") version "1.0.1"
    id("dev.hydraulic.conveyor") version "0.9.7"
}

version = "0.9.10"
group = "dev.hydraulic"

repositories {
    mavenCentral()
    google()
    maven {
        url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        name = "Compose for Desktop DEV"
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

tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn", "-Xjvm-default=all")
    }
}

// Runs Conveyor after doing a build.
tasks.register<Exec>("convey") {
    val dir = layout.buildDirectory.dir("conveyor.out")
    outputs.dir(dir)
    commandLine("conveyor", "make", "--output-dir", dir.get(), "site")
    dependsOn("jar", "writeConveyorConfig")
}

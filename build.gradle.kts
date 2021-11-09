import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
    id("org.jetbrains.compose") version "1.0.0-beta6-dev446"
}

group = "software.hydraulic.samples"
version = "1.1"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)

    // TEMP HACK
    implementation("org.jetbrains.skiko:skiko:0.5.10")
    implementation("org.jetbrains.skiko:skiko-jvm-runtime-macos-x64:0.5.10")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}

tasks.register("copyJars", Copy::class.java) {
    val dest = file("$projectDir/build/all-jars")
    into(dest)
    doFirst {
        dest.deleteRecursively()
    }
    from(configurations.runtimeClasspath, tasks.getByName("jar"))
}

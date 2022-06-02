import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    id("org.jetbrains.kotlinx.benchmark") version "0.4.2"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.6.21"
}

group = "codes.spectrum.conf2022"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    val kotestVersion = "5.3.0"
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:0.4.2")
}

tasks.test {
    useJUnitPlatform()
}

benchmark {
    targets {
        register("test")
    }
}

allOpen{
    annotation("org.openjdk.jmh.annotations.State")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:2.13.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}


tasks.create("convertFile", JavaExec::class) {
    val files = mutableListOf<String>()
    if (project.hasProperty("inputFile")) {
        files.add(project.properties["inputFile"].toString())
    } else {
        println("Using default file orders.csv")
        files.add("orders.csv")
    }

    if (project.hasProperty("outputFile")) {
        files.add(project.properties["inputFile"].toString())
    } else {
        println("Using default file output.csv")
        files.add("output.csv")
    }

    if (project.hasProperty("logFile")) {
        files.add(project.properties["inputFile"].toString())
    } else {
        println("Using default file log.txt")
        files.add("log.txt")
    }

    args = files
    mainClass.set("com.csvconverter.Runner")
    classpath = sourceSets["main"].runtimeClasspath
}
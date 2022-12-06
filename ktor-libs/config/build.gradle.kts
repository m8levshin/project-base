plugins {
    application
    kotlin("jvm") version Versions.kotlin
}

dependencies {
    koinDependecies.forEach(this::api)
    ktorServerDependecies.forEach(this::api)

    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.14.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.1")
}
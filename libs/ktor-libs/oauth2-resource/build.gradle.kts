plugins {
    application
    kotlin("jvm") version Versions.kotlin
}

dependencies {
    api(Dependecies.Ktor.Server.server)
    implementation(Dependecies.Kotlin.Koin.koinKtor)
    api(Dependecies.Ktor.Server.authPlugin)
    implementation(Dependecies.Ktor.Server.jwtAuthPlugin)

    implementation(project(":libs:ktor-libs:error"))
    implementation(project(":libs:logging"))
}

repositories {
    mavenCentral()
}
plugins {
    application
    kotlin("jvm") version Versions.kotlin
}

dependencies {
    implementation(Dependecies.Ktor.Server.server)
    implementation(Dependecies.Ktor.Server.statusPage)
}
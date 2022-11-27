plugins {
    application
    kotlin("jvm") version "1.7.21"
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(KtorDeps.ktorServer)
}
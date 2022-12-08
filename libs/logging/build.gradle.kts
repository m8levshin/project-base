plugins {
    id("java")
    kotlin("jvm") version Versions.kotlin
}

dependencies {
    api(Dependecies.Common.Logging.logbackJsonClassic)
    api(Dependecies.Common.Logging.logbackJsonJackson)
}

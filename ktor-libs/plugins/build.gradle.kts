plugins {
    id("java")
    kotlin("jvm") version Versions.kotlin
}

dependencies{
    ktorServerDependecies.forEach(this::api)
    ktorClientDependecies.forEach(this::api)
    openTelemetryDependecies.forEach(this::api)

    api(Dependecies.Common.logbackClassic)
    api(Dependecies.Common.consulApi)
    api(Dependecies.Kotlin.coroutinesSlf4j)
}
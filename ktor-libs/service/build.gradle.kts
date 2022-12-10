plugins {
    id("java")
    kotlin("jvm") version Versions.kotlin
}

dependencies {
    ktorServerDependecies.forEach(this::api)
    ktorClientDependecies.forEach(this::api)
    koinDependecies.forEach(this::api)
    openTelemetryDependecies.forEach(this::api)

    api(project(":libs:logging"))
    api(project(":ktor-libs:error"))
    api(project(":ktor-libs:plugins"))
    api(project(":ktor-libs:config"))

    api(Dependecies.Common.micrometerPrometheusRegistry)

}

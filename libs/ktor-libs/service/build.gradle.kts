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
    api(project(":libs:ktor-libs:error"))
    api(project(":libs:ktor-libs:plugins"))
    api(project(":libs:ktor-libs:config"))
    api(project(":libs:ktor-libs:oauth2-resource"))

    api(Dependecies.Common.micrometerPrometheusRegistry)

}

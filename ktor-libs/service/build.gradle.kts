plugins {
    id("java")
    kotlin("jvm") version Versions.kotlin
}

dependencies {
    ktorServerDependecies.forEach(this::api)
    ktorClientDependecies.forEach(this::api)
    koinDependecies.forEach(this::api)

    api(Dependecies.Common.Logging.logbackClassic)
    api(Dependecies.Common.Logging.logbackJsonClassic)

    api(project(":ktor-libs:error"))
    api(project(":ktor-libs:plugins"))
    api(project(":ktor-libs:config"))
}

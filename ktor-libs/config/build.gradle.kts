plugins {
    application
    kotlin("jvm") version Versions.kotlin
}

dependencies {
    koinDependecies.forEach(this::api)
    ktorServerDependecies.forEach(this::api)

    api(Dependecies.Common.Jackson.moduleKotlin)
    api(Dependecies.Common.Jackson.dataFormatYaml)
}
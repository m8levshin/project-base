plugins {
    application
    kotlin("jvm") version Versions.kotlin
}

dependencies {
    koinDependecies.forEach(this::api)
    ktorServerDependecies.forEach(this::api)


    api(project(":libs:logging"))
    api(Dependecies.Common.Jackson.moduleKotlin)
    api(Dependecies.Common.Jackson.dataFormatYaml)
}
plugins {
    application
    kotlin("jvm") version Versions.kotlin
    id("io.ktor.plugin") version Versions.ktor
    id("org.jetbrains.kotlin.plugin.serialization") version Versions.kotlin
}

dependencies {
    implementation(project(":libs:ktor-libs:service"))
    implementation(project(":libs:ktor-libs:plugins"))
    implementation(project(":libs:ktor-libs:error"))
    implementation(project(":libs:ktor-libs:config"))

    ktorServerDependecies.forEach(this::implementation)
    ktorClientDependecies.forEach(this::implementation)
    koinDependecies.forEach(this::implementation)

    implementation(Dependecies.Common.hibernateValidator)
    implementation(Dependecies.Common.javaxElApi)
    implementation(Dependecies.Common.jakartaEl)
    implementation(Dependecies.Common.apacheCommonLang)

}

application {
    mainClass.set("com.mlevshin.projectbase.KtorExampleServiceKt")
}

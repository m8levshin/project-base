plugins {
    application
    kotlin("jvm") version "1.7.21"
    id("io.ktor.plugin") version "2.1.3"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.21"
}

dependencies {
    implementation(project(":ktor-shared"))
    implementation(project(":ktor-plugins"))

    //Server
    implementation(KtorDeps.ktorServer)
    implementation(KtorDeps.ktorServerNetty)
    implementation(KtorDeps.ktorServerContentNegotiation)
    implementation(KtorDeps.ktorServerCallLogging)
    implementation(KtorDeps.ktorServerCallId)
    implementation(KtorDeps.ktorServerStatusPage)
    implementation(KtorDeps.ktorSessionPlugin)

    //Logging
    implementation(KtorDeps.logbackClassic)
    implementation(KtorDeps.ktorServerHostCommon)
    implementation(KtorDeps.logbackClassic)
    implementation(KtorDeps.ktorServerCallLogging)
    implementation(KtorDeps.kotlinCoroutinesSlf4j)

    //Testing
    implementation(KtorDeps.ktorServerTestsJvm)
    implementation(KtorDeps.kotlinTestJunit)

    //Ktor client
    implementation(KtorDeps.ktorClientJava)
    implementation(KtorDeps.ktorClientContentNegotiation)

    //Validation
    implementation(KtorDeps.hibernateValidator)
    implementation(KtorDeps.javaxElApi)
    implementation(KtorDeps.jakartaEl)


    //Koin
    implementation(KtorDeps.koinCore)
    implementation(KtorDeps.koinKtor)
    implementation(KtorDeps.koinLoggerSlf4f)
    implementation(KtorDeps.koinTest)
    implementation(KtorDeps.koinTestJunit5)


    //Serialization
    implementation(KtorDeps.ktorSerialization)
    implementation(KtorDeps.jacksonDataBind)

    implementation(KtorDeps.commonLang)

}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

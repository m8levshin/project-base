plugins {
    id("java")
    kotlin("jvm") version kotlin_version
}


dependencies {
    compileOnly(KtorDeps.ktorClientJava)
    compileOnly(KtorDeps.logbackClassic)
    compileOnly(KtorDeps.ktorServer)


    implementation(KtorDeps.kotlinCoroutinesSlf4j)
    implementation(KtorDeps.consulApi)

    compileOnly(CommonDeps.openTelemetryApi)
    compileOnly(CommonDeps.openTelemetrySdk)
    compileOnly(CommonDeps.openTelemetryExtenionKotlin)
    compileOnly(CommonDeps.openTelemetryExporterLogging)

}
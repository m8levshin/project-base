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
}
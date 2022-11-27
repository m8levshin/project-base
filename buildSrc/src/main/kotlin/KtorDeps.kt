const val ktor_version = "2.1.3"
const val kotlin_version = "1.7.21"
const val logback_version = "1.2.11"
const val koin_version = "3.2.2"
const val hibernate_validator_version = "8.0.0.Final"

object KtorDeps {

    //Server
    const val ktorServer = "io.ktor:ktor-server-core-jvm:$ktor_version"
    const val ktorServerNetty = "io.ktor:ktor-server-netty-jvm:$ktor_version"
    const val ktorServerContentNegotiation = "io.ktor:ktor-server-content-negotiation-jvm:$ktor_version"
    const val ktorServerStatusPage = "io.ktor:ktor-server-status-pages:$ktor_version"

    //Session
    const val ktorSessionPlugin = "io.ktor:ktor-server-sessions:$ktor_version"

    //Logging
    const val logbackClassic = "ch.qos.logback:logback-classic:$logback_version"
    const val ktorServerCallLogging = "io.ktor:ktor-server-call-logging:$ktor_version"
    const val ktorServerHostCommon = "io.ktor:ktor-server-host-common:$ktor_version"
    const val kotlinCoroutinesSlf4j = "org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:1.6.4"
    const val ktorServerCallId = "io.ktor:ktor-server-call-id:$ktor_version"

    //Testing
    const val ktorServerTestsJvm = "io.ktor:ktor-server-tests-jvm:$ktor_version"
    const val kotlinTestJunit = "org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version"

    //Ktor client
    const val ktorClientJava = "io.ktor:ktor-client-java:$ktor_version"
    const val ktorClientContentNegotiation = "io.ktor:ktor-client-content-negotiation:$ktor_version"

    //Validation
    const val hibernateValidator = "org.hibernate.validator:hibernate-validator:$hibernate_validator_version"
    const val javaxElApi = "javax.el:javax.el-api:3.0.0"
    const val jakartaEl = "org.glassfish:jakarta.el:4.0.2"

    //Koin
    const val koinCore = "io.insert-koin:koin-core:$koin_version"
    const val koinKtor = "io.insert-koin:koin-ktor:$koin_version"
    const val koinLoggerSlf4f = "io.insert-koin:koin-logger-slf4j:$koin_version"
    const val koinTest = "io.insert-koin:koin-test:$koin_version"
    const val koinTestJunit5 = "io.insert-koin:koin-test-junit5:$koin_version"

    //Consul
    const val consulApi = "com.ecwid.consul:consul-api:1.4.5"

    //Serialization
    const val ktorSerialization = "io.ktor:ktor-serialization-jackson:$ktor_version"
    const val jacksonDataBind = "com.fasterxml.jackson.core:jackson-databind:2.14.0"

    const val commonLang = "org.apache.commons:commons-lang3:3.12.0";

}

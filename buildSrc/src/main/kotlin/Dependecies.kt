object Dependecies {

    object Kotlin {
        const val coroutinesSlf4j = "org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:1.6.4"

        object Koin {
            private const val koinVersion = Versions.koin
            const val koinCore = "io.insert-koin:koin-core:$koinVersion"
            const val koinKtor = "io.insert-koin:koin-ktor:$koinVersion"
            const val koinLoggerSlf4f = "io.insert-koin:koin-logger-slf4j:$koinVersion"
            const val koinTest = "io.insert-koin:koin-test:$koinVersion"
            const val koinTestJunit5 = "io.insert-koin:koin-test-junit5:$koinVersion"
        }

    }

    object Common {
        const val hibernateValidator = "org.hibernate.validator:hibernate-validator:${Versions.hibernateValidator}"
        const val javaxElApi = "javax.el:javax.el-api:3.0.0"
        const val jakartaEl = "org.glassfish:jakarta.el:4.0.2"
        const val consulApi = "com.ecwid.consul:consul-api:1.4.5"
        const val apacheCommonLang = "org.apache.commons:commons-lang3:3.12.0";
        const val micrometerPrometheusRegistry =
            "io.micrometer:micrometer-registry-prometheus:${Versions.prometheusVersion}"
        const val auth0JwksRsa = "com.auth0:jwks-rsa:0.21.2"

        object Jackson {
            private const val jacksonVersion = Versions.jackson
            const val moduleKotlin = "com.fasterxml.jackson.module:jackson-module-kotlin:${jacksonVersion}"
            const val dataFormatYaml = "com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:${jacksonVersion}"
            const val dataBind = "com.fasterxml.jackson.core:jackson-databind:${jacksonVersion}"
        }

        object Logging {
            const val logbackClassic = "ch.qos.logback:logback-classic:${Versions.logback}"
            const val logbackJsonClassic = "ch.qos.logback.contrib:logback-json-classic:0.1.5"
            const val logbackJsonJackson = "ch.qos.logback.contrib:logback-jackson:0.1.5"
        }

        object OpenTelemtry {
            private const val openTelemetryVersoin = Versions.openTelemetry
            const val openTelemetryApi = "io.opentelemetry:opentelemetry-api:$openTelemetryVersoin"
            const val openTelemetrySdk = "io.opentelemetry:opentelemetry-sdk:$openTelemetryVersoin"
            const val openTelemetryExporterLogging =
                "io.opentelemetry:opentelemetry-exporter-logging:$openTelemetryVersoin"
            const val openTelemetryExtenionKotlin =
                "io.opentelemetry:opentelemetry-extension-kotlin:$openTelemetryVersoin"
            const val openTelemetryExporterOtlp = "io.opentelemetry:opentelemetry-exporter-otlp:$openTelemetryVersoin"
            const val openTelemetrySemconv = "io.opentelemetry:opentelemetry-semconv:${Versions.openTelemetry}-alpha"
        }
    }

    object Spring {
        const val authServer = "org.springframework.security:spring-security-oauth2-authorization-server:1.0.0"
    }

    object Ktor {
        private const val ktorVersion = Versions.ktor

        object Server {
            const val server = "io.ktor:ktor-server-core-jvm:$ktorVersion"
            const val netty = "io.ktor:ktor-server-netty-jvm:$ktorVersion"
            const val contentNegotiation = "io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion"
            const val jacksonSerialization = "io.ktor:ktor-serialization-jackson:$ktorVersion"
            const val hostCommon = "io.ktor:ktor-server-host-common:$ktorVersion"
            const val statusPage = "io.ktor:ktor-server-status-pages:$ktorVersion"
            const val callLoggingPlugin = "io.ktor:ktor-server-call-logging:$ktorVersion"
            const val sessionPlugin = "io.ktor:ktor-server-sessions:$ktorVersion"
            const val callIdPlugin = "io.ktor:ktor-server-call-id:$ktorVersion"
            const val micrometerPlugin = "io.ktor:ktor-server-metrics-micrometer:$ktorVersion"
            const val authPlugin = "io.ktor:ktor-server-auth:$ktorVersion"
            const val jwtAuthPlugin = "io.ktor:ktor-server-auth-jwt:$ktorVersion"
        }

        object Client {
            const val ktorClientJava = "io.ktor:ktor-client-java:$ktorVersion"
            const val ktorClientContentNegotiation = "io.ktor:ktor-client-content-negotiation:$ktorVersion"
        }

        object Testing {
            const val ktorServerTestsJvm = "io.ktor:ktor-server-tests-jvm:$ktorVersion"
            const val kotlinTestJunit = "org.jetbrains.kotlin:kotlin-test-junit:$ktorVersion"
        }

    }
}
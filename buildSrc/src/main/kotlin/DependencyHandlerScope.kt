import org.gradle.kotlin.dsl.DependencyHandlerScope

val DependencyHandlerScope.ktorServerDependecies
    get() = setOf(
        Dependecies.Ktor.Server.server,
        Dependecies.Ktor.Server.contentNegotiation,

        Dependecies.Ktor.Server.callIdPlugin,
        Dependecies.Ktor.Server.hostCommon,
        Dependecies.Ktor.Server.netty,
        Dependecies.Ktor.Server.jacksonSerialization,
        Dependecies.Ktor.Server.callLoggingPlugin,
        Dependecies.Ktor.Server.statusPage,
        Dependecies.Ktor.Server.sessionPlugin,
    )

val DependencyHandlerScope.ktorClientDependecies
    get() = setOf(
        Dependecies.Ktor.Client.ktorClientJava,
        Dependecies.Ktor.Client.ktorClientContentNegotiation,
    )

val DependencyHandlerScope.koinDependecies
    get() = setOf(
        Dependecies.Kotlin.Koin.koinCore,
        Dependecies.Kotlin.Koin.koinKtor,
        Dependecies.Kotlin.Koin.koinTest,
        Dependecies.Kotlin.Koin.koinLoggerSlf4f,
        Dependecies.Kotlin.Koin.koinTestJunit5,
)

val DependencyHandlerScope.openTelemetryDependecies get() = setOf(
    Dependecies.Common.OpenTelemtry.openTelemetryApi,
    Dependecies.Common.OpenTelemtry.openTelemetrySdk,
    Dependecies.Common.OpenTelemtry.openTelemetryExporterLogging,
    Dependecies.Common.OpenTelemtry.openTelemetryExtenionKotlin,
)
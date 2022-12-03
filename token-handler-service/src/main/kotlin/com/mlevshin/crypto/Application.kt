package com.mlevshin.crypto


import com.mlevshin.crypto.config.configureErrorHandling
import com.mlevshin.crypto.config.configureKoinComponents
import com.mlevshin.crypto.config.configureOpenTelemetryTracing
import com.mlevshin.crypto.config.configureRequestLogging
import com.mlevshin.crypto.config.configureRouting
import com.mlevshin.crypto.config.configureSecurity
import com.mlevshin.crypto.config.configureSerialization
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.personServiceModule() {

    install(Koin) {
        slf4jLogger()
        modules(configureKoinComponents())
    }
    configureOpenTelemetryTracing()
    configureSerialization()
    configureRouting()
    configureSecurity()
    configureRequestLogging()
    configureErrorHandling()
}

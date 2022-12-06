package com.mlevshin.base


import com.mlevshin.base.config.OAuthConfig
import com.mlevshin.base.config.SessionConfig
import com.mlevshin.base.config.addConfigBinding
import com.mlevshin.base.config.configureErrorHandling
import com.mlevshin.base.config.configureKoinComponents
import com.mlevshin.base.config.configureOpenTelemetryTracing
import com.mlevshin.base.config.configureRequestLogging
import com.mlevshin.base.config.configureRouting
import com.mlevshin.base.config.configureSecurity
import com.mlevshin.base.config.configureSerialization
import com.mlevshin.base.config.initConfiguration
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.tokenHandlerService() {
    install(Koin) {
        slf4jLogger()
        modules(
            initConfiguration("test") {
                addConfigBinding("security.oauth2", OAuthConfig::class.java)
                addConfigBinding("security.session", SessionConfig::class.java)
            },
            configureKoinComponents()
        )
    }
    configureOpenTelemetryTracing()
    configureSerialization()
    configureRouting()
    configureSecurity()
    configureRequestLogging()
    configureErrorHandling()
}

package com.mlevshin.base.service

import com.mlevshin.base.config.buildConfigurationModule
import com.mlevshin.base.config.buildOpenTelemetryModule
import com.mlevshin.base.config.configureErrorHandling
import com.mlevshin.base.config.configureOpenTelemetryTracing
import com.mlevshin.base.config.configureRequestLogging
import com.mlevshin.base.config.configureSerialization
import com.mlevshin.base.service.config.buildHttpClientModule
import io.ktor.server.application.*
import org.koin.core.module.Module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger


fun Application.setup(
    serviceName: String,
    environment: String,
    serviceModules: List<Module>,
    initConfiguration: Module.() -> Unit = {}
) {
    install(Koin) {
        val configurationModule = buildConfigurationModule(environment) {
            initConfiguration()
        }
        slf4jLogger()
        modules(configurationModule)
        modules(buildOpenTelemetryModule(serviceName))
        modules(buildHttpClientModule())
        modules(serviceModules)
    }
    configureOpenTelemetryTracing()
    configureSerialization()
    configureRequestLogging()
    configureErrorHandling()
}


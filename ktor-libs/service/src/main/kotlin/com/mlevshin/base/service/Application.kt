package com.mlevshin.base.service

import com.mlevshin.base.config.addConfigBinding
import com.mlevshin.base.config.buildConfigurationModule
import com.mlevshin.base.config.buildOpenTelemetryModule
import com.mlevshin.base.config.configureErrorHandling
import com.mlevshin.base.config.configureOpenTelemetryTracing
import com.mlevshin.base.config.configureRequestLogging
import com.mlevshin.base.config.configureSerialization
import com.mlevshin.base.service.config.ServiceConfig
import com.mlevshin.base.service.config.buildHttpClientModule
import com.mlevshin.projectbase.commonlibs.logging.ServiceJsonLayout
import io.ktor.server.application.*
import org.koin.core.module.Module
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger


fun Application.setup(
    environment: String,
    serviceModules: List<Module>,
    initConfiguration: Module.() -> Unit = {}
) {
    install(Koin) {
        var serviceConfig: ServiceConfig? = null
        val configurationModule = buildConfigurationModule(environment) {
            initConfiguration()
            serviceConfig = addConfigBinding("service", ServiceConfig::class.java)
        }

        serviceConfig ?: throw AssertionError("Service configuration should be initialized.")

        slf4jLogger()
        modules(configurationModule)
        modules(buildOpenTelemetryModule(serviceConfig!!))
        modules(buildHttpClientModule())
        modules(serviceModules)
    }
    val serviceConfig : ServiceConfig by inject()
    ServiceJsonLayout.serviceName = serviceConfig.name
    configureOpenTelemetryTracing()
    configureSerialization()
    configureRequestLogging()
    configureErrorHandling()
}


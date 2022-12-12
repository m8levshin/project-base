package com.mlevshin.projectbase.service

import com.mlevshin.projectbase.commonlibs.logging.utils.logger
import com.mlevshin.projectbase.config.PropertiesKoinModuleInitializer
import com.mlevshin.projectbase.config.buildOpenTelemetryModule
import com.mlevshin.projectbase.config.configureErrorHandling
import com.mlevshin.projectbase.config.configureOpenTelemetryTracing
import com.mlevshin.projectbase.config.configureRequestLogging
import com.mlevshin.projectbase.config.configureSerialization
import com.mlevshin.projectbase.security.oauth2.resource.JwkProviderProperties
import com.mlevshin.projectbase.security.oauth2.resource.configureResourceServerSecurity
import com.mlevshin.projectbase.service.config.ServiceProperties
import com.mlevshin.projectbase.service.config.buildHttpClientModule
import com.mlevshin.projectbase.service.config.configureMetrics
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.core.module.Module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

private const val CURRENT_ENV_SYSENV_NAME = "KTOR_PROFILES_ACTIVE"
private val log = logger(object {}::class)
fun configureAndRunKtorApplication(
    profile: String? = null,
    serviceModules: List<Module> = emptyList(),
    initAppProperties: PropertiesKoinModuleInitializer.() -> Unit = {},
    appSpecificKtorConfiguration: Application.() -> Unit = {}
) {
    val currentProfile = profile ?: resolveCurrentProfileName()
    log.info("Initializing service for $currentProfile profile.")
    val propertiesKoinModuleInitializer = PropertiesKoinModuleInitializer(currentProfile)
    with(propertiesKoinModuleInitializer) {
        addPropertiesBinding("service", ServiceProperties::class)
        addPropertiesBinding("security.oauth2.resource", JwkProviderProperties::class)
        initAppProperties()
    }
    val serviceProperties = propertiesKoinModuleInitializer.getProperties(ServiceProperties::class)

    embeddedServer(
        Netty,
        port = serviceProperties?.port ?: 8080,
        module = {
            configureService(appSpecificKtorConfiguration, serviceModules, propertiesKoinModuleInitializer) }
    ).start(wait = true)
}

fun Application.configureService(
    appKtorConfiguration: Application.() -> Unit,
    serviceModuleList: List<Module>,
    propertiesKoinModuleInitializer: PropertiesKoinModuleInitializer
) {
    val serviceProperties = propertiesKoinModuleInitializer.getProperties(ServiceProperties::class)
    install(Koin) {
        slf4jLogger()
        modules(propertiesKoinModuleInitializer.configurationModule)
        modules(buildOpenTelemetryModule(serviceProperties))
        modules(buildHttpClientModule())
        modules(serviceModuleList)
    }

    configureMetrics()
    serviceProperties?.tracing?.let { configureOpenTelemetryTracing() }
    configureSerialization()
    configureRequestLogging()
    configureErrorHandling()
    configureResourceServerSecurity()
    appKtorConfiguration()
}

fun resolveCurrentProfileName(): String {
    return System.getenv(CURRENT_ENV_SYSENV_NAME)
        ?: throw IllegalStateException("Can't run application without specification of current profile.")
}


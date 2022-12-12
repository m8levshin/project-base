package com.mlevshin.projectbase


import com.mlevshin.projectbase.config.OAuthConfig
import com.mlevshin.projectbase.config.SessionProperties
import com.mlevshin.projectbase.config.configureRouting
import com.mlevshin.projectbase.config.configureSecurity
import com.mlevshin.projectbase.service.configureAndRunKtorApplication

fun main() =
    configureAndRunKtorApplication(
        serviceModules = listOf(
            tokenHandlerServiceModule()
        ),
        initAppProperties = {
            addPropertiesBinding("security.oauth2", OAuthConfig::class)
            addPropertiesBinding("security.session", SessionProperties::class)
        },
        appSpecificKtorConfiguration = {
            configureRouting()
            configureSecurity()
        }
    )



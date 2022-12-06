package com.mlevshin.base


import com.mlevshin.base.config.OAuthConfig
import com.mlevshin.base.config.SessionConfig
import com.mlevshin.base.config.addConfigBinding
import com.mlevshin.base.config.configureRouting
import com.mlevshin.base.config.configureSecurity
import com.mlevshin.base.service.setup
import io.ktor.server.application.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.tokenHandlerService() {
    setup(
        "token-handler-service",
        "test",
        listOf(
            tokenHandlerServiceModule()
        )
    ) {
        addConfigBinding("security.oauth2", OAuthConfig::class.java)
        addConfigBinding("security.session", SessionConfig::class.java)
    }
    configureRouting()
    configureSecurity()
}

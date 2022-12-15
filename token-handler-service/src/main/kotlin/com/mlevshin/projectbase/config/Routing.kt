package com.mlevshin.projectbase.config

import com.mlevshin.projectbase.api.configureTokenHandlerRouting
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        configureTokenHandlerRouting()
    }
}

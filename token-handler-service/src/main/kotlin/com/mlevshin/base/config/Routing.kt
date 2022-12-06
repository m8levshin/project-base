package com.mlevshin.base.config

import com.mlevshin.base.api.configureTokenHandlerRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        configureTokenHandlerRouting()
    }
}

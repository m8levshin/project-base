package com.mlevshin.crypto.config

import com.mlevshin.crypto.api.configureTokenHandlerRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        configureTokenHandlerRouting()
    }
}

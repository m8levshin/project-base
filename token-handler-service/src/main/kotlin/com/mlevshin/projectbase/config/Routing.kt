package com.mlevshin.projectbase.config

import com.mlevshin.projectbase.api.configureTokenHandlerRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        configureTokenHandlerRouting()
    }
}

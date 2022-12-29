package com.mlevshin.projectbase.config

import com.mlevshin.projectbase.api.configureTestRouting
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        configureTestRouting()
    }
}

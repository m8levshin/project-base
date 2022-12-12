package com.mlevshin.projectbase.config

import com.mlevshin.projectbase.api.configureTestRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        configureTestRouting()
    }
}

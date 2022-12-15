package com.mlevshin.projectbase.config

import com.mlevshin.projectbase.error.ErrorResponse
import com.mlevshin.projectbase.error.exception.ApplicationException
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond

fun Application.configureErrorHandling() {
    install(StatusPages) {
        exception<ApplicationException> { call, cause ->
            call.respond(cause.httpStatusCode, ErrorResponse(cause.errorMessages))
        }
    }
}
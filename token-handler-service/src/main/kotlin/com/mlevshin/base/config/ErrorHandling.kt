package com.mlevshin.base.config

import com.mlevshin.base.error.ErrorResponse
import com.mlevshin.base.error.exception.ApplicationException
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureErrorHandling() {
    install(StatusPages) {
        exception<ApplicationException> { call, cause ->
            call.respond(cause.httpStatusCode, ErrorResponse(cause.errorMessages))
        }
    }
}
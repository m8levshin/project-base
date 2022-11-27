package com.mlevshin.crypto.config

import com.mlevshin.crypto.shared.ErrorResponse
import com.mlevshin.crypto.shared.exception.ApplicationException
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
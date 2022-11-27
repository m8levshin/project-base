package com.mlevshin.crypto.config

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import org.slf4j.MDC
import org.slf4j.event.Level

private const val REQUEST_ID_LENGTH = 20
private const val REQUEST_ID_MDC_KEY = "requestId"

fun Application.configureRequestLogging() {
    install(CallLogging) {
        callIdMdc(REQUEST_ID_MDC_KEY)
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(CallId) {
        generate(REQUEST_ID_LENGTH)
        header(HttpHeaders.XRequestId)
        verify { callId: String ->
            callId.isNotEmpty()
        }
    }
}
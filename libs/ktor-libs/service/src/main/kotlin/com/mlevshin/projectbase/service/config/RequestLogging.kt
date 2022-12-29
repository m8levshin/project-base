package com.mlevshin.projectbase.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.mlevshin.projectbase.error.plugins.server.requestlogging.CallLogging
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.request.path
import org.koin.ktor.ext.inject
import org.slf4j.event.Level


fun Application.configureRequestLogging() {
    val jsonObjectMapper: ObjectMapper by inject()

    install(CallLogging) {
        objectMapper = jsonObjectMapper
        level = Level.INFO
        filters.add { call -> call.request.path().startsWith("/api") }
    }
}
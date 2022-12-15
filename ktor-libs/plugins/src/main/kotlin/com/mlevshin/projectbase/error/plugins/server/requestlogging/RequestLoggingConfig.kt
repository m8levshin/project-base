package com.mlevshin.projectbase.error.plugins.server.requestlogging

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.server.application.ApplicationCall
import org.slf4j.Logger
import org.slf4j.event.Level

class CallLoggingConfig {
    val filters = mutableListOf<(ApplicationCall) -> Boolean>()
    var level: Level = Level.INFO
    var logger: Logger? = null
    var objectMapper = ObjectMapper()
}


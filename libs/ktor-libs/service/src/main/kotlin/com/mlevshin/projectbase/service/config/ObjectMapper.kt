package com.mlevshin.projectbase.service.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.server.application.Application
import org.koin.dsl.module

fun Application.buildJacksonObjectMapper() = module {
    single { ObjectMapper() }
}
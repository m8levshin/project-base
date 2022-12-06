package com.mlevshin.base.service.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.mlevshin.base.error.plugins.client.OpenTelemetryClientPlugin
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.context.propagation.TextMapSetter
import org.koin.dsl.module
import org.koin.ktor.ext.inject


fun Application.buildHttpClientModule() = module {
    val openTelemetryParam: OpenTelemetry by inject()
    val textMapSetterParam: TextMapSetter<HttpRequestBuilder> by inject()
    single { buildCommonHttpClient(openTelemetryParam, textMapSetterParam) }
}

private fun buildCommonHttpClient(
    openTelemetryParam: OpenTelemetry,
    textMapSetterParam: TextMapSetter<HttpRequestBuilder>
): HttpClient {
    return HttpClient() {
        install(ContentNegotiation) {
            jackson() {
                configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            }
        }
        install(OpenTelemetryClientPlugin) {
            telemetry = openTelemetryParam
            textMapSetter = textMapSetterParam
        }

    }
}
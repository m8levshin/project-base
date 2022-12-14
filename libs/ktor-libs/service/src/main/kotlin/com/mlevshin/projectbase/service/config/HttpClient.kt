package com.mlevshin.projectbase.service.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.mlevshin.projectbase.error.plugins.client.OpenTelemetryClientPlugin
import com.mlevshin.projectbase.service.injectOrNull
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.context.propagation.TextMapSetter
import org.koin.dsl.module


fun Application.buildHttpClientModule() = module {
    val openTelemetryParam: OpenTelemetry? by injectOrNull()
    val textMapSetterParam: TextMapSetter<HttpRequestBuilder>? by injectOrNull()
    single { buildCommonHttpClient(openTelemetryParam, textMapSetterParam) }
}

private fun buildCommonHttpClient(
    openTelemetryParam: OpenTelemetry?,
    textMapSetterParam: TextMapSetter<HttpRequestBuilder>?
): HttpClient {
    return HttpClient() {
        install(ContentNegotiation) {
            jackson() {
                configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            }
        }
        openTelemetryParam?.let {
            install(OpenTelemetryClientPlugin) {
                telemetry = openTelemetryParam
                textMapSetter = textMapSetterParam!!
            }
        }
    }
}
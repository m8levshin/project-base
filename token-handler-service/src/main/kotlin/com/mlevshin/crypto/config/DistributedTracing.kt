package com.mlevshin.crypto.config

import com.mlevshin.crypto.shared.plugins.OpenTelemetryUtils
import com.mlevshin.crypto.shared.plugins.server.OpenTelemetryServerPluginBuilder
import io.ktor.server.application.*
import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.api.trace.Tracer
import org.koin.ktor.ext.inject

fun Application.configureOpenTelemetryTracing() {

    val openTelemetry : OpenTelemetry by inject()
    val tracer : Tracer by inject()

    val openTelemetryServerPlugin = OpenTelemetryServerPluginBuilder(
        openTelemetry,
        tracer,
        OpenTelemetryUtils.httpHeaderTextMapGetter
    ).build()

    install(openTelemetryServerPlugin)
}
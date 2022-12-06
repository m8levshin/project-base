package com.mlevshin.base.config

import com.mlevshin.base.error.plugins.KtorTextMapGetter
import com.mlevshin.base.error.plugins.KtorTextMapSetter
import com.mlevshin.base.error.plugins.server.OpenTelemetryServerPluginBuilder
import io.ktor.client.request.*
import io.ktor.server.application.*
import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator
import io.opentelemetry.context.propagation.ContextPropagators
import io.opentelemetry.context.propagation.TextMapGetter
import io.opentelemetry.context.propagation.TextMapSetter
import io.opentelemetry.exporter.logging.LoggingSpanExporter
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.ktor.ext.inject


private fun initOpenTelemetry(): OpenTelemetry {
    val sdkTracerProvider: SdkTracerProvider = SdkTracerProvider.builder()
        .addSpanProcessor(SimpleSpanProcessor.create(LoggingSpanExporter.create()))
        .build()
    val sdk: OpenTelemetrySdk = OpenTelemetrySdk.builder()
        .setTracerProvider(sdkTracerProvider)
        .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
        .build()
    Runtime.getRuntime().addShutdownHook(Thread(sdkTracerProvider::close))
    return sdk
}

private fun initTracerApi(openTelemetry: OpenTelemetry, serviceName: String) =
    openTelemetry.getTracer(serviceName)


fun buildOpenTelemetryModule(serviceName: String): Module = module {
    single<TextMapSetter<HttpRequestBuilder>> { KtorTextMapSetter }
    single<TextMapGetter<ApplicationCall>> { KtorTextMapGetter }
    single<OpenTelemetry> { initOpenTelemetry() }
    single<Tracer> { initTracerApi(get(), serviceName) }
}

fun Application.configureOpenTelemetryTracing() {

    val openTelemetry: OpenTelemetry by inject()
    val tracer: Tracer by inject()
    val textMapGetter: TextMapGetter<ApplicationCall> by inject()

    val openTelemetryServerPlugin = OpenTelemetryServerPluginBuilder(
        openTelemetry,
        tracer,
        textMapGetter
    ).build()

    install(openTelemetryServerPlugin)
}
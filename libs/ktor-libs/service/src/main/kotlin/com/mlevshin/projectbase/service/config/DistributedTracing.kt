package com.mlevshin.projectbase.config

import com.mlevshin.projectbase.error.plugins.KtorTextMapGetter
import com.mlevshin.projectbase.error.plugins.KtorTextMapSetter
import com.mlevshin.projectbase.error.plugins.server.OpenTelemetryServerPlugin
import com.mlevshin.projectbase.service.config.ServiceProperties
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.install
import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.api.common.Attributes
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator
import io.opentelemetry.context.propagation.ContextPropagators
import io.opentelemetry.context.propagation.TextMapGetter
import io.opentelemetry.context.propagation.TextMapSetter
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.resources.Resource
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.ktor.ext.inject
import java.net.InetAddress


private fun initOpenTelemetry(serviceProperties: ServiceProperties): OpenTelemetry {

    val otlpGrpcSpanExporter = OtlpGrpcSpanExporter.builder()
        .setEndpoint(serviceProperties.tracing!!.otelEndpoint)
        .build()

    val sdkTracerProvider: SdkTracerProvider = SdkTracerProvider.builder()
        .setResource(generateResource(serviceProperties.name))
        .addSpanProcessor(BatchSpanProcessor.builder(otlpGrpcSpanExporter).build())
        .build()
    val sdk: OpenTelemetrySdk = OpenTelemetrySdk.builder()
        .setTracerProvider(sdkTracerProvider)
        .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
        .build()
    Runtime.getRuntime().addShutdownHook(Thread(sdkTracerProvider::close))
    return sdk
}

private fun generateResource(serviceName: String): Resource =
    Resource.create(Attributes.of(
        ResourceAttributes.SERVICE_NAME, serviceName,
        ResourceAttributes.HOST_NAME, InetAddress.getLocalHost().hostName,
    ))

private fun initTracerApi(openTelemetry: OpenTelemetry, serviceName: String) =
    openTelemetry.getTracer(serviceName)


fun buildOpenTelemetryModule(serviceProperties: ServiceProperties?): Module = module {
    serviceProperties?.tracing?.let {
        single<TextMapSetter<HttpRequestBuilder>> { KtorTextMapSetter }
        single<TextMapGetter<ApplicationCall>> { KtorTextMapGetter }
        single<OpenTelemetry> { initOpenTelemetry(serviceProperties) }
        single<Tracer> { initTracerApi(get(), serviceProperties.name) }
    }
}

fun Application.configureOpenTelemetryTracing() {

    val openTelemetry: OpenTelemetry by inject()
    val tracerApi: Tracer by inject()
    val textTelemetryMapGetter: TextMapGetter<ApplicationCall> by inject()

    install(OpenTelemetryServerPlugin) {
        telemetry = openTelemetry
        tracer = tracerApi
        textMapGetter = textTelemetryMapGetter
    }
}
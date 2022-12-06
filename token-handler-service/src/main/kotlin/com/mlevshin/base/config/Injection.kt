package com.mlevshin.base.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.mlevshin.base.error.plugins.KtorTextMapGetter
import com.mlevshin.base.error.plugins.KtorTextMapSetter
import com.mlevshin.base.error.plugins.client.OpenTelemetryClientPlugin
import com.mlevshin.base.service.AccessTokenService
import com.mlevshin.base.service.RefreshTokenService
import com.mlevshin.base.service.impl.AccessTokenServiceImpl
import com.mlevshin.base.service.impl.RefreshTokenServiceImpl
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.jackson.*
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
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.koin.core.module.Module
import org.koin.dsl.module

fun Application.configureKoinComponents(): Module {

    return module {
        single<Validator> { Validation.buildDefaultValidatorFactory().validator }
        single<RefreshTokenService> { buildRefreshTokenServiceImpl(get(), get()) }
        single<AccessTokenService> { buildAccessTokenServiceImpl(get(), get()) }

        single<TextMapSetter<HttpRequestBuilder>> { KtorTextMapSetter }
        single<TextMapGetter<ApplicationCall>> { KtorTextMapGetter }
        single<OpenTelemetry> { initOpenTelemetry() }
        single<Tracer> { initTracerApi(get()) }

        single<HttpClient> { buildCommonHttpClient(get(), get()) }
    }
}


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

private fun initTracerApi(openTelemetry: OpenTelemetry) =
    openTelemetry.getTracer("com.mlevshin.project-base.token-handler-service")


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

private fun buildRefreshTokenServiceImpl(httpClient: HttpClient, oAuthConfig: OAuthConfig) =
    RefreshTokenServiceImpl(httpClient, oAuthConfig)

private fun buildAccessTokenServiceImpl(httpClient: HttpClient, oAuthConfig: OAuthConfig) =
    AccessTokenServiceImpl(httpClient, oAuthConfig);
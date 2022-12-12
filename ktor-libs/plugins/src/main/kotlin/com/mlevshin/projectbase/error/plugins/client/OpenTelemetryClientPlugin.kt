package com.mlevshin.projectbase.error.plugins.client

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.util.*
import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.context.Context
import io.opentelemetry.context.propagation.TextMapSetter

private const val OPEN_TELEMETRY_CLIENT_PLUGIN = "OpenTelemetryClientPlugin"

/**
 * OpenTelemetryClientPlugin inject current span to a HttpClient request (headers).
 */
class OpenTelemetryClientPlugin(
    val telemetry: OpenTelemetry, val textMapSetter: TextMapSetter<HttpRequestBuilder>
) {

    class Config {
        lateinit var telemetry: OpenTelemetry
        lateinit var textMapSetter: TextMapSetter<HttpRequestBuilder>
        fun build(): OpenTelemetryClientPlugin = OpenTelemetryClientPlugin(telemetry, textMapSetter)
    }

    companion object Feature : HttpClientPlugin<Config, OpenTelemetryClientPlugin> {

        override val key = AttributeKey<OpenTelemetryClientPlugin>(OPEN_TELEMETRY_CLIENT_PLUGIN)

        override fun prepare(block: Config.() -> Unit): OpenTelemetryClientPlugin = Config().apply(block).build()

        override fun install(plugin: OpenTelemetryClientPlugin, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.Render) {
                with(plugin) {
                    telemetry.propagators.textMapPropagator.inject(Context.current(), context, textMapSetter)
                }
            }
        }
    }
}
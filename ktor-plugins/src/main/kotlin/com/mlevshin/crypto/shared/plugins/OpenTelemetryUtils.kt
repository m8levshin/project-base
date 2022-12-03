package com.mlevshin.crypto.shared.plugins

import io.ktor.client.request.*
import io.ktor.server.application.*
import io.ktor.util.*
import io.opentelemetry.context.propagation.TextMapGetter
import io.opentelemetry.context.propagation.TextMapSetter

object OpenTelemetryUtils {
    val httpHeaderTextMapGetter = object : TextMapGetter<ApplicationCall> {
        override fun keys(call: ApplicationCall): Iterable<String> {
            return call.request.headers.toMap().keys
        }
        override fun get(call: ApplicationCall?, key: String): String? = call?.request?.headers?.get(key)
    }
    val httpHeaderTextMapSetter =
        TextMapSetter<HttpRequestBuilder> { carrier, key, value -> carrier?.headers?.append(key, value) }
}
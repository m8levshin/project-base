package com.mlevshin.projectbase.error.plugins

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.server.application.ApplicationCall
import io.ktor.util.toMap
import io.opentelemetry.context.propagation.TextMapGetter
import io.opentelemetry.context.propagation.TextMapSetter

object KtorTextMapGetter : TextMapGetter<ApplicationCall> {
    override fun keys(call: ApplicationCall): Iterable<String> {
        return call.request.headers.toMap().keys
    }
    override fun get(call: ApplicationCall?, key: String): String? = call?.request?.headers?.get(key)
}

object KtorTextMapSetter : TextMapSetter<HttpRequestBuilder> {
    override fun set(carrier: HttpRequestBuilder?, key: String, value: String) {
        carrier?.headers?.append(key, value)
    }

}
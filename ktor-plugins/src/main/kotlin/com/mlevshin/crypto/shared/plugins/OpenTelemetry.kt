package com.mlevshin.crypto.shared.plugins

import io.ktor.client.request.*
import io.ktor.server.application.*
import io.ktor.util.*
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
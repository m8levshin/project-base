package com.mlevshin.crypto.shared.plugins.server

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.util.*
import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.api.trace.Span
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.context.Context
import io.opentelemetry.context.propagation.TextMapGetter
import io.opentelemetry.extension.kotlin.asContextElement
import kotlinx.coroutines.currentCoroutineContext
import org.slf4j.MDC


private const val OPEN_TELEMETRY_SERVER_PLUGIN = "OpenTelemetryServerPlugin"
private const val DEFAULT_TRACE_ID_MDC_KEY_NAME = "traceId"
private const val DEFAULT_SPAN_ID_MDC_KEY_NAME = "spanId"
private const val ATTRIBUTE_SPAN_PREFIX = "_span"

/**
 * Builder for initializing OpenTelemetryServerPlugin.
 * OpenTelemetryServerPlugin extract a parent span by textMapGetter from a request or creates new one.
 * Then put it to mdc, call attributes, and extends a coroutine context by span context elements.
 */
class OpenTelemetryServerPluginBuilder(
    private val telemetry: OpenTelemetry,
    private val tracer: Tracer,
    private val textMapGetter: TextMapGetter<ApplicationCall>,
    private val traceIdMdcKeyName: String = DEFAULT_TRACE_ID_MDC_KEY_NAME,
    private val spanIdMdcKeyName: String = DEFAULT_SPAN_ID_MDC_KEY_NAME
) {
    fun build() = createApplicationPlugin(name = OPEN_TELEMETRY_SERVER_PLUGIN) {

        onCall { call ->
            val context: Context = telemetry.propagators.textMapPropagator.extract(Context.current(), call, textMapGetter)
            val span = tracer.spanBuilder(call.request.uri).setParent(context).startSpan()
            putTraceInfoToMdc(span)
            currentCoroutineContext().plus(span.asContextElement())
            span.makeCurrent()
            putTracingAttributeToCall(call, span)
        }

        onCallRespond { call, _ ->
            val span = call.attributes[AttributeKey(OPEN_TELEMETRY_SERVER_PLUGIN + ATTRIBUTE_SPAN_PREFIX)] as Span
            span.end();
        }
    }

    private fun putTracingAttributeToCall(call: ApplicationCall, span: Span) {
        call.attributes.put(AttributeKey(OPEN_TELEMETRY_SERVER_PLUGIN + ATTRIBUTE_SPAN_PREFIX), span)
    }

    private fun putTraceInfoToMdc(span: Span) {
        MDC.put(traceIdMdcKeyName, span.spanContext.traceId)
        MDC.put(spanIdMdcKeyName, span.spanContext.spanId)
    }
}
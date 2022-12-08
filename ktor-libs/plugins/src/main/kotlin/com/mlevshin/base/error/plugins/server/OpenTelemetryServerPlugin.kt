package com.mlevshin.base.error.plugins.server

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.util.*
import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.api.common.AttributeKey.stringKey
import io.opentelemetry.api.trace.Span
import io.opentelemetry.api.trace.SpanKind
import io.opentelemetry.api.trace.StatusCode
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.context.Context
import io.opentelemetry.context.propagation.TextMapGetter
import io.opentelemetry.extension.kotlin.asContextElement
import io.opentelemetry.extension.kotlin.getOpenTelemetryContext
import io.opentelemetry.semconv.trace.attributes.SemanticAttributes
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withContext
import org.slf4j.MDC


private const val OPEN_TELEMETRY_SERVER_PLUGIN = "OpenTelemetryServerPlugin"
private const val DEFAULT_TRACE_ID_MDC_KEY_NAME = "traceId"
private const val DEFAULT_SPAN_ID_MDC_KEY_NAME = "spanId"
private const val ATTRIBUTE_SPAN_PREFIX = "_span"
private const val HTTP_PATH_ATTRIBUTE_KEY = "http.path"

class OpenTelemetryServerPlugin(
    private val telemetry: OpenTelemetry,
    private val tracer: Tracer,
    private val textMapGetter: TextMapGetter<ApplicationCall>,
    private val traceIdMdcKeyName: String = DEFAULT_TRACE_ID_MDC_KEY_NAME,
    private val spanIdMdcKeyName: String = DEFAULT_SPAN_ID_MDC_KEY_NAME
) {

    class Config {
        lateinit var telemetry: OpenTelemetry
        lateinit var tracer: Tracer
        lateinit var textMapGetter: TextMapGetter<ApplicationCall>
        var traceIdMdcKeyName: String = DEFAULT_TRACE_ID_MDC_KEY_NAME
        var spanIdMdcKeyName: String = DEFAULT_SPAN_ID_MDC_KEY_NAME
        fun build(): OpenTelemetryServerPlugin =
            OpenTelemetryServerPlugin(telemetry, tracer, textMapGetter, traceIdMdcKeyName, spanIdMdcKeyName)
    }

    companion object Feature : BaseApplicationPlugin<ApplicationCallPipeline, Config, OpenTelemetryServerPlugin> {

        override val key = AttributeKey<OpenTelemetryServerPlugin>(OPEN_TELEMETRY_SERVER_PLUGIN)
        override fun install(
            pipeline: ApplicationCallPipeline,
            configure: Config.() -> Unit
        ): OpenTelemetryServerPlugin {
            val plugin = Config().apply(configure).build()
            with(plugin) {
                pipeline.intercept(ApplicationCallPipeline.Monitoring) {
                    val textMapPropagator = plugin.telemetry.propagators.textMapPropagator
                    val coroutineOtelContext = currentCoroutineContext().getOpenTelemetryContext()
                    val parentContext =
                        textMapPropagator.extract(coroutineOtelContext, call, plugin.textMapGetter)
                            ?: coroutineOtelContext

                    val span = createNewSpan(tracer, call, parentContext)

                    withContext(currentCoroutineContext().plus(span.asContextElement())) {
                        span.makeCurrent()
                        putTraceInfoToMdc(span)
                        putTracingAttributeToCall(call, span)
                        proceed()
                        setSpanStatus(span, call)
                        span.end()
                    }
                }
                return plugin
            }
        }

    }

    private fun createNewSpan(tracer: Tracer, call: ApplicationCall, parentContext: Context) =
        tracer
            .spanBuilder(call.request.uri)
            .setParent(parentContext)
            .setAttribute(SemanticAttributes.HTTP_METHOD, call.request.httpMethod.value)
            .setAttribute(stringKey(HTTP_PATH_ATTRIBUTE_KEY), call.request.uri)
            .setAttribute(SemanticAttributes.HTTP_HOST, call.request.host())
            .setAttribute(SemanticAttributes.HTTP_USER_AGENT, call.request.userAgent() ?: "")
            .setSpanKind(SpanKind.SERVER)
            .startSpan()

    private fun setSpanStatus(span: Span, call: ApplicationCall) {

        call.response.status()?.let {
            span.setStatus(if (it.isSuccess()) StatusCode.OK else StatusCode.ERROR)
            span.setAttribute(SemanticAttributes.HTTP_STATUS_CODE, it.value)
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
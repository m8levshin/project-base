package com.mlevshin.projectbase.error.plugins.server.requestlogging

import io.ktor.events.Events
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.ApplicationPlugin
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.ApplicationStarting
import io.ktor.server.application.ApplicationStopped
import io.ktor.server.application.ApplicationStopping
import io.ktor.server.application.PluginBuilder
import io.ktor.server.application.createApplicationPlugin
import io.ktor.server.application.hooks.ResponseSent
import io.ktor.server.application.log
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import io.ktor.server.request.receiveText
import io.ktor.util.AttributeKey
import org.slf4j.event.Level


private const val RESPONSE_OBJECT_KEY_STRING: String = "response_object_key"
private const val CALL_LOGGING_PLUGIN_NAME = "CallLogging"

val CallLogging: ApplicationPlugin<CallLoggingConfig> = createApplicationPlugin(
    CALL_LOGGING_PLUGIN_NAME,
    ::CallLoggingConfig
) {
    val log = pluginConfig.logger ?: application.log
    val filters = pluginConfig.filters
    val objectMapper = pluginConfig.objectMapper
    val responseObjectAttributeKey = AttributeKey<Any>(RESPONSE_OBJECT_KEY_STRING)

    fun log(message: String) = when (pluginConfig.level) {
        Level.ERROR -> log.error(message)
        Level.WARN -> log.warn(message)
        Level.INFO -> log.info(message)
        Level.DEBUG -> log.debug(message)
        Level.TRACE -> log.trace(message)
    }

    suspend fun logRequest(call: ApplicationCall) {


        if (filters.isEmpty() || filters.any { it(call) }) {
            log("path: ${call.request.path()}, method: ${call.request.httpMethod.value}")
            log("requestBody: " + call.receiveText().ifEmpty { "null" })
        }
    }

    fun logResponse(call: ApplicationCall) {
        if (filters.isEmpty() || filters.any { it(call) }) {
            val any = call.attributes.getOrNull(responseObjectAttributeKey)
            val path = call.request.path()
            val responseCode = call.response.status()?.value ?: 200
            val responseRawString = objectMapper.writeValueAsString(any)
            log( "status: $responseCode, responseBody: $responseRawString")
        }
    }

    fun PluginBuilder<CallLoggingConfig>.setupCallLogging(
        logRequest: suspend (ApplicationCall) -> Unit,
        logResponse: (ApplicationCall) -> Unit
    ) {
        onCall {logRequest(it) }
        onCallRespond() { call, body ->
            call.attributes.put(responseObjectAttributeKey, body)
        }
        on(ResponseSent) {logResponse(it)}
    }

    fun setupAppEventsLogging(events: Events, log: (String) -> Unit) {
        val starting: (Application) -> Unit = { log("Application starting: $it") }
        val started: (Application) -> Unit = { log("Application started: $it") }
        val stopping: (Application) -> Unit = { log("Application stopping: $it") }
        var stopped: (Application) -> Unit = {}

        stopped = {
            log("Application stopped: $it")
            events.unsubscribe(ApplicationStarting, starting)
            events.unsubscribe(ApplicationStarted, started)
            events.unsubscribe(ApplicationStopping, stopping)
            events.unsubscribe(ApplicationStopped, stopped)
        }

        events.subscribe(ApplicationStarting, starting)
        events.subscribe(ApplicationStarted, started)
        events.subscribe(ApplicationStopping, stopping)
        events.subscribe(ApplicationStopped, stopped)
    }

    setupAppEventsLogging(application.environment.monitor, ::log)
    setupCallLogging(::logRequest, ::logResponse)
}







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
import io.ktor.util.AttributeKey
import io.ktor.utils.io.jvm.javaio.toInputStream
import org.slf4j.event.Level


private const val RESPONSE_OBJECT_KEY_STRING: String = "response_object_key"

val CallLogging: ApplicationPlugin<CallLoggingConfig> = createApplicationPlugin(
    "CallLogging",
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

    fun logRequest(call: ApplicationCall) {
        if (filters.isEmpty() || filters.any { it(call) }) {
            val loggingRequest = LoggingRequest(
                call.request.path(),
                call.request.httpMethod.value,
                objectMapper.readTree(call.request.receiveChannel().toInputStream())
            )
            log(objectMapper.writeValueAsString(loggingRequest))
        }
    }

    fun logResponse(call: ApplicationCall) {
        if (filters.isEmpty() || filters.any { it(call) }) {
            val loggingResponse = LoggingResponse(
                call.request.path(),
                call.request.httpMethod.value,
                call.response.status()?.value ?: 200,
                call.attributes[responseObjectAttributeKey]
            )
            log(objectMapper.writeValueAsString(loggingResponse))
        }
    }

    fun PluginBuilder<CallLoggingConfig>.setupCallLogging(
        logRequest: (ApplicationCall) -> Unit,
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







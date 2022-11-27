package com.mlevshin.crypto.shared.plugins.server

import io.ktor.server.application.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import kotlinx.coroutines.slf4j.MDCContext
import kotlinx.coroutines.withContext
import org.slf4j.MDC


class AuthenticationMdcFillerPlugin {

    companion object Plugin :
        BaseApplicationPlugin<ApplicationCallPipeline, Configuration, AuthenticationMdcFillerPlugin> {
        private val MdcAuthAddingPhase = PipelinePhase("AddingAuthToMDCAfterAuthenticationPipelinePhase")
        override val key = AttributeKey<AuthenticationMdcFillerPlugin>("AuthenticationMdcFillerPlugin")
        override fun install(
            pipeline: ApplicationCallPipeline,
            configure: Configuration.() -> Unit
        ): AuthenticationMdcFillerPlugin {
            val plugin = AuthenticationMdcFillerPlugin()
            val conf = Configuration().apply(configure)
            pipeline.insertPhaseAfter(ApplicationCallPipeline.Call, MdcAuthAddingPhase)

            pipeline.intercept(MdcAuthAddingPhase) {
                val mdcValue = conf.authMdcValueExtractor.invoke(call)
                if (mdcValue != null) {
                    withContext(MDCContext(MDC.getCopyOfContextMap() + Pair(conf.mdcKeyName, mdcValue))) {
                        proceed()
                    }
                } else {
                    proceed()
                }
            }
            return plugin
        }
    }

    class Configuration {
        var mdcKeyName = "authId"
        var authMdcValueExtractor: (ApplicationCall) -> String? = { null }
    }
}
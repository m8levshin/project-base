package com.mlevshin.base.error.plugins.server

import io.ktor.server.application.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import kotlinx.coroutines.slf4j.MDCContext
import kotlinx.coroutines.withContext
import org.slf4j.MDC


/**
 * KTOR plugin for extracting of authentication string to put into MDC context.
 */
class AuthenticationMdcFillerPlugin(
    private var mdcKeyName: String = "authId",
    private var authMdcValueExtractor: (ApplicationCall) -> String? = { null }
){

    companion object Plugin : BaseApplicationPlugin<ApplicationCallPipeline,
            Configuration, AuthenticationMdcFillerPlugin> {

        private const val AUTHENTICATION_MDC_FILLER_PLUGIN = "AuthenticationMdcFillerPlugin"
        private const val ADDING_AUTH_TO_MDC_AFTER_AUTHENTICATION_PIPELINE_PHASE =
            "AddingAuthToMDCAfterAuthenticationPipelinePhase"

        private val MdcAuthAddingPhase = PipelinePhase(ADDING_AUTH_TO_MDC_AFTER_AUTHENTICATION_PIPELINE_PHASE)
        override val key = AttributeKey<AuthenticationMdcFillerPlugin>(AUTHENTICATION_MDC_FILLER_PLUGIN)

        override fun install(pipeline: ApplicationCallPipeline,
                             configure: Configuration.() -> Unit): AuthenticationMdcFillerPlugin {

            val plugin = Configuration().apply(configure).build()
            with(plugin) {
                pipeline.intercept(MdcAuthAddingPhase) {
                    val mdcValue = authMdcValueExtractor.invoke(call)
                    if (mdcValue != null) {
                        withContext(buildMDCContextWithNewValue(mdcKeyName, mdcValue)) {
                            proceed()
                        }
                    } else {
                        proceed()
                    }
                }
                pipeline.insertPhaseAfter(ApplicationCallPipeline.Monitoring, MdcAuthAddingPhase)
            }
            return plugin
        }


    }

    private fun buildMDCContextWithNewValue(mdcKeyName: String, mdcValue: String) =
        MDCContext(MDC.getCopyOfContextMap() + Pair(mdcKeyName, mdcValue))

    class Configuration {
        var mdcKeyName = "authId"
        var authMdcValueExtractor: (ApplicationCall) -> String? = { null }
        fun build() = AuthenticationMdcFillerPlugin(mdcKeyName, authMdcValueExtractor)
    }
}
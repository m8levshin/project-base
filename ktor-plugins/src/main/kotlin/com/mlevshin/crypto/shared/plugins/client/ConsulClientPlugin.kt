package com.mlevshin.crypto.shared.plugins.client

import com.ecwid.consul.v1.ConsulClient
import com.ecwid.consul.v1.QueryParams
import com.ecwid.consul.v1.health.HealthServicesRequest
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.util.*

private const val CONSUL_PLUGIN = "ConsulPlugin"

class ConsulClientPlugin(var consulClient: ConsulClient) {

    class Config {
        lateinit var consulClient: ConsulClient
        fun build(): ConsulClientPlugin = ConsulClientPlugin(consulClient)
    }

    companion object Feature : HttpClientPlugin<Config, ConsulClientPlugin> {
        private var currentNodeIndex: Int = 0

        override val key = AttributeKey<ConsulClientPlugin>(CONSUL_PLUGIN)

        override fun prepare(block: Config.() -> Unit): ConsulClientPlugin = Config().apply(block).build()

        override fun install(plugin: ConsulClientPlugin, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.Render) {

                val healthServicesRequest: HealthServicesRequest = HealthServicesRequest.newBuilder()
                    .setPassing(true)
                    .setQueryParams(QueryParams.DEFAULT)
                    .build()
                val healthServices = plugin.consulClient.getHealthServices(context.url.host, healthServicesRequest)
                val healthNodes = healthServices.value


                val selectedNode = healthNodes[currentNodeIndex]
                context.url.host = selectedNode.service.address
                context.url.port = selectedNode.service.port
                currentNodeIndex = (currentNodeIndex + 1) % healthNodes.size
            }
        }
    }
}
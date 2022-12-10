package com.mlevshin.projectbase.service.config

import io.opentelemetry.instrumentation.api.instrumenter.http.HttpServerAttributesGetter
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cloud.sleuth.http.HttpServerRequest
import org.springframework.cloud.sleuth.http.HttpServerResponse
import org.springframework.cloud.sleuth.otel.bridge.SpringHttpServerAttributesGetter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CustomOtelConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = "spring.sleuth.otel.config", name = ["custom-span-name"])
    fun otelHttpServerAttributesGetter(): HttpServerAttributesGetter<HttpServerRequest?, HttpServerResponse?>? =
        object : SpringHttpServerAttributesGetter() {
            override fun route(httpServerRequest: HttpServerRequest?) =
                "${httpServerRequest?.method()?.uppercase()} ${httpServerRequest?.path()}"


        }
}
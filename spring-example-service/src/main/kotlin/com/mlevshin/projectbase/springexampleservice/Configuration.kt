package com.mlevshin.projectbase.springexampleservice

import io.opentelemetry.instrumentation.api.instrumenter.http.HttpServerAttributesGetter
import org.springframework.cloud.sleuth.http.HttpServerRequest
import org.springframework.cloud.sleuth.http.HttpServerResponse
import org.springframework.cloud.sleuth.otel.bridge.SpringHttpServerAttributesGetter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Configuration {
    @Bean
    fun otelHttpServerAttributesGetter(): HttpServerAttributesGetter<HttpServerRequest?, HttpServerResponse?>? {
        return object : SpringHttpServerAttributesGetter() {
            override fun route(httpServerRequest: HttpServerRequest?) =
                 "${httpServerRequest?.method()?.uppercase()} ${httpServerRequest?.path()}"

        }
    }

}
package com.mlevshin.base.service.config

data class TracingConfig(
    val otelEndpoint: String = "http://otel-collector:4317"
)
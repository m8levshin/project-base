package com.mlevshin.projectbase.service.config

data class TracingProperties(
    val otelEndpoint: String = "http://otel-collector:4317"
)
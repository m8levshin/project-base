package com.mlevshin.base.service.config

data class ServiceConfig(
    var name: String = "default_service_name",
    var tracing: TracingConfig
)
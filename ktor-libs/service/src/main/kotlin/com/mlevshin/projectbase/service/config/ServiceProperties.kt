package com.mlevshin.projectbase.service.config

data class ServiceProperties(
    var name: String = "default_service_name",
    var port: Int = 8080,
    var tracing: TracingProperties?
)
package com.mlevshin.projectbase.error.plugins.server.requestlogging

data class LoggingRequest(
    val path: String,
    val method: String,
    val body: Any?
)
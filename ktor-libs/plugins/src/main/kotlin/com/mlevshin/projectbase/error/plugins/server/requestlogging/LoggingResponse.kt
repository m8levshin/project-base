package com.mlevshin.projectbase.error.plugins.server.requestlogging

data class LoggingResponse(
    val path: String,
    val method: String,
    val httpStatus: Int,
    val body: Any?
)
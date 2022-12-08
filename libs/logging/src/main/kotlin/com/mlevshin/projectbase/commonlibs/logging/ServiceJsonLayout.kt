package com.mlevshin.projectbase.commonlibs.logging

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.contrib.json.classic.JsonLayout

class ServiceJsonLayout : JsonLayout() {
    companion object {
        var serviceName: String? = null
    }
    override fun addCustomDataToJsonMap(map: MutableMap<String, Any>?, event: ILoggingEvent?) {
        serviceName?.let{
            map?.put("service", it)
        }
    }
}
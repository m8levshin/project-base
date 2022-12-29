package com.mlevshin.projectbase.api

import com.mlevshin.projectbase.commonlibs.logging.utils.logger
import com.mlevshin.projectbase.security.oauth2.resource.OAuth2ResourceConfiguration
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond


import io.ktor.server.routing.Route
import io.ktor.server.routing.get


fun Route.configureTestRouting() {

    authenticate(OAuth2ResourceConfiguration.OAUTH2_SECURITY_CONFIGURATION) {
        get("/api/secured") {
            call.respond("secured")
        }
    }

    get("/api/unsecured") {
        logger().info("Start unsecure method.")
        call.respond(mapOf("key" to "value"))
    }

}
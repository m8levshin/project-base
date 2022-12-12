package com.mlevshin.projectbase.api

import com.mlevshin.projectbase.security.oauth2.resource.OAuth2ResourceConfiguration
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*



fun Route.configureTestRouting() {

    authenticate(OAuth2ResourceConfiguration.OAUTH2_SECURITY_CONFIGURATION) {
        get("/api/test") {
            call.respond("hey")
        }
    }

}
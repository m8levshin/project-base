package com.mlevshin.projectbase.config

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.sessions.SessionTransportTransformerEncrypt
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie
import io.ktor.util.hex
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {

    val sessionProperties: SessionProperties by inject()

    install(Sessions) {
        cookie<TokenHandlerSession>("session_key") {
            cookie.path = "/"
            cookie.httpOnly = true
            transform(SessionTransportTransformerEncrypt(hex(sessionProperties.encryptionKey), hex(sessionProperties.signKey)))
        }
    }
}

data class TokenHandlerSession(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val codeVerifier: String? = null,
    val successLoginRedirectUrl: String? = null
)
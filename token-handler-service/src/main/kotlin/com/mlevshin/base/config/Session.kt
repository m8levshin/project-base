package com.mlevshin.base.config

import io.ktor.server.application.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {

    val sessionConfig: SessionConfig by inject()

    install(Sessions) {
        cookie<TokenHandlerSession>("session_key") {
            cookie.path = "/"
            cookie.httpOnly = true
            transform(SessionTransportTransformerEncrypt(hex(sessionConfig.encryptionKey), hex(sessionConfig.signKey)))
        }
    }
}

data class TokenHandlerSession(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val codeVerifier: String? = null,
    val successLoginRedirectUrl: String? = null
)
package com.mlevshin.crypto.config

import io.ktor.server.application.*
import io.ktor.server.sessions.*
import io.ktor.util.*

fun Application.configureSecurity() {

    val sessionEncryptionKey = environment.config.property("security.session.encryptionKey").getString()
    val sessionSignKey = environment.config.property("security.session.signKey").getString()

    install(Sessions) {
        cookie<TokenHandlerSession>("session_key") {
            cookie.path = "/"
            cookie.httpOnly = true
            transform(SessionTransportTransformerEncrypt(hex(sessionEncryptionKey), hex(sessionSignKey)))
        }
    }
}

data class TokenHandlerSession(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val codeVerifier: String? = null,
    val successLoginRedirectUrl: String? = null
)
package com.mlevshin.crypto.config

import com.auth0.jwk.JwkProviderBuilder
import io.ktor.client.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import org.koin.core.qualifier.named
import org.koin.ktor.ext.inject
import java.net.URL

fun Application.configureSecurity() {

    install(Sessions) {
        val secretEncryptKey = hex("108a1d89d9ccd997b706040a1818cf70")
        val secretSignKey = hex("a811d5b15efbeb5b050d9afd30143b29")
        cookie<TokenHandlerSession>("session_key") {
            cookie.path = "/"
            cookie.httpOnly = true
            transform(SessionTransportTransformerEncrypt(secretEncryptKey, secretSignKey))
        }
    }
}

data class TokenHandlerSession(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val codeVerifier: String? = null
)
package com.mlevshin.crypto.api

import com.mlevshin.crypto.api.dto.GetAuthRedirectResponse
import com.mlevshin.crypto.api.dto.RefreshAccessTokenResponse
import com.mlevshin.crypto.config.TokenHandlerSession
import com.mlevshin.crypto.service.AccessTokenService
import com.mlevshin.crypto.service.RefreshTokenService
import com.mlevshin.crypto.shared.AppError
import com.mlevshin.crypto.shared.exception.AuthenticationException
import com.mlevshin.crypto.utils.CodeChallengeUtils
import com.mlevshin.crypto.utils.logger
import com.mlevshin.crypto.utils.toCodeChallenge
import io.ktor.client.statement.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject


fun Route.configureTokenHandlerRouting() {

    val refreshTokenService: RefreshTokenService by inject()
    val accessTokenService: AccessTokenService by inject()

    get("/api/auth") {
        val session = call.sessions.get<TokenHandlerSession>()
        val refreshToken = session?.refreshToken
        if (refreshToken == null) {
            val codeVerifier = CodeChallengeUtils.getCodeVerifierString()
            val codeChallenge = codeVerifier.toCodeChallenge()
            call.sessions.set(TokenHandlerSession(codeVerifier = codeVerifier))
            call.respond(GetAuthRedirectResponse(accessTokenService.generateAuthLink(codeChallenge)))
            return@get
        }
        val refreshedTokens = refreshTokenService.refreshTokens(session.refreshToken)
        call.sessions.set(TokenHandlerSession(refreshedTokens.accessToken, refreshedTokens.refreshToken))
        call.respond(RefreshAccessTokenResponse(refreshedTokens.accessToken))
    }

    get("/api/oauth/callback") {
        val session = call.sessions.get<TokenHandlerSession>()
        val codeVerifier = session?.codeVerifier
        val authCode = call.request.queryParameters["code"]

        if (authCode != null && codeVerifier != null) {
            val oAuthTokens = accessTokenService.getTokensByAuthToken(authCode, codeVerifier)
            call.sessions.set(TokenHandlerSession(oAuthTokens.accessToken, oAuthTokens.refreshToken))
            call.respondRedirect("/")
            return@get
        }
        logger().info("authCode request param:[{}] or codeVerifier is empty", authCode)
        throw AuthenticationException(AppError("Something went wrong."));
    }


}
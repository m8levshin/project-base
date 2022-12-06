package com.mlevshin.base.api

import com.mlevshin.base.api.dto.GetAuthRedirectResponse
import com.mlevshin.base.api.dto.RefreshAccessTokenResponse
import com.mlevshin.base.config.TokenHandlerSession
import com.mlevshin.base.error.AppError
import com.mlevshin.base.error.exception.AuthenticationException
import com.mlevshin.base.service.AccessTokenService
import com.mlevshin.base.service.RefreshTokenService
import com.mlevshin.base.utils.CodeChallengeUtils
import com.mlevshin.base.utils.OAuthConstants.Companion.AUTH_CODE_PARAM_NAME
import com.mlevshin.base.utils.logger
import com.mlevshin.base.utils.toCodeChallenge
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject


private const val REDIRECT_QUERY_PARAM = "redirect"

fun Route.configureTokenHandlerRouting() {

    val refreshTokenService: RefreshTokenService by inject()
    val accessTokenService: AccessTokenService by inject()

    get("/api/auth") {
        val session = call.sessions.get<TokenHandlerSession>()
        val refreshToken = session?.refreshToken
        if (refreshToken == null) {
            val successLoginRedirectUrl = call.request.queryParameters[REDIRECT_QUERY_PARAM]
            val codeVerifier = CodeChallengeUtils.getCodeVerifierString()
            val codeChallenge = codeVerifier.toCodeChallenge()
            call.sessions.set(
                TokenHandlerSession(
                    codeVerifier = codeVerifier,
                    successLoginRedirectUrl = successLoginRedirectUrl
                )
            )
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
        val authCode = call.request.queryParameters[AUTH_CODE_PARAM_NAME]


        if (authCode != null && codeVerifier != null) {
            val successLoginRedirectUrl = session.successLoginRedirectUrl
            val oAuthTokens = accessTokenService.getTokensByAuthToken(authCode, codeVerifier)
            call.sessions.set(TokenHandlerSession(oAuthTokens.accessToken, oAuthTokens.refreshToken))
            call.respondRedirect(successLoginRedirectUrl ?: "/")
            return@get
        }
        logger().info("authCode request param:[{}] or codeVerifier is empty", authCode)
        throw AuthenticationException(AppError("Something went wrong."));
    }


}
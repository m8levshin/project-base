package com.mlevshin.projectbase.api

import com.mlevshin.projectbase.api.dto.GetAuthRedirectResponse
import com.mlevshin.projectbase.api.dto.RefreshAccessTokenResponse
import com.mlevshin.projectbase.commonlibs.logging.utils.logger
import com.mlevshin.projectbase.config.TokenHandlerSession
import com.mlevshin.projectbase.error.AppError
import com.mlevshin.projectbase.error.exception.AuthenticationException
import com.mlevshin.projectbase.service.AccessTokenService
import com.mlevshin.projectbase.service.RefreshTokenService
import com.mlevshin.projectbase.utils.CodeChallengeUtils
import com.mlevshin.projectbase.utils.OAuthConstants.Companion.AUTH_CODE_PARAM_NAME
import com.mlevshin.projectbase.utils.toCodeChallenge
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.sessions.clear
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set
import org.koin.ktor.ext.inject


private const val REDIRECT_QUERY_PARAM = "redirect"

fun Route.configureTokenHandlerRouting() {

    val refreshTokenService: RefreshTokenService by inject()
    val accessTokenService: AccessTokenService by inject()

    get("/api/auth") {
        val session = call.sessions.get<TokenHandlerSession>()
        val refreshToken = session?.refreshToken

        if (refreshToken == null) {
            //todo url checking
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

        try {
            val refreshedTokens = refreshTokenService.refreshTokens(session.refreshToken)
            call.sessions.set(TokenHandlerSession(refreshedTokens.accessToken, refreshedTokens.refreshToken))
            call.respond(RefreshAccessTokenResponse(refreshedTokens.accessToken))
        } catch (e: AuthenticationException) {
            call.sessions.clear<TokenHandlerSession>()
            throw e
        }
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
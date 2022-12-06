package com.mlevshin.base.service.impl

import com.mlevshin.base.config.OAuthConfig
import com.mlevshin.base.domain.OAuthTokens
import com.mlevshin.base.error.AppError
import com.mlevshin.base.error.exception.AuthenticationException
import com.mlevshin.base.service.RefreshTokenService
import com.mlevshin.base.utils.OAuthConstants
import com.mlevshin.base.utils.logger
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*

class RefreshTokenServiceImpl(
    private val httpClient: HttpClient,
    private val oAuthConfig: OAuthConfig
) : RefreshTokenService {

    override suspend fun refreshTokens(refreshToken: String): OAuthTokens {
        val refreshTokenResponse = makeRefreshTokenRequest(refreshToken)
        if (refreshTokenResponse.status == HttpStatusCode.OK) {
            return refreshTokenResponse.body()
        }
        logger().info("Token refreshing is failed, authorization server response is {}",
            refreshTokenResponse.bodyAsText())
        throw AuthenticationException(AppError("Getting of access token is failed."));
    }

    private suspend fun makeRefreshTokenRequest(refreshToken: String) = httpClient.submitForm(
        url = oAuthConfig.accessTokenUrl,
        formParameters = Parameters.build {
            append(OAuthConstants.CLIENT_ID_PARAM_NAME, oAuthConfig.clientId)
            append(OAuthConstants.CLIENT_SECRET_PARAM_NAME, oAuthConfig.clientSecret)
            append(OAuthConstants.REFRESH_TOKEN_GRANT_TYPE_VALUE, refreshToken)
            append(OAuthConstants.GRANT_TYPE_PARAM_NAME, OAuthConstants.REFRESH_TOKEN_GRANT_TYPE_VALUE)
        },
        encodeInQuery = false
    ) {
        method = HttpMethod.Post
    }
}
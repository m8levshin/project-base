package com.mlevshin.projectbase.service.impl

import com.mlevshin.projectbase.commonlibs.logging.utils.logger
import com.mlevshin.projectbase.config.OAuthConfig
import com.mlevshin.projectbase.domain.OAuthTokens
import com.mlevshin.projectbase.error.AppError
import com.mlevshin.projectbase.error.exception.AuthenticationException
import com.mlevshin.projectbase.service.RefreshTokenService
import com.mlevshin.projectbase.utils.OAuthConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters

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
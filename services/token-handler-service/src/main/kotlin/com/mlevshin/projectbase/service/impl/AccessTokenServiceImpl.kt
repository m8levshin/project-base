package com.mlevshin.projectbase.service.impl

import com.mlevshin.projectbase.commonlibs.logging.utils.logger
import com.mlevshin.projectbase.config.OAuthConfig
import com.mlevshin.projectbase.domain.OAuthTokens
import com.mlevshin.projectbase.error.AppError
import com.mlevshin.projectbase.error.exception.AuthenticationException
import com.mlevshin.projectbase.service.AccessTokenService
import com.mlevshin.projectbase.utils.OAuthConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.http.URLBuilder

class AccessTokenServiceImpl(
    private val httpClient: HttpClient,
    private val oAuthConfig: OAuthConfig
) : AccessTokenService {
    override suspend fun generateAuthLink(codeChallenge: String): String {
        val urlBuilder = URLBuilder(oAuthConfig.authorizeUrl)
        urlBuilder.parameters.apply {
            append(OAuthConstants.CODE_CHALLENGE_PARAM_NAME, codeChallenge)
            append(OAuthConstants.CODE_CHALLENGE_METHOD_PARAM_NAME, OAuthConstants.SH256)
            append(OAuthConstants.RESPONSE_TYPE_PARAM_NAME, OAuthConstants.AUTH_CODE_PARAM_NAME)
            append(OAuthConstants.SCOPE_PARAM_NAME, oAuthConfig.defaultScopes)
            append(OAuthConstants.CLIENT_ID_PARAM_NAME, oAuthConfig.clientId)
            append(OAuthConstants.REDIRECT_URI_PARAM_NAME, oAuthConfig.callbackUrl)
        }
        return urlBuilder.buildString();
    }

    override suspend fun getTokensByAuthToken(authCode: String, codeVerifier: String): OAuthTokens {
        val accessTokenResponse = requestAccessToken(authCode, codeVerifier)
        if (accessTokenResponse.status == HttpStatusCode.OK) {
            return accessTokenResponse.body()
        }
        logger().info("Getting of access token is failed, authorization server response is {}",
            accessTokenResponse.bodyAsText())
        throw AuthenticationException(AppError("Getting of access token is failed."));
    }

    private suspend fun requestAccessToken(code: String, codeChallenge: String) =
        httpClient.submitForm(
            url = oAuthConfig.accessTokenUrl,
            formParameters = Parameters
                .build {
                    append(OAuthConstants.CLIENT_ID_PARAM_NAME, oAuthConfig.clientId)
                    append(OAuthConstants.CLIENT_SECRET_PARAM_NAME, oAuthConfig.clientSecret)
                    append(OAuthConstants.GRANT_TYPE_PARAM_NAME, OAuthConstants.AUTH_CODE_GRANT_TYPE_VALUE)
                    append(OAuthConstants.CODE_VERIFIER_PARAM_NAME, codeChallenge)
                    append(OAuthConstants.AUTH_CODE_PARAM_NAME, code)
                    append(OAuthConstants.REDIRECT_URI_PARAM_NAME, oAuthConfig.callbackUrl)
                },
            encodeInQuery = false
        ) {
            method = HttpMethod.Post
        }
}


package com.mlevshin.base.service

import com.mlevshin.base.domain.OAuthTokens

interface AccessTokenService {
    suspend fun generateAuthLink(codeChallenge: String): String
    suspend fun getTokensByAuthToken(authCode: String, codeVerifier: String) : OAuthTokens
}
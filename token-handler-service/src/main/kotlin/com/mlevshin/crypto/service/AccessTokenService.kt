package com.mlevshin.crypto.service

import com.mlevshin.crypto.domain.OAuthTokens

interface AccessTokenService {
    suspend fun generateAuthLink(codeChallenge: String): String
    suspend fun getTokensByAuthToken(authCode: String, codeVerifier: String) : OAuthTokens
}
package com.mlevshin.projectbase.service

import com.mlevshin.projectbase.domain.OAuthTokens

interface AccessTokenService {
    suspend fun generateAuthLink(codeChallenge: String): String
    suspend fun getTokensByAuthToken(authCode: String, codeVerifier: String) : OAuthTokens
}
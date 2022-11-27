package com.mlevshin.crypto.service

import com.mlevshin.crypto.domain.OAuthTokens

interface RefreshTokenService {
    suspend fun refreshTokens(refreshToken: String): OAuthTokens
}
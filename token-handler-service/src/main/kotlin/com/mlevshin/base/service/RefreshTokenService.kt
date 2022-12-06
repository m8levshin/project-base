package com.mlevshin.base.service

import com.mlevshin.base.domain.OAuthTokens

interface RefreshTokenService {
    suspend fun refreshTokens(refreshToken: String): OAuthTokens
}
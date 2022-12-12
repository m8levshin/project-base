package com.mlevshin.projectbase.service

import com.mlevshin.projectbase.domain.OAuthTokens

interface RefreshTokenService {
    suspend fun refreshTokens(refreshToken: String): OAuthTokens
}
package com.mlevshin.projectbase.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class OAuthTokens(
    @JsonProperty("access_token") val accessToken: String,
    @JsonProperty("refresh_token") val refreshToken: String
)

package com.mlevshin.base.config

data class OAuthConfig(
    val accessTokenUrl: String,
    val clientId: String,
    val clientSecret: String,
    val callbackUrl: String,
    val defaultScopes: String,
    val authorizeUrl: String,
)
package com.mlevshin.projectbase.config

data class OAuthConfig(
    val accessTokenUrl: String,
    val clientId: String,
    val clientSecret: String,
    val callbackUrl: String,
    val defaultScopes: String,
    val authorizeUrl: String,
)
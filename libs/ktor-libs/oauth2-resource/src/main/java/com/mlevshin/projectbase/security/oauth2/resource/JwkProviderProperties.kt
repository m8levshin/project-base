package com.mlevshin.projectbase.security.oauth2.resource

data class JwkProviderProperties(
    val issuer: String,
    val audience: String,
    val jwkSetUrl: String,
    val realm: String
)
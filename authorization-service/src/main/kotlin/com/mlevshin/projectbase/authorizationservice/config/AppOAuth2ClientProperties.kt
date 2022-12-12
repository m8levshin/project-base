package com.mlevshin.projectbase.authorizationservice.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app.security.oauth2.client")
data class AppOAuth2ClientProperties(
    val clientId: String,
    val clientSecret: String,
    val callbackUrl: String,
    val scopes: List<String>
)

package com.mlevshin.crypto.config

class OAuthConfig(
    val accessTokenUrl: String,
    val clientId: String,
    val clientSecret: String,
    val callbackUrl: String,
    val defaultScopes: List<String>,
    val authorizeUrl: String,
) {
    data class Builder(
        var accessTokenUrl: String? = null,
        var clientId: String? = null,
        var clientSecret: String? = null,
        var callbackUrl: String? = null,
        var defaultScopes: List<String>? = null,
        var authorizeUrl: String? = null,
    ) {

        fun accessTokenUrl(accessTokenUrl: String) = apply { this.accessTokenUrl = accessTokenUrl }
        fun clientId(clientId: String) = apply { this.clientId = clientId }
        fun clientSecret(clientSecret: String) = apply { this.clientSecret = clientSecret }
        fun callbackUrl(callbackUrl: String) = apply { this.callbackUrl = callbackUrl }
        fun authorizeUrl(authorizeUrl: String) = apply { this.authorizeUrl = authorizeUrl }
        fun defaultScopes(defaultScopes: List<String>) = apply { this.defaultScopes = defaultScopes }
        fun build() = OAuthConfig(accessTokenUrl!!, clientId!!, clientSecret!!, callbackUrl!!, defaultScopes!!, authorizeUrl!!)
    }
}
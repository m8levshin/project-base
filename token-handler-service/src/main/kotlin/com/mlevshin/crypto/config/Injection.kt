package com.mlevshin.crypto.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.mlevshin.crypto.service.AccessTokenService
import com.mlevshin.crypto.service.RefreshTokenService
import com.mlevshin.crypto.service.impl.AccessTokenServiceImpl
import com.mlevshin.crypto.service.impl.RefreshTokenServiceImpl
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.koin.core.module.Module
import org.koin.dsl.module

fun Application.configureKoinComponents(): Module {

    return module {
        single<HttpClient> { buildCommonHttpClient() }
        single<Validator> { Validation.buildDefaultValidatorFactory().validator }
        single<OAuthConfig> { buildOAuthConfig() }
        single<RefreshTokenService> { buildRefreshTokenServiceImpl(get(), get()) }
        single<AccessTokenService> { buildAccessTokenServiceImpl(get(), get()) }
    }
}

private fun buildCommonHttpClient(): HttpClient {
    return HttpClient() {
        install(ContentNegotiation) {
            jackson() {
                configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            }
        }
    }
}

private fun buildRefreshTokenServiceImpl(httpClient: HttpClient, oAuthConfig: OAuthConfig) =
    RefreshTokenServiceImpl(httpClient, oAuthConfig)

private fun buildAccessTokenServiceImpl(httpClient: HttpClient, oAuthConfig: OAuthConfig) =
    AccessTokenServiceImpl(httpClient, oAuthConfig);

private fun Application.buildOAuthConfig(): OAuthConfig {
    val accessTokenUrlParam = environment.config.property("security.oauth2.accessTokenUrl").getString()
    val clientIdParam = environment.config.property("security.oauth2.clientId").getString()
    val clientSecretParam = environment.config.property("security.oauth2.clientSecret").getString()
    val callbackUrlParam = environment.config.property("security.oauth2.callbackUrl").getString()
    val defaultScopesParam = environment.config.property("security.oauth2.defaultScopes").getString().split(',')
    val authorizeUrlParam = environment.config.property("security.oauth2.authorizeUrl").getString()

    return OAuthConfig.Builder().apply {
        accessTokenUrl(accessTokenUrlParam)
        clientId(clientIdParam)
        clientSecret(clientSecretParam)
        callbackUrl(callbackUrlParam)
        defaultScopes(defaultScopesParam)
        authorizeUrl(authorizeUrlParam)
    }.build()


}
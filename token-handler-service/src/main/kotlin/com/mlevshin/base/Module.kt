package com.mlevshin.base

import com.mlevshin.base.config.OAuthConfig
import com.mlevshin.base.service.AccessTokenService
import com.mlevshin.base.service.RefreshTokenService
import com.mlevshin.base.service.impl.AccessTokenServiceImpl
import com.mlevshin.base.service.impl.RefreshTokenServiceImpl
import io.ktor.client.*
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.koin.dsl.module

fun tokenHandlerServiceModule() = module {
    single<Validator> { Validation.buildDefaultValidatorFactory().validator }
    single<RefreshTokenService> { buildRefreshTokenServiceImpl(get(), get()) }
    single<AccessTokenService> { buildAccessTokenServiceImpl(get(), get()) }
}

private fun buildRefreshTokenServiceImpl(httpClient: HttpClient, oAuthConfig: OAuthConfig) =
    RefreshTokenServiceImpl(httpClient, oAuthConfig)

private fun buildAccessTokenServiceImpl(httpClient: HttpClient, oAuthConfig: OAuthConfig) =
    AccessTokenServiceImpl(httpClient, oAuthConfig);
package com.mlevshin.projectbase

import com.mlevshin.projectbase.config.OAuthConfig
import com.mlevshin.projectbase.service.AccessTokenService
import com.mlevshin.projectbase.service.RefreshTokenService
import com.mlevshin.projectbase.service.impl.AccessTokenServiceImpl
import com.mlevshin.projectbase.service.impl.RefreshTokenServiceImpl
import io.ktor.client.*
import org.koin.dsl.module

fun tokenHandlerServiceModule() = module {
    single<RefreshTokenService> { buildRefreshTokenServiceImpl(get(), get()) }
    single<AccessTokenService> { buildAccessTokenServiceImpl(get(), get()) }
}

private fun buildRefreshTokenServiceImpl(httpClient: HttpClient, oAuthConfig: OAuthConfig) =
    RefreshTokenServiceImpl(httpClient, oAuthConfig)

private fun buildAccessTokenServiceImpl(httpClient: HttpClient, oAuthConfig: OAuthConfig) =
    AccessTokenServiceImpl(httpClient, oAuthConfig);
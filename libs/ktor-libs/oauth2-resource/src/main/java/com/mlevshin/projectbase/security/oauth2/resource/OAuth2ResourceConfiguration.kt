package com.mlevshin.projectbase.security.oauth2.resource

import com.auth0.jwk.JwkProviderBuilder
import com.mlevshin.projectbase.commonlibs.logging.utils.logger
import com.mlevshin.projectbase.error.exception.ForbiddenException
import io.ktor.http.Url
import io.ktor.http.toURI
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import org.koin.ktor.ext.getKoin
import java.util.concurrent.TimeUnit

class OAuth2ResourceConfiguration {

    companion object {
        const val OAUTH2_SECURITY_CONFIGURATION = "oauth2-resource"
    }

}

fun Application.configureResourceServerSecurity() {
    val jwkProviderProperties = getKoin().getOrNull<JwkProviderProperties>()

    jwkProviderProperties?.let {
        logger().info("OAuth2 resource server security initializing.")
        val jwkProvider = JwkProviderBuilder(Url(jwkProviderProperties.jwkSetUrl).toURI().toURL())
            .cached(10, 24, TimeUnit.HOURS)
            .rateLimited(10, 1, TimeUnit.MINUTES)
            .build()

        install(Authentication) {
            jwt(OAuth2ResourceConfiguration.OAUTH2_SECURITY_CONFIGURATION) {
                verifier(jwkProvider, jwkProviderProperties.issuer) {
                    withAnyOfAudience(*jwkProviderProperties.audiences)
                }
                validate { credential ->
                    JWTPrincipal(credential.payload)
                }
                challenge { defaultScheme, realm ->
                    throw ForbiddenException()
                }
            }
        }
    }
}
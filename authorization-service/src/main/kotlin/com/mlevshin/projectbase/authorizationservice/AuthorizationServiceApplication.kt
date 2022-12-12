package com.mlevshin.projectbase.authorizationservice

import com.mlevshin.projectbase.authorizationservice.config.AppOAuth2ClientProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(
    AppOAuth2ClientProperties::class
)
class AuthorizationServiceApplication

fun main(args: Array<String>) {
    runApplication<AuthorizationServiceApplication>(*args)
}

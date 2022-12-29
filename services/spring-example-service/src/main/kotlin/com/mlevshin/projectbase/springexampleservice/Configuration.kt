package com.mlevshin.projectbase.springexampleservice

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
class Configuration {

    fun  springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        http
            .authorizeExchange()
            .pathMatchers("/api/**").hasAuthority("SCOPE_ui_access")
            .anyExchange().authenticated()
            .and()
            .oauth2ResourceServer().jwt();
        return http.build();
    }

}
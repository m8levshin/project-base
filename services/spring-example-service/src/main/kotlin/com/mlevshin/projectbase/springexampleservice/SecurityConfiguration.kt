package com.mlevshin.projectbase.springexampleservice

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
class SecurityConfiguration {

    @Bean
    fun  springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        http
            .authorizeExchange()
            .pathMatchers("/api/test").permitAll()
            .pathMatchers("/api/**").hasAuthority("SCOPE_ui_access")
            .anyExchange().authenticated()
            .and()
            .oauth2ResourceServer().jwt();
        return http.build();
    }

}
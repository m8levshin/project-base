package com.mlevshin.projectbase.authorizationservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
class DefaultSecurityConfig {

    @Bean
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { authorize -> authorize
                .requestMatchers("/actuator/*").permitAll()
                .anyRequest().authenticated()
            }
            .formLogin(Customizer.withDefaults())
        return http.build()
    }

    @Bean
    fun users(): UserDetailsService {
        val user = User.withDefaultPasswordEncoder()
            .username("user")
            .password("pass")
            .roles("USER")
            .build()
        return InMemoryUserDetailsManager(user)
    }
}
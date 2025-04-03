package org.xxavierr404.auth.starter

import org.openapitools.client.apis.DefaultApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class CrosswaveAuthConfiguration {
    @Bean
    fun crosswaveAuthTokenFilter(apiClient: DefaultApi): AuthTokenFilter = AuthTokenFilter(apiClient)

    @Bean
    fun securityFilterChain(
        httpFilter: HttpSecurity,
        authTokenFilter: AuthTokenFilter
    ): SecurityFilterChain = httpFilter
        .addFilter(authTokenFilter)
        .authorizeHttpRequests { it.anyRequest().permitAll() }
        .build()
}
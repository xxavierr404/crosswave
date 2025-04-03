package org.xxavierr404.auth.starter

import org.openapitools.client.apis.DefaultApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity

@Configuration
open class CrosswaveAuthConfiguration {
    @Bean
    open fun crosswaveAuthTokenFilter(apiClient: DefaultApi) = AuthTokenFilter(apiClient)

    @Bean
    open fun securityFilterChain(
        httpFilter: HttpSecurity,
        authTokenFilter: AuthTokenFilter
    ) = httpFilter
        .addFilter(authTokenFilter)
        .authorizeHttpRequests { it.anyRequest().permitAll() }
        .build()
}
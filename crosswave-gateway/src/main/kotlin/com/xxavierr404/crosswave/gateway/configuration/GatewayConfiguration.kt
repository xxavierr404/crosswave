package com.xxavierr404.crosswave.gateway.configuration

import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GatewayConfiguration {
    @Bean
    fun routeLocator(builder: RouteLocatorBuilder): RouteLocator = builder.routes()
        .route("auth") {
            it.path("/auth/**").uri("lb://crosswave-auth")
        }
        .route("music") {
            it.path("/music/**").uri("lb://crosswave-music")
        }.build()
}
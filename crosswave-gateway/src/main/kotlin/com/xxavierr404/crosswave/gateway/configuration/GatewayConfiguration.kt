package com.xxavierr404.crosswave.gateway.configuration

import com.xxavierr404.crosswave.kafka.events.model.JwtService
import com.xxavierr404.crosswave.gateway.filters.EnrichWithUserDataFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory.NameConfig
import org.springframework.cloud.gateway.filter.factory.RemoveRequestHeaderGatewayFilterFactory
import org.springframework.cloud.gateway.filter.factory.StripPrefixGatewayFilterFactory
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GatewayConfiguration {
    @Bean
    fun jwtService(
        @Value("\${jwt.secret}") jwtSigningKey: String,
        @Value("\${jwt.expiration.millis}") jwtExpiration: Long,
    ) = JwtService(jwtSigningKey, jwtExpiration)

    @Bean
    fun routeLocator(
        builder: RouteLocatorBuilder,
        enrichWithUserDataFilter: EnrichWithUserDataFilter,
    ): RouteLocator = builder.routes()
        .route("auth-swagger") { it ->
            val config = StripPrefixGatewayFilterFactory.Config()
            config.parts = 1
            it.path("/auth/v3/api-docs").filters {
                it.filter(StripPrefixGatewayFilterFactory().apply(config))
            }.uri("lb://crosswave-auth")
        }
        .route("music-swagger") { it ->
            val config = StripPrefixGatewayFilterFactory.Config()
            config.parts = 1
            it.path("/music/v3/api-docs").filters {
                it.filter(StripPrefixGatewayFilterFactory().apply(config))
            }.uri("lb://crosswave-music")
        }
        .route("profiles-swagger") { it ->
            val config = StripPrefixGatewayFilterFactory.Config()
            config.parts = 1
            it.path("/profile/v3/api-docs").filters {
                it.filter(StripPrefixGatewayFilterFactory().apply(config))
            }.uri("lb://crosswave-analytics")
        }
        .route("auth") {
            it.path("/auth/**").uri("lb://crosswave-auth")
        }
        .route("music") { route ->
            route.path("/music/**")
                .filters {
                    val userIdHeader = NameConfig()
                    userIdHeader.name = "X-User-Id"
                    it
                        .filter(RemoveRequestHeaderGatewayFilterFactory().apply(userIdHeader))
                        .filter(enrichWithUserDataFilter.apply(""))
                }
                .uri("lb://crosswave-music")
        }
        .route("profile") { route ->
            route.path("/profile/**")
                .filters {
                    val userIdHeader = NameConfig()
                    userIdHeader.name = "X-User-Id"
                    it
                        .filter(RemoveRequestHeaderGatewayFilterFactory().apply(userIdHeader))
                        .filter(enrichWithUserDataFilter.apply(""))
                }
                .uri("lb://crosswave-analytics")
        }
        .route("suggest") { route ->
            route.path("/suggest/**")
                .filters {
                    val userIdHeader = NameConfig()
                    userIdHeader.name = "X-User-Id"
                    it
                        .filter(RemoveRequestHeaderGatewayFilterFactory().apply(userIdHeader))
                        .filter(enrichWithUserDataFilter.apply(""))
                }
                .uri("lb://crosswave-ai")
        }.build()

    @Bean
    fun enrichFilter(jwtService: JwtService) = EnrichWithUserDataFilter(jwtService)
}
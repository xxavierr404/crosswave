package com.xxavierr404.crosswave.gateway.filters

import com.xxavierr404.crosswave.kafka.events.model.JwtService
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.http.HttpMethod
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

private const val SKIP_BEARER = 7

class EnrichWithUserDataFilter(
    private val jwtService: JwtService
) : AbstractGatewayFilterFactory<String>() {

    override fun apply(ignored: String): GatewayFilter {
        return GatewayFilter { serverWebExchange: ServerWebExchange, gatewayFilterChain: GatewayFilterChain ->
            filter(serverWebExchange, gatewayFilterChain)
        }
    }

    private fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val token = exchange.request.headers["Authorization"]?.first()?.substring(SKIP_BEARER)

        if (token == null) {
            return chain.filter(exchange)
        }

        val enrichedRequest = token.let {
            when (exchange.request.method) {
                HttpMethod.GET -> enrich(exchange.request, token)
                HttpMethod.POST -> enrich(exchange.request, token)
                else -> exchange.request
            }
        }

        val enrichedExchange = exchange.mutate().request(enrichedRequest).build()

        return chain.filter(enrichedExchange)
    }

    private fun enrich(httpRequest: ServerHttpRequest, jwt: String): ServerHttpRequest {
        val userId = jwtService.parseToken(jwt)
        return httpRequest.mutate().header("X-User-Id", userId.toString()).build()
    }
}
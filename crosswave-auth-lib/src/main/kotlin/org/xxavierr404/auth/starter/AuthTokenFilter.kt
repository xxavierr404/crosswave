package org.xxavierr404.auth.starter

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.openapitools.client.apis.DefaultApi
import org.openapitools.client.models.LoginRequestDto
import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean

class AuthTokenFilter(
    private val authApiClient: DefaultApi
) : GenericFilterBean() {
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as? HttpServletRequest
        val authorizationHeader = httpRequest?.getHeader("Authorization")

        if (authorizationHeader?.startsWith("Bearer ") == true) {
            val accessToken = authorizationHeader.substring(7)
            val user = authApiClient.login(
                LoginRequestDto(accessToken)
            )
            SecurityContextHolder.getContext().authentication = OneTimeTokenAuthenticationToken(
                user,
                accessToken
            )
        }

        chain.doFilter(request, response)
    }
}
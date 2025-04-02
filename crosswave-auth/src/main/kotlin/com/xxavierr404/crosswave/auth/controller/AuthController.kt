package com.xxavierr404.crosswave.auth.controller

import com.xxavierr404.crosswave.auth.service.AuthService
import org.openapitools.api.AuthApiController
import org.openapitools.model.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authService: AuthService
): AuthApiController() {
    override fun getToken(getTokenDto: GetTokenDto): ResponseEntity<TokenResponseDto> {
        return ResponseEntity.ok(
            TokenResponseDto(
                authService.login(getTokenDto.username, getTokenDto.password)
            )
        )
    }

    override fun login(loginRequestDto: LoginRequestDto): ResponseEntity<UserDataDto> {
        return ResponseEntity.ok(
            UserDataDto(
                authService.resolveUUIDByToken(loginRequestDto.token)
            )
        )
    }

    override fun register(registerDto: RegisterDto): ResponseEntity<Unit> {
        authService.register(
            registerDto.username,
            registerDto.password,
            registerDto.email
        )

        return ResponseEntity.status(HttpStatus.CREATED).build()
    }
}
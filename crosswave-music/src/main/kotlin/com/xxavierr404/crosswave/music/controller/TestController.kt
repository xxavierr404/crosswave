package com.xxavierr404.crosswave.music.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.xxavierr404.crosswave.auth.client.apis.DefaultApi
import org.xxavierr404.crosswave.music.api.apis.TestApiController
import org.xxavierr404.crosswave.music.api.models.LoginRequestDto
import org.xxavierr404.crosswave.music.api.models.UserDataDto

@RestController
class TestController(
    private val authClient: DefaultApi
) : TestApiController() {
    override fun login(loginRequestDto: LoginRequestDto): ResponseEntity<UserDataDto> {
        return ResponseEntity.ok(
            UserDataDto(
                authClient.login(org.xxavierr404.crosswave.auth.client.models.LoginRequestDto(loginRequestDto.token)).block()!!.id
            )
        )
    }
}

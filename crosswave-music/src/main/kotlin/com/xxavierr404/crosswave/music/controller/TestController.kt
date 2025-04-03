package com.xxavierr404.crosswave.music.controller

import org.openapitools.api.TestApiController
import org.openapitools.model.LoginRequestDto
import org.openapitools.model.UserDataDto
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController : TestApiController() {
    override fun login(loginRequestDto: LoginRequestDto): ResponseEntity<UserDataDto> {
        return ResponseEntity.ok(
            (
                    SecurityContextHolder
                        .getContext()
                        .authentication
                        .principal as org.openapitools.client.models.UserDataDto
            ).toModel()
        )
    }
}

private fun org.openapitools.client.models.UserDataDto.toModel(): UserDataDto = UserDataDto(
    id = id,
)

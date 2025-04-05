package com.xxavierr404.crosswave.music.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.xxavierr404.crosswave.music.api.apis.MusicApiController
import org.xxavierr404.crosswave.music.api.models.UserDataDto
import java.util.*

@RestController
class MusicController: MusicApiController() {
    override fun login(xUserId: String): ResponseEntity<UserDataDto> {
        return ResponseEntity.ok(
            UserDataDto(
                UUID.fromString(xUserId)
            )
        )
    }
}

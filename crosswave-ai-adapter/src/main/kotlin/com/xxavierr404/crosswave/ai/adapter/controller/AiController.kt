package com.xxavierr404.crosswave.ai.adapter.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.xxavierr404.crosswave.music.api.apis.SuggestApiController
import org.xxavierr404.crosswave.music.api.models.GenrePredictionDto
import org.xxavierr404.crosswave.music.api.models.TrackInfo
import org.xxavierr404.crosswave.music.api.models.UserProfileDto

@RestController
class AiController : SuggestApiController() {
    override fun suggestTracks(xUserId: String): ResponseEntity<List<TrackInfo>> {
        return super.suggestTracks(xUserId)
    }

    override fun suggestUsers(xUserId: String): ResponseEntity<List<UserProfileDto>> {
        return super.suggestUsers(xUserId)
    }

    override fun predictGenre(file: MultipartFile): ResponseEntity<GenrePredictionDto> {
        return super.predictGenre(file)
    }
}
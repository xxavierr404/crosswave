package com.xxavierr404.crosswave.analytics.controller

import com.xxavierr404.crosswave.analytics.domain.UserProfile
import com.xxavierr404.crosswave.analytics.service.UserProfileService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import org.xxavierr404.crosswave.music.api.apis.ProfileApiController
import org.xxavierr404.crosswave.music.api.models.UpdateUserProfileDto
import org.xxavierr404.crosswave.music.api.models.UserProfileDto
import java.util.*

@RestController
class UserProfileController(
    private val profileService: UserProfileService,
) : ProfileApiController() {
    override fun updateProfile(
        xUserId: String,
        updateUserProfileDto: UpdateUserProfileDto
    ): ResponseEntity<UserProfileDto> {
        val userId = UUID.fromString(xUserId)
        val userProfile = profileService.findProfile(userId)

        if (userProfile == null) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }

        profileService.updateProfile(
            userProfile.copy(
                name = updateUserProfileDto.name,
                surname = updateUserProfileDto.surname,
                bio = updateUserProfileDto.bio
            )
        )

        return ResponseEntity.ok(profileService.findProfile(userId)!!.toDto())
    }

    override fun getProfile(xUserId: String, userId: String): ResponseEntity<UserProfileDto> {
        return profileService.findProfile(UUID.fromString(userId))?.let { ResponseEntity.ok(it.toDto()) }
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    override fun dislikeTrack(xUserId: String, trackId: String): ResponseEntity<UserProfileDto> {
        val userId = UUID.fromString(xUserId)
        profileService.dislikeTrack(userId, UUID.fromString(trackId))
        return profileService.findProfile(userId)?.let { ResponseEntity.ok(it.toDto()) }
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    override fun likeTrack(xUserId: String, trackId: String): ResponseEntity<UserProfileDto> {
        val userId = UUID.fromString(xUserId)
        profileService.likeTrack(userId, UUID.fromString(trackId))
        return profileService.findProfile(userId)?.let { ResponseEntity.ok(it.toDto()) }
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}

private fun UserProfile.toDto() = UserProfileDto(
    id,
    name,
    surname,
    bio,
    likedTracks
)

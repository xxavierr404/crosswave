package org.xxavierr404.crosswave.music.api.apis

import org.xxavierr404.crosswave.music.api.models.ErrorDto
import org.xxavierr404.crosswave.music.api.models.UpdateUserProfileDto
import org.xxavierr404.crosswave.music.api.models.UserProfileDto
import org.junit.jupiter.api.Test
import org.springframework.http.ResponseEntity

class ProfileApiTest {

    private val api: ProfileApiController = ProfileApiController()

    /**
     * To test ProfileApiController.getProfile
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    fun getProfileTest() {
        val xUserId: kotlin.String = TODO()
        val userId: kotlin.String = TODO()
        val response: ResponseEntity<UserProfileDto> = api.getProfile(xUserId, userId)

        // TODO: test validations
    }

    /**
     * To test ProfileApiController.getProfile
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    fun getProfileTest() {
        val xUserId: kotlin.String = TODO()
        val updateUserProfileDto: UpdateUserProfileDto = TODO()
        val response: ResponseEntity<UserProfileDto> = api.getProfile(xUserId, updateUserProfileDto)

        // TODO: test validations
    }
}

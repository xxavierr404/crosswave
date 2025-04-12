package org.xxavierr404.crosswave.music.api.apis

import org.xxavierr404.crosswave.music.api.models.ErrorDto
import org.xxavierr404.crosswave.music.api.models.UserDataDto
import org.junit.jupiter.api.Test
import org.springframework.http.ResponseEntity

class MusicApiTest {

    private val api: MusicApiController = MusicApiController()

    /**
     * To test MusicApiController.login
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    fun loginTest() {
        val xUserId: kotlin.String = TODO()
        val response: ResponseEntity<UserDataDto> = api.login(xUserId)

        // TODO: test validations
    }
}

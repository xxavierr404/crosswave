package org.xxavierr404.crosswave.music.api.apis

import org.xxavierr404.crosswave.music.api.models.ErrorDto
import org.xxavierr404.crosswave.music.api.models.LoginRequestDto
import org.xxavierr404.crosswave.music.api.models.UserDataDto
import org.junit.jupiter.api.Test
import org.springframework.http.ResponseEntity

class TestApiTest {

    private val api: TestApiController = TestApiController()

    /**
     * To test TestApiController.login
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    fun loginTest() {
        val loginRequestDto: LoginRequestDto = TODO()
        val response: ResponseEntity<UserDataDto> = api.login(loginRequestDto)

        // TODO: test validations
    }
}

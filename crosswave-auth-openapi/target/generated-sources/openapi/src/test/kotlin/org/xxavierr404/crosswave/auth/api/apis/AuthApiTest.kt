package org.xxavierr404.crosswave.auth.api.apis

import org.xxavierr404.crosswave.auth.api.models.ErrorDto
import org.xxavierr404.crosswave.auth.api.models.GetTokenDto
import org.xxavierr404.crosswave.auth.api.models.LoginRequestDto
import org.xxavierr404.crosswave.auth.api.models.RegisterDto
import org.xxavierr404.crosswave.auth.api.models.TokenResponseDto
import org.xxavierr404.crosswave.auth.api.models.UserDataDto
import org.junit.jupiter.api.Test
import org.springframework.http.ResponseEntity

class AuthApiTest {

    private val api: AuthApiController = AuthApiController()

    /**
     * To test AuthApiController.getToken
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    fun getTokenTest() {
        val getTokenDto: GetTokenDto = TODO()
        val response: ResponseEntity<TokenResponseDto> = api.getToken(getTokenDto)

        // TODO: test validations
    }

    /**
     * To test AuthApiController.login
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

    /**
     * To test AuthApiController.register
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    fun registerTest() {
        val registerDto: RegisterDto = TODO()
        val response: ResponseEntity<Unit> = api.register(registerDto)

        // TODO: test validations
    }
}

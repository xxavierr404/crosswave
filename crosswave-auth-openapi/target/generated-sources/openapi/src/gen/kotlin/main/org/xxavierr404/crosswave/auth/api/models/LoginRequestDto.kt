package org.xxavierr404.crosswave.auth.api.models

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.media.Schema

/**
 * 
 * @param token JWT-токен авторизации
 */
data class LoginRequestDto(

    @Schema(example = "xxxxxx.xxxxxx.xxxxxx", required = true, description = "JWT-токен авторизации")
    @get:JsonProperty("token", required = true) val token: kotlin.String
    ) {

}


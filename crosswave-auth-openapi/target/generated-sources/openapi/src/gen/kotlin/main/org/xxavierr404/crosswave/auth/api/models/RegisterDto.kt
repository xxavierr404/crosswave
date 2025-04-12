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
 * @param username Имя пользователя
 * @param password Пароль пользователя
 * @param email Электронная почта
 */
data class RegisterDto(

    @Schema(example = "john_doe", required = true, description = "Имя пользователя")
    @get:JsonProperty("username", required = true) val username: kotlin.String,

    @Schema(example = "securepassword123", required = true, description = "Пароль пользователя")
    @get:JsonProperty("password", required = true) val password: kotlin.String,

    @Schema(example = "john.doe@example.com", required = true, description = "Электронная почта")
    @get:JsonProperty("email", required = true) val email: kotlin.String
    ) {

}


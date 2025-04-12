package org.xxavierr404.crosswave.auth.api.apis

import org.xxavierr404.crosswave.auth.api.models.ErrorDto
import org.xxavierr404.crosswave.auth.api.models.GetTokenDto
import org.xxavierr404.crosswave.auth.api.models.LoginRequestDto
import org.xxavierr404.crosswave.auth.api.models.RegisterDto
import org.xxavierr404.crosswave.auth.api.models.TokenResponseDto
import org.xxavierr404.crosswave.auth.api.models.UserDataDto
import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.annotations.enums.*
import io.swagger.v3.oas.annotations.media.*
import io.swagger.v3.oas.annotations.responses.*
import io.swagger.v3.oas.annotations.security.*
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

import org.springframework.web.bind.annotation.*
import org.springframework.validation.annotation.Validated
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.beans.factory.annotation.Autowired

import jakarta.validation.Valid
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

import kotlin.collections.List
import kotlin.collections.Map

@RestController
@Validated
@RequestMapping("\${api.base-path:}")
class AuthApiController() {

    @Operation(
        summary = "Вход в систему",
        operationId = "getToken",
        description = """Авторизация пользователя по логину и паролю.""",
        responses = [
            ApiResponse(responseCode = "200", description = "Успешный вход в систему", content = [Content(schema = Schema(implementation = TokenResponseDto::class))]),
            ApiResponse(responseCode = "400", description = "Некорректные данные для входа", content = [Content(schema = Schema(implementation = ErrorDto::class))]),
            ApiResponse(responseCode = "401", description = "Неверные логин или пароль", content = [Content(schema = Schema(implementation = ErrorDto::class))]),
            ApiResponse(responseCode = "500", description = "Ошибка сервера", content = [Content(schema = Schema(implementation = ErrorDto::class))]) ]
    )
    @RequestMapping(
        method = [RequestMethod.POST],
        value = ["/auth/v1/get-token"],
        produces = ["application/json"],
        consumes = ["application/json"]
    )
    fun getToken(@Parameter(description = "", required = true) @Valid @RequestBody getTokenDto: GetTokenDto): ResponseEntity<TokenResponseDto> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @Operation(
        summary = "Чтение данных о пользователе по токену",
        operationId = "login",
        description = """""",
        responses = [
            ApiResponse(responseCode = "200", description = "Успешный выход из системы", content = [Content(schema = Schema(implementation = UserDataDto::class))]),
            ApiResponse(responseCode = "400", description = "Ошибка в токене", content = [Content(schema = Schema(implementation = ErrorDto::class))]),
            ApiResponse(responseCode = "500", description = "Ошибка сервера", content = [Content(schema = Schema(implementation = ErrorDto::class))]) ]
    )
    @RequestMapping(
        method = [RequestMethod.POST],
        value = ["/auth/v1/login"],
        produces = ["application/json"],
        consumes = ["application/json"]
    )
    fun login(@Parameter(description = "", required = true) @Valid @RequestBody loginRequestDto: LoginRequestDto): ResponseEntity<UserDataDto> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @Operation(
        summary = "Регистрация пользователя",
        operationId = "register",
        description = """Создание нового пользователя.""",
        responses = [
            ApiResponse(responseCode = "201", description = "Пользователь успешно зарегистрирован"),
            ApiResponse(responseCode = "400", description = "Некорректные данные для регистрации"),
            ApiResponse(responseCode = "409", description = "Пользователь с таким именем уже существует"),
            ApiResponse(responseCode = "500", description = "Ошибка сервера") ]
    )
    @RequestMapping(
        method = [RequestMethod.POST],
        value = ["/auth/v1/register"],
        consumes = ["application/json"]
    )
    fun register(@Parameter(description = "", required = true) @Valid @RequestBody registerDto: RegisterDto): ResponseEntity<Unit> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }
}

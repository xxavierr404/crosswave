package org.xxavierr404.crosswave.music.api.apis

import org.xxavierr404.crosswave.music.api.models.ErrorDto
import org.xxavierr404.crosswave.music.api.models.UserDataDto
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
class TestApiController() {

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
        value = ["/test"],
        produces = ["application/json"]
    )
    fun login(@Parameter(description = "", `in` = ParameterIn.HEADER, required = true) @RequestHeader(value = "X-User-Id", required = true) xUserId: kotlin.String): ResponseEntity<UserDataDto> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }
}

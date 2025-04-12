package org.xxavierr404.crosswave.music.api.apis

import org.xxavierr404.crosswave.music.api.models.ErrorDto
import org.xxavierr404.crosswave.music.api.models.UpdateUserProfileDto
import org.xxavierr404.crosswave.music.api.models.UserProfileDto
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
class ProfileApiController() {

    @Operation(
        summary = "Удалить трек из любимых",
        operationId = "dislikeTrack",
        description = """""",
        responses = [
            ApiResponse(responseCode = "200", description = "Информация о пользователе", content = [Content(schema = Schema(implementation = UserProfileDto::class))]),
            ApiResponse(responseCode = "403", description = "Нет доступа к треку", content = [Content(schema = Schema(implementation = ErrorDto::class))]),
            ApiResponse(responseCode = "500", description = "Ошибка сервера", content = [Content(schema = Schema(implementation = ErrorDto::class))]) ]
    )
    @RequestMapping(
        method = [RequestMethod.POST],
        value = ["/profile/v1/dislike"],
        produces = ["application/json"]
    )
    fun dislikeTrack(@Parameter(description = "", `in` = ParameterIn.HEADER, required = true) @RequestHeader(value = "X-User-Id", required = true) xUserId: kotlin.String,@NotNull @Parameter(description = "", required = true) @Valid @RequestParam(value = "trackId", required = true) trackId: kotlin.String): ResponseEntity<UserProfileDto> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @Operation(
        summary = "Получить данные о профиле",
        operationId = "getProfile",
        description = """""",
        responses = [
            ApiResponse(responseCode = "200", description = "Информация о пользователе", content = [Content(schema = Schema(implementation = UserProfileDto::class))]),
            ApiResponse(responseCode = "403", description = "Нет доступа к треку", content = [Content(schema = Schema(implementation = ErrorDto::class))]),
            ApiResponse(responseCode = "500", description = "Ошибка сервера", content = [Content(schema = Schema(implementation = ErrorDto::class))]) ]
    )
    @RequestMapping(
        method = [RequestMethod.GET],
        value = ["/profile/v1/get"],
        produces = ["application/json"]
    )
    fun getProfile(@Parameter(description = "", `in` = ParameterIn.HEADER, required = true) @RequestHeader(value = "X-User-Id", required = true) xUserId: kotlin.String,@NotNull @Parameter(description = "", required = true) @Valid @RequestParam(value = "userId", required = true) userId: kotlin.String): ResponseEntity<UserProfileDto> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @Operation(
        summary = "Добавить трек в любимые",
        operationId = "likeTrack",
        description = """""",
        responses = [
            ApiResponse(responseCode = "200", description = "Информация о пользователе", content = [Content(schema = Schema(implementation = UserProfileDto::class))]),
            ApiResponse(responseCode = "403", description = "Нет доступа к треку", content = [Content(schema = Schema(implementation = ErrorDto::class))]),
            ApiResponse(responseCode = "500", description = "Ошибка сервера", content = [Content(schema = Schema(implementation = ErrorDto::class))]) ]
    )
    @RequestMapping(
        method = [RequestMethod.POST],
        value = ["/profile/v1/like"],
        produces = ["application/json"]
    )
    fun likeTrack(@Parameter(description = "", `in` = ParameterIn.HEADER, required = true) @RequestHeader(value = "X-User-Id", required = true) xUserId: kotlin.String,@NotNull @Parameter(description = "", required = true) @Valid @RequestParam(value = "trackId", required = true) trackId: kotlin.String): ResponseEntity<UserProfileDto> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @Operation(
        summary = "Подписаться на пользователя",
        operationId = "subscribe",
        description = """""",
        responses = [
            ApiResponse(responseCode = "200", description = "Информация о пользователе", content = [Content(schema = Schema(implementation = UserProfileDto::class))]),
            ApiResponse(responseCode = "403", description = "Нет доступа к треку", content = [Content(schema = Schema(implementation = ErrorDto::class))]),
            ApiResponse(responseCode = "500", description = "Ошибка сервера", content = [Content(schema = Schema(implementation = ErrorDto::class))]) ]
    )
    @RequestMapping(
        method = [RequestMethod.POST],
        value = ["/profile/v1/subscribe"],
        produces = ["application/json"]
    )
    fun subscribe(@Parameter(description = "", `in` = ParameterIn.HEADER, required = true) @RequestHeader(value = "X-User-Id", required = true) xUserId: kotlin.String,@NotNull @Parameter(description = "", required = true) @Valid @RequestParam(value = "targetUserId", required = true) targetUserId: kotlin.String): ResponseEntity<UserProfileDto> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @Operation(
        summary = "Отписаться от пользователя",
        operationId = "unsubscribe",
        description = """""",
        responses = [
            ApiResponse(responseCode = "200", description = "Информация о пользователе", content = [Content(schema = Schema(implementation = UserProfileDto::class))]),
            ApiResponse(responseCode = "403", description = "Нет доступа к треку", content = [Content(schema = Schema(implementation = ErrorDto::class))]),
            ApiResponse(responseCode = "500", description = "Ошибка сервера", content = [Content(schema = Schema(implementation = ErrorDto::class))]) ]
    )
    @RequestMapping(
        method = [RequestMethod.POST],
        value = ["/profile/v1/unsubscribe"],
        produces = ["application/json"]
    )
    fun unsubscribe(@Parameter(description = "", `in` = ParameterIn.HEADER, required = true) @RequestHeader(value = "X-User-Id", required = true) xUserId: kotlin.String,@NotNull @Parameter(description = "", required = true) @Valid @RequestParam(value = "targetUserId", required = true) targetUserId: kotlin.String): ResponseEntity<UserProfileDto> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @Operation(
        summary = "Обновить данные о профиле",
        operationId = "updateProfile",
        description = """""",
        responses = [
            ApiResponse(responseCode = "200", description = "Информация о пользователе", content = [Content(schema = Schema(implementation = UserProfileDto::class))]),
            ApiResponse(responseCode = "403", description = "Нет доступа к треку", content = [Content(schema = Schema(implementation = ErrorDto::class))]),
            ApiResponse(responseCode = "500", description = "Ошибка сервера", content = [Content(schema = Schema(implementation = ErrorDto::class))]) ]
    )
    @RequestMapping(
        method = [RequestMethod.POST],
        value = ["/profile/v1/update"],
        produces = ["application/json"],
        consumes = ["application/json"]
    )
    fun updateProfile(@Parameter(description = "", `in` = ParameterIn.HEADER, required = true) @RequestHeader(value = "X-User-Id", required = true) xUserId: kotlin.String,@Parameter(description = "", required = true) @Valid @RequestBody updateUserProfileDto: UpdateUserProfileDto): ResponseEntity<UserProfileDto> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }
}

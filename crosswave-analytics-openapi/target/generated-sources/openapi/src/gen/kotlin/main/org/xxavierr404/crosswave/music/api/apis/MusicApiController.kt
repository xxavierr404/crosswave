package org.xxavierr404.crosswave.music.api.apis

import org.xxavierr404.crosswave.music.api.models.ErrorDto
import org.xxavierr404.crosswave.music.api.models.TrackInfo
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
class MusicApiController() {

    @Operation(
        summary = "Получить информацию о треке",
        operationId = "getInfo",
        description = """""",
        responses = [
            ApiResponse(responseCode = "200", description = "Информация о треке", content = [Content(schema = Schema(implementation = TrackInfo::class))]),
            ApiResponse(responseCode = "403", description = "Нет доступа к треку", content = [Content(schema = Schema(implementation = ErrorDto::class))]),
            ApiResponse(responseCode = "500", description = "Ошибка сервера", content = [Content(schema = Schema(implementation = ErrorDto::class))]) ]
    )
    @RequestMapping(
        method = [RequestMethod.GET],
        value = ["/music/v1/get-info"],
        produces = ["application/json"]
    )
    fun getInfo(@Parameter(description = "", `in` = ParameterIn.HEADER, required = true) @RequestHeader(value = "X-User-Id", required = true) xUserId: kotlin.String,@NotNull @Parameter(description = "", required = true) @Valid @RequestParam(value = "trackId", required = true) trackId: kotlin.String): ResponseEntity<TrackInfo> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @Operation(
        summary = "Потоковое вещание трека",
        operationId = "stream",
        description = """""",
        responses = [
            ApiResponse(responseCode = "200", description = "Файл трека", content = [Content(schema = Schema(implementation = org.springframework.core.io.Resource::class))]),
            ApiResponse(responseCode = "403", description = "Нет доступа к треку", content = [Content(schema = Schema(implementation = ErrorDto::class))]),
            ApiResponse(responseCode = "500", description = "Ошибка сервера", content = [Content(schema = Schema(implementation = ErrorDto::class))]) ]
    )
    @RequestMapping(
        method = [RequestMethod.GET],
        value = ["/music/v1/play"],
        produces = ["audio/mpeg", "application/json"]
    )
    fun stream(@Parameter(description = "", `in` = ParameterIn.HEADER, required = true) @RequestHeader(value = "X-User-Id", required = true) xUserId: kotlin.String,@NotNull @Parameter(description = "", required = true) @Valid @RequestParam(value = "trackId", required = true) trackId: kotlin.String,@NotNull @Parameter(description = "", required = true) @Valid @RequestParam(value = "offset", required = true) offset: kotlin.Int,@NotNull @Parameter(description = "", required = true) @Valid @RequestParam(value = "length", required = true) length: kotlin.Int): ResponseEntity<org.springframework.core.io.Resource> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @Operation(
        summary = "Загрузить трек",
        operationId = "upload",
        description = """""",
        responses = [
            ApiResponse(responseCode = "200", description = "Информация о загруженном треке", content = [Content(schema = Schema(implementation = TrackInfo::class))]),
            ApiResponse(responseCode = "401", description = "Пользователь не авторизован", content = [Content(schema = Schema(implementation = ErrorDto::class))]),
            ApiResponse(responseCode = "500", description = "Ошибка сервера", content = [Content(schema = Schema(implementation = ErrorDto::class))]) ]
    )
    @RequestMapping(
        method = [RequestMethod.POST],
        value = ["/music/v1/upload"],
        produces = ["application/json"],
        consumes = ["multipart/form-data"]
    )
    fun upload(@Parameter(description = "", `in` = ParameterIn.HEADER, required = true) @RequestHeader(value = "X-User-Id", required = true) xUserId: kotlin.String,@Parameter(description = "") @Valid @RequestPart("file", required = true) file: org.springframework.web.multipart.MultipartFile,@Parameter(description = "", required = true) @RequestParam(value = "author", required = true) author: kotlin.String ,@Parameter(description = "", required = true) @RequestParam(value = "name", required = true) name: kotlin.String ): ResponseEntity<TrackInfo> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }
}

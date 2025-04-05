package com.xxavierr404.crosswave.auth.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.xxavierr404.crosswave.auth.api.models.ErrorDto

@ControllerAdvice
class AuthControllerAdvice {
    val logger: Logger = LoggerFactory.getLogger(AuthControllerAdvice::class.java)

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleIllegalArgumentException(illegalArgumentException: IllegalArgumentException): ResponseEntity<ErrorDto> {
        logger.error("Invalid request: ${illegalArgumentException.message}")
        return ResponseEntity.badRequest().body(
            ErrorDto(
                code = HttpStatus.BAD_REQUEST.value(),
                message = illegalArgumentException.message!!
            )
        )
    }
}
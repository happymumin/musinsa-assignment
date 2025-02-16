package com.musinsa.coordinator.rest.advice

import com.musinsa.coordinator.exception.CustomException
import com.musinsa.coordinator.rest.dto.ErrorResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ControllerExceptionHandler {

    private val logger = KotlinLogging.logger { }

    @ExceptionHandler(CustomException::class)
    fun handle(e: CustomException): ResponseEntity<ErrorResponse> {
        logger.warn { e }
        return ResponseEntity.status(e.errorCode.httpStatus).body(ErrorResponse(e.errorCode))
    }

    @ExceptionHandler(RuntimeException::class)
    fun handle(e: RuntimeException): ResponseEntity<ErrorResponse> {
        logger.error { e }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse("SERVER_ERROR", "서버에 문제가 발생했습니다. 잠시 후 다시 시도해주세요."))
    }
}
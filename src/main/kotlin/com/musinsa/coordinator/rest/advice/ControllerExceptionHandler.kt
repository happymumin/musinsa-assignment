package com.musinsa.coordinator.rest.advice

import com.musinsa.coordinator.exception.CustomException
import com.musinsa.coordinator.rest.dto.ErrorResponse
import io.github.oshai.kotlinlogging.KotlinLogging
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
}
package com.musinsa.coordinator.rest.errorcode

import org.springframework.http.HttpStatus

interface RestErrorCode {
    val httpStatus: HttpStatus
    val name: String
    val reason: String
}
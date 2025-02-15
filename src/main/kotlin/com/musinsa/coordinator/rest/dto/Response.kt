package com.musinsa.coordinator.rest.dto

import com.musinsa.coordinator.rest.errorcode.RestErrorCode

data class ErrorResponse(
    val errorCode: String,
    val reason: String
) {
    constructor(errorCode: RestErrorCode) : this(errorCode.name, errorCode.reason)
}
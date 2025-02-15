package com.musinsa.coordinator.exception

import com.musinsa.coordinator.rest.errorcode.RestErrorCode

abstract class CustomException : RuntimeException() {
    abstract val errorCode: RestErrorCode

    override fun toString(): String {
        return "${this::class.simpleName}(errorCode=${errorCode.name}, reason=${errorCode.reason})"
    }
}

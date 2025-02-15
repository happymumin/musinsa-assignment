package com.musinsa.coordinator.domain.product.rest

import com.musinsa.coordinator.rest.errorcode.RestErrorCode
import org.springframework.http.HttpStatus

enum class ProductErrorCode(override val reason: String, override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST) :
    RestErrorCode {
    NOT_FOUND_PRODUCT("존재하지 않는 상품입니다.", HttpStatus.NOT_FOUND),
    DELETED_PRODUCT("삭제된 상품입니다.", HttpStatus.BAD_REQUEST)
}
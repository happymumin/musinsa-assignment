package com.musinsa.coordinator.domain.brand.rest

import com.musinsa.coordinator.rest.errorcode.RestErrorCode
import org.springframework.http.HttpStatus

enum class BrandErrorCode(override val reason: String, override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST) :
    RestErrorCode {
    NOT_FOUND_BRAND("존재하지 않는 브랜드입니다.", HttpStatus.NOT_FOUND),
    DELETED_BRAND("삭제된 브랜드입니다.", HttpStatus.BAD_REQUEST)
}
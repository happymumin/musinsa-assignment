package com.musinsa.coordinator.domain.category.rest

import com.musinsa.coordinator.rest.errorcode.RestErrorCode
import org.springframework.http.HttpStatus

enum class CategoryErrorCode(
    override val reason: String,
    override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST
) :
    RestErrorCode {
    NOT_FOUND_CATEGORY("존재하지 않는 카테고리입니다.", HttpStatus.NOT_FOUND),
}
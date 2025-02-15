package com.musinsa.coordinator.domain.category

import com.musinsa.coordinator.domain.category.rest.CategoryErrorCode
import com.musinsa.coordinator.exception.CustomException
import com.musinsa.coordinator.rest.errorcode.RestErrorCode

class CategoryException(errorCode: CategoryErrorCode) : CustomException() {
    override val errorCode: RestErrorCode = errorCode
}
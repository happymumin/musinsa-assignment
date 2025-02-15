package com.musinsa.coordinator.domain.brand

import com.musinsa.coordinator.domain.brand.rest.BrandErrorCode
import com.musinsa.coordinator.exception.CustomException
import com.musinsa.coordinator.rest.errorcode.RestErrorCode

class BrandException(errorCode: BrandErrorCode) : CustomException() {
    override val errorCode: RestErrorCode = errorCode
}
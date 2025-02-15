package com.musinsa.coordinator.domain.product

import com.musinsa.coordinator.domain.product.rest.ProductErrorCode
import com.musinsa.coordinator.exception.CustomException
import com.musinsa.coordinator.rest.errorcode.RestErrorCode

class ProductException(errorCode: ProductErrorCode) : CustomException() {
    override val errorCode: RestErrorCode = errorCode
}
package com.musinsa.coordinator.domain.product.dto

import com.musinsa.coordinator.domain.product.Product
import com.querydsl.core.annotations.QueryProjection

data class ProductWithBrandDetail @QueryProjection constructor(
    val product: Product,
    val brandName: String
)
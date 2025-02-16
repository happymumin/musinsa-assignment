package com.musinsa.coordinator.domain.product.dto

import com.musinsa.coordinator.util.BrandId
import com.musinsa.coordinator.util.CategoryId
import com.querydsl.core.annotations.QueryProjection

data class MinPriceWithBrandCategory @QueryProjection constructor(
    val minPrice: Int,
    val brandId: BrandId,
    val categoryId: CategoryId,
)
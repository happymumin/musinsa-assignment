package com.musinsa.coordinator.domain.product.rest.dto

import com.musinsa.coordinator.util.BrandId
import com.musinsa.coordinator.util.CategoryId
import io.swagger.v3.oas.annotations.media.Schema

data class ProductCreateOrUpdateRequest(
    @Schema(description = "상품명")
    val name: String,
    @Schema(description = "가격")
    val price: Int,
    @Schema(description = "카테고리 ID")
    val categoryId: CategoryId,
    @Schema(description = "브랜드 ID")
    val brandId: BrandId
)
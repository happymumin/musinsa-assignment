package com.musinsa.coordinator.domain.brand.dto

import io.swagger.v3.oas.annotations.media.Schema

data class BrandCreateOrUpdateRequest(
    @Schema(description = "브랜드명")
    val name: String
)
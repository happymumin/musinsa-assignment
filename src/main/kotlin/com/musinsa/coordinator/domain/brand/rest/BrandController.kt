package com.musinsa.coordinator.domain.brand.rest

import com.musinsa.coordinator.domain.brand.BrandService
import com.musinsa.coordinator.domain.brand.dto.BrandCreateOrUpdateRequest
import com.musinsa.coordinator.domain.brand.dto.BrandCreateResponse
import com.musinsa.coordinator.util.BrandId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.web.bind.annotation.*


@RequestMapping("/api")
@RestController
class BrandController(
    private val brandService: BrandService
) {

    @PostMapping("/v1/brands")
    suspend fun create(
        @RequestBody request: BrandCreateOrUpdateRequest
    ): BrandCreateResponse {
        val brand = withContext(Dispatchers.IO) {
            brandService.createSync(request)
        }
        return BrandCreateResponse(brand.id!!)
    }

    @PutMapping("/v1/brands/{brandId}")
    suspend fun update(
        @PathVariable brandId: BrandId,
        @RequestBody request: BrandCreateOrUpdateRequest
    ) {
        withContext(Dispatchers.IO) {
            brandService.updateSync(brandId, request = request)
        }
    }

    @DeleteMapping("/v1/brands/{brandId}")
    suspend fun delete(
        @PathVariable brandId: BrandId,
    ) {
        withContext(Dispatchers.IO) {
            brandService.deleteSync(brandId)
        }
    }
}
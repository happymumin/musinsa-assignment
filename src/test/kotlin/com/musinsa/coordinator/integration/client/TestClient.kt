package com.musinsa.coordinator.integration.client

import com.musinsa.coordinator.domain.brand.dto.BrandCreateOrUpdateRequest
import com.musinsa.coordinator.domain.brand.dto.BrandCreateResponse
import com.musinsa.coordinator.util.BrandId
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

class TestClient(private val client: WebClient) {

    // ========================= brand ======================

    /**
     * @see [com.musinsa.coordinator.domain.brand.BrandController.create]
     */
    suspend fun createBrand(request: BrandCreateOrUpdateRequest): BrandCreateResponse {
        return client.post()
            .uri("/api/v1/brands")
            .bodyValue(request)
            .retrieve()
            .awaitBody<BrandCreateResponse>()
    }

    /**
     * @see [com.musinsa.coordinator.domain.brand.BrandController.update]
     */
    suspend fun updateBrand(brandId: BrandId, request: BrandCreateOrUpdateRequest) {
        return client.put()
            .uri("/api/v1/brands/${brandId}")
            .bodyValue(request)
            .retrieve()
            .awaitBody()
    }

    /**
     * @see [com.musinsa.coordinator.domain.brand.BrandController.delete]
     */
    suspend fun deleteBrand(brandId: BrandId) {
        return client.delete()
            .uri("/api/v1/brands/${brandId}")
            .retrieve()
            .awaitBody()
    }
}
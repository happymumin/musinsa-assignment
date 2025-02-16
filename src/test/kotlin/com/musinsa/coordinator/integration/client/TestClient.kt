package com.musinsa.coordinator.integration.client

import com.musinsa.coordinator.domain.brand.rest.dto.BrandCreateOrUpdateRequest
import com.musinsa.coordinator.domain.brand.rest.dto.BrandCreateResponse
import com.musinsa.coordinator.domain.product.rest.dto.ProductCreateOrUpdateRequest
import com.musinsa.coordinator.domain.product.rest.dto.ProductCreateResponse
import com.musinsa.coordinator.domain.stat.dto.CategoriesWithMinPriceAndBrandResponse
import com.musinsa.coordinator.domain.stat.dto.CategoryWithMinMaxPriceAndBrandResponse
import com.musinsa.coordinator.domain.stat.dto.CheapestBrandWithMinPriceByCategoryResponse
import com.musinsa.coordinator.util.BrandId
import com.musinsa.coordinator.util.ProductId
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

class TestClient(private val client: WebClient) {

    // ========================= brand ======================

    /**
     * @see [com.musinsa.coordinator.domain.brand.rest.BrandController.create]
     */
    suspend fun createBrand(request: BrandCreateOrUpdateRequest): BrandCreateResponse {
        return client.post()
            .uri("/api/v1/brands")
            .bodyValue(request)
            .retrieve()
            .awaitBody<BrandCreateResponse>()
    }

    /**
     * @see [com.musinsa.coordinator.domain.brand.rest.BrandController.update]
     */
    suspend fun updateBrand(brandId: BrandId, request: BrandCreateOrUpdateRequest) {
        return client.put()
            .uri("/api/v1/brands/${brandId}")
            .bodyValue(request)
            .retrieve()
            .awaitBody()
    }

    /**
     * @see [com.musinsa.coordinator.domain.brand.rest.BrandController.delete]
     */
    suspend fun deleteBrand(brandId: BrandId) {
        return client.delete()
            .uri("/api/v1/brands/${brandId}")
            .retrieve()
            .awaitBody()
    }

    // ========================= product ======================

    /**
     * @see [com.musinsa.coordinator.domain.product.rest.ProductController.create]
     */
    suspend fun createProduct(request: ProductCreateOrUpdateRequest): ProductCreateResponse {
        return client.post()
            .uri("/api/v1/products")
            .bodyValue(request)
            .retrieve()
            .awaitBody<ProductCreateResponse>()
    }

    /**
     * @see [com.musinsa.coordinator.domain.product.rest.ProductController.update]
     */
    suspend fun updateProduct(productId: ProductId, request: ProductCreateOrUpdateRequest) {
        return client.put()
            .uri("/api/v1/products/${productId}")
            .bodyValue(request)
            .retrieve()
            .awaitBody()
    }

    /**
     * @see [com.musinsa.coordinator.domain.product.rest.ProductController.delete]
     */
    suspend fun deleteProduct(productId: ProductId) {
        return client.delete()
            .uri("/api/v1/products/${productId}")
            .retrieve()
            .awaitBody()
    }

    // ========================= stat ======================
    /**
     * @see [com.musinsa.coordinator.domain.stat.rest.StatController.getCategoriesWithMinPriceAndBrand]
     */
    suspend fun getCategoriesWithMinPriceAndBrandStat(): CategoriesWithMinPriceAndBrandResponse {
        return client.get()
            .uri("/api/v1/stats/categories/min-price-brand")
            .retrieve()
            .awaitBody<CategoriesWithMinPriceAndBrandResponse>()
    }

    /**
     * @see [com.musinsa.coordinator.domain.stat.rest.StatController.getCategoryWithMinMaxPriceAndBrand]
     */
    suspend fun getCategoryWithMinMaxPriceAndBrand(categoryName: String): CategoryWithMinMaxPriceAndBrandResponse {
        return client.get()
            .uri("/api/v1/stats/categories/{categoryName}/min-max-price-brand", categoryName)
            .retrieve()
            .awaitBody<CategoryWithMinMaxPriceAndBrandResponse>()
    }

    /**
     * @see [com.musinsa.coordinator.domain.stat.rest.StatController.getCheapestBrandWithMinPriceByCategory]
     */
    suspend fun getCheapestBrandWithMinPriceByCategory(): CheapestBrandWithMinPriceByCategoryResponse {
        return client.get()
            .uri("/api/v1/stats/brands/min-total-price")
            .retrieve()
            .awaitBody<CheapestBrandWithMinPriceByCategoryResponse>()
    }
}
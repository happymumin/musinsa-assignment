package com.musinsa.coordinator.domain.product.rest

import com.musinsa.coordinator.domain.product.ProductService
import com.musinsa.coordinator.domain.product.rest.dto.ProductCreateOrUpdateRequest
import com.musinsa.coordinator.domain.product.rest.dto.ProductCreateResponse
import com.musinsa.coordinator.util.ProductId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.web.bind.annotation.*


@RequestMapping("/api")
@RestController
class ProductController(
    private val productService: ProductService,
) {

    @PostMapping("/v1/products")
    suspend fun create(
        @RequestBody request: ProductCreateOrUpdateRequest
    ): ProductCreateResponse {
        val product = productService.create(request)
        return ProductCreateResponse(product.id!!)
    }

    @PutMapping("/v1/products/{productId}")
    suspend fun update(
        @PathVariable productId: ProductId,
        @RequestBody request: ProductCreateOrUpdateRequest
    ) {
        productService.update(productId, request = request)
    }

    @DeleteMapping("/v1/products/{productId}")
    suspend fun delete(
        @PathVariable productId: ProductId
    ) {
        withContext(Dispatchers.IO) {
            productService.deleteSync(productId)
        }
    }
}
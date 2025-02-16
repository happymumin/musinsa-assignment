package com.musinsa.coordinator.domain.stat

import com.musinsa.coordinator.domain.category.CategoryService
import com.musinsa.coordinator.domain.product.ProductService
import com.musinsa.coordinator.domain.product.dto.ProductWithBrandDetail
import com.musinsa.coordinator.util.CategoryId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service

@Service
class StatService(
    private val productService: ProductService,
    private val categoryService: CategoryService
) {

    suspend fun getMinPriceAndBrandByCategory(): Map<CategoryId, ProductWithBrandDetail> {
        return categoryService.getCategoryManager().categories
            .chunked(3)
            .map { chunked ->
                withContext(Dispatchers.IO) {
                    chunked.map { category ->
                        async { productService.findMinPriceEnabledProductByCategorySync(category.id)!! }
                    }.awaitAll()
                }
            }
            .flatten()
            .associateBy { it.product.categoryId }
    }
}
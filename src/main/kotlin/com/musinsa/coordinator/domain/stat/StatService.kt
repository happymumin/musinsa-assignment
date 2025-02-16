package com.musinsa.coordinator.domain.stat

import com.musinsa.coordinator.domain.brand.Brand
import com.musinsa.coordinator.domain.brand.BrandService
import com.musinsa.coordinator.domain.category.CategoryService
import com.musinsa.coordinator.domain.product.ProductService
import com.musinsa.coordinator.domain.product.dto.MinPriceWithBrandCategory
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
    private val categoryService: CategoryService,
    private val brandService: BrandService,
) {

    suspend fun getMinPriceAndBrandByCategory(): Map<CategoryId, ProductWithBrandDetail> {
        return categoryService.getCategoryManager().categories
            .chunked(3)
            .map { chunked ->
                withContext(Dispatchers.IO) {
                    chunked.map { category ->
                        async { productService.findMinPriceSellingProductByCategorySync(category.id)!! }
                    }.awaitAll()
                }
            }
            .flatten()
            .associateBy { it.product.categoryId }
    }

    suspend fun getCheapestBrandWithMinPriceByCategory(): Pair<Brand, List<MinPriceWithBrandCategory>> {
        return withContext(Dispatchers.IO) {
            productService.getMinPriceByBrandCategorySync()
                .groupBy { it.brandId }
                .minBy { (_, minPriceByBrandCategory) -> minPriceByBrandCategory.sumOf { it.minPrice } }
                .let { brandService.getEnabledOrThrowSync(it.key) to it.value }
        }
    }
}
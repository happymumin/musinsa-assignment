package com.musinsa.coordinator.domain.stat.rest

import com.musinsa.coordinator.domain.category.CategoryService
import com.musinsa.coordinator.domain.product.dto.ProductWithBrandDetail
import com.musinsa.coordinator.domain.stat.StatService
import com.musinsa.coordinator.domain.stat.dto.CategoriesWithMinPriceAndBrandResponse
import com.musinsa.coordinator.domain.stat.dto.CategoryWithMinMaxPriceAndBrandResponse
import com.musinsa.coordinator.domain.stat.dto.CheapestBrandWithMinPriceByCategoryResponse
import com.musinsa.coordinator.util.CategoryId
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RequestMapping("/api")
@RestController
class StatController(
    private val statService: StatService,
    private val categoryService: CategoryService,
) {

    @Operation(summary = "카테고리별 최저가 상품의 가격과 브랜드 조회")
    @GetMapping("/v1/stats/categories/min-price-brand")
    suspend fun getCategoriesWithMinPriceAndBrand(): CategoriesWithMinPriceAndBrandResponse {
        val minPriceProductByCategoryId: Map<CategoryId, ProductWithBrandDetail> =
            statService.getMinPriceAndBrandByCategory()
        return CategoriesWithMinPriceAndBrandResponse.of(
            minPriceProductByCategoryId = minPriceProductByCategoryId,
            categoryManager = categoryService.getCategoryManager(),
        )
    }

    @Operation(summary = "카테고리의 최저/최고 상품의 가격과 브랜드 조회")
    @GetMapping("/v1/stats/categories/{categoryName}/min-max-price-brand")
    suspend fun getCategoryWithMinMaxPriceAndBrand(
        @PathVariable categoryName: String
    ): CategoryWithMinMaxPriceAndBrandResponse {
        val category = categoryService.getCategoryManager().getByNameOrThrow(categoryName)
        val (minPriceProduct, maxPriceProduct) = statService.getMinMaxPriceAdBrand(category.id)
        return CategoryWithMinMaxPriceAndBrandResponse.of(
            category = category, minPriceProduct = minPriceProduct, maxPriceProduct = maxPriceProduct
        )
    }

    @Operation(summary = "카테고리별 총액이 가장 저렴한 단일 브랜드 조회")
    @GetMapping("/v1/stats/brands/min-total-price")
    suspend fun getCheapestBrandWithMinPriceByCategory(): CheapestBrandWithMinPriceByCategoryResponse {
        val (brand, minPriceWithCategory) = statService.getCheapestBrandWithMinPriceByCategory()
        return CheapestBrandWithMinPriceByCategoryResponse.of(
            brand = brand,
            minPriceWithCategory = minPriceWithCategory,
            categoryManager = categoryService.getCategoryManager()
        )
    }
}
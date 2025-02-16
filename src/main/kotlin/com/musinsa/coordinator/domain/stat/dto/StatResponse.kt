package com.musinsa.coordinator.domain.stat.dto

import com.musinsa.coordinator.domain.category.dto.CategoryManager
import com.musinsa.coordinator.domain.product.dto.ProductWithBrandDetail
import com.musinsa.coordinator.util.CategoryId

data class CategoriesWithMinPriceAndBrandResponse(
    val categories: List<Data>,
    val totalPrice: Int
) {
    class Data(
        val categoryName: String,
        val brandName: String,
        val minPrice: Int
    )

    companion object {
        fun of(
            minPriceProductByCategoryId: Map<CategoryId, ProductWithBrandDetail>,
            categoryManager: CategoryManager,
        ): CategoriesWithMinPriceAndBrandResponse {
            var totalPrice = 0
            val categories = minPriceProductByCategoryId.map { (categoryId, productWithBrand) ->
                totalPrice += productWithBrand.product.price
                Data(
                    categoryManager.getByIdOrThrow(categoryId).name,
                    productWithBrand.brandName,
                    productWithBrand.product.price
                )
            }
            return CategoriesWithMinPriceAndBrandResponse(categories, totalPrice)
        }
    }
}
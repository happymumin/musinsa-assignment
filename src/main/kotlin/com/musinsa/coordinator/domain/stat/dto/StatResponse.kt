package com.musinsa.coordinator.domain.stat.dto

import com.musinsa.coordinator.domain.brand.Brand
import com.musinsa.coordinator.domain.category.Category
import com.musinsa.coordinator.domain.category.dto.CategoryManager
import com.musinsa.coordinator.domain.product.dto.MinPriceWithBrandCategory
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

data class CheapestBrandWithMinPriceByCategoryResponse(
    val minPrice: Data
) {
    class Data(
        val brandName: String,
        val minPricesWithCategory: List<MinPriceWithCategory>,
        val totalPrice: Int
    ) {
        class MinPriceWithCategory(
            val categoryName: String,
            val minPrice: Int
        )
    }

    companion object {
        fun of(
            brand: Brand,
            minPriceWithCategory: List<MinPriceWithBrandCategory>,
            categoryManager: CategoryManager,
        ): CheapestBrandWithMinPriceByCategoryResponse {
            return CheapestBrandWithMinPriceByCategoryResponse(
                Data(
                    brandName = brand.name,
                    minPricesWithCategory = minPriceWithCategory.map {
                        Data.MinPriceWithCategory(categoryManager.getByIdOrThrow(it.categoryId).name, it.minPrice)
                    },
                    totalPrice = minPriceWithCategory.sumOf { it.minPrice }
                )
            )
        }
    }
}

class CategoryWithMinMaxPriceAndBrandResponse(
    val categoryName: String,
    val minPrice: Data,
    val maxPrice: Data
) {
    class Data(
        val brandName: String,
        val price: Int,
    )

    companion object {
        fun of(
            category: Category,
            minPriceProduct: ProductWithBrandDetail,
            maxPriceProduct: ProductWithBrandDetail,
        ): CategoryWithMinMaxPriceAndBrandResponse {
            return CategoryWithMinMaxPriceAndBrandResponse(
                categoryName = category.name,
                minPrice = Data(
                    brandName = minPriceProduct.brandName,
                    price = minPriceProduct.product.price
                ), maxPrice = Data(
                    brandName = maxPriceProduct.brandName,
                    price = maxPriceProduct.product.price
                )
            )
        }
    }
}


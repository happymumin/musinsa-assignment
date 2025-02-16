package com.musinsa.coordinator.domain.stat

import com.musinsa.coordinator.domain.brand.Brand
import com.musinsa.coordinator.domain.brand.BrandService
import com.musinsa.coordinator.domain.category.CategoryService
import com.musinsa.coordinator.domain.product.Product
import com.musinsa.coordinator.domain.product.ProductService
import com.musinsa.coordinator.domain.product.dto.MinPriceWithBrandCategory
import com.musinsa.coordinator.domain.product.dto.ProductWithBrandDetail
import com.musinsa.coordinator.testutil.defaultCategoryManager
import com.musinsa.coordinator.testutil.mock
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given

class StatServiceTest {

    private val productService: ProductService = mock()
    private val categoryService: CategoryService = mock()
    private val brandService: BrandService = mock()
    private val statService = StatService(productService, categoryService, brandService)

    @Test
    fun `카테고리별 최저가 상품의 정보를 조회한다`() {
        runBlocking {
            given(categoryService.getCategoryManager()).willReturn(defaultCategoryManager) // 20개

            val productByCategory = defaultCategoryManager.categories.mapIndexed { index, category ->
                category.id to ProductWithBrandDetail(Product(index, "상품", 1000 * (index + 1), 0, category.id), "")
            }.toMap()

            defaultCategoryManager.categories.forEach { category ->
                given(productService.findMinPriceSellingProductByCategorySync(category.id))
                    .willReturn(productByCategory.getValue(category.id))
            }

            val result = statService.getMinPriceAndBrandByCategory()
            assertEquals(result, productByCategory)
        }
    }

    @Test
    fun `전체 브랜드 중 카테고리별 최저가 상품의 총액이 가장 적은 브랜드를 조회한다 `() {
        runBlocking {

            given(productService.getMinPriceByBrandCategorySync())
                .willReturn(
                    listOf(
                        // brand 1 total=4000
                        MinPriceWithBrandCategory(1000, 1, "100"),
                        MinPriceWithBrandCategory(3000, 1, "200"),

                        // brand 2 total=2600 -> 최저가 브랜드
                        MinPriceWithBrandCategory(600, 2, "100"),
                        MinPriceWithBrandCategory(2000, 2, "200"),

                        // brand 3 total=90000
                        MinPriceWithBrandCategory(6000, 3, "100"),
                        MinPriceWithBrandCategory(84000, 3, "200"),
                    )
                )

            given(brandService.getEnabledOrThrowSync(2)).willReturn(Brand(2, "브랜드"))
            val (brand, minPriceByBrandCategory) = statService.getCheapestBrandWithMinPriceByCategory()

            assertEquals(brand.id, 2)
            assertEquals(minPriceByBrandCategory.sumOf { it.minPrice }, 2600)
        }
    }
}
package com.musinsa.coordinator.domain.stat

import com.musinsa.coordinator.domain.category.CategoryService
import com.musinsa.coordinator.domain.product.Product
import com.musinsa.coordinator.domain.product.ProductService
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
    private val statService = StatService(productService, categoryService)

    @Test
    fun `카테고리별 최저가 상품의 정보를 조회한다`() {
        runBlocking {
            given(categoryService.getCategoryManager()).willReturn(defaultCategoryManager) // 20개

            val productByCategory = defaultCategoryManager.categories.mapIndexed { index, category ->
                category.id to ProductWithBrandDetail(Product(index, "상품", 1000 * (index + 1), 0, category.id), "")
            }.toMap()

            defaultCategoryManager.categories.forEach { category ->
                given(productService.findMinPriceEnabledProductByCategorySync(category.id))
                    .willReturn(productByCategory.getValue(category.id))
            }

            val result = statService.getMinPriceAndBrandByCategory()
            assertEquals(result, productByCategory)
        }

    }
}
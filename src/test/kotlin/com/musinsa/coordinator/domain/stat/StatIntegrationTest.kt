package com.musinsa.coordinator.domain.stat

import com.musinsa.coordinator.domain.brand.BrandRepository
import com.musinsa.coordinator.domain.brand.rest.dto.BrandCreateOrUpdateRequest
import com.musinsa.coordinator.domain.product.ProductRepository
import com.musinsa.coordinator.domain.product.rest.dto.ProductCreateOrUpdateRequest
import com.musinsa.coordinator.domain.product.rest.dto.ProductCreateResponse
import com.musinsa.coordinator.integration.IntegrationTest
import com.musinsa.coordinator.integration.client.TestClient
import com.musinsa.coordinator.util.BrandId
import com.musinsa.coordinator.util.CategoryId
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@IntegrationTest
class StatIntegrationTest(
    private val client: TestClient,
    private val productRepository: ProductRepository,
    private val brandRepository: BrandRepository
) {

    @BeforeEach
    fun beforeEach() {
        brandRepository.deleteAll()
        productRepository.deleteAll()
    }

    @Test
    fun `카테고리별 최저가 상품의 가격과 브랜드를 조회할 수 있다`() {
        runBlocking {
            val brands = (1..4).map {
                client.createBrand(BrandCreateOrUpdateRequest("브랜드 $it"))
            }

            // category 100
            createProduct("100", brands[0].id, 1000)
            createProduct("100", brands[1].id, 500) // minPrice
            createProduct("100", brands[2].id, 2000)
            createProduct("100", brands[3].id, 3000)

            // category 200
            createProduct("200", brands[0].id, 800) // minPrice
            createProduct("200", brands[1].id, 15000)
            createProduct("200", brands[2].id, 3000)
            createProduct("200", brands[3].id, 8000)

            // category 300
            createProduct("300", brands[0].id, 50000)
            createProduct("300", brands[1].id, 80000)
            createProduct("300", brands[2].id, 100000)
            createProduct("300", brands[3].id, 28000) // minPrice

            // category 400
            createProduct("400", brands[0].id, 16000)
            createProduct("400", brands[1].id, 500000)
            createProduct("400", brands[2].id, 8000) // minPrice
            createProduct("400", brands[3].id, 8500)

            val result = client.getCategoriesWithMinPriceAndBrandStat()

            assertEquals(result.totalPrice, 500 + 800 + 28000 + 8000)
            assertThat(result.categories.map { it.minPrice }).containsAll(setOf(500, 800, 28000, 8000))
        }

    }

    private suspend fun createProduct(categoryId: CategoryId, brandId: BrandId, price: Int): ProductCreateResponse {
        return client.createProduct(ProductCreateOrUpdateRequest("상품", price, categoryId, brandId))
    }
}
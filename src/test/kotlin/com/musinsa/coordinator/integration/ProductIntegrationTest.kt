package com.musinsa.coordinator.integration

import com.musinsa.coordinator.domain.brand.dto.BrandCreateOrUpdateRequest
import com.musinsa.coordinator.domain.product.Product
import com.musinsa.coordinator.domain.product.ProductRepository
import com.musinsa.coordinator.domain.product.dto.ProductCreateOrUpdateRequest
import com.musinsa.coordinator.integration.client.TestClient
import com.musinsa.coordinator.testutil.assertNotFound
import com.musinsa.coordinator.util.ProductId
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.jvm.optionals.getOrNull
import kotlin.test.assertEquals


@IntegrationTest
class ProductIntegrationTest(
    private val client: TestClient,
    private val productRepository: ProductRepository
) {

    private val TOP_CATEGORY_ID = "100"

    @Test
    fun `상품을 생성 수정 삭제할 수 있다`() {
        runBlocking {
            val brand = client.createBrand(BrandCreateOrUpdateRequest("나이키"))

            // 존재하지 않는 브랜드면 에러
            assertNotFound {
                client.createProduct(
                    ProductCreateOrUpdateRequest("나이키 맨투맨", 1000, "404", brand.id)
                ).id
            }

            // 존재하지 않는 카테고리면 에러
            assertNotFound {
                client.createProduct(
                    ProductCreateOrUpdateRequest("나이키 맨투맨", 1000, TOP_CATEGORY_ID, 404)
                ).id
            }

            // 상품 생성 성공
            val productId = client.createProduct(
                ProductCreateOrUpdateRequest("나이키 맨투맨", 1000, TOP_CATEGORY_ID, brand.id)
            ).id

            findProduct(productId).also { product ->
                assertThat(product)
                    .returns("나이키 맨투맨") { it?.name }
                    .returns(1000) { it?.price }
                    .returns(Product.Status.SELLING) { it?.status }
            }

            // 상품 수정
            assertDoesNotThrow {
                client.updateProduct(
                    productId,
                    ProductCreateOrUpdateRequest("2025 뉴 나이키 맨투맨", 2000, TOP_CATEGORY_ID, brand.id)
                )
            }
            findProduct(productId).also { product ->
                assertThat(product)
                    .returns("2025 뉴 나이키 맨투맨") { it?.name }
                    .returns(2000) { it?.price }
                    .returns(Product.Status.SELLING) { it?.status }
            }

            // 상품 삭제
            assertDoesNotThrow { client.deleteProduct(productId) }
            assertEquals(findProduct(productId)?.status, Product.Status.DELETED)
        }
    }

    private fun findProduct(productId: ProductId): Product? {
        return productRepository.findById(productId).getOrNull()
    }
}
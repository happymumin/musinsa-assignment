package com.musinsa.coordinator.domain.brand

import com.musinsa.coordinator.domain.brand.rest.dto.BrandCreateOrUpdateRequest
import com.musinsa.coordinator.integration.IntegrationTest
import com.musinsa.coordinator.integration.client.TestClient
import com.musinsa.coordinator.testutil.assertBadRequest
import com.musinsa.coordinator.testutil.assertNotFound
import com.musinsa.coordinator.util.BrandId
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.jvm.optionals.getOrNull
import kotlin.test.assertEquals

@IntegrationTest
class BrandIntegrationTest(
    private val client: TestClient,
    private val brandRepository: BrandRepository
) {

    @Test
    fun `브랜드를 생성 수정 삭제할 수 있다`() {
        runBlocking {
            // 생성
            val brandId = client.createBrand(BrandCreateOrUpdateRequest("나이키")).id

            findBrand(brandId)?.also { assertEquals(it.name, "나이키") }

            // 수정
            val updateRequest = BrandCreateOrUpdateRequest("아디다스")
            assertDoesNotThrow { client.updateBrand(brandId, updateRequest) }
            findBrand(brandId)?.also { assertEquals(it.name, "아디다스") }

            // 삭제
            assertDoesNotThrow { client.deleteBrand(brandId) }

            // 삭제된 브랜드는 수정, 삭제 불가능
            assertBadRequest { client.updateBrand(brandId, updateRequest) }
            assertBadRequest { client.deleteBrand(brandId) }

            // 없는 브랜드는 수정, 삭제 불가능
            assertNotFound { client.updateBrand(404, updateRequest) }
            assertNotFound { client.updateBrand(404, updateRequest) }
        }
    }

    private fun findBrand(brandId: BrandId): Brand? {
        return brandRepository.findById(brandId).getOrNull()
    }
}
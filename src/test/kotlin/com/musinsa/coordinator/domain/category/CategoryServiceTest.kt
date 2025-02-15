package com.musinsa.coordinator.domain.category

import com.musinsa.coordinator.testutil.mock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given

class CategoryServiceTest {

    private val categoryRepository: CategoryRepository = mock()
    private val service = CategoryService(categoryRepository)

    @Test
    fun `카테고리 ID로 카테고리를 조회한다`() {
        val targetCategoryId = "100"

        given(categoryRepository.findAll()).willReturn(
            listOf(
                Category(id = targetCategoryId, "상의"),
                Category(id = "200", "하의")
            )
        )

        val result = service.getCategory(targetCategoryId)

        assertEquals(result?.name, "상의")
    }
}
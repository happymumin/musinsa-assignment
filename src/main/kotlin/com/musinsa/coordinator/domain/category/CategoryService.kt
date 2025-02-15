package com.musinsa.coordinator.domain.category

import com.musinsa.coordinator.domain.category.rest.CategoryErrorCode
import com.musinsa.coordinator.util.CategoryId
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
) {
    private val logger = KotlinLogging.logger {}

    var categoryById: Map<CategoryId, Category> = mapOf()

    @Scheduled(cron = "0 0 * * * *") // 매 시 마다
    fun refresh() {
        logger.info { "start refreshCategories" }
        categoryById = categoryRepository.findAll().associateBy { it.id }
        logger.info { "end refreshCategories. category size: ${categoryById.size}" }
    }

    suspend fun getOrThrow(categoryId: CategoryId): Category {
        return getOrNull(categoryId) ?: throw CategoryException(CategoryErrorCode.NOT_FOUND_CATEGORY)
    }

    suspend fun getOrNull(categoryId: CategoryId): Category? {
        if (categoryById.isEmpty()) {
            withContext(Dispatchers.IO) { refresh() }
        }
        return categoryById[categoryId]
    }
}
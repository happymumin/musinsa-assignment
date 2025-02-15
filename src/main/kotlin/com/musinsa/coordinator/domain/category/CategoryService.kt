package com.musinsa.coordinator.domain.category

import com.musinsa.coordinator.util.CategoryId
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
) {
    private val logger = KotlinLogging.logger {}

    var categoryById: Map<CategoryId, Category> = mapOf()

    @Scheduled(cron = "0 0 * * * *") // 매 시 마다
    fun refreshCategories() {
        logger.info { "start refreshCategories" }
        categoryById = categoryRepository.findAll().associateBy { it.id }
        logger.info { "end refreshCategories. category size: ${categoryById.size}" }
    }

    fun getCategory(categoryId: CategoryId): Category? {
        if (categoryById.isEmpty()) refreshCategories()
        return categoryById[categoryId]
    }
}
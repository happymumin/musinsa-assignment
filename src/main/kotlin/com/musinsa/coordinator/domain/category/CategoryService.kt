package com.musinsa.coordinator.domain.category

import com.musinsa.coordinator.domain.category.dto.CategoryManager
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

    private var categoryManager: CategoryManager? = null

    @Scheduled(cron = "0 0 * * * *") // 매 시 마다
    fun refreshCategoryManager() {
        logger.info { "start refresh CategoryManager" }
        val categories = categoryRepository.findAll()
        categoryManager = CategoryManager(categories)
        logger.info { "end refresh CategoryManager. category size: ${categories.size}" }
    }

    suspend fun getCategoryManager(): CategoryManager {
        if (categoryManager == null) {
            withContext(Dispatchers.IO) {
                refreshCategoryManager()
            }
        }
        return categoryManager!!
    }
}
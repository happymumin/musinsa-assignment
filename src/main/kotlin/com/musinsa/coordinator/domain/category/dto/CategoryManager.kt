package com.musinsa.coordinator.domain.category.dto

import com.musinsa.coordinator.domain.category.Category
import com.musinsa.coordinator.domain.category.CategoryException
import com.musinsa.coordinator.domain.category.rest.CategoryErrorCode
import com.musinsa.coordinator.util.CategoryId

class CategoryManager(val categories: List<Category>) {
    private val categoryById: Map<CategoryId, Category> = categories.associateBy { it.id }
    private val categoryByName: Map<String, Category> = categories.associateBy { it.name }

    fun getByIdOrThrow(categoryId: CategoryId): Category {
        return getByIdOrNull(categoryId) ?: throw CategoryException(CategoryErrorCode.NOT_FOUND_CATEGORY)
    }

    fun getByIdOrNull(categoryId: CategoryId): Category? {
        return categoryById[categoryId]
    }

    fun getByNameOrThrow(categoryName: String): Category {
        return categoryByName[categoryName] ?: throw CategoryException(CategoryErrorCode.NOT_FOUND_CATEGORY)
    }

}
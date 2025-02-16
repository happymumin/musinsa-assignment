package com.musinsa.coordinator.domain.category.dto

import com.musinsa.coordinator.domain.category.Category
import com.musinsa.coordinator.domain.category.CategoryException
import com.musinsa.coordinator.domain.category.rest.CategoryErrorCode
import com.musinsa.coordinator.util.CategoryId

class CategoryManager(val categories: List<Category>) {
    private val categoryById: Map<CategoryId, Category> = categories.associateBy { it.id }

    fun getByIdOrThrow(categoryId: CategoryId): Category {
        return getByIdOrNull(categoryId) ?: throw CategoryException(CategoryErrorCode.NOT_FOUND_CATEGORY)
    }

    fun getByIdOrNull(categoryId: CategoryId): Category? {
        return categoryById[categoryId]
    }
}
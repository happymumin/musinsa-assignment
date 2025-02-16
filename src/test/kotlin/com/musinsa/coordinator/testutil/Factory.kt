package com.musinsa.coordinator.testutil

import com.musinsa.coordinator.domain.category.Category
import com.musinsa.coordinator.domain.category.dto.CategoryManager

val defaultCategoryManager = CategoryManager(
    (1..20).map {
        val id = (it * 100).toString()
        Category(id, "카테고리 $id")
    }
)
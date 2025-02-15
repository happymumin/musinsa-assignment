package com.musinsa.coordinator.domain.category

import com.musinsa.coordinator.util.CategoryId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : JpaRepository<Category, CategoryId>
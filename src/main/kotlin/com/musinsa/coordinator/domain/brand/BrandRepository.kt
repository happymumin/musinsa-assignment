package com.musinsa.coordinator.domain.brand

import com.musinsa.coordinator.util.BrandId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BrandRepository : JpaRepository<Brand, BrandId>
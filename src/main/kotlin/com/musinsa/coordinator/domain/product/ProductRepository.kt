package com.musinsa.coordinator.domain.product

import com.musinsa.coordinator.util.ProductId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, ProductId>
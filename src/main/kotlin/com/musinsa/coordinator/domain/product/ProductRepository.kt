package com.musinsa.coordinator.domain.product

import com.musinsa.coordinator.domain.product.Product.Status.SELLING
import com.musinsa.coordinator.domain.product.dto.MinPriceWithBrandCategory
import com.musinsa.coordinator.domain.product.dto.ProductWithBrandDetail
import com.musinsa.coordinator.domain.product.dto.QMinPriceWithBrandCategory
import com.musinsa.coordinator.domain.product.dto.QProductWithBrandDetail
import com.musinsa.coordinator.util.ProductId
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.Predicate
import com.querydsl.jpa.impl.JPAQuery
import jakarta.persistence.EntityManager
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import com.musinsa.coordinator.domain.brand.QBrand.brand as qBrand
import com.musinsa.coordinator.domain.product.QProduct.product as qProduct

@Repository
interface ProductRepository : JpaRepository<Product, ProductId>, QuerydslPredicateExecutor<Product>, QProductRepository

interface QProductRepository {

    @Transactional(readOnly = true)
    fun findProductWithBrandDetailFirst(predicate: Predicate, order: OrderSpecifier<*>): ProductWithBrandDetail?

    @Transactional(readOnly = true)
    fun getMinPriceByBrandCategory(): List<MinPriceWithBrandCategory>
}

@Repository
class QProductRepositoryImpl(private val entityManager: EntityManager) : QProductRepository {

    override fun findProductWithBrandDetailFirst(
        predicate: Predicate,
        order: OrderSpecifier<*>
    ): ProductWithBrandDetail? {
        return JPAQuery<ProductWithBrandDetail>(entityManager)
            .select(QProductWithBrandDetail(qProduct, qBrand.name))
            .from(qProduct)
            .join(qBrand).on(qProduct.brandId.eq(qBrand.id).and(qBrand.enabled.isTrue))
            .where(predicate)
            .orderBy(order)
            .fetchFirst()
    }

    override fun getMinPriceByBrandCategory(): List<MinPriceWithBrandCategory> {
        return JPAQuery<MinPriceWithBrandCategory>(entityManager)
            .select(QMinPriceWithBrandCategory(qProduct.price.min(), qProduct.brandId, qProduct.categoryId))
            .from(qProduct)
            .join(qBrand).on(qProduct.brandId.eq(qBrand.id).and(qBrand.enabled.isTrue))
            .where(qProduct.status.eq(SELLING))
            .groupBy(qProduct.brandId, qProduct.categoryId)
            .fetch()
    }
}
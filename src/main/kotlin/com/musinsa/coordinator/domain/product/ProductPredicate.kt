package com.musinsa.coordinator.domain.product

import com.musinsa.coordinator.domain.product.Product.Status.SELLING
import com.musinsa.coordinator.util.CategoryId
import com.querydsl.core.types.dsl.BooleanExpression
import com.musinsa.coordinator.domain.product.QProduct.product as qProduct

object ProductPredicate {

    fun categoryIdEqAndSelling(categoryId: CategoryId): BooleanExpression {
        return qProduct.categoryId.eq(categoryId).and(selling())
    }

    fun selling(): BooleanExpression {
        return qProduct.status.eq(SELLING)
    }
}
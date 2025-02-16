package com.musinsa.coordinator.domain.product

import com.musinsa.coordinator.domain.brand.Brand
import com.musinsa.coordinator.domain.brand.BrandRepository
import com.musinsa.coordinator.repository.RepositoryTest
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@RepositoryTest
class ProductRepositoryTest(
    private val entityManager: EntityManager,
    private val productRepository: ProductRepository,
    private val brandRepository: BrandRepository
) {

    @Test
    fun `특정 카테고리의 최저가 상품의 정보를 조회한다`() {
        val categoryId = "100"
        val brand = brandRepository.save(Brand(name = "나이키"))
        val deletedBrand = brandRepository.save(Brand(name = "삭제된 브랜드", enabled = false))

        val expectProduct =
            Product(name = "나이키 맨투맨", price = 1000, brandId = brand.id!!, categoryId = categoryId).save()

        // not expect
        Product(name = "삭제된 브랜드 맨투맨", price = 900, brandId = deletedBrand.id!!, categoryId = categoryId).save()
        Product(name = "나이키 맨투맨", price = 1500, brandId = brand.id!!, categoryId = categoryId).save()
        Product(name = "나이키 맨투맨", price = 50000, brandId = brand.id!!, categoryId = categoryId).save()
        Product(name = "나이키 맨투맨", price = 100, brandId = brand.id!!, categoryId = "200").save()
        flushAndClear()

        val result = productRepository.findProductWithBrandDetailFirst(
            ProductPredicate.categoryIdEqAndSelling(categoryId),
            QProduct.product.price.asc()
        )

        assertThat(result?.product?.id).isEqualTo(expectProduct.id)
    }

    fun Product.save() = productRepository.save(this)

    private fun flushAndClear() {
        entityManager.flush()
        entityManager.clear()
    }
}
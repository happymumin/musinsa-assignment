package com.musinsa.coordinator.domain.product

import com.musinsa.coordinator.domain.brand.Brand
import com.musinsa.coordinator.domain.brand.BrandRepository
import com.musinsa.coordinator.repository.RepositoryTest
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@RepositoryTest
class ProductRepositoryTest(
    private val entityManager: EntityManager,
    private val productRepository: ProductRepository,
    private val brandRepository: BrandRepository
) {


    @BeforeEach
    fun beforeEach() {
        productRepository.deleteAll()
        brandRepository.deleteAll()
    }

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

    @Test
    fun `브랜드 카테고리별 최저가를 조회한다`() {
        val brand1 = brandRepository.save(Brand(name = "나이키"))
        val brand2 = brandRepository.save(Brand(name = "아디다스"))
        val deletedBrand = brandRepository.save(Brand(name = "삭제된 브랜드", enabled = false))

        Product(name = "나이키 맨투맨", price = 75000, brandId = brand1.id!!, categoryId = "100").save()
        Product(name = "나이키 맨투맨", price = 60000, brandId = brand1.id!!, categoryId = "100").save() // expected

        Product(name = "나이키 잠바", price = 20000, brandId = brand1.id!!, categoryId = "200").save() // expected
        Product(name = "나이키 잠바", price = 85000, brandId = brand1.id!!, categoryId = "200").save()

        Product(name = "아디다스 맨투맨", price = 10000, brandId = brand2.id!!, categoryId = "100").save()
        Product(name = "아디다스 맨투맨", price = 5000, brandId = brand2.id!!, categoryId = "100").save() // expected

        Product(name = "아디다스 잠바", price = 4000, brandId = brand2.id!!, categoryId = "200").save() // expected
        Product(name = "아디다스 잠바", price = 90000, brandId = brand2.id!!, categoryId = "200").save()

        Product(name = "삭제된 맨투맨", price = 3000, brandId = deletedBrand.id!!, categoryId = "100").save()
        Product(name = "삭제된 잠바", price = 10000, brandId = deletedBrand.id!!, categoryId = "200").save()

        flushAndClear()

        val result = productRepository.getMinPriceByBrandCategory()

        assertThat(result).hasSize(4)

        assertThat(
            result.groupBy { it.brandId }
                .mapValues { (_, minPrices) -> minPrices.associate { it.categoryId to it.minPrice } }
        )
            .returns(60000) { it.getValue(brand1.id)["100"] }
            .returns(20000) { it.getValue(brand1.id)["200"] }
            .returns(5000) { it.getValue(brand2.id)["100"] }
            .returns(4000) { it.getValue(brand2.id)["200"] }
    }

    fun Product.save() = productRepository.save(this)

    private fun flushAndClear() {
        entityManager.flush()
        entityManager.clear()
    }
}
package com.musinsa.coordinator.domain.product

import com.musinsa.coordinator.config.database.TransactionTemplateHolder
import com.musinsa.coordinator.domain.brand.BrandService
import com.musinsa.coordinator.domain.category.CategoryService
import com.musinsa.coordinator.domain.product.Product.Status.DELETED
import com.musinsa.coordinator.domain.product.Product.Status.SELLING
import com.musinsa.coordinator.domain.product.dto.MinPriceWithBrandCategory
import com.musinsa.coordinator.domain.product.dto.ProductWithBrandDetail
import com.musinsa.coordinator.domain.product.rest.ProductErrorCode
import com.musinsa.coordinator.domain.product.rest.dto.ProductCreateOrUpdateRequest
import com.musinsa.coordinator.util.CategoryId
import com.musinsa.coordinator.util.ProductId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull
import com.musinsa.coordinator.domain.product.QProduct.product as qProduct

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val categoryService: CategoryService,
    private val brandService: BrandService,
    private val txHolder: TransactionTemplateHolder
) {
    suspend fun create(request: ProductCreateOrUpdateRequest): Product {
        categoryService.getCategoryManager().getByIdOrThrow(request.categoryId)
        return withContext(Dispatchers.IO) {
            brandService.getEnabledOrThrowSync(request.brandId)
            createSync(request)
        }
    }

    private fun createSync(request: ProductCreateOrUpdateRequest): Product {
        return txHolder.writer.execute {
            productRepository.save(
                Product(
                    name = request.name,
                    price = request.price,
                    categoryId = request.categoryId,
                    brandId = request.brandId
                )
            )
        }!!
    }

    suspend fun update(productId: ProductId, request: ProductCreateOrUpdateRequest) {
        categoryService.getCategoryManager().getByIdOrThrow(request.categoryId)
        withContext(Dispatchers.IO) {
            brandService.getEnabledOrThrowSync(request.brandId)
            updateSync(productId, request)
        }
    }

    private fun updateSync(productId: ProductId, request: ProductCreateOrUpdateRequest) {
        txHolder.writer.execute {
            val product = getSellingOrThrowSync(productId)
            product.name = request.name
            product.price = request.price
            productRepository.save(product)
        }
    }

    @Transactional
    fun deleteSync(productId: ProductId) {
        val product = getSellingOrThrowSync(productId)
        product.status = DELETED
        productRepository.save(product)
    }

    fun findMinPriceSellingProductByCategorySync(categoryId: CategoryId): ProductWithBrandDetail? {
        return productRepository.findProductWithBrandDetailFirst(
            predicate = ProductPredicate.categoryIdEqAndSelling(categoryId),
            order = qProduct.price.asc()
        )
    }

    fun findMaxPriceSellingProductByCategorySync(categoryId: CategoryId): ProductWithBrandDetail? {
        return productRepository.findProductWithBrandDetailFirst(
            predicate = ProductPredicate.categoryIdEqAndSelling(categoryId),
            order = qProduct.price.desc()
        )
    }

    fun getMinPriceByBrandCategorySync(): List<MinPriceWithBrandCategory> {
        return productRepository.getMinPriceByBrandCategory()
    }

    private fun getSellingOrThrowSync(productId: ProductId): Product {
        return getOrThrowSync(productId).takeIf { it.status == SELLING }
            ?: throw ProductException(ProductErrorCode.DELETED_PRODUCT)
    }

    private fun getOrThrowSync(productId: ProductId): Product {
        return productRepository.findById(productId).getOrNull()
            ?: throw ProductException(ProductErrorCode.NOT_FOUND_PRODUCT)
    }
}
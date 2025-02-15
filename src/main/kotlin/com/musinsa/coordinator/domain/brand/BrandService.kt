package com.musinsa.coordinator.domain.brand

import com.musinsa.coordinator.domain.brand.dto.BrandCreateOrUpdateRequest
import com.musinsa.coordinator.domain.brand.rest.BrandErrorCode
import com.musinsa.coordinator.util.BrandId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
class BrandService(
    private val brandRepository: BrandRepository
) {

    @Transactional
    fun createSync(request: BrandCreateOrUpdateRequest): Brand {
        return brandRepository.save(Brand(name = request.name))
    }

    @Transactional
    fun updateSync(brandId: BrandId, request: BrandCreateOrUpdateRequest): Brand {
        val brand = getEnabledOrThrow(brandId)
        brand.name = request.name
        brandRepository.save(brand)
        return brand
    }

    @Transactional
    fun deleteSync(brandId: BrandId) {
        val brand = getEnabledOrThrow(brandId)
        brand.enabled = false
        brandRepository.save(brand)
    }

    private fun getEnabledOrThrow(brandId: BrandId): Brand {
        return getOrThrow(brandId).takeIf { it.enabled } ?: throw BrandException(BrandErrorCode.DELETED_BRAND)
    }

    private fun getOrThrow(brandId: BrandId): Brand {
        return brandRepository.findById(brandId).getOrNull() ?: throw BrandException(BrandErrorCode.NOT_FOUND_BRAND)
    }
}
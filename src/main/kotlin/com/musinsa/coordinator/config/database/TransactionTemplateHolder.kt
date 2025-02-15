package com.musinsa.coordinator.config.database

import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate

@Component
class TransactionTemplateHolder(
    private val platformTransactionManager: PlatformTransactionManager
) {
    val writer = TransactionTemplate(platformTransactionManager)
}
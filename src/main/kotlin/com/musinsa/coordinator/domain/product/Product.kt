package com.musinsa.coordinator.domain.product

import com.musinsa.coordinator.util.BrandId
import com.musinsa.coordinator.util.CategoryId
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime


@EntityListeners(AuditingEntityListener::class)
@Table(name = "product")
@Entity
class Product(

    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    val id: Int? = null,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "price", nullable = false)
    var price: Int,

    @Column(name = "brand_id", nullable = false)
    val brandId: BrandId,

    @Column(name = "category_id", nullable = false)
    val categoryId: CategoryId,

    @Column(name = "status", nullable = false)
    var status: Status = Status.SELLING
) {
    @CreatedDate
    @Column(name = "created_at", nullable = false)
    lateinit var createdAt: LocalDateTime

    @LastModifiedDate
    @Column(name = "modified_at", nullable = false)
    lateinit var modifiedAt: LocalDateTime

    enum class Status(val dbColumn: Int) {
        SELLING(0),
        DELETED(1),
        UNKNOWN(999);

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<Status, Int> {
            override fun convertToDatabaseColumn(attribute: Status?): Int? {
                return attribute?.dbColumn
            }

            override fun convertToEntityAttribute(dbData: Int?): Status {
                return Status.entries.find { it.dbColumn == dbData } ?: UNKNOWN
            }
        }
    }
}
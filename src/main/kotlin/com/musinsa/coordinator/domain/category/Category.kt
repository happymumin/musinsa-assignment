package com.musinsa.coordinator.domain.category

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table


@Table(name = "category")
@Entity
class Category(

    @Id
    val id: String,

    @Column(nullable = false, unique = true)
    val name: String
)
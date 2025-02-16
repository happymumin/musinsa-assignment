package com.musinsa.coordinator.repository

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.TestConstructor.AutowireMode


@EnableJpaAuditing
@DataJpaTest
@TestConstructor(autowireMode = AutowireMode.ALL)
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class RepositoryTest
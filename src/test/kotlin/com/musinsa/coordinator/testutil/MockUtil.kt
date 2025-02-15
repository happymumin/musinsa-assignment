package com.musinsa.coordinator.testutil

import org.mockito.Mockito

inline fun <reified T> mock() = Mockito.mock(T::class.java)
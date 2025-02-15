package com.musinsa.coordinator.testutil

import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.WebClientResponseException
import kotlin.test.assertEquals


suspend fun assertBadRequest(block: suspend () -> Any?) {
    assertThrowWebClientResponseException(HttpStatus.BAD_REQUEST, block)
}

suspend fun assertNotFound(block: suspend () -> Any?) {
    assertThrowWebClientResponseException(HttpStatus.NOT_FOUND, block)
}

private suspend fun assertThrowWebClientResponseException(status: HttpStatus, block: suspend () -> Any?) {
    assertThrows<WebClientResponseException> { block() }
        .also { assertEquals(it.statusCode, status) }
}



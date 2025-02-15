package com.musinsa.coordinator.integration.config

import com.musinsa.coordinator.integration.client.TestClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Lazy
import org.springframework.web.reactive.function.client.WebClient

@Lazy
@TestConfiguration
class TestClientConfig(
    @Value("\${local.server.port}") val port: Int
) {

    @Bean
    fun client(): TestClient {
        return TestClient(WebClient.create("http://localhost:$port"))
    }
}
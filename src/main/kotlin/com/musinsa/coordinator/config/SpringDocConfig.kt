package com.musinsa.coordinator.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.boot.info.BuildProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile(Env.DEV)
@Configuration
class SpringDocConfig(private val buildProperties: BuildProperties) {

    @Bean
    fun openApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title(buildProperties.name)
                    .version(buildProperties.version)
            )
    }
}
package com.shykhov.backtest.infrastructure.config

import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
    @Bean
    fun defaultApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("default")
            .packagesToScan("com.shykhov.backtest")
            .build()
    }
}

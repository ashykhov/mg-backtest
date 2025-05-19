package com.shykhov.backtest.infrastructure.config

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("!test")
class CoroutineConfig {
    @Bean("ioDispatcher")
    fun ioDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Bean("defaultDispatcher")
    fun coroutineDispatcher(): CoroutineDispatcher = Dispatchers.Default
}

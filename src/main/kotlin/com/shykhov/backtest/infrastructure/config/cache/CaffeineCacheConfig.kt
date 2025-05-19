package com.shykhov.backtest.infrastructure.config.cache

import com.shykhov.common.cache.state.CacheConfig
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CaffeineCacheConfig {
    @Bean
    fun cacheConfig(meterRegistry: MeterRegistry) = CacheConfig(
        meterRegistry = meterRegistry,
        testClock = null,
    )
}

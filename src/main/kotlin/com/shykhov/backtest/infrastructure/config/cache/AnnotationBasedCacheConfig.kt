package com.shykhov.backtest.infrastructure.config.cache

import com.shykhov.common.cache.CacheUtil
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AnnotationBasedCacheConfig {
    @Bean
    fun cacheManager(meterRegistry: MeterRegistry): CacheManager {
        val cacheManager = CaffeineCacheManager()

        CacheNames.entries.forEach { cache ->
            cacheManager.registerCustomCache(
                cache.cacheName,
                CacheUtil.meterCache1<Any, Any>(
                    cache.cacheName,
                    cache.cacheDuration,
                    cache.maxSize.toLong(),
                    meterRegistry,
                ),
            )
        }

        return cacheManager
    }
}

package com.shykhov.backtest.infrastructure.config.cache

import com.shykhov.backtest.infrastructure.config.cache.CacheConstants.BACKTEST_BY_ID_TF_LIMIT
import com.shykhov.backtest.infrastructure.config.cache.CacheConstants.LP_BY_ID_TF_LIMIT
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

enum class CacheNames(val cacheName: String, val cacheDuration: Duration, val maxSize: Int) {
    BACKTEST_BY_ID_TF_LIMIT_NAME(BACKTEST_BY_ID_TF_LIMIT, 2.seconds, 1000),
    LP_BY_ID_TF_LIMIT_NAME(LP_BY_ID_TF_LIMIT, 2.seconds, 1000),
}

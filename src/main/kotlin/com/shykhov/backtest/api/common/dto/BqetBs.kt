package com.shykhov.backtest.api.common.dto

import com.shykhov.common.candle.timeframe.TimeFrame
import com.shykhov.common.sharedClasses.bqet.Bqet
import com.shykhov.common.sharedClasses.bqet.IBqet
import java.time.Instant

data class BqetBs(
    override val bqet: Bqet,
    val timeFrame: TimeFrame,
    val limit: Int,
    val beforeTs: Long? = null,
) : IBqet {
    val before: Instant? = beforeTs?.let { Instant.ofEpochMilli(it) }
}

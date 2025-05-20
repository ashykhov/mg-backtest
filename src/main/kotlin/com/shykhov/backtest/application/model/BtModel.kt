package com.shykhov.backtest.application.model

import com.shykhov.backtest.api.common.dto.BtType
import com.shykhov.backtest.api.common.dto.BtTypeConfig
import com.shykhov.backtest.application.repo.BtEntity
import com.shykhov.common.candle.timeframe.TimeFrame
import com.shykhov.common.sharedClasses.bqetBs.BqetBs
import com.shykhov.common.sharedClasses.bqetBs.BqetBs.Companion.buyBase
import com.shykhov.common.sharedClasses.bqetBs.BqetBs.Companion.buyExchange
import com.shykhov.common.sharedClasses.bqetBs.BqetBs.Companion.buyQuote
import com.shykhov.common.sharedClasses.bqetBs.BqetBs.Companion.buyType
import com.shykhov.common.sharedClasses.bqetBs.BqetBs.Companion.sellBase
import com.shykhov.common.sharedClasses.bqetBs.BqetBs.Companion.sellExchange
import com.shykhov.common.sharedClasses.bqetBs.BqetBs.Companion.sellQuote
import com.shykhov.common.sharedClasses.bqetBs.BqetBs.Companion.sellType
import com.shykhov.common.sharedClasses.bqetBs.IBqetBs
import java.time.Instant

data class BtModel(
    override val bqetBs: BqetBs,
    val btType: BtType,
    val btParams: Set<String>,
    val status: String,
    val startedAt: Instant,
    val finishedAt: Instant?,
    val tickDataRequested: BtTypeConfig,
    val timeFrom: Instant,
    val timeTo: Instant,
    val timeFrame: TimeFrame,
    

    val id: Long? = null,
    ) : IBqetBs {
    fun toEntity(): BtEntity {
        return BtEntity(
            id = id,
            buyBase = bqetBs.buyBase,
            buyQuote = bqetBs.buyQuote,
            buyExchange = bqetBs.buyExchange,
            buyType = bqetBs.buyType,
            sellBase = bqetBs.sellBase,
            sellQuote = bqetBs.sellQuote,
            sellExchange = bqetBs.sellExchange,
            sellType = bqetBs.sellType,
            btType = btType,
            btParams = btParams,
            status = status,
            jobStartedAt = startedAt,
            jobFinishedAt = finishedAt,
            tickDataRequested = tickDataRequested,
            timeFrom = timeFrom,
            timeTo = timeTo,
            timeFrame = timeFrame,
        )
    }
}

package com.shykhov.backtest.api.common.dto

import com.shykhov.backtest.application.model.BtModel
import com.shykhov.common.enums.Exchange
import com.shykhov.common.enums.PairType
import com.shykhov.common.numbers.decimal
import com.shykhov.common.sharedClasses.Ticker
import com.shykhov.common.sharedClasses.bqetBs.BqetBs.Companion.buyBase
import com.shykhov.common.sharedClasses.bqetBs.BqetBs.Companion.buyExchange
import com.shykhov.common.sharedClasses.bqetBs.BqetBs.Companion.buyQuote
import com.shykhov.common.sharedClasses.bqetBs.BqetBs.Companion.buyType
import com.shykhov.common.sharedClasses.bqetBs.BqetBs.Companion.sellBase
import com.shykhov.common.sharedClasses.bqetBs.BqetBs.Companion.sellExchange
import com.shykhov.common.sharedClasses.bqetBs.BqetBs.Companion.sellQuote
import com.shykhov.common.sharedClasses.bqetBs.BqetBs.Companion.sellType
import java.math.BigDecimal
import java.time.Instant

data class BtDto(
    val id: Long,
    val buyBase: Ticker,
    val buyQuote: Ticker,
    val buyExchange: Exchange,
    val buyType: PairType,
    val sellBase: Ticker,
    val sellQuote: Ticker,
    val sellExchange: Exchange,
    val sellType: PairType,
    val btType: BtType,
    val btParams: Set<String>,
    val status: String,
    val startedAt: Instant,
    val finishedAt: Instant?,
    val tickDataRequested: BtTypeConfig,
    val timeFrom: Instant,
    val timeTo: Instant,
    val profit: BigDecimal? = 42.decimal,
    val profitPercent: BigDecimal? = 2.decimal,
    val budget: BigDecimal =  1000.decimal,
) {
    companion object {
        fun BtModel.toDto(): BtDto {

            return BtDto(
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
                id = id!!,
                status = status,
                startedAt = startedAt,
                finishedAt = finishedAt,
                tickDataRequested = tickDataRequested,
                timeFrom = timeFrom,
                timeTo = timeTo,
            )
        }
    }
}

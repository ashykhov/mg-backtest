package com.shykhov.backtest.api.common.dto

import com.shykhov.backtest.application.model.BtModel
import com.shykhov.common.enums.Exchange
import com.shykhov.common.enums.PairType
import com.shykhov.common.sharedClasses.Ticker
import com.shykhov.common.sharedClasses.bqetBs.BqetBs.Companion.buyBase
import com.shykhov.common.sharedClasses.bqetBs.BqetBs.Companion.buyExchange
import com.shykhov.common.sharedClasses.bqetBs.BqetBs.Companion.buyQuote
import com.shykhov.common.sharedClasses.bqetBs.BqetBs.Companion.buyType
import com.shykhov.common.sharedClasses.bqetBs.BqetBs.Companion.sellBase
import com.shykhov.common.sharedClasses.bqetBs.BqetBs.Companion.sellExchange
import com.shykhov.common.sharedClasses.bqetBs.BqetBs.Companion.sellQuote
import com.shykhov.common.sharedClasses.bqetBs.BqetBs.Companion.sellType
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
) {
    companion object {
        fun fromModel(model: BtModel?): BtDto? {
            if (model == null) {
                return null
            }
            return BtDto(
                buyBase = model.bqetBs.buyBase,
                buyQuote = model.bqetBs.buyQuote,
                buyExchange = model.bqetBs.buyExchange,
                buyType = model.bqetBs.buyType,
                sellBase = model.bqetBs.sellBase,
                sellQuote = model.bqetBs.sellQuote,
                sellExchange = model.bqetBs.sellExchange,
                sellType = model.bqetBs.sellType,
                btType = model.btType,
                btParams = model.btParams,
                id = model.id!!,
                status = model.status,
                startedAt = model.startedAt,
                finishedAt = model.finishedAt,
                tickDataRequested = model.tickDataRequested,
                timeFrom = model.timeFrom,
                timeTo = model.timeTo,
            )
        }
    }
}

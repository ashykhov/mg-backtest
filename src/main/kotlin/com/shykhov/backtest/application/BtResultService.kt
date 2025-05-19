package com.shykhov.backtest.application

import com.shykhov.api.candle.client.CandleClient
import com.shykhov.backtest.api.common.dto.TickOutputType
import com.shykhov.backtest.api.common.dto.TickOutputType.FUNDING_RATE_BUY
import com.shykhov.backtest.api.common.dto.TickOutputType.FUNDING_RATE_SELL
import com.shykhov.backtest.api.common.dto.TickOutputType.PRICE_BUY_ASK
import com.shykhov.backtest.api.common.dto.TickOutputType.PRICE_BUY_BID
import com.shykhov.backtest.api.common.dto.TickOutputType.PRICE_SELL_ASK
import com.shykhov.backtest.api.common.dto.TickOutputType.PRICE_SELL_BID
import com.shykhov.backtest.api.common.dto.TickOutputType.PRICE_SPREAD
import com.shykhov.backtest.application.model.BtModel
import com.shykhov.common.candle.subject.TickSubject
import com.shykhov.common.candle.subject.TickSubject.PERP_BID
import com.shykhov.common.candle.tick.ITick
import com.shykhov.common.candle.tick.NullableTick
import com.shykhov.common.candle.timeframe.TimeFrame
import com.shykhov.common.enums.PairType.FUTURES
import com.shykhov.common.enums.PairType.SPOT
import java.time.Instant
import org.springframework.stereotype.Service

@Service
class BtResultService(
    private val btService: BtService,
    private val candleClient: CandleClient,
) {

    suspend fun getResult(id: Long, type: TickOutputType): List<ITick> {
        val backtest = btService.get(id) ?: return emptyList()

        val res = when (type) {
            PRICE_BUY_BID -> processPriceBuyBid(backtest)
            PRICE_BUY_ASK -> processPriceBuyAsk(backtest)
            PRICE_SELL_ASK -> processPriceSellAsk(backtest)
            PRICE_SELL_BID -> processPriceSellBid(backtest)
            PRICE_SPREAD -> emptyList()
            FUNDING_RATE_BUY -> emptyList()
            FUNDING_RATE_SELL -> emptyList()
        }

        return res
    }


    suspend fun processPriceBuyBid(
        bt: BtModel,
    ): List<ITick> {
        val subject = if (bt.bqetBs.buyBqet.type == SPOT) TickSubject.SPOT_BID
        else if (bt.bqetBs.buyBqet.type == FUTURES) PERP_BID
        else throw IllegalArgumentException("Unknown type: ${bt.bqetBs.buyBqet.type}")


        val a = candleClient.getTickHistory(
            subject = subject.value,
            timeFrame = bt.timeFrame.value,
            bqetId = bt.bqetBs.buyBqet.bqetId,
            limit = 5000,
            beforeTs = bt.timeTo.toEpochMilli(),
            fillGaps = false,
            cutToLimit = false,
        ).body
        return a?.map { tickDto -> NullableTick(tickDto.value, Instant.ofEpochMilli(tickDto.ts)) }
            ?: emptyList()
    }

    suspend fun processPriceBuyAsk(
        bt: BtModel,
    ): List<ITick> {
        val subject = if (bt.bqetBs.buyBqet.type == SPOT) TickSubject.SPOT_ASK
        else if (bt.bqetBs.buyBqet.type == FUTURES) TickSubject.PERP_ASK
        else throw IllegalArgumentException("Unknown type: ${bt.bqetBs.buyBqet.type}")

        val a = candleClient.getTickHistory(
            subject = subject.value,
            timeFrame = bt.timeFrame.value,
            bqetId = bt.bqetBs.buyBqet.bqetId,
            limit = 5000,
            beforeTs = bt.timeTo.toEpochMilli(),
            fillGaps = false,
            cutToLimit = false,
        ).body
        return a?.map { tickDto -> NullableTick(tickDto.value, Instant.ofEpochMilli(tickDto.ts)) }
            ?: emptyList()
    }

    suspend fun processPriceSellAsk(
        bt: BtModel,
    ): List<ITick> {
        val subject = if (bt.bqetBs.sellBqet.type == SPOT) TickSubject.SPOT_ASK
        else if (bt.bqetBs.sellBqet.type == FUTURES) TickSubject.PERP_ASK
        else throw IllegalArgumentException("Unknown type: ${bt.bqetBs.sellBqet.type}")

        val a = candleClient.getTickHistory(
            subject = subject.value,
            timeFrame = bt.timeFrame.value,
            bqetId = bt.bqetBs.sellBqet.bqetId,
            limit = 5000,
            beforeTs = bt.timeTo.toEpochMilli(),
            fillGaps = false,
            cutToLimit = false,
        ).body
        return a?.map { tickDto -> NullableTick(tickDto.value, Instant.ofEpochMilli(tickDto.ts)) }
            ?: emptyList()
    }

    suspend fun processPriceSellBid(
        bt: BtModel,
    ): List<ITick> {
        val subject = if (bt.bqetBs.sellBqet.type == SPOT) TickSubject.SPOT_BID
        else if (bt.bqetBs.sellBqet.type == FUTURES) PERP_BID
        else throw IllegalArgumentException("Unknown type: ${bt.bqetBs.sellBqet.type}")

        val a = candleClient.getTickHistory(
            subject = subject.value,
            timeFrame = bt.timeFrame.value,
            bqetId = bt.bqetBs.sellBqet.bqetId,
            limit = 5000,
            beforeTs = bt.timeTo.toEpochMilli(),
            fillGaps = false,
            cutToLimit = false,
        ).body
        return a?.map { tickDto -> NullableTick(tickDto.value, Instant.ofEpochMilli(tickDto.ts)) }
            ?: emptyList()
    }


}

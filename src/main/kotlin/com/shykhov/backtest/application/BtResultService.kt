package com.shykhov.backtest.application

import com.shykhov.api.candle.client.CandleClient
import com.shykhov.backtest.api.common.dto.TickOutputType
import com.shykhov.backtest.common.toBqetId
import com.shykhov.common.candle.tick.ITick
import com.shykhov.common.commonDto.TickDto
import com.shykhov.common.enums.TimeFrame
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class BtResultService(
    private val btService: BtService,
    private val candleClient: CandleClient,
) {

    fun getResult(btId: String, type: TickOutputType): List<ITick> {
        val backtest = btService.get(btId) ?: return emptyList()
        
        val (subject, bqetId) = when (type) {
            TickOutputType.PRICE_BUY_BID -> "spot-bid" to backtest.bqetBs.buyBqet.toBqetId()
            TickOutputType.PRICE_BUY_ASK -> "spot-ask" to backtest.bqetBs.buyBqet.toBqetId()
            TickOutputType.PRICE_SELL_ASK -> "perp-ask" to backtest.bqetBs.sellBqet.toBqetId()
            TickOutputType.PRICE_SELL_BID -> "perp-bid" to backtest.bqetBs.sellBqet.toBqetId()
            TickOutputType.PRICE_SPREAD -> "price-spread" to backtest.bqetBs.buyBqet.toBqetId()
            TickOutputType.FUNDING_RATE_BUY -> "funding-rate" to backtest.bqetBs.buyBqet.toBqetId()
            TickOutputType.FUNDING_RATE_SELL -> "funding-rate" to backtest.bqetBs.sellBqet.toBqetId()
        }
        
        return runBlocking {
            try {
                val response = candleClient.getTickHistory(
                    subject = subject,
                    timeFrame = TimeFrame.MIN_1.toString(),
                    bqetId = bqetId,
                    limit = 5000,
                    beforeTs = null,
                    fillGaps = true,
                    cutToLimit = true
                )
                
                response.body?.map { tickDto ->
                    // Преобразуем TickDto в ITick
                    object : ITick {
                        override fun getTime(): Long = tickDto.timestamp
                        override fun getOpen(): Double = tickDto.open
                        override fun getHigh(): Double = tickDto.high
                        override fun getLow(): Double = tickDto.low
                        override fun getClose(): Double = tickDto.close
                        override fun getVolume(): Long = tickDto.volumeLong
                    }
                } ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

}

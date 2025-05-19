package com.shykhov.backtest.application

import com.shykhov.backtest.api.common.dto.TickOutputType
import com.shykhov.common.candle.tick.ITick
import org.springframework.stereotype.Service

@Service
class BtResultService {

    fun getResult(btId: String, type: TickOutputType): List<ITick> {

        return when (type) {
            TickOutputType.PRICE_BUY_BID -> emptyList()
            TickOutputType.PRICE_BUY_ASK -> emptyList()
            TickOutputType.PRICE_SELL_ASK -> emptyList()
            TickOutputType.PRICE_SELL_BID -> emptyList()
            TickOutputType.PRICE_SPREAD -> emptyList()
            TickOutputType.FUNDING_RATE_BUY -> emptyList()
            TickOutputType.FUNDING_RATE_SELL -> emptyList()
        }
    }

}

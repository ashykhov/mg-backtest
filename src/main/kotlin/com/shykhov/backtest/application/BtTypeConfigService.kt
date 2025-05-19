package com.shykhov.backtest.application

import com.shykhov.backtest.api.common.dto.BtType
import com.shykhov.backtest.api.common.dto.BtTypeConfig
import com.shykhov.backtest.api.common.dto.TickOutputType
import org.springframework.stereotype.Service

@Service
class BtTypeConfigService {

    fun getConfig(btType: BtType): BtTypeConfig {
        return when (btType) {
            BtType.PRICE_SPREAD -> BtTypeConfig(
                possibleTickOutput = setOf(
                    TickOutputType.PRICE_BUY_BID,
                    TickOutputType.PRICE_BUY_ASK,
                    TickOutputType.PRICE_SELL_ASK,
                    TickOutputType.PRICE_SELL_BID,
                    TickOutputType.PRICE_SPREAD,
                ),
            )
            BtType.FUNDING_RATE -> BtTypeConfig(
                possibleTickOutput = setOf(
                    TickOutputType.FUNDING_RATE_BUY,
                    TickOutputType.FUNDING_RATE_SELL,
                ),
            )
        }
    }
}

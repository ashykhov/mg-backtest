package com.shykhov.backtest.application

import com.shykhov.backtest.api.common.dto.BtType
import com.shykhov.backtest.api.common.dto.BtTypeConfig
import com.shykhov.backtest.application.model.BtModel
import com.shykhov.common.sharedClasses.bqet.Bqet
import com.shykhov.common.sharedClasses.bqet.Bqet.Companion.toBqetBs
import java.time.Clock
import org.springframework.stereotype.Service

@Service
class BtService(
    private val clock: Clock,
) {

    fun get(btId: String): BtModel? {

        return null
    }

    fun init(
        buyBqet: Bqet,
        sellBqet: Bqet,
        btType: BtType,
        btParams: Set<String>,
    ): BtModel {

        return BtModel(
            bqetBs = buyBqet toBqetBs sellBqet,
            btType = btType,
            btParams = btParams,
            id = "",
            status = "",
            startedAt = clock.instant(),
            finishedAt = clock.instant(),
            config = BtTypeConfig(emptySet()),
        )
    }
}

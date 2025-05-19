package com.shykhov.backtest.application

import com.shykhov.backtest.api.common.dto.BtType
import com.shykhov.backtest.api.common.dto.BtTypeConfig
import com.shykhov.backtest.application.model.BtModel
import com.shykhov.backtest.application.repo.BtRepo
import com.shykhov.common.candle.timeframe.TimeFrame
import com.shykhov.common.sharedClasses.bqet.Bqet
import com.shykhov.common.sharedClasses.bqet.Bqet.Companion.toBqetBs
import java.time.Clock
import java.time.Instant
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BtService(
    private val clock: Clock,
    private val btRepo: BtRepo,
) {

    fun get(id: Long): BtModel? {
        return btRepo.findById(id).orElse(null)?.toModel()
    }

    @Transactional
    fun init(
        buyBqet: Bqet,
        sellBqet: Bqet,
        btType: BtType,
        btParams: Set<String>,
        timeFrom: Instant,
        timeTo: Instant,
        timeFrame: TimeFrame,
    ): BtModel {
        val model = BtModel(
            bqetBs = buyBqet toBqetBs sellBqet,
            btType = btType,
            btParams = btParams,
            status = "CREATED",
            startedAt = clock.instant(),
            finishedAt = null,
            config = BtTypeConfig(emptySet()),
            outputConfig = null,
            timeFrom = timeFrom,
            timeTo = timeTo,
            timeFrame = timeFrame,
        )

        val entity = model.toEntity()
        val saved = btRepo.save(entity)
        return saved.toModel()
    }

}

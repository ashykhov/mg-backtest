package com.shykhov.backtest.application

import com.shykhov.backtest.api.common.dto.BtType
import com.shykhov.backtest.api.common.dto.BtTypeConfig
import com.shykhov.backtest.application.model.BtModel
import com.shykhov.backtest.application.repo.BtEntity
import com.shykhov.backtest.application.repo.BtRepo
import com.shykhov.common.sharedClasses.bqet.Bqet
import com.shykhov.common.sharedClasses.bqet.Bqet.Companion.toBqetBs
import java.time.Clock
import java.time.Instant
import java.util.UUID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BtService(
    private val clock: Clock,
    private val btRepo: BtRepo,
) {

    fun get(btId: String): BtModel? {
        return btRepo.findById(btId).orElse(null)?.toModel()
    }

    @Transactional
    fun init(
        buyBqet: Bqet,
        sellBqet: Bqet,
        btType: BtType,
        btParams: Set<String>,
        timeFrom: Instant,
        timeTo: Instant,
    ): BtModel {
        val entity = BtEntity(
            id = UUID.randomUUID().toString(),
            bqetBs = buyBqet toBqetBs sellBqet,
            btType = btType,
            btParams = btParams,
            status = "CREATED",
            startedAt = clock.instant(),
            finishedAt = null,
            config = BtTypeConfig(emptySet()),
            timeFrom = timeFrom,
            timeTo = timeTo,
        )
        
        val saved = btRepo.save(entity)
        return saved.toModel()
    }
    
    @Transactional
    fun updateStatus(btId: String, status: String): BtModel? {
        val entity = btRepo.findById(btId).orElse(null) ?: return null
        val updated = entity.copy(
            status = status,
            finishedAt = if (status == "COMPLETED" || status == "FAILED") clock.instant() else entity.finishedAt
        )
        val saved = btRepo.save(updated)
        return saved.toModel()
    }
    
    private fun BtEntity.toModel() = BtModel(
        bqetBs = this.bqetBs,
        btType = this.btType,
        btParams = this.btParams,
        id = this.id,
        status = this.status,
        startedAt = this.startedAt,
        finishedAt = this.finishedAt ?: clock.instant(),
        config = this.config,
    )
}

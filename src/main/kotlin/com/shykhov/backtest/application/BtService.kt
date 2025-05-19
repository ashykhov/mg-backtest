package com.shykhov.backtest.application

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.shykhov.backtest.api.common.dto.BtType
import com.shykhov.backtest.api.common.dto.BtTypeConfig
import com.shykhov.backtest.application.mapper.BtMapper
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
    private val btMapper: BtMapper,
) {

    fun get(btId: String): BtModel? {
        return btRepo.findByBtId(btId)?.let { btMapper.entityToModel(it) }
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
        val model = BtModel(
            bqetBs = buyBqet toBqetBs sellBqet,
            btType = btType,
            btParams = btParams,
            id = UUID.randomUUID().toString(),
            status = "CREATED",
            startedAt = clock.instant(),
            finishedAt = null,
            config = BtTypeConfig(emptySet()),
            outputConfig = null,
            result = null,
            timeFrom = timeFrom,
            timeTo = timeTo,
        )
        
        val entity = btMapper.modelToEntity(model)
        val saved = btRepo.save(entity)
        return btMapper.entityToModel(saved)
    }
    
    @Transactional
    fun updateStatus(btId: String, status: String): BtModel? {
        val entity = btRepo.findByBtId(btId) ?: return null
        val model = btMapper.entityToModel(entity)
        val updatedModel = model.copy(
            status = status,
            finishedAt = if (status == "COMPLETED" || status == "FAILED") clock.instant() else model.finishedAt
        )
        val updatedEntity = btMapper.modelToEntity(updatedModel, entity.id)
        val saved = btRepo.save(updatedEntity)
        return btMapper.entityToModel(saved)
    }
}

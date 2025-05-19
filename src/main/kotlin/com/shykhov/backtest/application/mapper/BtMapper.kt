package com.shykhov.backtest.application.mapper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.shykhov.backtest.api.common.dto.BtTypeConfig
import com.shykhov.backtest.application.model.BtModel
import com.shykhov.backtest.application.repo.BtEntity
import com.shykhov.common.sharedClasses.bqet.Bqet
import com.shykhov.common.sharedClasses.bqetBs.BqetBs
import org.springframework.stereotype.Component

@Component
class BtMapper(
    private val objectMapper: ObjectMapper
) {
    fun entityToModel(entity: BtEntity): BtModel {
        val buyBqet = Bqet(
            baseTicker = entity.buyBase,
            quoteTicker = entity.buyQuote,
            exchange = entity.buyExchange,
            type = entity.buyType
        )
        val sellBqet = Bqet(
            baseTicker = entity.sellBase,
            quoteTicker = entity.sellQuote,
            exchange = entity.sellExchange,
            type = entity.sellType
        )
        return BtModel(
            bqetBs = BqetBs(buyBqet = buyBqet, sellBqet = sellBqet),
            btType = entity.btType,
            btParams = objectMapper.readValue(entity.btParams, Set::class.java) as Set<String>,
            id = entity.btId,
            status = entity.status,
            startedAt = entity.startedAt,
            finishedAt = entity.finishedAt,
            config = objectMapper.readValue(entity.config, BtTypeConfig::class.java),
            outputConfig = entity.outputConfig?.let { objectMapper.readValue(it, BtTypeConfig::class.java) },
            result = entity.result?.let { objectMapper.readValue(it, Any::class.java) },
            timeFrom = entity.timeFrom,
            timeTo = entity.timeTo
        )
    }

    fun modelToEntity(model: BtModel, existingId: Long? = null): BtEntity {
        return BtEntity(
            id = existingId,
            btId = model.id,
            buyBase = model.bqetBs.buyBqet.baseTicker,
            buyQuote = model.bqetBs.buyBqet.quoteTicker,
            buyExchange = model.bqetBs.buyBqet.exchange,
            buyType = model.bqetBs.buyBqet.type,
            sellBase = model.bqetBs.sellBqet.baseTicker,
            sellQuote = model.bqetBs.sellBqet.quoteTicker,
            sellExchange = model.bqetBs.sellBqet.exchange,
            sellType = model.bqetBs.sellBqet.type,
            btType = model.btType,
            btParams = objectMapper.writeValueAsString(model.btParams),
            status = model.status,
            startedAt = model.startedAt,
            finishedAt = model.finishedAt,
            config = objectMapper.writeValueAsString(model.config),
            outputConfig = model.outputConfig?.let { objectMapper.writeValueAsString(it) },
            result = model.result?.let { objectMapper.writeValueAsString(it) },
            timeFrom = model.timeFrom,
            timeTo = model.timeTo
        )
    }
}
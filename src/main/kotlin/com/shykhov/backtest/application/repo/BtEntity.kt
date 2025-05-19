package com.shykhov.backtest.application.repo

import com.shykhov.backtest.api.common.dto.BtType
import com.shykhov.backtest.api.common.dto.BtTypeConfig
import com.shykhov.backtest.application.model.BtModel
import com.shykhov.common.candle.timeframe.TimeFrame
import com.shykhov.common.enums.Exchange
import com.shykhov.common.enums.PairType
import com.shykhov.common.sharedClasses.Ticker
import com.shykhov.common.sharedClasses.bqet.Bqet.Companion.toBqetBs
import io.hypersistence.utils.hibernate.type.json.JsonType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import org.hibernate.annotations.Type

@Entity
@Table(name = "backtests")
data class BtEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long? = null,

    @Column(name = "buy_base")
    val buyBase: Ticker,
    @Column(name = "buy_quote")
    val buyQuote: Ticker,
    @Enumerated(EnumType.STRING)
    @Column(name = "buy_exchange")
    val buyExchange: Exchange,
    @Column(name = "buy_type")
    val buyType: PairType,
    @Column(name = "sell_base")
    val sellBase: Ticker,
    @Column(name = "sell_quote")
    val sellQuote: Ticker,
    @Enumerated(EnumType.STRING)
    @Column(name = "sell_exchange")
    val sellExchange: Exchange,
    @Column(name = "sell_type")
    val sellType: PairType,

    @Enumerated(EnumType.STRING)
    @Column(name = "bt_type")
    val btType: BtType,

    @Type(JsonType::class)
    @Column(name = "bt_params", columnDefinition = "jsonb")
    val btParams: Set<String>,

    @Column(name = "status")
    val status: String,

    @Column(name = "job_started_at")
    val jobStartedAt: Instant,

    @Column(name = "job_finished_at", nullable = true)
    val jobFinishedAt: Instant?,

    @Type(JsonType::class)
    @Column(name = "tick_data_requested", columnDefinition = "jsonb", nullable = true)
    val tickDataRequested: BtTypeConfig,

    @Column(name = "time_from")
    val timeFrom: Instant,

    @Column(name = "time_to")
    val timeTo: Instant,

    @Enumerated(EnumType.STRING)
    @Column(name = "timetrame")
    val timeFrame: TimeFrame,

    ) {

    fun toModel(): BtModel {
        val buyBqet = buyBase / buyQuote on buyExchange type buyType
        val sellBqet = sellBase / sellQuote on sellExchange type sellType

        return BtModel(
            bqetBs = buyBqet toBqetBs sellBqet,
            btType = btType,
            btParams = btParams,
            id = id!!,
            status = status,
            startedAt = jobStartedAt,
            finishedAt = jobFinishedAt,
            tickDataRequested = tickDataRequested,
            timeFrom = timeFrom,
            timeTo = timeTo,
            timeFrame = timeFrame,
        )
    }
}

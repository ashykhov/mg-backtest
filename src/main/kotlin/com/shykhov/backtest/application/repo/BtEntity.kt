package com.shykhov.backtest.application.repo

import com.shykhov.backtest.api.common.dto.BtType
import com.shykhov.common.enums.Exchange
import com.shykhov.common.enums.PairType
import com.shykhov.common.sharedClasses.Ticker
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "backtests")
data class BtEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long? = null,

    @Column(name = "bt_id", nullable = false, unique = true)
    val btId: String,

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

    @Column(name = "bt_params", columnDefinition = "jsonb")
    val btParams: String,

    @Column(name = "status")
    val status: String,

    @Column(name = "job_started_at")
    val jobStartedAt: Instant,

    @Column(name = "job_finished_at", nullable = true)
    val jobFinishedAt: Instant?,

    @Column(name = "config", columnDefinition = "jsonb")
    val config: String,

    @Column(name = "output_config", columnDefinition = "jsonb", nullable = true)
    val outputConfig: String?,

    @Column(name = "result", columnDefinition = "jsonb", nullable = true)
    val result: String?,

    @Column(name = "time_from")
    val timeFrom: Instant,

    @Column(name = "time_to")
    val timeTo: Instant,
)

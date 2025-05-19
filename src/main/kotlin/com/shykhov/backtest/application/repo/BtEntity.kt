package com.shykhov.backtest.application.repo

import com.shykhov.backtest.api.common.dto.BtType
import com.shykhov.backtest.api.common.dto.BtTypeConfig
import com.shykhov.common.enums.Exchange
import com.shykhov.common.enums.PairType
import com.shykhov.common.sharedClasses.Ticker
import com.shykhov.common.sharedClasses.bqetBs.IBqetBs
import jakarta.persistence.*
import java.time.Instant
import org.hibernate.annotations.Type

@Entity
@Table(name = "backtests")
class BtEntity(

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

    @Type(value = com.shykhov.common.dal.jpaUtils.SetJsonbType::class)
    @Column(name = "bt_params", columnDefinition = "json")
    val btParams: Set<String>,

    @Column(name = "status")
    val status: String,

    @Column(name = "started_at")
    val startedAt: Instant,

    @Column(name = "finished_at", nullable = true)
    val finishedAt: Instant?,

    @Type(value = com.shykhov.backtest.infrastructure.jpa.BtTypeConfigJsonType::class)
    @Column(name = "config", columnDefinition = "json")
    val config: BtTypeConfig,

    @Column(name = "time_from")
    val timeFrom: Instant,

    @Column(name = "time_to")
    val timeTo: Instant,
) : IBqetBs {

}

package com.shykhov.backtest.test.testconst

import com.shykhov.common.sharedClasses.Ticker.Companion.ticker
import java.time.Instant
import kotlin.time.Duration
import kotlin.time.toJavaDuration

object TestConst {
    val BTC = "BTC".ticker
    val ETH = "ETH".ticker
    val BNB = "BNB".ticker
    val USDT = "USDT".ticker
    val XRP = "XRP".ticker
    val ADA = "ADA".ticker

    infix operator fun Instant.plus(duration: Duration): Instant {
        return this.plus(duration.toJavaDuration())
    }

    infix operator fun Instant.minus(duration: Duration): Instant {
        return this.minus(duration.toJavaDuration())
    }
}

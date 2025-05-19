package com.shykhov.backtest.common

import com.shykhov.common.sharedClasses.bqet.Bqet

fun Bqet.toBqetId(): String {
    return "${this.baseTicker.value}-${this.quoteTicker.value}-${this.exchange.name}-${this.type.name}"
}
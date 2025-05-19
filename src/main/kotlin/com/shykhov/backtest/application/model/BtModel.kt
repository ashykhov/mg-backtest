package com.shykhov.backtest.application.model

import com.shykhov.backtest.api.common.dto.BtType
import com.shykhov.backtest.api.common.dto.BtTypeConfig
import com.shykhov.common.sharedClasses.bqetBs.BqetBs
import com.shykhov.common.sharedClasses.bqetBs.IBqetBs
import java.time.Instant

data class BtModel(
    override val bqetBs: BqetBs,
    val btType: BtType,
    val btParams: Set<String>,
    val id: String,
    val status: String,
    val startedAt: Instant,
    val finishedAt: Instant,
    val config: BtTypeConfig,
) : IBqetBs

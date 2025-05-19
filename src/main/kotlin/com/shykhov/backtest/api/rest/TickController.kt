package com.shykhov.backtest.api.rest

import com.shykhov.common.candle.subject.TickSubject
import com.shykhov.common.commonDto.TickDto
import com.shykhov.common.commonDto.TimeFrameDto
import com.shykhov.common.enums.Exchange
import com.shykhov.common.enums.PairType
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import java.time.Clock
import mu.KLogging
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@Tag(name = "Tick")
@RequestMapping("/v1/tick")
class TickController(
    private val clock: Clock,
) {
    companion object : KLogging()

    @GetMapping("/history/{subject}/{timeFrame}")
    fun getTickHistory(
        @Schema(implementation = TickSubject::class)
        @PathVariable("subject") subjectValue: String,
        @Schema(implementation = TimeFrameDto::class)
        @PathVariable("timeFrame") timeFrameValue: String,
        @RequestParam("base")
        @Parameter(example = "BTC")
        base: String,
        @RequestParam("quote")
        @Parameter(example = "USDT")
        quote: String,
        @RequestParam("exchange")
        @Parameter(example = "BINANCE")
        exchange: Exchange,
        @RequestParam("type")
        @Parameter(example = "FUTURES")
        type: PairType,
        @RequestParam("limit", defaultValue = "5000") limit: Int,
        @RequestParam("fillGaps", defaultValue = "true") fillGaps: Boolean,
        @RequestParam("cutToLimit", defaultValue = "true") cutToLimit: Boolean,
        @RequestParam("beforeTs", required = false) beforeTs: Long? = null,
    ): ResponseEntity<List<TickDto>> {

        return ResponseEntity.ok(emptyList())
    }


}

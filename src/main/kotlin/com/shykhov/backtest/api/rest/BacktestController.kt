package com.shykhov.backtest.api.rest

import com.shykhov.backtest.api.common.dto.BtDto
import com.shykhov.backtest.api.common.dto.BtType
import com.shykhov.backtest.api.common.dto.BtTypeConfig
import com.shykhov.backtest.api.common.dto.TickOutputType
import com.shykhov.backtest.application.BtResultService
import com.shykhov.backtest.application.BtService
import com.shykhov.backtest.application.BtTypeConfigService
import com.shykhov.common.candle.timeframe.TimeFrame
import com.shykhov.common.commonDto.TickDto
import com.shykhov.common.commonDto.TickDto.Companion.toLinearPointResp
import com.shykhov.common.enums.Exchange
import com.shykhov.common.enums.PairType
import com.shykhov.common.sharedClasses.Ticker.Companion.ticker
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import java.time.Clock
import java.time.Instant
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
@Tag(name = "Backtest")
@RequestMapping("/v1/backtest")
class BacktestController(
    private val clock: Clock,
    private val config: BtTypeConfigService,
    private val btService: BtService,
    private val btResultService: BtResultService,
) {
    companion object : KLogging()


    @GetMapping("/types")
    fun getTypes(): ResponseEntity<List<BtType>> {
        return ResponseEntity.ok(BtType.entries)
    }

    @GetMapping("/types/{type}/config")
    fun getTypeConfig(
        @RequestParam("type")
        @Parameter(example = "PRICE_SPREAD")
        type: BtType,
    ): ResponseEntity<BtTypeConfig> {
        return ResponseEntity.ok(config.getConfig(type))
    }


    @GetMapping("/init")
    suspend fun init(
        @RequestParam("buyBase") buyBase: String,
        @RequestParam("buyQuote") buyQuote: String,
        @RequestParam("buyExchange") buyExchange: Exchange,
        @RequestParam("buyType") buyType: PairType,
        @RequestParam("sellBase") sellBase: String,
        @RequestParam("sellQuote") sellQuote: String,
        @RequestParam("sellExchange") sellExchange: Exchange,
        @RequestParam("sellType") sellType: PairType,
        @RequestParam("btType") btType: BtType,
        @RequestParam("btParams") btParams: Set<String>,
        @RequestParam("timeFrom") timeFrom: Instant,
        @RequestParam("timeTo") timeTo: Instant,
    ): ResponseEntity<BtDto> {

        val buyBqet = buyBase.ticker / buyQuote.ticker on buyExchange type buyType
        val sellBqet = sellBase.ticker / sellQuote.ticker on sellExchange type sellType
        val result = btService.init(
            buyBqet = buyBqet,
            sellBqet = sellBqet,
            btType = btType,
            btParams = btParams,
            timeFrom = timeFrom,
            timeTo = timeTo,
        )
        val dto = BtDto.fromModel(result)
        return if (dto != null) {
            ResponseEntity.ok(dto)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/{id}")
    suspend fun get(
        @RequestParam("id") id: String,
    ): ResponseEntity<BtDto> {
        val resp = btService.get(id)
        val dto = BtDto.fromModel(resp)
        return if (dto != null) {
            ResponseEntity.ok(dto)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/{id}/result/{tickOutputType}")
    suspend fun getResult(
        @PathVariable("id") id: String,
        @PathVariable("tickOutputType") type: TickOutputType,
    ): ResponseEntity<List<TickDto>> {
        val timeFrame = TimeFrame.SECONDS_30
        val resp = btResultService.getResult(id, type)
            .toLinearPointResp(
                fillGaps = true,
                cutToLimit = true,
                tf = timeFrame,
                beforeExcl = clock.instant(),
                limit = 5000,
            )
        return ResponseEntity.ok(resp)
    }


}

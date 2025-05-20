package com.shykhov.backtest.api.rest

import com.shykhov.backtest.api.common.dto.BtDto
import com.shykhov.backtest.api.common.dto.BtDto.Companion.toDto
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
import org.springframework.web.bind.annotation.PostMapping
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

    @GetMapping("/list")
    suspend fun getList(
        @RequestParam("limit", required = false) limit: Int = 100,
        @RequestParam("strategy", required = false) strategy: String? = null,
        @RequestParam("status", required = false) status: String? = null,
        @RequestParam("startDate", required = false) startDate: String? = null,
        @RequestParam("endDate", required = false) endDate: String? = null,
        @RequestParam("sortBy", required = false) sortBy: String? = null,
        @RequestParam("sortDirection", required = false) sortDirection: String? = null
    ): ResponseEntity<List<BtDto>> {
        // Here we would add filtering logic based on the parameters
        // For now, we'll just handle the limit parameter as before
        val resp = btService.getAll(limit)
        val dto = resp.map { it.toDto() }
        return ResponseEntity.ok(dto)
    }


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


    @PostMapping("/init")
    suspend fun init(
        @Parameter(example = "BTC")
        @RequestParam("buyBase") buyBase: String,
        @Parameter(example = "USDT")
        @RequestParam("buyQuote") buyQuote: String,
        @Parameter(example = "BINANCE")
        @RequestParam("buyExchange") buyExchange: Exchange,
        @Parameter(example = "FUTURES")
        @RequestParam("buyType") buyType: PairType,
        @Parameter(example = "BTC")
        @RequestParam("sellBase") sellBase: String,
        @Parameter(example = "USDT")
        @RequestParam("sellQuote") sellQuote: String,
        @Parameter(example = "MEXC")
        @RequestParam("sellExchange") sellExchange: Exchange,
        @Parameter(example = "FUTURES")
        @RequestParam("sellType") sellType: PairType,
        @Parameter(example = "PRICE_SPREAD")
        @RequestParam("btType") btType: BtType,
        @RequestParam("btParams") btParams: Set<String> = emptySet(),
        @RequestParam("timeFrom") timeFrom: Instant? = null,
        @RequestParam("timeTo") timeTo: Instant? = null,
        @RequestParam("tickDataRequested", required = false)
        tickDataRequested: BtTypeConfig = config.getConfig(btType),
    ): ResponseEntity<BtDto> {
        val timeFrom = timeFrom ?: clock.instant().minusSeconds(60 * 60 * 24 * 30)
        val timeTo = timeTo ?: clock.instant()

        val buyBqet = buyBase.ticker / buyQuote.ticker on buyExchange type buyType
        val sellBqet = sellBase.ticker / sellQuote.ticker on sellExchange type sellType
        val result = btService.init(
            buyBqet = buyBqet,
            sellBqet = sellBqet,
            btType = btType,
            btParams = btParams,
            timeFrom = timeFrom,
            timeTo = timeTo,
            timeFrame = TimeFrame.SECONDS_30,
            tickDataRequested = tickDataRequested,
        )
        val dto = result.toDto()
        return if (dto != null) {
            ResponseEntity.ok(dto)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/{id}")
    suspend fun get(
        @RequestParam("id") id: Long,
    ): ResponseEntity<BtDto> {
        val resp = btService.get(id)
        val dto = resp?.toDto()
        return if (dto != null) {
            ResponseEntity.ok(dto)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/{id}/result/{tickOutputType}")
    suspend fun getResult(
        @PathVariable("id") id: Long,
        @PathVariable("tickOutputType") type: TickOutputType,
    ): ResponseEntity<List<TickDto>> {
        val timeFrame = TimeFrame.SECONDS_30
        val resp = btResultService.getResult(id, type)
            .toLinearPointResp(
//                fillGaps = false,
//                cutToLimit = false,
                fillGaps = true,
                cutToLimit = true,
                tf = timeFrame,
                beforeExcl = clock.instant(),
                limit = 5000,
            )
        return ResponseEntity.ok(resp)
    }


}

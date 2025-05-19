package com.shykhov.backtest.application

import com.shykhov.backtest.api.common.dto.BtType
import com.shykhov.backtest.application.repo.BtRepo
import com.shykhov.common.enums.Exchange
import com.shykhov.common.enums.PairType
import com.shykhov.common.sharedClasses.Ticker
import com.shykhov.common.sharedClasses.Ticker.Companion.ticker
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.util.Optional
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat

class BtServiceTest {
    
    private lateinit var clock: Clock
    private lateinit var btRepo: BtRepo
    private lateinit var btService: BtService
    
    @BeforeEach
    fun setup() {
        clock = Clock.fixed(Instant.parse("2024-01-01T00:00:00Z"), ZoneId.of("UTC"))
        btRepo = mockk(relaxed = true)
        btService = BtService(clock, btRepo)
    }
    
    @Test
    fun `should create new backtest`() {
        // Given
        val buyBqet = "BTC".ticker / "USDT".ticker on Exchange.BINANCE type PairType.SPOT
        val sellBqet = "BTC".ticker / "USDT".ticker on Exchange.BINANCE type PairType.PERP
        val btType = BtType.PRICE_SPREAD
        val btParams = setOf("param1", "param2")
        val timeFrom = Instant.parse("2023-12-01T00:00:00Z")
        val timeTo = Instant.parse("2023-12-31T23:59:59Z")
        
        // When
        val result = btService.init(buyBqet, sellBqet, btType, btParams, timeFrom, timeTo)
        
        // Then
        assertThat(result).isNotNull
        assertThat(result.status).isEqualTo("CREATED")
        assertThat(result.btType).isEqualTo(btType)
        assertThat(result.btParams).isEqualTo(btParams)
        assertThat(result.startedAt).isEqualTo(clock.instant())
        
        verify { btRepo.save(any()) }
    }
    
    @Test
    fun `should update backtest status`() {
        // Given
        val btId = "test-id"
        val existingEntity = mockk<com.shykhov.backtest.application.repo.BtEntity>(relaxed = true) {
            every { copy(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns this
        }
        
        every { btRepo.findById(btId) } returns Optional.of(existingEntity)
        every { btRepo.save(any()) } returns existingEntity
        
        // When
        val result = btService.updateStatus(btId, "COMPLETED")
        
        // Then
        assertThat(result).isNotNull
        verify { btRepo.findById(btId) }
        verify { btRepo.save(any()) }
    }
}
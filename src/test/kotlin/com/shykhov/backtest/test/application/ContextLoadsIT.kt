package com.shykhov.backtest.test.application

import com.shykhov.backtest.application.TickRepository
import com.shykhov.backtest.test.common.AbstractTestContainerPostgres
import com.shykhov.common.backtest.subject.TickSubject.PERP_ASK
import com.shykhov.common.backtest.tick.Tick
import com.shykhov.common.backtest.timeframe.TimeFrame.SECONDS_10
import com.shykhov.common.numbers.decimal
import com.shykhov.common.test.TestClock
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import java.util.concurrent.TimeUnit

// @SpringBootTest
class ContextLoadsIT(
    var clock: TestClock,
    val tickRepository: TickRepository,
) : AbstractTestContainerPostgres() {
    init {
        test("context loads") {
            // Test that the context loads successfully
        }

        beforeTest {
            // Reset the clock before each test
            clock.reset()
            clearAllMocks()
//            beforeSpec {
//                extension(coroutineTestExtension)
        }

        test("should save ticks and retrieve backtests") {
            // Подготовка данных
            val subject = PERP_ASK
            val identifier = "[FUTURES_BTC_USDT_BINANCE]"
            val baseTime = clock.instant()
            val timeFrame = SECONDS_10

            // Генерируем тики на протяжении 1 минуты с интервалом в 1 секунду
            val ticks = (0..59).map { i ->
                val time = baseTime.plusSeconds(i.toLong())
                val value = 2000.decimal + i.decimal
                identifier to Tick(value, time)
            }

            // Сохраняем тики
            tickRepository.saveTicks(subject, ticks)

            // Ждем, чтобы материализованное представление обновилось
            // Обычно это происходит по расписанию, но для теста мы форсируем обновление
            TimeUnit.SECONDS.sleep(2)
            tickRepository.callRefreshAgg(subject, timeFrame)

            // Получаем свечи
            val backtests = tickRepository.getTicks(subject, timeFrame, identifier, 10)

            // Проверяем результаты
            backtests shouldHaveSize 6 // Ожидаем 6 свечей по 10 секунд в минуте

            // Проверяем первую свечу (самую последнюю по времени)
            val firstBacktest = backtests.first()
            firstBacktest.value shouldBe ticks.last().second.value // close должен быть равен последнему значению в группе
        }
    }
}

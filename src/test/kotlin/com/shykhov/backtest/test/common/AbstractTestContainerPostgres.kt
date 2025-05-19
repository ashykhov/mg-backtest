package com.shykhov.backtest.test.common

import io.kotest.core.spec.style.FunSpec
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@ActiveProfiles("test")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(
//    classes = [
//        TestClockConfig::class,
//        TestEmbeddedKafkaConfig::class,
//    ],
)
abstract class AbstractTestContainerPostgres() : FunSpec() {
    companion object {
        // Указываем совместимость TimescaleDB с PostgreSQL
        private val TIMESCALE_IMAGE = DockerImageName
            .parse("timescale/timescaledb:latest-pg15")
            .asCompatibleSubstituteFor("postgres")

        @JvmStatic
        @Container
        val postgres = PostgreSQLContainer<Nothing>(TIMESCALE_IMAGE).apply {
            withDatabaseName("backtest-test")
            withUsername("test")
            withPassword("test")
            // PostgreSQL по умолчанию использует порт 5432
            withExposedPorts(5432)
            // Можно включить повторное использование контейнера
            // для ускорения тестов
            // withReuse(true)
        }

        init {
            postgres.start()
        }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
        }
    }
}

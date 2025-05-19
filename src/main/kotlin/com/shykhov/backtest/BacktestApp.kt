package com.shykhov.backtest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients

@ConfigurationPropertiesScan
@EnableFeignClients
@EnableDiscoveryClient
@EnableCaching
@SpringBootApplication
class BacktestApp

fun main(args: Array<String>) {
    runApplication<BacktestApp>(*args)
}

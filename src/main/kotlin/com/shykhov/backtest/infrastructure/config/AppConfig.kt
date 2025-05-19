package com.shykhov.backtest.infrastructure.config

import com.shykhov.common.config.DisabledCorsConfig
import com.shykhov.common.config.ExtendedOpenApiConfig
import com.shykhov.common.config.GlobalApiClientConfig
import com.shykhov.common.config.GlobalTimeConfig
import com.shykhov.common.config.LatencyMetricsConfig
import com.shykhov.common.security.HttpSecurityConfig
import com.shykhov.common.security.OAuth2ClientConfig
import com.shykhov.common.security.RsocketSecurityConfig
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    GlobalApiClientConfig::class,
    DisabledCorsConfig::class,
    ExtendedOpenApiConfig::class,
    GlobalTimeConfig::class,
    HttpSecurityConfig::class,
    OAuth2ClientConfig::class,
    RsocketSecurityConfig::class,
    LatencyMetricsConfig::class,
)
class AppConfig

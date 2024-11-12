package com.yb.config

import io.micrometer.observation.ObservationRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient


@Configuration
class WebClientConfig {

    @Bean
    fun webClient(observationRegistry: ObservationRegistry): WebClient {
        return WebClient.builder()
            .observationRegistry(observationRegistry)
            .build()
    }


}


package com.yb.config

import io.micrometer.observation.ObservationRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient


@Configuration
class WebClientConfig(
    val observationRegistry: ObservationRegistry,
) {
    private val studentUrl: String = "http://localhost:8089"
    private val sugangUrl: String = "http://localhost:8089"

    @Bean
    fun webClient(): WebClient {
        return this.webClientBuilder().build();
    }

    @Bean
    fun studentWebClient(): WebClient {
        return webClientBuilder().baseUrl(studentUrl).build();
    }

    @Bean
    fun sugangWebClient(): WebClient {
        return webClientBuilder().baseUrl(sugangUrl).build();
    }

    private fun webClientBuilder(): WebClient.Builder {
        return WebClient.builder()
            .observationRegistry(observationRegistry)
    }

}


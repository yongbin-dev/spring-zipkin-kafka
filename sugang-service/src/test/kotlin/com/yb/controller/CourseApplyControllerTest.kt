package com.yb.controller

import com.yb.dto.request.CourseApplyRequestDto
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody


class CourseApplyControllerTest {

    private val webClient: WebClient = WebClient.create("http://localhost:8085")
    private val log = KotlinLogging.logger { }

    @Test
    fun test() {
        runBlocking {
            for (i in 2..1005) {
                CoroutineScope(Dispatchers.IO).launch {
                    log.info { "coroutine log start " }
                    val requestMono = webClient.post()
                        .uri("/course-apply")
                        .bodyValue(CourseApplyRequestDto(i.toLong(), 1L))
                        .retrieve()
                        .awaitBody<Any>()

                    log.info { requestMono }
                }.join()
            }
        }
    }
}
package com.yb.coroutine

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private val log = KotlinLogging.logger {};

object CpsToFsm {

}

private suspend fun child() {
//    log.info { "context in suspend : $coroutineContext " }

    var context1 = EmptyCoroutineContext;
    log.info { "context : $context1 " }
    val element1 = CoroutineName("custom name")
    val context2 = context1 + element1;

    val result = suspendCoroutine<Int> { cont ->
        log.info { "context by continuation ${cont.context}" }
        cont.resume(100)
    }
    log.info { "result:$result" }

}

fun main() {
    runBlocking {
        var handler = CoroutineExceptionHandler { coroutineContext, throwable ->
            log.error { "exception cauth" }
        }

        val job = CoroutineScope(Dispatchers.IO).launch {
            launch(handler) {
                launch {
                    throw IllegalArgumentException("exception in launch")
                }
            }
        }
    }
    runBlocking {
        log.info { "context in CoroutineScope : ${this.coroutineContext}" }
        child()
    }
}



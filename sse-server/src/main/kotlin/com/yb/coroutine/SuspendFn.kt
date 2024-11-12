package com.yb.coroutine

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlin.coroutines.*

private val log = KotlinLogging.logger {};

object SuspendFn {
    fun calculate(
        initialValue: Int,
        continuation: (Int) -> Unit
    ) {
        initialize(initialValue) { initial ->
            addOne(initial) { added ->
                multiplyTwo(added) { multiplied ->
                    continuation(multiplied)
                }
            }
        }
    }

    private fun initialize(value: Int, continuation: (Int) -> Unit) {
        log.info { "Initial" }
        continuation(value)
    }

    private fun addOne(value: Int, continuation: (Int) -> Unit) {
        log.info { "Add one" }
        continuation(value + 1)
    }

    private fun multiplyTwo(value: Int, continuation: (Int) -> Unit) {
        log.info { "Multiply two" }
        continuation(value * 2)
    }
}

fun main() {

    var visited = false;
    val continuation = object : Continuation<Int> {
        override val context: CoroutineContext
            get() = EmptyCoroutineContext

        override fun resumeWith(result: Result<Int>) {
            if (visited) {
                log.info { "Result: $result" }
            } else {
                log.info { "Visit not" }
                visited = true
            }
        }
    }

    continuation.resume(10)
    continuation.resume(10)
    continuation.resumeWithException(
        IllegalArgumentException()
    )

//    SuspendFn.calculate(5) { result ->
//        log.info { "Result : $result" }
//    }
}



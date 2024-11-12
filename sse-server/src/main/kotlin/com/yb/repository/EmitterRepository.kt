package com.yb.repository

import com.yb.exception.NotFoundEmitterException
import org.springframework.stereotype.Repository
import reactor.core.publisher.Sinks
import java.util.concurrent.ConcurrentHashMap

@Repository
class EmitterRepository {
    private val emitters: MutableMap<String, Sinks.Many<Any>> = ConcurrentHashMap()

    fun findById(id: String): Sinks.Many<Any> {
        val emitter = emitters[id] ?: throw NotFoundEmitterException()
        return emitter
    }

    fun save(id: String, sink: Sinks.Many<Any>): Sinks.Many<Any> {
        emitters[id] = sink
        return sink
    }

    fun deleteById(id: String) {
        emitters.remove(id)
    }
}
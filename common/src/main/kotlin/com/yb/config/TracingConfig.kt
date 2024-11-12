package com.yb.config

import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component


@Aspect
@Component
class TracingConfig {


    //    @Around("@annotation(com.yb.annotation.Traced)")
//    @Throws(Throwable::class)
//    fun traceMethod(joinPoint: ProceedingJoinPoint) {
//
//        val method = getMethod(joinPoint)
//        log.info("======= method name = {} =======", method.name)
//
//        // 메서드 정보 받아오기
//        // 파라미터 받아오기
//        val args: Array<Any> = joinPoint.args
//        if (args.isEmpty()) log.info("no parameter")
//        for (arg in args) {
//            log.info("parameter type = {}", arg.javaClass.simpleName)
//            log.info("parameter value = {}", arg)
//        }
//
//
////        val span: brave.Span = tracer.nextSpan().name(method.name).start()
////        try {
////            tracer.withSpanInScope(span).use { ws ->
////                return joinPoint.proceed()
////            }
////        } catch (t: Throwable) {
////            span.tag("error", t.message)
////            throw t
////        } finally {
////            span.finish()
////        }
//    }
//
//    private fun getMethod(proceedingJoinPoint: ProceedingJoinPoint): Method {
//        val signature: MethodSignature = proceedingJoinPoint.signature as MethodSignature
//        return signature.getMethod()
//    }

}
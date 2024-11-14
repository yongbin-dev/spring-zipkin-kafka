package com.yb.config

import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory
import io.awspring.cloud.sqs.listener.errorhandler.ErrorHandler
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsAsyncClient

@Configuration
class AwsSqsClient(

    @Value("\${spring.cloud.aws.credentials.access-key}")
    private val awsAccessKey: String,

    @Value("\${spring.cloud.aws.credentials.secret-key}")
    private val awsSecretKey: String,

    @Value("\${spring.cloud.aws.region.static}")
    private val awsRegion: String,
) {

    private val log = KotlinLogging.logger { }

    @Bean
    fun sqsAsyncClient(): SqsAsyncClient {
        return SqsAsyncClient.builder()
            .credentialsProvider {
                AwsBasicCredentials.create(
                    awsAccessKey,
                    awsSecretKey
                )
            }.region(
                Region.of(awsRegion)
            )
            .build();
    }

    @Bean
    fun customAwsSqsFactory(): SqsMessageListenerContainerFactory<Any> {
        return SqsMessageListenerContainerFactory
            .builder<Any>()
            .sqsAsyncClient(sqsAsyncClient())
            .errorHandler(object : ErrorHandler<Any> {
                override fun handle(message: Message<Any>, t: Throwable) {
                    log.error { t }
                }
            })
            .build();
    }
}
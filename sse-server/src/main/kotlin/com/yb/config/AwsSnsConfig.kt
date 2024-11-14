package com.yb.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sns.SnsClient

@Configuration
class AwsSnsConfig(

    @Value("\${spring.cloud.aws.credentials.access-key}")
    private val awsAccessKey: String,

    @Value("\${spring.cloud.aws.credentials.secret-key}")
    private val awsSecretKey: String,

    @Value("\${spring.cloud.aws.region.static}")
    private val awsRegion: String,

    @Value("\${spring.cloud.aws.sns.topic.arn}")
    private val snsTopicArn: String,

    @Value("\${spring.cloud.aws.sqs.queue.name}")
    private val awsQueueName: String
) {

    // snsTopicArn에 대한 getter를 제공
    fun getTopicArc(): String = snsTopicArn

    @Bean
    fun getSnsClient(): SnsClient {
        return SnsClient.builder()
            .region(Region.of(awsRegion))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(
                        awsAccessKey,
                        awsSecretKey
                    )
                )
            )
            .build()
    }
}
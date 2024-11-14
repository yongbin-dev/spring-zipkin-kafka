package com.yb.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.support.micrometer.KafkaListenerObservation
import org.springframework.kafka.support.micrometer.KafkaTemplateObservation


@EnableKafka
@Configuration
class KafkaConfig(
    @Value("\${kafka.brokers}")
    private val brokers: String,
//    @Value("\${kafka.enable-auto-commit}")
//    private val enableAutoCommit: Boolean,
//    @Value("\${kafka.auto-offset-reset}")
//    private val autoOffsetReset: String,
) {
    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.consumerFactory = consumerFactory()
        factory.setConcurrency(1)
//        # observation config
        factory.containerProperties.observationConvention =
            KafkaListenerObservation.DefaultKafkaListenerObservationConvention.INSTANCE
        factory.containerProperties.isObservationEnabled = true
        factory.containerProperties.ackMode = ContainerProperties.AckMode.MANUAL
//        factory.setCommonErrorHandler(kafkaErrorHandler)
        return factory
    }

    @Bean
    fun consumerFactory(): ConsumerFactory<String, String> {
        return DefaultKafkaConsumerFactory(consumerConfigs())
    }

    @Bean
    fun consumerConfigs(): Map<String, Any> {
        val props = mutableMapOf<String, Any>()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = brokers
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
//        props[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = enableAutoCommit
//        props[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = autoOffsetReset
        props[ConsumerConfig.MAX_POLL_RECORDS_CONFIG] = 1

        return props
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, String> {
        val kafkaTemplate = KafkaTemplate(producerFactory())
        kafkaTemplate.setObservationEnabled(true)
        kafkaTemplate.setObservationConvention(KafkaTemplateObservation.DefaultKafkaTemplateObservationConvention.INSTANCE)
        return kafkaTemplate
    }

    @Bean
    fun producerFactory(): ProducerFactory<String, String> {
        val config = mutableMapOf<String, Any>()
        config[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = brokers
        config[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        config[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java

        return DefaultKafkaProducerFactory(config)
    }
}
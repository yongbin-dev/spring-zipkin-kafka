package com.yb.config

import io.asyncer.r2dbc.mysql.MySqlConnectionConfiguration
import io.asyncer.r2dbc.mysql.MySqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.r2dbc.connection.R2dbcTransactionManager
import org.springframework.transaction.ReactiveTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.reactive.TransactionalOperator

@EnableR2dbcRepositories(basePackages = ["com.yb.repository.reactive"])
@EnableTransactionManagement
@Configuration
class R2dbcConfig : AbstractR2dbcConfiguration() {

    @Bean
    override fun connectionFactory(): ConnectionFactory {
        return MySqlConnectionFactory.from(
            MySqlConnectionConfiguration.builder()
                .host("localhost")          // MySQL 서버 호스트
                .port(3306)                 // MySQL 서버 포트
                .username("yb")         // MySQL 사용자명
                .password("1234")     // MySQL 비밀번호
                .database("yb")           // MySQL 데이터베이스 이름
                .build()
        )
    }

    @Bean("r2dbcTransactionManager")
    fun transactionManager(connectionFactory: ConnectionFactory): ReactiveTransactionManager {
        return R2dbcTransactionManager(connectionFactory)
    }

    @Bean
    fun transactionalOperator(transactionManager: ReactiveTransactionManager): TransactionalOperator {
        return TransactionalOperator.create(transactionManager)
    }
}
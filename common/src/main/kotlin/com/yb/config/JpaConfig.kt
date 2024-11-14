package com.yb.config

import jakarta.persistence.EntityManagerFactory
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource


@Configuration
//@EnableJpaRepositories(basePackages = ["com.yb"]) // JPA 리포지토리 패키지 경로 설정
//@EnableJpaAuditing
//@EnableTransactionManagement
class JpaConfig(
    val dataSource: DataSource
) {

    @Bean
    fun entityManagerFactory(builder: EntityManagerFactoryBuilder): LocalContainerEntityManagerFactoryBean {
        return builder
            .dataSource(dataSource) // DataSource 빈을 사용
            .packages("com.yb") // JPA 엔티티 클래스 경로
            .persistenceUnit("default")
//            .properties(hibernateProperties()) // Hibernate 추가 설정
            .build()
    }

    @Bean
    fun transactionManager(entityManagerFactory: EntityManagerFactory): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory)
    }
//
//    private fun hibernateProperties(): Map<String, Any> {
//        val properties: MutableMap<String, Any> = HashMap()
//        properties["hibernate.dialect"] = "org.hibernate.dialect.MySQLDialect"
//        properties["hibernate.hbm2ddl.auto"] = "update"
//        properties["hibernate.show_sql"] = true
//        properties["hibernate.format_sql"] = true
//        return properties
//    }

}
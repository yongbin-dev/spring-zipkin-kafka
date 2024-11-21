package com.yb.config

import jakarta.persistence.EntityManagerFactory
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import java.util.*
import javax.sql.DataSource


@Configuration
@EnableJpaRepositories(basePackages = ["com.yb.repository.jpa"]) // JPA 리포지토리 패키지 경로 설정
class JpaConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.jpa")
    fun jpaDataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    fun dataSource(): DataSource {
        return jpaDataSourceProperties().initializeDataSourceBuilder().build()
    }

    @Bean
    fun entityManagerFactory(dataSource: DataSource): LocalContainerEntityManagerFactoryBean {
        val em = LocalContainerEntityManagerFactoryBean()
        em.dataSource = dataSource
        em.setPackagesToScan("com.yb.domain.jpa") // 엔티티 클래스가 위치한 패키지

        val vendorAdapter = HibernateJpaVendorAdapter()
        em.jpaVendorAdapter = vendorAdapter
        em.setJpaProperties(additionalProperties())
        return em
    }

    @Bean
    fun transactionManager(entityManagerFactory: EntityManagerFactory): PlatformTransactionManager {
        val txManager = JpaTransactionManager()
        txManager.entityManagerFactory = entityManagerFactory
        return txManager
    }


    private fun additionalProperties(): Properties {
        val properties = Properties()
        properties.setProperty("hibernate.hbm2ddl.auto", "update")
        properties.setProperty("hibernate.show_sql", "true")
        properties.setProperty("hibernate.format_sql", "true")
        properties.setProperty(
            "hibernate.transaction.jta.platform",
            "org.hibernate.service.jta.JtaPlatform"
        )
        return properties
    }

}
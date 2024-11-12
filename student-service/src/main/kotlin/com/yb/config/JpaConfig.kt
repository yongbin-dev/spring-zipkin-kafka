package com.yb.config

import com.zaxxer.hikari.HikariDataSource
import jakarta.persistence.EntityManagerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.util.*
import javax.sql.DataSource

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(
    basePackages = ["com.yb.domain"],
    entityManagerFactoryRef = "EntityManagerFactory",
    transactionManagerRef = "CustomTransactionManager"
)
@EnableTransactionManagement
class JpaConfig {

    @Bean("customDataSource")
    @ConfigurationProperties(prefix = "spring.jpa.legacy-ad.hikari")
    fun dataSource(): DataSource {
        return DataSourceBuilder.create().type(HikariDataSource::class.java).build()
    }

    @Bean("customJpaProperties")
    @ConfigurationProperties(prefix = "spring.jpa.legacy-ad.properties")
    fun jpaProperties(): Properties {
        return Properties()
    }

    @Bean("EntityManagerFactory")
    fun EntityManagerFactory(
        builder: EntityManagerFactoryBuilder,
        @Qualifier("customDataSource") dataSource: DataSource?,
        @Qualifier("customJpaProperties") jpaProperties: Properties?
    ): LocalContainerEntityManagerFactoryBean {
        val factoryBean = builder
            .dataSource(dataSource)
            .packages("com.yb.domain")
            .build()
        factoryBean.jpaVendorAdapter = HibernateJpaVendorAdapter()
        factoryBean.setJpaProperties(jpaProperties)
        return factoryBean
    }

    @Bean("CustomTransactionManager")
    fun transactionManager(
        @Qualifier("EntityManagerFactory") entityManagerFactory: EntityManagerFactory?
    ): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory)
    }
}
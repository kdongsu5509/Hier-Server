package com.dt.find_restaurant.global.config.db;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * JPA 설정을 위한 Configuration 클래스
 */

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.dt.find_restaurant")
public class JpaConfig {
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }
}

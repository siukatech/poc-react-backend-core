package com.siukatech.poc.react.backend.parent.data.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
////@AutoConfigureAfter(JpaRepositoriesAutoConfiguration.class)
////@EnableTransactionManagement(proxyTargetClass = true)
@EntityScan(basePackages = {"com.siukatech.poc.react.backend.parent.data.entity"})
@EnableJpaRepositories("com.siukatech.poc.react.backend.parent.data.repository")
//@ComponentScan(basePackages = { "com.siukatech.poc.react.backend.parent.data" })
////@Import(StarterEntityRegistrar.class)
public class DataConfig {
}

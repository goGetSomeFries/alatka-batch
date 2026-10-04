package com.alatka.batch.monitor.admin;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@EntityScan
@ComponentScan(basePackages = {"com.alatka.batch.monitor.admin", "com.alatka.batch.infra"})
@Configuration
public class AdminAutoConfiguration {

}

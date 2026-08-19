package com.alatka.batch.param.admin;

import com.alatka.batch.infra.support.DefaultAuditorAware;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@ComponentScan(basePackages = {"com.alatka.batch.param.admin", "com.alatka.batch.infra"})
@Configuration
public class AdminAutoConfiguration {

    @Configuration
    @ConditionalOnMissingBean(name = "jpaAuditingHandler")
    @EnableJpaAuditing(auditorAwareRef = DefaultAuditorAware.BEAN_NAME)
    public static class ParamAdminJpaAutoConfiguration {

        @Bean
        @ConditionalOnMissingBean(name = DefaultAuditorAware.BEAN_NAME)
        public DefaultAuditorAware defaultAuditorAware() {
            return new DefaultAuditorAware();
        }
    }

}

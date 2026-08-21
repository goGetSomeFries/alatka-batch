package com.alatka.batch.param;

import com.alatka.batch.infra.util.ApplicationContextUtil;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.alatka\\.batch\\.param\\.admin"))
@EntityScan
@EnableJpaRepositories
public class AutoConfiguration {

    @Bean
    public ApplicationContextUtil applicationContextUtil() {
        return new ApplicationContextUtil();
    }

}

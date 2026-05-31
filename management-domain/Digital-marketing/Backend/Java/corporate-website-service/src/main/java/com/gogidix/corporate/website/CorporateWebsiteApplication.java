package com.gogidix.corporate.website;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.filter.ForwardedHeaderFilter;

@SpringBootApplication(scanBasePackages = "com.gogidix.corporate.website")
@EnableMongoAuditing
@EnableCaching
@EnableFeignClients(basePackages = "com.gogidix.corporate.website.infrastructure.external")
@EnableScheduling
public class CorporateWebsiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(CorporateWebsiteApplication.class, args);
    }

    @Bean
    public ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    }
}

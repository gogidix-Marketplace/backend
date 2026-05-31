package com.gogidix.customersupport.customerportal.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableAutoConfiguration(exclude = {
    MongoAutoConfiguration.class,
    MongoDataAutoConfiguration.class,
    SecurityAutoConfiguration.class
})
@ComponentScan(basePackages = "com.gogidix.customersupport.customerportal.interfaces.rest")
public class TestWebConfig {

    @EnableWebSecurity
    static class TestSecurityConfig {
        public WebSecurityCustomizer webSecurityCustomizer() {
            return (web) -> web.ignoring().anyRequest();
        }
    }
}

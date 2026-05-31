package com.gogidix.platform.metering.test;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ComponentScan.Filter;

/**
 * Minimal test configuration for Financial-Grade tests.
 * Only loads REST controllers, excludes all database and security auto-configuration.
 */
@Configuration
@EnableAutoConfiguration(exclude = {
    DataSourceAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class,
    JpaRepositoriesAutoConfiguration.class,
    TransactionAutoConfiguration.class,
    BatchAutoConfiguration.class,
    SecurityAutoConfiguration.class,
    UserDetailsServiceAutoConfiguration.class,
    SpringApplicationAdminJmxAutoConfiguration.class
})
@ComponentScan(
    basePackages = "com.gogidix.platform.metering.interfaces.rest",
    useDefaultFilters = true,
    excludeFilters = {
        @Filter(type = FilterType.REGEX, pattern = ".*\\.repository\\..*"),
        @Filter(type = FilterType.REGEX, pattern = ".*\\.persistence\\..*"),
        @Filter(type = FilterType.REGEX, pattern = ".*\\.config\\..*")
    }
)
public class FgTestApplication {
}

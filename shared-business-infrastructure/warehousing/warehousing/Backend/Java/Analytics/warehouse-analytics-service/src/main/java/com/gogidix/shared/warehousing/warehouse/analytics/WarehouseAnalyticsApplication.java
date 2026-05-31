package com.gogidix.shared.warehousing.warehouse.analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * Warehouse Analytics Service Application
 *
 * Multi-tenant warehouse analytics with MongoDB
 * Provides metrics, utilization reports, and performance data
 */
@OpenAPIDefinition(
    info = @Info(
        title = "Warehouse Analytics API",
        version = "1.0.0",
        description = "Multi-tenant warehouse analytics service providing metrics, utilization, and performance data"
    )
)
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.gogidix.shared.warehousing.warehouse.analytics",
    "com.gogidix.shared.multitenancy"
})
@EnableMongoAuditing
@EnableKafka
public class WarehouseAnalyticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(WarehouseAnalyticsApplication.class, args);
    }
}

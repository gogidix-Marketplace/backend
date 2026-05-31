package com.gogidix.shared.warehousing.inventory.analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
    info = @Info(
        title = "Inventory Analytics API",
        version = "1.0.0",
        description = "Multi-tenant inventory analytics service providing turnover, stockout, and forecast data"
    )
)
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.gogidix.shared.warehousing.inventory.analytics",
    "com.gogidix.shared.multitenancy"
})
@EnableMongoAuditing
@EnableKafka
public class InventoryAnalyticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryAnalyticsApplication.class, args);
    }
}

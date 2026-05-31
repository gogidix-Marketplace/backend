package com.gogidix.shared.warehousing.inventory_analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EnableMongoAuditing
@EnableKafka
@OpenAPIDefinition(
    info = @Info(
        title = "Inventory Analytics API",
        version = "1.0.0",
        description = "Inventory analytics service - provides metrics, turnover reports, and stockout analysis"
    )
)
public class InventoryAnalyticsApplication {
    public static void main(String[] args) {
        SpringApplication.run(InventoryAnalyticsApplication.class, args);
    }
}

package com.gogidix.shared.warehousing.fulfillment.analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
    info = @Info(
        title = "Fulfillment Analytics API",
        version = "1.0.0",
        description = "Multi-tenant fulfillment analytics service providing metrics, cycle time, and throughput data"
    )
)
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.gogidix.shared.warehousing.fulfillment.analytics",
    "com.gogidix.shared.multitenancy"
})
@EnableMongoAuditing
@EnableKafka
public class FulfillmentAnalyticsApplication {
    public static void main(String[] args) {
        SpringApplication.run(FulfillmentAnalyticsApplication.class, args);
    }
}

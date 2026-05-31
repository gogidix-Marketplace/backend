package com.gogidix.shared.warehousing.ecommerce.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI ecommerceWarehouseOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("E-Commerce Warehouse Integration API")
                        .version("1.0.0")
                        .description("Zone-based warehouse fulfillment integration for e-commerce"));
    }
}

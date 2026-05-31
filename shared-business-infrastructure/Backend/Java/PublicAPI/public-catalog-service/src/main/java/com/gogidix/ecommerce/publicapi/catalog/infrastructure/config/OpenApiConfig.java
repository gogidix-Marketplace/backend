package com.gogidix.ecommerce.publicapi.catalog.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI publicCatalogServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Public Catalog Service API")
                        .description("REST API for public catalog in the Gogidix E-commerce platform")
                        .version("1.0.0"));
    }
}

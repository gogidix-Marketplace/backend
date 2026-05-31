package com.gogidix.ecommerce.category.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class OpenApiConfigTest {
    @Test void openApiBean_isCreated() {
        OpenApiConfig config = new OpenApiConfig();
        OpenAPI openAPI = config.categoryServiceOpenAPI();
        assertThat(openAPI).isNotNull();
        assertThat(openAPI.getInfo()).isNotNull();
        assertThat(openAPI.getInfo().getTitle()).isNotNull();
    }
}
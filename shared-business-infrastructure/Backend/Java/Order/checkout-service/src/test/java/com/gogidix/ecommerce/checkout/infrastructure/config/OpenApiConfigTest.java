package com.gogidix.ecommerce.checkout.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class OpenApiConfigTest {
    @Test void openApiBean_isCreated() {
        OpenApiConfig config = new OpenApiConfig();
        OpenAPI openAPI = config.checkoutServiceOpenAPI();
        assertThat(openAPI).isNotNull();
        assertThat(openAPI.getInfo()).isNotNull();
        assertThat(openAPI.getInfo().getTitle()).isNotNull();
    }
}
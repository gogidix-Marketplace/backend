package com.gogidix.ecommerce.cart.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class OpenApiConfigTest {
    @Test void openApiBean() {
        OpenApiConfig config = new OpenApiConfig();
        OpenAPI api = config.cartServiceOpenAPI();
        assertThat(api).isNotNull();
        assertThat(api.getInfo()).isNotNull();
    }
}
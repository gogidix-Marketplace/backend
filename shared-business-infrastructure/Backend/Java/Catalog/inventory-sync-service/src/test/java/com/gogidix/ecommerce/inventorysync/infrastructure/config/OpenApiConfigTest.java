package com.gogidix.ecommerce.inventorysync.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OpenApiConfigTest {

    @Test
    void openApiBean() {
        OpenApiConfig config = new OpenApiConfig();
        OpenAPI api = config.inventorySyncServiceOpenAPI();
        assertThat(api).isNotNull();
        assertThat(api.getInfo().getTitle()).isEqualTo("Inventory Sync Service API");
        assertThat(api.getInfo().getVersion()).isEqualTo("1.0.0");
        assertThat(api.getServers()).hasSize(2);
    }
}

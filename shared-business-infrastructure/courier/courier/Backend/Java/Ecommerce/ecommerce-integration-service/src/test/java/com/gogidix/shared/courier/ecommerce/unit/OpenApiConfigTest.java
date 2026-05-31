package com.gogidix.shared.courier.ecommerce.unit;

import com.gogidix.shared.courier.ecommerce.infrastructure.config.OpenApiConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenApiConfigTest {

    @Test
    void shouldCreateOpenAPI() {
        OpenApiConfig config = new OpenApiConfig();
        var openAPI = config.ecommerceIntegrationOpenAPI();
        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());
        assertEquals("E-Commerce Courier Integration API", openAPI.getInfo().getTitle());
        assertEquals("1.0.0", openAPI.getInfo().getVersion());
    }
}

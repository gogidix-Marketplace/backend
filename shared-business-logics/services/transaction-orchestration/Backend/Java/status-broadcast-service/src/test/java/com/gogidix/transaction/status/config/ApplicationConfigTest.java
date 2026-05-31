package com.gogidix.transaction.status.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.filter.CorsFilter;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ApplicationConfig Tests")
class ApplicationConfigTest {

    @Test
    @DisplayName("Should create ObjectMapper")
    void shouldCreateObjectMapper() {
        ApplicationConfig config = new ApplicationConfig();
        ObjectMapper mapper = config.objectMapper();
        assertNotNull(mapper);
    }

    @Test
    @DisplayName("Should create CorsFilter")
    void shouldCreateCorsFilter() {
        ApplicationConfig config = new ApplicationConfig();
        CorsFilter filter = config.corsFilter();
        assertNotNull(filter);
    }
}

package com.gogidix.analytics.data.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ApplicationConfig Tests")
class ApplicationConfigTest {

    @Test
    @DisplayName("Should create ObjectMapper bean")
    void shouldCreateObjectMapperBean() {
        ApplicationConfig config = new ApplicationConfig();
        ObjectMapper mapper = config.objectMapper();
        assertNotNull(mapper);
    }
}

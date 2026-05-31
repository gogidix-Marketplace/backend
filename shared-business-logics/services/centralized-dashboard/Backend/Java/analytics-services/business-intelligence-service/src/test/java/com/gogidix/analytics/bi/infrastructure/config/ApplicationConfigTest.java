package com.gogidix.analytics.bi.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationConfigTest {

    @Test
    @DisplayName("objectMapper bean is configured correctly")
    void objectMapperBeanIsConfiguredCorrectly() {
        ApplicationConfig config = new ApplicationConfig();
        ObjectMapper mapper = config.objectMapper();

        assertNotNull(mapper);
        assertFalse(mapper.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
    }
}

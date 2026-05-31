package com.gogidix.dashboard.gateway.chart.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationConfigTest {

    private final ApplicationConfig config = new ApplicationConfig();

    @Test
    void objectMapper_beanIsConfiguredCorrectly() {
        ObjectMapper mapper = config.objectMapper();

        assertNotNull(mapper);
        assertFalse(mapper.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
    }
}

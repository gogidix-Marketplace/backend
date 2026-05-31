package com.gogidix.shared.infrastructure.services.security.usermanagement.infrastructure.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Config Tests")
class ConfigTest {

    @Test
    void shouldCreateMongoConfig() {
        MongoConfig config = new MongoConfig();
        assertNotNull(config);
    }
}

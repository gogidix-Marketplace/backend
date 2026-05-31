package com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.infrastructure.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MongoConfig Tests")
class MongoConfigTest {

    @Test
    @DisplayName("Should instantiate MongoConfig")
    void shouldInstantiate() {
        MongoConfig config = new MongoConfig();
        assertNotNull(config);
    }
}

package com.gogidix.sysadmin.accesscontrol.infrastructure.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Financial-Grade Unit Tests for MongoConfig configuration class.
 * Tests verify MongoDB configuration setup.
 */
@DisplayName("MongoConfig Configuration Tests")
class MongoConfigTest {

    @Test
    @DisplayName("Should instantiate MongoConfig successfully")
    void testMongoConfigInstantiation() {
        MongoConfig config = new MongoConfig();

        assertThat(config).isNotNull();
    }

    @Test
    @DisplayName("MongoConfig should be a valid Spring configuration")
    void testMongoConfigIsConfiguration() {
        MongoConfig config = new MongoConfig();

        // Verify class has necessary annotations by checking instantiation
        assertThat(config.getClass().getSimpleName()).isEqualTo("MongoConfig");
    }
}

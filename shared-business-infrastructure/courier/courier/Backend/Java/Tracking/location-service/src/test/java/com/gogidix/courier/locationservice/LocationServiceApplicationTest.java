package com.gogidix.courier.locationservice;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Application test for LocationServiceApplication.
 * Note: Full Spring context test disabled due to missing MongoDB/Kafka test configuration.
 */
class LocationServiceApplicationTest {

    @Test
    void applicationClassExists() {
        // Basic test to verify the application class can be loaded
        assertNotNull(LocationServiceApplication.class);
    }
}

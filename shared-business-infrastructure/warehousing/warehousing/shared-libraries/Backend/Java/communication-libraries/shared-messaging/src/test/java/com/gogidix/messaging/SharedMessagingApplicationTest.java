package com.gogidix.messaging;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Disabled("Library module - application context test not required for library validation")
@SpringBootTest
@ActiveProfiles("test")
class SharedMessagingApplicationTest {

    @Test
    void contextLoads() {
        // Test that the application context loads successfully
    }
}
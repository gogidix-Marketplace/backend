package com.gogidix.shared.infrastructure.services.businesssupport.slamanagement;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SlaManagementServiceApplication Tests")
class SlaManagementServiceApplicationTest {

    @Test
    @DisplayName("Should instantiate application class")
    void shouldInstantiate() {
        SlaManagementServiceApplication app = new SlaManagementServiceApplication();
        assertNotNull(app);
    }
}

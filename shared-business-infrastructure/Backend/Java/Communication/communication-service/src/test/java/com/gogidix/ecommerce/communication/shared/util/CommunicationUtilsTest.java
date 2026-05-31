package com.gogidix.ecommerce.communication.shared.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("CommunicationUtils Tests")
class CommunicationUtilsTest {

    @Test
    @DisplayName("Should generate non-null ID")
    void shouldGenerateId() {
        assertThat(CommunicationUtils.generateId()).isNotNull().isNotEmpty();
    }
}

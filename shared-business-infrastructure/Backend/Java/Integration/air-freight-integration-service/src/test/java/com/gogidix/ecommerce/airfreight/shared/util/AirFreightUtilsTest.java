package com.gogidix.ecommerce.airfreight.shared.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("AirFreightUtils Tests")
class AirFreightUtilsTest {

    @Test
    @DisplayName("Should generate non-null ID")
    void shouldGenerateId() {
        assertThat(AirFreightUtils.generateId()).isNotNull().isNotEmpty();
    }
}

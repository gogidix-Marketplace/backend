package com.gogidix.ecommerce.loyalty.shared.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("LoyaltyUtils Tests")
class LoyaltyUtilsTest {

    @Test
    @DisplayName("Should generate non-null ID")
    void shouldGenerateId() {
        assertThat(LoyaltyUtils.generateId()).isNotNull().isNotEmpty();
    }
}

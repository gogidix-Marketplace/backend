package com.gogidix.ecommerce.discount.shared.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("DiscountUtils Tests")
class DiscountUtilsTest {

    @Test
    @DisplayName("Should generate non-null ID")
    void shouldGenerateId() {
        assertThat(DiscountUtils.generateId()).isNotNull().isNotEmpty();
    }
}

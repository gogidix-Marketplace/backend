package com.gogidix.ecommerce.customer.shared.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("CustomerUtils Tests")
class CustomerUtilsTest {

    @Test
    @DisplayName("Should generate non-null ID")
    void shouldGenerateId() {
        assertThat(CustomerUtils.generateId()).isNotNull().isNotEmpty();
    }
}

package com.gogidix.ecommerce.oceanshipping.shared.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("OceanShippingUtils Tests")
class OceanShippingUtilsTest {

    @Test
    @DisplayName("Should generate non-null ID")
    void shouldGenerateId() {
        assertThat(OceanShippingUtils.generateId()).isNotNull().isNotEmpty();
    }
}

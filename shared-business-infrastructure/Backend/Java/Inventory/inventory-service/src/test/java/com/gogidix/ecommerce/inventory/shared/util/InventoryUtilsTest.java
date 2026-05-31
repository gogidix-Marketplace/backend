package com.gogidix.ecommerce.inventory.shared.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("InventoryUtils Tests")
class InventoryUtilsTest {

    @Test
    @DisplayName("Should generate non-null ID")
    void shouldGenerateId() {
        assertThat(InventoryUtils.generateId()).isNotNull().isNotEmpty();
    }
}

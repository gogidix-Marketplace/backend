package com.gogidix.ecommerce.warehouse.shared.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("WarehouseUtils Tests")
class WarehouseUtilsTest {

    @Test
    @DisplayName("Should generate non-null ID")
    void shouldGenerateId() {
        assertThat(WarehouseUtils.generateId()).isNotNull().isNotEmpty();
    }
}

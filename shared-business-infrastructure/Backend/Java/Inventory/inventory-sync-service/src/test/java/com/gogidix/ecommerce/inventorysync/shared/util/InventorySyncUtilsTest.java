package com.gogidix.ecommerce.inventorysync.shared.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("InventorySyncUtils Tests")
class InventorySyncUtilsTest {

    @Test
    @DisplayName("Should generate non-null ID")
    void shouldGenerateId() {
        assertThat(InventorySyncUtils.generateId()).isNotNull().isNotEmpty();
    }
}

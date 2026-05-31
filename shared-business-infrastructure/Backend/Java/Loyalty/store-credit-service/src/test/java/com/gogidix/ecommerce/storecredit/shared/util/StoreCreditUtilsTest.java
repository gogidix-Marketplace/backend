package com.gogidix.ecommerce.storecredit.shared.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("StoreCreditUtils Tests")
class StoreCreditUtilsTest {

    @Test
    @DisplayName("Should generate non-null ID")
    void shouldGenerateId() {
        assertThat(StoreCreditUtils.generateId()).isNotNull().isNotEmpty();
    }
}

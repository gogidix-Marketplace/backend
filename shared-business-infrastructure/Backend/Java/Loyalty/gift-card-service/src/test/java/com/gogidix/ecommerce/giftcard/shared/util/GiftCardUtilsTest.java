package com.gogidix.ecommerce.giftcard.shared.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("GiftCardUtils Tests")
class GiftCardUtilsTest {

    @Test
    @DisplayName("Should generate non-null ID")
    void shouldGenerateId() {
        assertThat(GiftCardUtils.generateId()).isNotNull().isNotEmpty();
    }
}

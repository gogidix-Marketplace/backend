package com.gogidix.ecommerce.wishlist.shared.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("WishlistUtils Tests")
class WishlistUtilsTest {

    @Test
    @DisplayName("Should generate non-null ID")
    void shouldGenerateId() {
        assertThat(WishlistUtils.generateId()).isNotNull().isNotEmpty();
    }
}

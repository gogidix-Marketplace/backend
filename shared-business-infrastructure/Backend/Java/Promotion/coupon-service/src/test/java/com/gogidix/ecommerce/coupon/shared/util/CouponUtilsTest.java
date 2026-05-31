package com.gogidix.ecommerce.coupon.shared.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("CouponUtils Tests")
class CouponUtilsTest {

    @Test
    @DisplayName("Should generate non-null ID")
    void shouldGenerateId() {
        assertThat(CouponUtils.generateId()).isNotNull().isNotEmpty();
    }
}

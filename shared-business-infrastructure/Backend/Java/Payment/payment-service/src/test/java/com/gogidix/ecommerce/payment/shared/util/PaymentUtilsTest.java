package com.gogidix.ecommerce.payment.shared.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("PaymentUtils Tests")
class PaymentUtilsTest {

    @Test
    @DisplayName("Should generate non-null ID")
    void shouldGenerateId() {
        assertThat(PaymentUtils.generateId()).isNotNull().isNotEmpty();
    }
}

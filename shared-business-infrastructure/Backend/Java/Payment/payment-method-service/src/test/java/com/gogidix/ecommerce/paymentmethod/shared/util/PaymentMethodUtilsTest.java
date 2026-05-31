package com.gogidix.ecommerce.paymentmethod.shared.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("PaymentMethodUtils Tests")
class PaymentMethodUtilsTest {

    @Test
    @DisplayName("Should generate non-null ID")
    void shouldGenerateId() {
        assertThat(PaymentMethodUtils.generateId()).isNotNull().isNotEmpty();
    }
}

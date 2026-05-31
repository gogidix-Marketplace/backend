package com.gogidix.ecommerce.paymentgateway.shared.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("PaymentGatewayUtils Tests")
class PaymentGatewayUtilsTest {

    @Test
    @DisplayName("Should generate non-null ID")
    void shouldGenerateId() {
        assertThat(PaymentGatewayUtils.generateId()).isNotNull().isNotEmpty();
    }
}

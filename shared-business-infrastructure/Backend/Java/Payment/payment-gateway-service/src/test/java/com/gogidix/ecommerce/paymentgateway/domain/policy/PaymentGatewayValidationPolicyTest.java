package com.gogidix.ecommerce.paymentgateway.domain.policy;

import com.gogidix.ecommerce.paymentgateway.domain.model.PaymentGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("PaymentGatewayValidationPolicy Tests")
class PaymentGatewayValidationPolicyTest {

    private PaymentGatewayValidationPolicy policy;

    @BeforeEach
    void setUp() { policy = new PaymentGatewayValidationPolicy(); }

    @Test
    @DisplayName("Should pass for valid entity")
    void shouldPassValid() {
        PaymentGateway entity = new PaymentGateway("tenant-1");
        entity.setName("valid-name");
        assertThatCode(() -> policy.validate(entity)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should reject null entity")
    void shouldRejectNull() {
        assertThatThrownBy(() -> policy.validate(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("cannot be null");
    }

    @Test
    @DisplayName("Should reject blank name")
    void shouldRejectBlankName() {
        PaymentGateway entity = new PaymentGateway("tenant-1");
        entity.setName("");
        assertThatThrownBy(() -> policy.validate(entity))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("name is required");
    }

    @Test
    @DisplayName("Should reject null name")
    void shouldRejectNullName() {
        PaymentGateway entity = new PaymentGateway("tenant-1");
        assertThatThrownBy(() -> policy.validate(entity))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("name is required");
    }
}

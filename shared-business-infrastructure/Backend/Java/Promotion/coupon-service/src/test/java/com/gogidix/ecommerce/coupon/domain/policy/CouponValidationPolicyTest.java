package com.gogidix.ecommerce.coupon.domain.policy;

import com.gogidix.ecommerce.coupon.domain.model.Coupon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("CouponValidationPolicy Tests")
class CouponValidationPolicyTest {

    private CouponValidationPolicy policy;

    @BeforeEach
    void setUp() { policy = new CouponValidationPolicy(); }

    @Test
    @DisplayName("Should pass for valid entity")
    void shouldPassValid() {
        Coupon entity = new Coupon("tenant-1");
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
        Coupon entity = new Coupon("tenant-1");
        entity.setName("");
        assertThatThrownBy(() -> policy.validate(entity))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("name is required");
    }

    @Test
    @DisplayName("Should reject null name")
    void shouldRejectNullName() {
        Coupon entity = new Coupon("tenant-1");
        assertThatThrownBy(() -> policy.validate(entity))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("name is required");
    }
}

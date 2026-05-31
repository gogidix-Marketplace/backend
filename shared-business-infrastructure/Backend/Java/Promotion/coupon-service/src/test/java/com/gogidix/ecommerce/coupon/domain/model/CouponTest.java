package com.gogidix.ecommerce.coupon.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Coupon Domain Model Tests")
class CouponTest {

    @Test
    @DisplayName("Should create with tenant ID")
    void shouldCreateWithTenantId() {
        Coupon entity = new Coupon("tenant-1");
        assertThat(entity.getTenantId()).isEqualTo("tenant-1");
        assertThat(entity.getIsActive()).isTrue();
        assertThat(entity.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should set and get name")
    void shouldSetAndGetName() {
        Coupon entity = new Coupon();
        entity.setName("test-name");
        assertThat(entity.getName()).isEqualTo("test-name");
    }

    @Test
    @DisplayName("Should update timestamp")
    void shouldUpdateTimestamp() {
        Coupon entity = new Coupon("tenant-1");
        var before = entity.getUpdatedAt();
        entity.updateTimestamp();
        assertThat(entity.getUpdatedAt()).isAfterOrEqualTo(before);
    }

    @Test
    @DisplayName("Should set isActive")
    void shouldSetIsActive() {
        Coupon entity = new Coupon();
        entity.setIsActive(false);
        assertThat(entity.getIsActive()).isFalse();
    }
}

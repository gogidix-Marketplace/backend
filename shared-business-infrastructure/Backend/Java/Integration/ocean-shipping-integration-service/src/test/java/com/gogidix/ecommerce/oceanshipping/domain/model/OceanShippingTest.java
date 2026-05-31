package com.gogidix.ecommerce.oceanshipping.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("OceanShipping Domain Model Tests")
class OceanShippingTest {

    @Test
    @DisplayName("Should create with tenant ID")
    void shouldCreateWithTenantId() {
        OceanShipping entity = new OceanShipping("tenant-1");
        assertThat(entity.getTenantId()).isEqualTo("tenant-1");
        assertThat(entity.getIsActive()).isTrue();
        assertThat(entity.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should set and get name")
    void shouldSetAndGetName() {
        OceanShipping entity = new OceanShipping();
        entity.setName("test-name");
        assertThat(entity.getName()).isEqualTo("test-name");
    }

    @Test
    @DisplayName("Should update timestamp")
    void shouldUpdateTimestamp() {
        OceanShipping entity = new OceanShipping("tenant-1");
        var before = entity.getUpdatedAt();
        entity.updateTimestamp();
        assertThat(entity.getUpdatedAt()).isAfterOrEqualTo(before);
    }

    @Test
    @DisplayName("Should set isActive")
    void shouldSetIsActive() {
        OceanShipping entity = new OceanShipping();
        entity.setIsActive(false);
        assertThat(entity.getIsActive()).isFalse();
    }
}

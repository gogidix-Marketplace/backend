package com.gogidix.ecommerce.inventorysync.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("InventorySync Domain Model Tests")
class InventorySyncTest {

    @Test
    @DisplayName("Should create with tenant ID")
    void shouldCreateWithTenantId() {
        InventorySync entity = new InventorySync("tenant-1");
        assertThat(entity.getTenantId()).isEqualTo("tenant-1");
        assertThat(entity.getIsActive()).isTrue();
        assertThat(entity.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should set and get name")
    void shouldSetAndGetName() {
        InventorySync entity = new InventorySync();
        entity.setName("test-name");
        assertThat(entity.getName()).isEqualTo("test-name");
    }

    @Test
    @DisplayName("Should update timestamp")
    void shouldUpdateTimestamp() {
        InventorySync entity = new InventorySync("tenant-1");
        var before = entity.getUpdatedAt();
        entity.updateTimestamp();
        assertThat(entity.getUpdatedAt()).isAfterOrEqualTo(before);
    }

    @Test
    @DisplayName("Should set isActive")
    void shouldSetIsActive() {
        InventorySync entity = new InventorySync();
        entity.setIsActive(false);
        assertThat(entity.getIsActive()).isFalse();
    }
}

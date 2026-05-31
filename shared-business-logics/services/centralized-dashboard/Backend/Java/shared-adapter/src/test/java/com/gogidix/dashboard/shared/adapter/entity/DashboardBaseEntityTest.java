package com.gogidix.dashboard.shared.adapter.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DashboardBaseEntity Tests")
class DashboardBaseEntityTest {

    static class TestEntity extends DashboardBaseEntity {
        @Override
        protected void validateBusinessRules() {
        }
    }

    @Test
    @DisplayName("Should set and get id")
    void shouldSetAndGetId() {
        TestEntity entity = new TestEntity();
        entity.setId("test-id");
        assertEquals("test-id", entity.getId());
    }

    @Test
    @DisplayName("Should set and get tenantId")
    void shouldSetAndGetTenantId() {
        TestEntity entity = new TestEntity();
        entity.setTenantId("tenant-1");
        assertEquals("tenant-1", entity.getTenantId());
    }

    @Test
    @DisplayName("Should set and get createdBy")
    void shouldSetAndGetCreatedBy() {
        TestEntity entity = new TestEntity();
        entity.setCreatedBy("user-1");
        assertEquals("user-1", entity.getCreatedBy());
    }

    @Test
    @DisplayName("Should set and get createdAt")
    void shouldSetAndGetCreatedAt() {
        TestEntity entity = new TestEntity();
        LocalDateTime now = LocalDateTime.now();
        entity.setCreatedAt(now);
        assertEquals(now, entity.getCreatedAt());
    }

    @Test
    @DisplayName("Should set and get updatedBy")
    void shouldSetAndGetUpdatedBy() {
        TestEntity entity = new TestEntity();
        entity.setUpdatedBy("user-2");
        assertEquals("user-2", entity.getUpdatedBy());
    }

    @Test
    @DisplayName("Should set and get updatedAt")
    void shouldSetAndGetUpdatedAt() {
        TestEntity entity = new TestEntity();
        LocalDateTime now = LocalDateTime.now();
        entity.setUpdatedAt(now);
        assertEquals(now, entity.getUpdatedAt());
    }

    @Test
    @DisplayName("Should set and get version")
    void shouldSetAndGetVersion() {
        TestEntity entity = new TestEntity();
        entity.setVersion(5L);
        assertEquals(5L, entity.getVersion());
    }

    @Test
    @DisplayName("Should soft delete")
    void shouldSoftDelete() {
        TestEntity entity = new TestEntity();
        entity.softDelete();
        assertTrue(entity.getDeleted());
        assertNotNull(entity.getDeletedAt());
    }

    @Test
    @DisplayName("Should restore soft deleted")
    void shouldRestoreSoftDeleted() {
        TestEntity entity = new TestEntity();
        entity.softDelete();
        entity.restore();
        assertFalse(entity.getDeleted());
        assertNull(entity.getDeletedAt());
    }

    @Test
    @DisplayName("Should activate")
    void shouldActivate() {
        TestEntity entity = new TestEntity();
        entity.setActive(false);
        entity.activate();
        assertTrue(entity.getActive());
    }

    @Test
    @DisplayName("Should deactivate")
    void shouldDeactivate() {
        TestEntity entity = new TestEntity();
        entity.deactivate();
        assertFalse(entity.getActive());
    }

    @Test
    @DisplayName("Should be accessible when active and not deleted")
    void shouldBeAccessibleWhenActiveAndNotDeleted() {
        TestEntity entity = new TestEntity();
        entity.setActive(true);
        entity.setDeleted(false);
        assertTrue(entity.isAccessible());
    }

    @Test
    @DisplayName("Should not be accessible when inactive")
    void shouldNotBeAccessibleWhenInactive() {
        TestEntity entity = new TestEntity();
        entity.setActive(false);
        entity.setDeleted(false);
        assertFalse(entity.isAccessible());
    }

    @Test
    @DisplayName("Should not be accessible when deleted")
    void shouldNotBeAccessibleWhenDeleted() {
        TestEntity entity = new TestEntity();
        entity.setActive(true);
        entity.setDeleted(true);
        assertFalse(entity.isAccessible());
    }

    @Test
    @DisplayName("Should set id on onCreate when null")
    void shouldSetIdOnCreateWhenNull() {
        TestEntity entity = new TestEntity();
        entity.onCreate();
        assertNotNull(entity.getId());
        assertNotNull(entity.getCreatedAt());
        assertTrue(entity.getActive());
        assertFalse(entity.getDeleted());
    }

    @Test
    @DisplayName("Should not overwrite id on onCreate when set")
    void shouldNotOverwriteIdOnCreateWhenSet() {
        TestEntity entity = new TestEntity();
        entity.setId("existing-id");
        entity.onCreate();
        assertEquals("existing-id", entity.getId());
    }

    @Test
    @DisplayName("Should set updatedAt on onUpdate")
    void shouldSetUpdatedAtOnUpdate() {
        TestEntity entity = new TestEntity();
        entity.onUpdate();
        assertNotNull(entity.getUpdatedAt());
    }
}

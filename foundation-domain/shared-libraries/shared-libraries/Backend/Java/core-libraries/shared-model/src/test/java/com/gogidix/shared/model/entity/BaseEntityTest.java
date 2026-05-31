package com.gogidix.shared.model.entity;

import com.gogidix.shared.model.domain.model.EntityStatus;
import com.gogidix.shared.model.domain.model.ValidationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Entity BaseEntity Facade Tests")
class EntityBaseEntityTest {

    static class TestEntity extends BaseEntity {
        public TestEntity(UUID id, Long version, String entityType,
                         LocalDateTime createdAt, String createdBy,
                         LocalDateTime updatedAt, String updatedBy) {
            super(id, version, entityType, createdAt, createdBy, updatedAt, updatedBy);
        }

        public TestEntity(UUID id, Long version, String entityType,
                         LocalDateTime createdAt, String createdBy,
                         LocalDateTime updatedAt, String updatedBy,
                         boolean deleted, LocalDateTime deletedAt, String deletedBy, String deletionReason,
                         EntityStatus status, String statusReason, LocalDateTime statusChangedAt, String statusChangedBy) {
            super(id, version, entityType, createdAt, createdBy, updatedAt, updatedBy,
                  deleted, deletedAt, deletedBy, deletionReason, status, statusReason, statusChangedAt, statusChangedBy);
        }

        @Override public boolean isValid() { return getId() != null; }
        @Override public boolean isComplete() { return getId() != null; }
        @Override public ValidationResult validateBusinessRules() { return ValidationResult.success(); }
    }

    @Test
    void testBasicConstructor() {
        LocalDateTime now = LocalDateTime.now();
        UUID id = UUID.randomUUID();
        TestEntity e = new TestEntity(id, 1L, "TEST", now, "admin", now, "admin");
        assertEquals(id, e.getId());
        assertEquals("TEST", e.getEntityType());
        assertFalse(e.isDeleted());
        assertEquals(EntityStatus.ACTIVE, e.getStatus());
    }

    @Test
    void testFullConstructor() {
        LocalDateTime now = LocalDateTime.now();
        UUID id = UUID.randomUUID();
        TestEntity e = new TestEntity(id, 2L, "ORDER", now, "user", now, "user",
            true, now, "admin", "expired", EntityStatus.DELETED, "cleanup", now, "admin");
        assertTrue(e.isDeleted());
        assertEquals(EntityStatus.DELETED, e.getStatus());
    }

    @Test
    void testIsValid() {
        LocalDateTime now = LocalDateTime.now();
        TestEntity e = new TestEntity(UUID.randomUUID(), 1L, "TEST", now, "a", now, "a");
        assertTrue(e.isValid());
    }
}

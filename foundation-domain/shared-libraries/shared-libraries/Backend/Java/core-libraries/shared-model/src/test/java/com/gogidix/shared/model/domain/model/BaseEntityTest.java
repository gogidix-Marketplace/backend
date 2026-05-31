package com.gogidix.shared.model.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BaseEntity Tests")
class BaseEntityTest {

    static class TestBaseEntity extends BaseEntity {
        public TestBaseEntity(UUID id, Long version, String entityType,
                             LocalDateTime createdAt, String createdBy,
                             LocalDateTime updatedAt, String updatedBy,
                             boolean deleted, LocalDateTime deletedAt, String deletedBy, String deletionReason,
                             EntityStatus status, String statusReason, LocalDateTime statusChangedAt, String statusChangedBy) {
            super(id, version, entityType, createdAt, createdBy, updatedAt, updatedBy,
                  deleted, deletedAt, deletedBy, deletionReason, status, statusReason, statusChangedAt, statusChangedBy);
        }

        @Override public boolean isValid() { return getId() != null; }
        @Override public boolean isComplete() { return getId() != null && getEntityType() != null; }
        @Override public ValidationResult validateBusinessRules() {
            if (getId() == null) return ValidationResult.failure("ID required");
            return ValidationResult.success();
        }
    }

    private TestBaseEntity createActive() {
        LocalDateTime now = LocalDateTime.now();
        return new TestBaseEntity(UUID.randomUUID(), 1L, "TEST", now, "admin", now, "admin",
            false, null, null, null, EntityStatus.ACTIVE, null, now, "admin");
    }

    private TestBaseEntity createDeleted() {
        LocalDateTime now = LocalDateTime.now();
        return new TestBaseEntity(UUID.randomUUID(), 1L, "TEST", now, "admin", now, "admin",
            true, now, "admin", "test delete", EntityStatus.ARCHIVED, null, now, "admin");
    }

    private TestBaseEntity createNew() {
        return new TestBaseEntity(null, 0L, "TEST", null, null, null, null,
            false, null, null, null, EntityStatus.DRAFT, null, null, null);
    }

    @Test void testGetId() { assertNotNull(createActive().getId()); }
    @Test void testGetVersion() { assertEquals(1L, createActive().getVersion()); }
    @Test void testGetEntityType() { assertEquals("TEST", createActive().getEntityType()); }
    @Test void testGetCreatedAt() { assertNotNull(createActive().getCreatedAt()); }
    @Test void testGetCreatedBy() { assertEquals("admin", createActive().getCreatedBy()); }
    @Test void testGetUpdatedAt() { assertNotNull(createActive().getUpdatedAt()); }
    @Test void testGetUpdatedBy() { assertEquals("admin", createActive().getUpdatedBy()); }
    @Test void testGetStatus() { assertEquals(EntityStatus.ACTIVE, createActive().getStatus()); }

    @Test void testIsNew() { assertTrue(createNew().isNew()); }
    @Test void testIsNotNew() { assertFalse(createActive().isNew()); }
    @Test void testIsDeleted() { assertTrue(createDeleted().isDeleted()); }
    @Test void testIsNotDeleted() { assertFalse(createActive().isDeleted()); }
    @Test void testIsActive() { assertTrue(createActive().isActive()); }
    @Test void testIsDraft() { assertTrue(createNew().isDraft()); }
    @Test void testIsSuspended() {
        LocalDateTime now = LocalDateTime.now();
        TestBaseEntity e = new TestBaseEntity(UUID.randomUUID(), 1L, "TEST", now, "a", now, "a",
            false, null, null, null, EntityStatus.SUSPENDED, null, now, "a");
        assertTrue(e.isSuspended());
    }
    @Test void testIsArchived() {
        LocalDateTime now = LocalDateTime.now();
        TestBaseEntity e = new TestBaseEntity(UUID.randomUUID(), 1L, "TEST", now, "a", now, "a",
            false, null, null, null, EntityStatus.ARCHIVED, null, now, "a");
        assertTrue(e.isArchived());
    }

    @Test void testCanBeDeleted() { assertTrue(createActive().canBeDeleted()); }
    @Test void testCannotBeDeletedIfDeleted() { assertFalse(createDeleted().canBeDeleted()); }
    @Test void testCannotBeDeletedIfProcessing() {
        LocalDateTime now = LocalDateTime.now();
        TestBaseEntity e = new TestBaseEntity(UUID.randomUUID(), 1L, "TEST", now, "a", now, "a",
            false, null, null, null, EntityStatus.PROCESSING, null, now, "a");
        assertFalse(e.canBeDeleted());
    }

    @Test void testCanBeRestored() { assertTrue(createDeleted().canBeRestored()); }
    @Test void testCannotBeRestoredIfActive() { assertFalse(createActive().canBeRestored()); }

    @Test void testCanBeArchived() { assertTrue(createActive().canBeArchived()); }
    @Test void testCanBeActivated() { assertTrue(createNew().canBeActivated()); }

    @Test void testRequiresAttentionError() {
        LocalDateTime now = LocalDateTime.now();
        TestBaseEntity e = new TestBaseEntity(UUID.randomUUID(), 1L, "TEST", now, "a", now, "a",
            false, null, null, null, EntityStatus.ERROR, null, now, "a");
        assertTrue(e.requiresAttention());
    }

    @Test void testGetHealthScore() { assertTrue(createActive().getHealthScore() > 0); }
    @Test void testGetHealthScoreDeleted() { assertEquals(0, createDeleted().getHealthScore()); }

    @Test void testGetLifecycleStage() { assertNotNull(createActive().getLifecycleStage()); }
    @Test void testGetLifecycleStageDeleted() { assertEquals(EntityLifecycleStage.DELETED, createDeleted().getLifecycleStage()); }

    @Test void testHasCompleteAuditTrail() { assertTrue(createActive().hasCompleteAuditTrail()); }
    @Test void testIncompleteAuditTrail() { assertFalse(createNew().hasCompleteAuditTrail()); }

    @Test void testIsConsistent() { assertTrue(createActive().isConsistent()); }
    @Test void testGetAgeInDays() { assertTrue(createActive().getAgeInDays() >= 0); }
    @Test void testGetMinutesSinceLastUpdate() { assertTrue(createActive().getMinutesSinceLastUpdate() >= 0); }
    @Test void testIsRecentlyUpdated() { assertTrue(createActive().isRecentlyUpdated()); }
    @Test void testIsStale() { assertFalse(createActive().isStale(30)); }

    @Test void testWasCreatedBy() { assertTrue(createActive().wasCreatedBy("admin")); }
    @Test void testWasNotCreatedBy() { assertFalse(createActive().wasCreatedBy("other")); }
    @Test void testWasLastUpdatedBy() { assertTrue(createActive().wasLastUpdatedBy("admin")); }
    @Test void testWasDeletedBy() { assertTrue(createDeleted().wasDeletedBy("admin")); }
    @Test void testWasNotDeletedBy() { assertFalse(createDeleted().wasDeletedBy("other")); }

    @Test void testGetSummary() { assertNotNull(createActive().getSummary()); }
    @Test void testIsValid() { assertTrue(createActive().isValid()); }
    @Test void testIsComplete() { assertTrue(createActive().isComplete()); }
    @Test void testValidateBusinessRules() { assertTrue(createActive().validateBusinessRules().isValid()); }

    @Test void testEquals() {
        TestBaseEntity e = createActive();
        assertEquals(e, e);
    }
    @Test void testNotEquals() { assertNotEquals(createActive(), createActive()); }
    @Test void testNotEqualsNull() { assertNotEquals(createActive(), null); }
    @Test void testHashCode() { assertNotNull(createActive().hashCode()); }
    @Test void testToString() { assertTrue(createActive().toString().contains("TestBaseEntity")); }

    @Test void testGetTimeUntilStale() { assertNotNull(createActive().getTimeUntilStale(30)); }

    @Test void testNullVersionDefaults() {
        LocalDateTime now = LocalDateTime.now();
        TestBaseEntity e = new TestBaseEntity(UUID.randomUUID(), null, "TEST", now, "a", now, "a",
            false, null, null, null, null, null, now, "a");
        assertEquals(0L, e.getVersion());
        assertEquals(EntityStatus.ACTIVE, e.getStatus());
    }
}

package com.gogidix.shared.model.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DomainEntity Tests")
class DomainEntityTest {

    static class TestDomainEntity extends DomainEntity {
        @Override
        public boolean isValid() { return getId() != null; }

        @Override
        public String getEntityType() { return "TEST"; }
    }

    @Test
    void testNewEntity() {
        TestDomainEntity entity = new TestDomainEntity();
        assertNull(entity.getId());
        assertTrue(entity.isNew());
    }

    @Test
    void testInitializeForCreation() {
        TestDomainEntity entity = new TestDomainEntity();
        entity.initializeForCreation("user1");
        assertNotNull(entity.getId());
        assertEquals(0L, entity.getVersion());
        assertEquals("user1", entity.getCreatedBy());
        assertNotNull(entity.getCreatedAt());
        assertFalse(entity.isDeleted());
    }

    @Test
    void testIsNewWithNullId() {
        TestDomainEntity entity = new TestDomainEntity();
        assertTrue(entity.isNew());
    }

    @Test
    void testIsNewWithId() {
        TestDomainEntity entity = new TestDomainEntity();
        entity.initializeForCreation("user1");
        assertFalse(entity.isNew());
    }

    @Test
    void testMarkAsDeleted() {
        TestDomainEntity entity = new TestDomainEntity();
        entity.initializeForCreation("user1");
        entity.markAsDeleted("admin");
        assertTrue(entity.isDeleted());
        assertEquals("admin", entity.getDeletedBy());
        assertNotNull(entity.getDeletedAt());
    }

    @Test
    void testRestore() {
        TestDomainEntity entity = new TestDomainEntity();
        entity.initializeForCreation("user1");
        entity.markAsDeleted("admin");
        entity.restore("admin2");
        assertFalse(entity.isDeleted());
        assertNull(entity.getDeletedBy());
        assertNull(entity.getDeletedAt());
    }

    @Test
    void testUpdateWith() {
        TestDomainEntity entity = new TestDomainEntity();
        entity.initializeForCreation("user1");
        entity.updateWith("user2");
        assertEquals("user2", entity.getUpdatedBy());
        assertNotNull(entity.getUpdatedAt());
    }

    @Test
    void testCanBeDeleted() {
        TestDomainEntity entity = new TestDomainEntity();
        entity.initializeForCreation("user1");
        assertTrue(entity.canBeDeleted());
    }

    @Test
    void testCannotBeDeletedIfAlreadyDeleted() {
        TestDomainEntity entity = new TestDomainEntity();
        entity.initializeForCreation("user1");
        entity.markAsDeleted("admin");
        assertFalse(entity.canBeDeleted());
    }

    @Test
    void testCanBeRestored() {
        TestDomainEntity entity = new TestDomainEntity();
        entity.initializeForCreation("user1");
        entity.markAsDeleted("admin");
        assertTrue(entity.canBeRestored());
    }

    @Test
    void testCannotBeRestoredIfNotDeleted() {
        TestDomainEntity entity = new TestDomainEntity();
        entity.initializeForCreation("user1");
        assertFalse(entity.canBeRestored());
    }

    @Test
    void testIsValid() {
        TestDomainEntity entity = new TestDomainEntity();
        assertFalse(entity.isValid());
        entity.initializeForCreation("user1");
        assertTrue(entity.isValid());
    }

    @Test
    void testGetAgeInDays() {
        TestDomainEntity entity = new TestDomainEntity();
        assertEquals(0, entity.getAgeInDays());
        entity.initializeForCreation("user1");
        assertEquals(0, entity.getAgeInDays());
    }

    @Test
    void testGetMinutesSinceLastUpdate() {
        TestDomainEntity entity = new TestDomainEntity();
        assertEquals(0, entity.getMinutesSinceLastUpdate());
    }

    @Test
    void testIsRecentlyUpdated() {
        TestDomainEntity entity = new TestDomainEntity();
        entity.initializeForCreation("user1");
        assertTrue(entity.isRecentlyUpdated());
    }

    @Test
    void testEqualsSameId() {
        TestDomainEntity e1 = new TestDomainEntity();
        TestDomainEntity e2 = new TestDomainEntity();
        UUID id = UUID.randomUUID();
        e1.initializeForCreation("u1");
        e2.initializeForCreation("u2");
        assertNotEquals(e1, e2);
    }

    @Test
    void testEqualsSameInstance() {
        TestDomainEntity entity = new TestDomainEntity();
        assertEquals(entity, entity);
    }

    @Test
    void testEqualsNull() {
        TestDomainEntity entity = new TestDomainEntity();
        assertNotEquals(entity, null);
    }

    @Test
    void testHashCodeWithNullId() {
        TestDomainEntity entity = new TestDomainEntity();
        assertEquals(entity.getClass().hashCode(), entity.hashCode());
    }

    @Test
    void testToString() {
        TestDomainEntity entity = new TestDomainEntity();
        entity.initializeForCreation("user1");
        String str = entity.toString();
        assertTrue(str.contains("TestDomainEntity"));
    }

    @Test
    void testIsStale() {
        TestDomainEntity entity = new TestDomainEntity();
        entity.initializeForCreation("user1");
        assertFalse(entity.isStale(30));
    }

    @Test
    void testGetEntityType() {
        TestDomainEntity entity = new TestDomainEntity();
        assertEquals("TEST", entity.getEntityType());
    }
}

package com.gogidix.shared.model.common;

import com.gogidix.libraries.sharedmodel.domain.model.BaseEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Common BaseEntity Tests")
class BaseEntityTest {

    static class TestEntity extends BaseEntity {
        public TestEntity() {}
        public TestEntity(UUID id, Long version, LocalDateTime createdAt, String createdBy,
                         LocalDateTime updatedAt, String updatedBy, boolean deleted,
                         LocalDateTime deletedAt, String deletedBy) {
            setId(id);
            setVersion(version);
            setCreatedAt(createdAt);
            setCreatedBy(createdBy);
            setUpdatedAt(updatedAt);
            setUpdatedBy(updatedBy);
            setDeleted(deleted);
            setDeletedAt(deletedAt);
            setDeletedBy(deletedBy);
        }
        public void callOnCreate() { onCreate(); }
        public void callOnUpdate() { onUpdate(); }
    }

    private TestEntity createEntity() {
        return new TestEntity(UUID.randomUUID(), 1L, LocalDateTime.now(), "admin",
            LocalDateTime.now(), "admin", false, null, null);
    }

    @Test void testGetId() { assertNotNull(createEntity().getId()); }
    @Test void testGetVersion() { assertEquals(1L, createEntity().getVersion()); }
    @Test void testGetCreatedAt() { assertNotNull(createEntity().getCreatedAt()); }
    @Test void testGetCreatedBy() { assertEquals("admin", createEntity().getCreatedBy()); }
    @Test void testGetUpdatedAt() { assertNotNull(createEntity().getUpdatedAt()); }
    @Test void testGetUpdatedBy() { assertEquals("admin", createEntity().getUpdatedBy()); }
    @Test void testIsDeletedDefaultFalse() { assertFalse(createEntity().isDeleted()); }

    @Test void testSetId() {
        TestEntity e = new TestEntity();
        UUID id = UUID.randomUUID();
        e.setId(id);
        assertEquals(id, e.getId());
    }

    @Test void testSetVersion() {
        TestEntity e = new TestEntity();
        e.setVersion(5L);
        assertEquals(5L, e.getVersion());
    }

    @Test void testSetCreatedAt() {
        TestEntity e = new TestEntity();
        LocalDateTime now = LocalDateTime.now();
        e.setCreatedAt(now);
        assertEquals(now, e.getCreatedAt());
    }

    @Test void testSetCreatedBy() {
        TestEntity e = new TestEntity();
        e.setCreatedBy("user1");
        assertEquals("user1", e.getCreatedBy());
    }

    @Test void testSetUpdatedAt() {
        TestEntity e = new TestEntity();
        LocalDateTime now = LocalDateTime.now();
        e.setUpdatedAt(now);
        assertEquals(now, e.getUpdatedAt());
    }

    @Test void testSetUpdatedBy() {
        TestEntity e = new TestEntity();
        e.setUpdatedBy("user2");
        assertEquals("user2", e.getUpdatedBy());
    }

    @Test void testSetDeleted() {
        TestEntity e = new TestEntity();
        e.setDeleted(true);
        assertTrue(e.isDeleted());
    }

    @Test void testSetDeletedAt() {
        TestEntity e = new TestEntity();
        LocalDateTime now = LocalDateTime.now();
        e.setDeletedAt(now);
        assertEquals(now, e.getDeletedAt());
    }

    @Test void testSetDeletedBy() {
        TestEntity e = new TestEntity();
        e.setDeletedBy("admin");
        assertEquals("admin", e.getDeletedBy());
    }

    @Test void testIsNewWithNullId() { assertTrue(new TestEntity().isNew()); }
    @Test void testIsNotNewWithId() { assertFalse(createEntity().isNew()); }

    @Test void testOnCreateSetsIdWhenNull() {
        TestEntity e = new TestEntity();
        e.callOnCreate();
        assertNotNull(e.getId());
        assertNotNull(e.getCreatedAt());
        assertEquals(e.getCreatedAt(), e.getUpdatedAt());
    }

    @Test void testOnCreateKeepsExistingId() {
        UUID existing = UUID.randomUUID();
        TestEntity e = new TestEntity();
        e.setId(existing);
        e.callOnCreate();
        assertEquals(existing, e.getId());
    }

    @Test void testOnUpdate() {
        TestEntity e = createEntity();
        LocalDateTime before = e.getUpdatedAt();
        e.callOnUpdate();
        assertNotNull(e.getUpdatedAt());
    }

    @Test void testMarkAsDeleted() {
        TestEntity e = createEntity();
        e.markAsDeleted("admin");
        assertTrue(e.isDeleted());
        assertNotNull(e.getDeletedAt());
        assertEquals("admin", e.getDeletedBy());
    }

    @Test void testRestore() {
        TestEntity e = createEntity();
        e.markAsDeleted("admin");
        e.restore();
        assertFalse(e.isDeleted());
        assertNull(e.getDeletedAt());
        assertNull(e.getDeletedBy());
    }

    @Test void testEqualsSameObject() {
        TestEntity e = createEntity();
        assertEquals(e, e);
    }

    @Test void testEqualsDifferentType() {
        TestEntity e = createEntity();
        assertNotEquals(e, "not an entity");
    }

    @Test void testEqualsNull() {
        TestEntity e = createEntity();
        assertNotEquals(e, null);
    }

    @Test void testEqualsWithSameId() {
        UUID id = UUID.randomUUID();
        TestEntity e1 = new TestEntity();
        e1.setId(id);
        TestEntity e2 = new TestEntity();
        e2.setId(id);
        assertEquals(e1, e2);
    }

    @Test void testEqualsWithDifferentId() {
        TestEntity e1 = createEntity();
        TestEntity e2 = createEntity();
        assertNotEquals(e1, e2);
    }

    @Test void testHashCodeWithId() {
        TestEntity e = createEntity();
        assertEquals(e.getClass().hashCode(), e.hashCode());
    }

    @Test void testHashCodeNullId() {
        TestEntity e = new TestEntity();
        assertEquals(e.getClass().hashCode(), e.hashCode());
    }

    @Test void testNoArgsConstructor() {
        TestEntity e = new TestEntity();
        assertNull(e.getId());
        assertNull(e.getVersion());
        assertFalse(e.isDeleted());
    }

    @Test void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        TestEntity e = new TestEntity(id, 1L, now, "admin", now, "admin", false, null, null);
        assertEquals(id, e.getId());
        assertEquals(1L, e.getVersion());
    }
}

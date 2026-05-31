package com.gogidix.digitalmarketing.shared.domain;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BaseEntityTest {

    private static class TestEntity extends BaseEntity {
        TestEntity(String tenantId) { super(tenantId); }
        TestEntity() { super(); }
    }

    private TestEntity entity;

    @BeforeEach
    void setUp() { entity = new TestEntity("tenant-123"); }

    @Test void testConstructorWithTenantId() {
        assertNotNull(entity.getId());
        assertEquals("tenant-123", entity.getTenantId());
        assertNotNull(entity.getCreatedAt());
        assertTrue(entity.isNew());
    }
    @Test void testDefaultConstructor() {
        var e = new TestEntity();
        assertNotNull(e.getCreatedAt());
        assertFalse(e.isNew());
    }
    @Test void testSetTenantId() { entity.setTenantId("t2"); assertEquals("t2", entity.getTenantId()); }
    @Test void testSetCreatedAt() { var now = Instant.now(); entity.setCreatedAt(now); assertEquals(now, entity.getCreatedAt()); }
    @Test void testSetUpdatedAt() { var now = Instant.now(); entity.setUpdatedAt(now); assertEquals(now, entity.getUpdatedAt()); }
    @Test void testSetCreatedBy() { entity.setCreatedBy("u1"); assertEquals("u1", entity.getCreatedBy()); }
    @Test void testSetUpdatedBy() { entity.setUpdatedBy("u2"); assertEquals("u2", entity.getUpdatedBy()); }
    @Test void testTouchWithUpdater() { entity.touch("up1"); assertEquals("up1", entity.getUpdatedBy()); }
    @Test void testTouch() { entity.touch(); assertNotNull(entity.getUpdatedAt()); }
    @Test void testMarkAsSaved() { entity.markAsSaved(); assertFalse(entity.isNew()); }
    @Test void testEqualsSelf() { assertEquals(entity, entity); }
    @Test void testEqualsNull() { assertNotEquals(entity, null); }
    @Test void testEqualsDifferent() { assertNotEquals(entity, new TestEntity("other")); }
    @Test void testHashCode() { assertNotEquals(0, entity.hashCode()); }
    @Test void testToString() { assertTrue(entity.toString().contains("tenant-123")); }
    @Test void testGetId() { assertFalse(entity.getId().isEmpty()); }
    @Test void testTenantIdNullThrows() { assertThrows(NullPointerException.class, () -> new TestEntity(null)); }
}

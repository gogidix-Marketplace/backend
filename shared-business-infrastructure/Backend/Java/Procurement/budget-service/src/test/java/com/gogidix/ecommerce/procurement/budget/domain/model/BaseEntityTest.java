package com.gogidix.ecommerce.procurement.budget.domain.model;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

class BaseEntityTest {

    static class TestEntity extends BaseEntity {
        private String name;
        TestEntity() { super(); }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    @Test void constructor_shouldSetTimestamps() {
        TestEntity e = new TestEntity();
        assertThat(e.getCreatedAt()).isNotNull();
        assertThat(e.getUpdatedAt()).isNotNull();
    }
    @Test void id_getterSetter() {
        TestEntity e = new TestEntity(); e.setId("id1"); assertThat(e.getId()).isEqualTo("id1");
    }
    @Test void tenantId_getterSetter() {
        TestEntity e = new TestEntity(); e.setTenantId("t1"); assertThat(e.getTenantId()).isEqualTo("t1");
    }
    @Test void createdAt_getterSetter() {
        TestEntity e = new TestEntity(); Instant t = Instant.parse("2024-01-01T00:00:00Z"); e.setCreatedAt(t); assertThat(e.getCreatedAt()).isEqualTo(t);
    }
    @Test void updatedAt_getterSetter() {
        TestEntity e = new TestEntity(); Instant t = Instant.parse("2024-06-15T12:00:00Z"); e.setUpdatedAt(t); assertThat(e.getUpdatedAt()).isEqualTo(t);
    }
    @Test void createdBy_getterSetter() {
        TestEntity e = new TestEntity(); e.setCreatedBy("admin"); assertThat(e.getCreatedBy()).isEqualTo("admin");
    }
    @Test void updatedBy_getterSetter() {
        TestEntity e = new TestEntity(); e.setUpdatedBy("user"); assertThat(e.getUpdatedBy()).isEqualTo("user");
    }
    @Test void markAsUpdated_updatesTimestamp() {
        TestEntity e = new TestEntity(); Instant before = e.getUpdatedAt(); e.markAsUpdated(); assertThat(e.getUpdatedAt()).isAfterOrEqualTo(before);
    }
    @Test void equals_sameId_equal() {
        TestEntity a = new TestEntity(); a.setId("x"); TestEntity b = new TestEntity(); b.setId("x"); assertThat(a).isEqualTo(b);
    }
    @Test void equals_differentId_notEqual() {
        TestEntity a = new TestEntity(); a.setId("a"); TestEntity b = new TestEntity(); b.setId("b"); assertThat(a).isNotEqualTo(b);
    }
    @Test void equals_nullId_notEqual() {
        TestEntity a = new TestEntity(); TestEntity b = new TestEntity(); assertThat(a).isNotEqualTo(b);
    }
    @Test void equals_sameRef_equal() { TestEntity e = new TestEntity(); assertThat(e).isEqualTo(e); }
    @Test void equals_null_false() { assertThat(new TestEntity()).isNotEqualTo(null); }
    @Test void equals_otherType_false() { assertThat(new TestEntity()).isNotEqualTo("string"); }
    @Test void hashCode_consistent() { TestEntity e = new TestEntity(); e.setId("id"); assertThat(e.hashCode()).isEqualTo(e.hashCode()); }
}
package com.gogidix.ecommerce.influencer.social.domain.model;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

class BaseEntityTest {
    static class TestEntity extends BaseEntity { TestEntity() { super(); } }
    @Test void constructor_timestamps() { TestEntity e = new TestEntity(); assertThat(e.getCreatedAt()).isNotNull(); assertThat(e.getUpdatedAt()).isNotNull(); }
    @Test void id() { TestEntity e = new TestEntity(); e.setId("id1"); assertThat(e.getId()).isEqualTo("id1"); }
    @Test void tenantId() { TestEntity e = new TestEntity(); e.setTenantId("t1"); assertThat(e.getTenantId()).isEqualTo("t1"); }
    @Test void createdAt() { TestEntity e = new TestEntity(); Instant t = Instant.parse("2024-01-01T00:00:00Z"); e.setCreatedAt(t); assertThat(e.getCreatedAt()).isEqualTo(t); }
    @Test void updatedAt() { TestEntity e = new TestEntity(); Instant t = Instant.parse("2024-06-15T12:00:00Z"); e.setUpdatedAt(t); assertThat(e.getUpdatedAt()).isEqualTo(t); }
    @Test void createdBy() { TestEntity e = new TestEntity(); e.setCreatedBy("admin"); assertThat(e.getCreatedBy()).isEqualTo("admin"); }
    @Test void updatedBy() { TestEntity e = new TestEntity(); e.setUpdatedBy("user"); assertThat(e.getUpdatedBy()).isEqualTo("user"); }
    @Test void markAsUpdated() { TestEntity e = new TestEntity(); Instant b = e.getUpdatedAt(); e.markAsUpdated(); assertThat(e.getUpdatedAt()).isAfterOrEqualTo(b); }
    @Test void equals_sameId() { TestEntity a = new TestEntity(); a.setId("x"); TestEntity b2 = new TestEntity(); b2.setId("x"); assertThat(a).isEqualTo(b2); }
    @Test void equals_diffId() { TestEntity a = new TestEntity(); a.setId("a"); TestEntity b2 = new TestEntity(); b2.setId("b"); assertThat(a).isNotEqualTo(b2); }
    @Test void equals_nullId() { assertThat(new TestEntity()).isNotEqualTo(new TestEntity()); }
    @Test void equals_sameRef() { TestEntity e = new TestEntity(); assertThat(e).isEqualTo(e); }
    @Test void equals_null() { assertThat(new TestEntity()).isNotEqualTo(null); }
    @Test void hashCode_consistent() { TestEntity e = new TestEntity(); e.setId("id"); assertThat(e.hashCode()).isEqualTo(e.hashCode()); }
}
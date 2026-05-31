package com.gogidix.ecommerce.vendor.dropship.domain.model;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

class DropshipLombokTest {
    private static final Instant FIXED = Instant.parse("2026-01-01T00:00:00Z");
    private Dropship buildFull() {
        Dropship e = new Dropship();
        e.setId("id1"); e.setTenantId("t1"); e.setCreatedAt(FIXED); e.setUpdatedAt(FIXED);
        return e;
    }

    @Test void equals_same() { assertThat(buildFull()).isEqualTo(buildFull()); }
    @Test void equals_different() {
        Dropship e1 = buildFull(); Dropship e2 = buildFull(); e2.setId("other");
        assertThat(e1).isNotEqualTo(e2);
    }
    @Test void equals_null() { assertThat(buildFull()).isNotEqualTo(null); }
    @Test void hashCode_consistency() { Dropship e = buildFull(); assertThat(e.hashCode()).isEqualTo(e.hashCode()); }
    @Test void toString_notNull() { assertThat(buildFull().toString()).contains("Dropship"); }
    @Test void canEqual() { assertThat(buildFull().canEqual(new Dropship())).isTrue(); }
    @Test void canEqual_false() { assertThat(buildFull().canEqual("str")).isFalse(); }
    @Test void equals_self() { Dropship e = buildFull(); assertThat(e).isEqualTo(e); }
}
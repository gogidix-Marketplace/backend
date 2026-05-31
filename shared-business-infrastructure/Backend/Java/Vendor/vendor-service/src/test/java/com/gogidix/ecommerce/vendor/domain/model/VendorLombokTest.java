package com.gogidix.ecommerce.vendor.domain.model;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

class VendorLombokTest {
    private static final Instant FIXED = Instant.parse("2026-01-01T00:00:00Z");
    private Vendor buildFull() {
        Vendor e = new Vendor();
        e.setId("id1"); e.setTenantId("t1"); e.setCreatedAt(FIXED); e.setUpdatedAt(FIXED);
        return e;
    }

    @Test void equals_same() { assertThat(buildFull()).isEqualTo(buildFull()); }
    @Test void equals_different() {
        Vendor e1 = buildFull(); Vendor e2 = buildFull(); e2.setId("other");
        assertThat(e1).isNotEqualTo(e2);
    }
    @Test void equals_null() { assertThat(buildFull()).isNotEqualTo(null); }
    @Test void hashCode_consistency() { Vendor e = buildFull(); assertThat(e.hashCode()).isEqualTo(e.hashCode()); }
    @Test void toString_notNull() { assertThat(buildFull().toString()).contains("Vendor"); }
    @Test void canEqual() { assertThat(buildFull().canEqual(new Vendor())).isTrue(); }
    @Test void canEqual_false() { assertThat(buildFull().canEqual("str")).isFalse(); }
    @Test void equals_self() { Vendor e = buildFull(); assertThat(e).isEqualTo(e); }
}
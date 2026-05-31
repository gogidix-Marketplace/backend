package com.gogidix.ecommerce.vendor.analytics.domain.model;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

class VendorAnalyticsLombokTest {
    private static final Instant FIXED = Instant.parse("2026-01-01T00:00:00Z");
    private VendorAnalytics buildFull() {
        VendorAnalytics e = new VendorAnalytics();
        e.setId("id1"); e.setTenantId("t1"); e.setCreatedAt(FIXED); e.setUpdatedAt(FIXED);
        return e;
    }

    @Test void equals_same() { assertThat(buildFull()).isEqualTo(buildFull()); }
    @Test void equals_different() {
        VendorAnalytics e1 = buildFull(); VendorAnalytics e2 = buildFull(); e2.setId("other");
        assertThat(e1).isNotEqualTo(e2);
    }
    @Test void equals_null() { assertThat(buildFull()).isNotEqualTo(null); }
    @Test void hashCode_consistency() { VendorAnalytics e = buildFull(); assertThat(e.hashCode()).isEqualTo(e.hashCode()); }
    @Test void toString_notNull() { assertThat(buildFull().toString()).contains("VendorAnalytics"); }
    @Test void canEqual() { assertThat(buildFull().canEqual(new VendorAnalytics())).isTrue(); }
    @Test void canEqual_false() { assertThat(buildFull().canEqual("str")).isFalse(); }
    @Test void equals_self() { VendorAnalytics e = buildFull(); assertThat(e).isEqualTo(e); }
}
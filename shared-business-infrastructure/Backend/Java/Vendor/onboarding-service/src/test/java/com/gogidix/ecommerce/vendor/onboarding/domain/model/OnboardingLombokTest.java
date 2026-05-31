package com.gogidix.ecommerce.vendor.onboarding.domain.model;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

class OnboardingLombokTest {
    private static final Instant FIXED = Instant.parse("2026-01-01T00:00:00Z");
    private Onboarding buildFull() {
        Onboarding e = new Onboarding();
        e.setId("id1"); e.setTenantId("t1"); e.setCreatedAt(FIXED); e.setUpdatedAt(FIXED);
        return e;
    }

    @Test void equals_same() { assertThat(buildFull()).isEqualTo(buildFull()); }
    @Test void equals_different() {
        Onboarding e1 = buildFull(); Onboarding e2 = buildFull(); e2.setId("other");
        assertThat(e1).isNotEqualTo(e2);
    }
    @Test void equals_null() { assertThat(buildFull()).isNotEqualTo(null); }
    @Test void hashCode_consistency() { Onboarding e = buildFull(); assertThat(e.hashCode()).isEqualTo(e.hashCode()); }
    @Test void toString_notNull() { assertThat(buildFull().toString()).contains("Onboarding"); }
    @Test void canEqual() { assertThat(buildFull().canEqual(new Onboarding())).isTrue(); }
    @Test void canEqual_false() { assertThat(buildFull().canEqual("str")).isFalse(); }
    @Test void equals_self() { Onboarding e = buildFull(); assertThat(e).isEqualTo(e); }
}
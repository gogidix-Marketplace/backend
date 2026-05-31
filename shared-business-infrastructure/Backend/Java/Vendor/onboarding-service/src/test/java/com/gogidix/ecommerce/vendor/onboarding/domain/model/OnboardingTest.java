package com.gogidix.ecommerce.vendor.onboarding.domain.model;
import org.junit.jupiter.api.Test; import java.time.Instant; import static org.assertj.core.api.Assertions.assertThat;
class OnboardingTest {
    @Test void allFields() {
        Onboarding e = new Onboarding();
        e.setId("id1"); e.setTenantId("t1"); e.setVendorId("v1"); e.setName("test");
        e.setCreatedAt(Instant.now()); e.setUpdatedAt(Instant.now());
        assertThat(e.getId()).isEqualTo("id1"); assertThat(e.getTenantId()).isEqualTo("t1");
        assertThat(e.getVendorId()).isEqualTo("v1"); assertThat(e.getName()).isEqualTo("test");
        assertThat(e.getCreatedAt()).isNotNull(); assertThat(e.getUpdatedAt()).isNotNull();
    }
    @Test void nullDefaults() {
        Onboarding e = new Onboarding();
        assertThat(e.getId()).isNull(); assertThat(e.getVendorId()).isNull(); assertThat(e.getName()).isNull();
    }
    @Test void statusEnum() {
        assertThat(Onboarding.OnboardingStatus.values()).hasSize(6);
        assertThat(Onboarding.OnboardingStatus.valueOf("DRAFT")).isEqualTo(Onboarding.OnboardingStatus.DRAFT);
    }
}
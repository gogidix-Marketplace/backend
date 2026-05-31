package com.gogidix.ecommerce.vendor.analytics.domain.model;
import org.junit.jupiter.api.Test; import java.time.Instant; import static org.assertj.core.api.Assertions.assertThat;
class VendorAnalyticsTest {
    @Test void allFields() {
        VendorAnalytics e = new VendorAnalytics();
        e.setId("id1"); e.setTenantId("t1"); e.setVendorId("v1"); e.setName("test");
        e.setCreatedAt(Instant.now()); e.setUpdatedAt(Instant.now());
        assertThat(e.getId()).isEqualTo("id1"); assertThat(e.getTenantId()).isEqualTo("t1");
        assertThat(e.getVendorId()).isEqualTo("v1"); assertThat(e.getName()).isEqualTo("test");
        assertThat(e.getCreatedAt()).isNotNull(); assertThat(e.getUpdatedAt()).isNotNull();
    }
    @Test void nullDefaults() {
        VendorAnalytics e = new VendorAnalytics();
        assertThat(e.getId()).isNull(); assertThat(e.getVendorId()).isNull(); assertThat(e.getName()).isNull();
    }

}
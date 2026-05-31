package com.gogidix.ecommerce.vendor.analytics.application.dto;
import org.junit.jupiter.api.Test; import java.time.Instant; import static org.assertj.core.api.Assertions.assertThat;
class VendorAnalyticsDtoTest {
    @Test void response_allFields() {
        Instant now = Instant.now();
        VendorAnalyticsResponse r = new VendorAnalyticsResponse("id1", "t1", "ACTIVE", now, now);
        assertThat(r.id()).isEqualTo("id1"); assertThat(r.tenantId()).isEqualTo("t1");
        assertThat(r.status()).isEqualTo("ACTIVE"); assertThat(r.createdAt()).isEqualTo(now);
    }
    @Test void response_nulls() { VendorAnalyticsResponse r = new VendorAnalyticsResponse(null, null, null, null, null); assertThat(r.id()).isNull(); }
    @Test void createRequest() { CreateVendorAnalyticsRequest req = new CreateVendorAnalyticsRequest("n1"); assertThat(req.name()).isEqualTo("n1"); }
    @Test void createRequest_null() { CreateVendorAnalyticsRequest req = new CreateVendorAnalyticsRequest(null); assertThat(req.name()).isNull(); }
    @Test void response_equality() {
        Instant now = Instant.now();
        VendorAnalyticsResponse a = new VendorAnalyticsResponse("id", "t", "S", now, now);
        VendorAnalyticsResponse b = new VendorAnalyticsResponse("id", "t", "S", now, now);
        assertThat(a).isEqualTo(b);
    }
}
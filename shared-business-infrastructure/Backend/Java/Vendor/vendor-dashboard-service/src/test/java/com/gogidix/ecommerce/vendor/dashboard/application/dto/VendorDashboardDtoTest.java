package com.gogidix.ecommerce.vendor.dashboard.application.dto;
import org.junit.jupiter.api.Test; import java.time.Instant; import static org.assertj.core.api.Assertions.assertThat;
class VendorDashboardDtoTest {
    @Test void response_allFields() {
        Instant now = Instant.now();
        VendorDashboardResponse r = new VendorDashboardResponse("id1", "t1", "ACTIVE", now, now);
        assertThat(r.id()).isEqualTo("id1"); assertThat(r.tenantId()).isEqualTo("t1");
        assertThat(r.status()).isEqualTo("ACTIVE"); assertThat(r.createdAt()).isEqualTo(now);
    }
    @Test void response_nulls() { VendorDashboardResponse r = new VendorDashboardResponse(null, null, null, null, null); assertThat(r.id()).isNull(); }
    @Test void createRequest() { CreateVendorDashboardRequest req = new CreateVendorDashboardRequest("n1"); assertThat(req.name()).isEqualTo("n1"); }
    @Test void createRequest_null() { CreateVendorDashboardRequest req = new CreateVendorDashboardRequest(null); assertThat(req.name()).isNull(); }
    @Test void response_equality() {
        Instant now = Instant.now();
        VendorDashboardResponse a = new VendorDashboardResponse("id", "t", "S", now, now);
        VendorDashboardResponse b = new VendorDashboardResponse("id", "t", "S", now, now);
        assertThat(a).isEqualTo(b);
    }
}
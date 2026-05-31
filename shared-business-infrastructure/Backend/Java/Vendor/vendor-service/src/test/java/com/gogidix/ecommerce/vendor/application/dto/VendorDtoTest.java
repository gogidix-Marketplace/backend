package com.gogidix.ecommerce.vendor.application.dto;
import org.junit.jupiter.api.Test; import java.time.Instant; import static org.assertj.core.api.Assertions.assertThat;
class VendorDtoTest {
    @Test void response_allFields() {
        Instant now = Instant.now();
        VendorResponse r = new VendorResponse("id1", "t1", "ACTIVE", now, now);
        assertThat(r.id()).isEqualTo("id1"); assertThat(r.tenantId()).isEqualTo("t1");
        assertThat(r.status()).isEqualTo("ACTIVE"); assertThat(r.createdAt()).isEqualTo(now);
    }
    @Test void response_nulls() { VendorResponse r = new VendorResponse(null, null, null, null, null); assertThat(r.id()).isNull(); }
    @Test void createRequest() { CreateVendorRequest req = new CreateVendorRequest("n1"); assertThat(req.name()).isEqualTo("n1"); }
    @Test void createRequest_null() { CreateVendorRequest req = new CreateVendorRequest(null); assertThat(req.name()).isNull(); }
    @Test void response_equality() {
        Instant now = Instant.now();
        VendorResponse a = new VendorResponse("id", "t", "S", now, now);
        VendorResponse b = new VendorResponse("id", "t", "S", now, now);
        assertThat(a).isEqualTo(b);
    }
}
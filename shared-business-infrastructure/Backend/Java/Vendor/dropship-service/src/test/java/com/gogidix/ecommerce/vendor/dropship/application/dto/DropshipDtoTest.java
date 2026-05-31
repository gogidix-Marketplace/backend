package com.gogidix.ecommerce.vendor.dropship.application.dto;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

class DropshipDtoTest {

    @Test void response_allFields() {
        Instant now = Instant.now();
        DropshipResponse r = new DropshipResponse("id1", "t1", "PENDING", now, now);
        assertThat(r.id()).isEqualTo("id1");
        assertThat(r.tenantId()).isEqualTo("t1");
        assertThat(r.status()).isEqualTo("PENDING");
        assertThat(r.createdAt()).isEqualTo(now);
    }
    @Test void response_nulls() {
        DropshipResponse r = new DropshipResponse(null, null, null, null, null);
        assertThat(r.id()).isNull(); assertThat(r.status()).isNull();
    }
    @Test void createRequest() {
        CreateDropshipRequest req = new CreateDropshipRequest("order1");
        assertThat(req.name()).isEqualTo("order1");
    }
    @Test void createRequest_null() {
        CreateDropshipRequest req = new CreateDropshipRequest(null);
        assertThat(req.name()).isNull();
    }
    @Test void response_equality() {
        Instant now = Instant.now();
        DropshipResponse a = new DropshipResponse("id", "t", "S", now, now);
        DropshipResponse b = new DropshipResponse("id", "t", "S", now, now);
        assertThat(a).isEqualTo(b);
    }
}
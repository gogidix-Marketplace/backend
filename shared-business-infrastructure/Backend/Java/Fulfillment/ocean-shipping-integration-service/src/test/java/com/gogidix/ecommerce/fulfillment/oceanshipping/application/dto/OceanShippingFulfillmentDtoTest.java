package com.gogidix.ecommerce.fulfillment.oceanshipping.application.dto;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

class OceanShippingFulfillmentDtoTest {

    @Test void dto_allFields() {
        Instant now = Instant.now();
        OceanShippingFulfillmentDto dto = new OceanShippingFulfillmentDto("id1", "t1", "name", true, now, now);
        assertThat(dto.id()).isEqualTo("id1");
        assertThat(dto.tenantId()).isEqualTo("t1");
        assertThat(dto.name()).isEqualTo("name");
        assertThat(dto.active()).isTrue();
        assertThat(dto.createdAt()).isEqualTo(now);
        assertThat(dto.updatedAt()).isEqualTo(now);
    }
    @Test void dto_nulls() {
        OceanShippingFulfillmentDto dto = new OceanShippingFulfillmentDto(null, null, null, false, null, null);
        assertThat(dto.id()).isNull(); assertThat(dto.active()).isFalse();
    }
    @Test void response_fromDto() {
        OceanShippingFulfillmentDto dto = new OceanShippingFulfillmentDto("id1", "t1", "name1", true, null, null);
        OceanShippingFulfillmentResponse r = OceanShippingFulfillmentResponse.from(dto);
        assertThat(r.id()).isEqualTo("id1"); assertThat(r.name()).isEqualTo("name1"); assertThat(r.active()).isTrue();
    }
    @Test void response_direct() {
        OceanShippingFulfillmentResponse r = new OceanShippingFulfillmentResponse("id2", "n2", false);
        assertThat(r.id()).isEqualTo("id2"); assertThat(r.active()).isFalse();
    }
    @Test void createRequest() { CreateOceanShippingFulfillmentRequest req = new CreateOceanShippingFulfillmentRequest("new"); assertThat(req.name()).isEqualTo("new"); }
    @Test void createRequest_null() { CreateOceanShippingFulfillmentRequest req = new CreateOceanShippingFulfillmentRequest(null); assertThat(req.name()).isNull(); }
    @Test void equality() {
        OceanShippingFulfillmentDto a = new OceanShippingFulfillmentDto("id", "t", "n", true, null, null);
        OceanShippingFulfillmentDto b = new OceanShippingFulfillmentDto("id", "t", "n", true, null, null);
        assertThat(a).isEqualTo(b); assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
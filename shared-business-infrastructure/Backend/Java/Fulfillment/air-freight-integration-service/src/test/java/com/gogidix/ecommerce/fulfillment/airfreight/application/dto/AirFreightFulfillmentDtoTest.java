package com.gogidix.ecommerce.fulfillment.airfreight.application.dto;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

class AirFreightFulfillmentDtoTest {

    @Test void dto_allFields() {
        Instant now = Instant.now();
        AirFreightFulfillmentDto dto = new AirFreightFulfillmentDto("id1", "t1", "name", true, now, now);
        assertThat(dto.id()).isEqualTo("id1");
        assertThat(dto.tenantId()).isEqualTo("t1");
        assertThat(dto.name()).isEqualTo("name");
        assertThat(dto.active()).isTrue();
        assertThat(dto.createdAt()).isEqualTo(now);
        assertThat(dto.updatedAt()).isEqualTo(now);
    }
    @Test void dto_nulls() {
        AirFreightFulfillmentDto dto = new AirFreightFulfillmentDto(null, null, null, false, null, null);
        assertThat(dto.id()).isNull(); assertThat(dto.active()).isFalse();
    }
    @Test void response_fromDto() {
        AirFreightFulfillmentDto dto = new AirFreightFulfillmentDto("id1", "t1", "name1", true, null, null);
        AirFreightFulfillmentResponse r = AirFreightFulfillmentResponse.from(dto);
        assertThat(r.id()).isEqualTo("id1"); assertThat(r.name()).isEqualTo("name1"); assertThat(r.active()).isTrue();
    }
    @Test void response_direct() {
        AirFreightFulfillmentResponse r = new AirFreightFulfillmentResponse("id2", "n2", false);
        assertThat(r.id()).isEqualTo("id2"); assertThat(r.active()).isFalse();
    }
    @Test void createRequest() { CreateAirFreightFulfillmentRequest req = new CreateAirFreightFulfillmentRequest("new"); assertThat(req.name()).isEqualTo("new"); }
    @Test void createRequest_null() { CreateAirFreightFulfillmentRequest req = new CreateAirFreightFulfillmentRequest(null); assertThat(req.name()).isNull(); }
    @Test void equality() {
        AirFreightFulfillmentDto a = new AirFreightFulfillmentDto("id", "t", "n", true, null, null);
        AirFreightFulfillmentDto b = new AirFreightFulfillmentDto("id", "t", "n", true, null, null);
        assertThat(a).isEqualTo(b); assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
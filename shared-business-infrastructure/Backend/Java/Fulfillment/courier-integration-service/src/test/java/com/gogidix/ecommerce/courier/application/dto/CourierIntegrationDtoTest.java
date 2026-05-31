package com.gogidix.ecommerce.courier.application.dto;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

class CourierIntegrationDtoTest {

    @Test void dto_allFields() {
        Instant now = Instant.now();
        CourierIntegrationDto dto = new CourierIntegrationDto("id1", "t1", "name", true, now, now);
        assertThat(dto.id()).isEqualTo("id1");
        assertThat(dto.tenantId()).isEqualTo("t1");
        assertThat(dto.name()).isEqualTo("name");
        assertThat(dto.active()).isTrue();
        assertThat(dto.createdAt()).isEqualTo(now);
        assertThat(dto.updatedAt()).isEqualTo(now);
    }
    @Test void dto_nulls() {
        CourierIntegrationDto dto = new CourierIntegrationDto(null, null, null, false, null, null);
        assertThat(dto.id()).isNull(); assertThat(dto.active()).isFalse();
    }
    @Test void response_fromDto() {
        CourierIntegrationDto dto = new CourierIntegrationDto("id1", "t1", "name1", true, null, null);
        CourierIntegrationResponse r = CourierIntegrationResponse.from(dto);
        assertThat(r.id()).isEqualTo("id1"); assertThat(r.name()).isEqualTo("name1"); assertThat(r.active()).isTrue();
    }
    @Test void response_direct() {
        CourierIntegrationResponse r = new CourierIntegrationResponse("id2", "n2", false);
        assertThat(r.id()).isEqualTo("id2"); assertThat(r.active()).isFalse();
    }
    @Test void createRequest() { CreateCourierIntegrationRequest req = new CreateCourierIntegrationRequest("new"); assertThat(req.name()).isEqualTo("new"); }
    @Test void createRequest_null() { CreateCourierIntegrationRequest req = new CreateCourierIntegrationRequest(null); assertThat(req.name()).isNull(); }
    @Test void equality() {
        CourierIntegrationDto a = new CourierIntegrationDto("id", "t", "n", true, null, null);
        CourierIntegrationDto b = new CourierIntegrationDto("id", "t", "n", true, null, null);
        assertThat(a).isEqualTo(b); assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
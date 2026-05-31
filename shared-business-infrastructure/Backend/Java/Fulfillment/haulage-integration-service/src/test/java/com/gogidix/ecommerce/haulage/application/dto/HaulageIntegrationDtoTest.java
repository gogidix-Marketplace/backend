package com.gogidix.ecommerce.haulage.application.dto;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

class HaulageIntegrationDtoTest {

    @Test void dto_allFields() {
        Instant now = Instant.now();
        HaulageIntegrationDto dto = new HaulageIntegrationDto("id1", "t1", "name", true, now, now);
        assertThat(dto.id()).isEqualTo("id1");
        assertThat(dto.tenantId()).isEqualTo("t1");
        assertThat(dto.name()).isEqualTo("name");
        assertThat(dto.active()).isTrue();
        assertThat(dto.createdAt()).isEqualTo(now);
        assertThat(dto.updatedAt()).isEqualTo(now);
    }
    @Test void dto_nulls() {
        HaulageIntegrationDto dto = new HaulageIntegrationDto(null, null, null, false, null, null);
        assertThat(dto.id()).isNull(); assertThat(dto.active()).isFalse();
    }
    @Test void response_fromDto() {
        HaulageIntegrationDto dto = new HaulageIntegrationDto("id1", "t1", "name1", true, null, null);
        HaulageIntegrationResponse r = HaulageIntegrationResponse.from(dto);
        assertThat(r.id()).isEqualTo("id1"); assertThat(r.name()).isEqualTo("name1"); assertThat(r.active()).isTrue();
    }
    @Test void response_direct() {
        HaulageIntegrationResponse r = new HaulageIntegrationResponse("id2", "n2", false);
        assertThat(r.id()).isEqualTo("id2"); assertThat(r.active()).isFalse();
    }
    @Test void createRequest() { CreateHaulageIntegrationRequest req = new CreateHaulageIntegrationRequest("new"); assertThat(req.name()).isEqualTo("new"); }
    @Test void createRequest_null() { CreateHaulageIntegrationRequest req = new CreateHaulageIntegrationRequest(null); assertThat(req.name()).isNull(); }
    @Test void equality() {
        HaulageIntegrationDto a = new HaulageIntegrationDto("id", "t", "n", true, null, null);
        HaulageIntegrationDto b = new HaulageIntegrationDto("id", "t", "n", true, null, null);
        assertThat(a).isEqualTo(b); assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
package com.gogidix.ecommerce.fulfillment.warehouse.application.dto;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

class WarehouseFulfillmentDtoTest {

    @Test void dto_allFields() {
        Instant now = Instant.now();
        WarehouseFulfillmentDto dto = new WarehouseFulfillmentDto("id1", "t1", "name", true, now, now);
        assertThat(dto.id()).isEqualTo("id1");
        assertThat(dto.tenantId()).isEqualTo("t1");
        assertThat(dto.name()).isEqualTo("name");
        assertThat(dto.active()).isTrue();
        assertThat(dto.createdAt()).isEqualTo(now);
        assertThat(dto.updatedAt()).isEqualTo(now);
    }
    @Test void dto_nulls() {
        WarehouseFulfillmentDto dto = new WarehouseFulfillmentDto(null, null, null, false, null, null);
        assertThat(dto.id()).isNull(); assertThat(dto.active()).isFalse();
    }
    @Test void response_fromDto() {
        WarehouseFulfillmentDto dto = new WarehouseFulfillmentDto("id1", "t1", "name1", true, null, null);
        WarehouseFulfillmentResponse r = WarehouseFulfillmentResponse.from(dto);
        assertThat(r.id()).isEqualTo("id1"); assertThat(r.name()).isEqualTo("name1"); assertThat(r.active()).isTrue();
    }
    @Test void response_direct() {
        WarehouseFulfillmentResponse r = new WarehouseFulfillmentResponse("id2", "n2", false);
        assertThat(r.id()).isEqualTo("id2"); assertThat(r.active()).isFalse();
    }
    @Test void createRequest() { CreateWarehouseFulfillmentRequest req = new CreateWarehouseFulfillmentRequest("new"); assertThat(req.name()).isEqualTo("new"); }
    @Test void createRequest_null() { CreateWarehouseFulfillmentRequest req = new CreateWarehouseFulfillmentRequest(null); assertThat(req.name()).isNull(); }
    @Test void equality() {
        WarehouseFulfillmentDto a = new WarehouseFulfillmentDto("id", "t", "n", true, null, null);
        WarehouseFulfillmentDto b = new WarehouseFulfillmentDto("id", "t", "n", true, null, null);
        assertThat(a).isEqualTo(b); assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
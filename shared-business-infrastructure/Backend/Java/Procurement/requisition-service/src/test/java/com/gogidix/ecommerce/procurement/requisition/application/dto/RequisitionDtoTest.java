package com.gogidix.ecommerce.procurement.requisition.application.dto;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

class RequisitionDtoTest {

    @Test void dto_allFields() {
        Instant now = Instant.now();
        RequisitionDto dto = new RequisitionDto("id1", "t1", "name", true, now, now);
        assertThat(dto.id()).isEqualTo("id1");
        assertThat(dto.tenantId()).isEqualTo("t1");
        assertThat(dto.name()).isEqualTo("name");
        assertThat(dto.active()).isTrue();
        assertThat(dto.createdAt()).isEqualTo(now);
        assertThat(dto.updatedAt()).isEqualTo(now);
    }
    @Test void dto_nullFields() {
        RequisitionDto dto = new RequisitionDto(null, null, null, false, null, null);
        assertThat(dto.id()).isNull(); assertThat(dto.active()).isFalse();
    }
    @Test void response_fromDto() {
        RequisitionDto dto = new RequisitionDto("id1", "t1", "name1", true, null, null);
        RequisitionResponse r = RequisitionResponse.from(dto);
        assertThat(r.id()).isEqualTo("id1"); assertThat(r.name()).isEqualTo("name1"); assertThat(r.active()).isTrue();
    }
    @Test void response_direct() {
        RequisitionResponse r = new RequisitionResponse("id2", "n2", false);
        assertThat(r.id()).isEqualTo("id2"); assertThat(r.active()).isFalse();
    }
    @Test void createRequest_withName() {
        CreateRequisitionRequest req = new CreateRequisitionRequest("new"); assertThat(req.name()).isEqualTo("new");
    }
    @Test void createRequest_null() {
        CreateRequisitionRequest req = new CreateRequisitionRequest(null); assertThat(req.name()).isNull();
    }
    @Test void dto_equality() {
        RequisitionDto a = new RequisitionDto("id", "t", "n", true, null, null);
        RequisitionDto b = new RequisitionDto("id", "t", "n", true, null, null);
        assertThat(a).isEqualTo(b); assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
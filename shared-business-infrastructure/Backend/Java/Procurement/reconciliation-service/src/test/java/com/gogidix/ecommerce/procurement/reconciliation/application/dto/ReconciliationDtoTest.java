package com.gogidix.ecommerce.procurement.reconciliation.application.dto;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

class ReconciliationDtoTest {

    @Test void dto_allFields() {
        Instant now = Instant.now();
        ReconciliationDto dto = new ReconciliationDto("id1", "t1", "name", true, now, now);
        assertThat(dto.id()).isEqualTo("id1");
        assertThat(dto.tenantId()).isEqualTo("t1");
        assertThat(dto.name()).isEqualTo("name");
        assertThat(dto.active()).isTrue();
        assertThat(dto.createdAt()).isEqualTo(now);
        assertThat(dto.updatedAt()).isEqualTo(now);
    }
    @Test void dto_nullFields() {
        ReconciliationDto dto = new ReconciliationDto(null, null, null, false, null, null);
        assertThat(dto.id()).isNull(); assertThat(dto.active()).isFalse();
    }
    @Test void response_fromDto() {
        ReconciliationDto dto = new ReconciliationDto("id1", "t1", "name1", true, null, null);
        ReconciliationResponse r = ReconciliationResponse.from(dto);
        assertThat(r.id()).isEqualTo("id1"); assertThat(r.name()).isEqualTo("name1"); assertThat(r.active()).isTrue();
    }
    @Test void response_direct() {
        ReconciliationResponse r = new ReconciliationResponse("id2", "n2", false);
        assertThat(r.id()).isEqualTo("id2"); assertThat(r.active()).isFalse();
    }
    @Test void createRequest_withName() {
        CreateReconciliationRequest req = new CreateReconciliationRequest("new"); assertThat(req.name()).isEqualTo("new");
    }
    @Test void createRequest_null() {
        CreateReconciliationRequest req = new CreateReconciliationRequest(null); assertThat(req.name()).isNull();
    }
    @Test void dto_equality() {
        ReconciliationDto a = new ReconciliationDto("id", "t", "n", true, null, null);
        ReconciliationDto b = new ReconciliationDto("id", "t", "n", true, null, null);
        assertThat(a).isEqualTo(b); assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
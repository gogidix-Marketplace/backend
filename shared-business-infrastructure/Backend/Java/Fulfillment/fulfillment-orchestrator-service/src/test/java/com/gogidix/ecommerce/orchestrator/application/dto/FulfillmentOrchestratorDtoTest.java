package com.gogidix.ecommerce.orchestrator.application.dto;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

class FulfillmentOrchestratorDtoTest {

    @Test void dto_allFields() {
        Instant now = Instant.now();
        FulfillmentOrchestratorDto dto = new FulfillmentOrchestratorDto("id1", "t1", "name", true, now, now);
        assertThat(dto.id()).isEqualTo("id1");
        assertThat(dto.tenantId()).isEqualTo("t1");
        assertThat(dto.name()).isEqualTo("name");
        assertThat(dto.active()).isTrue();
        assertThat(dto.createdAt()).isEqualTo(now);
        assertThat(dto.updatedAt()).isEqualTo(now);
    }
    @Test void dto_nulls() {
        FulfillmentOrchestratorDto dto = new FulfillmentOrchestratorDto(null, null, null, false, null, null);
        assertThat(dto.id()).isNull(); assertThat(dto.active()).isFalse();
    }
    @Test void response_fromDto() {
        FulfillmentOrchestratorDto dto = new FulfillmentOrchestratorDto("id1", "t1", "name1", true, null, null);
        FulfillmentOrchestratorResponse r = FulfillmentOrchestratorResponse.from(dto);
        assertThat(r.id()).isEqualTo("id1"); assertThat(r.name()).isEqualTo("name1"); assertThat(r.active()).isTrue();
    }
    @Test void response_direct() {
        FulfillmentOrchestratorResponse r = new FulfillmentOrchestratorResponse("id2", "n2", false);
        assertThat(r.id()).isEqualTo("id2"); assertThat(r.active()).isFalse();
    }
    @Test void createRequest() { CreateFulfillmentOrchestratorRequest req = new CreateFulfillmentOrchestratorRequest("new"); assertThat(req.name()).isEqualTo("new"); }
    @Test void createRequest_null() { CreateFulfillmentOrchestratorRequest req = new CreateFulfillmentOrchestratorRequest(null); assertThat(req.name()).isNull(); }
    @Test void equality() {
        FulfillmentOrchestratorDto a = new FulfillmentOrchestratorDto("id", "t", "n", true, null, null);
        FulfillmentOrchestratorDto b = new FulfillmentOrchestratorDto("id", "t", "n", true, null, null);
        assertThat(a).isEqualTo(b); assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
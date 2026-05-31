package com.gogidix.ecommerce.procurement.approval.application.dto;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

class ApprovalWorkflowDtoTest {

    @Test void dto_allFields() {
        Instant now = Instant.now();
        ApprovalWorkflowDto dto = new ApprovalWorkflowDto("id1", "t1", "name", true, now, now);
        assertThat(dto.id()).isEqualTo("id1");
        assertThat(dto.tenantId()).isEqualTo("t1");
        assertThat(dto.name()).isEqualTo("name");
        assertThat(dto.active()).isTrue();
        assertThat(dto.createdAt()).isEqualTo(now);
        assertThat(dto.updatedAt()).isEqualTo(now);
    }
    @Test void dto_nullFields() {
        ApprovalWorkflowDto dto = new ApprovalWorkflowDto(null, null, null, false, null, null);
        assertThat(dto.id()).isNull(); assertThat(dto.active()).isFalse();
    }
    @Test void response_fromDto() {
        ApprovalWorkflowDto dto = new ApprovalWorkflowDto("id1", "t1", "name1", true, null, null);
        ApprovalWorkflowResponse r = ApprovalWorkflowResponse.from(dto);
        assertThat(r.id()).isEqualTo("id1"); assertThat(r.name()).isEqualTo("name1"); assertThat(r.active()).isTrue();
    }
    @Test void response_direct() {
        ApprovalWorkflowResponse r = new ApprovalWorkflowResponse("id2", "n2", false);
        assertThat(r.id()).isEqualTo("id2"); assertThat(r.active()).isFalse();
    }
    @Test void createRequest_withName() {
        CreateApprovalWorkflowRequest req = new CreateApprovalWorkflowRequest("new"); assertThat(req.name()).isEqualTo("new");
    }
    @Test void createRequest_null() {
        CreateApprovalWorkflowRequest req = new CreateApprovalWorkflowRequest(null); assertThat(req.name()).isNull();
    }
    @Test void dto_equality() {
        ApprovalWorkflowDto a = new ApprovalWorkflowDto("id", "t", "n", true, null, null);
        ApprovalWorkflowDto b = new ApprovalWorkflowDto("id", "t", "n", true, null, null);
        assertThat(a).isEqualTo(b); assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
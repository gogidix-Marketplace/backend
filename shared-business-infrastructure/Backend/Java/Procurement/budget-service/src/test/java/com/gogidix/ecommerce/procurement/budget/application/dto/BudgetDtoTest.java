package com.gogidix.ecommerce.procurement.budget.application.dto;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

class BudgetDtoTest {

    @Test void dto_allFields() {
        Instant now = Instant.now();
        BudgetDto dto = new BudgetDto("id1", "t1", "name", true, now, now);
        assertThat(dto.id()).isEqualTo("id1");
        assertThat(dto.tenantId()).isEqualTo("t1");
        assertThat(dto.name()).isEqualTo("name");
        assertThat(dto.active()).isTrue();
        assertThat(dto.createdAt()).isEqualTo(now);
        assertThat(dto.updatedAt()).isEqualTo(now);
    }
    @Test void dto_nullFields() {
        BudgetDto dto = new BudgetDto(null, null, null, false, null, null);
        assertThat(dto.id()).isNull(); assertThat(dto.active()).isFalse();
    }
    @Test void response_fromDto() {
        BudgetDto dto = new BudgetDto("id1", "t1", "name1", true, null, null);
        BudgetResponse r = BudgetResponse.from(dto);
        assertThat(r.id()).isEqualTo("id1"); assertThat(r.name()).isEqualTo("name1"); assertThat(r.active()).isTrue();
    }
    @Test void response_direct() {
        BudgetResponse r = new BudgetResponse("id2", "n2", false);
        assertThat(r.id()).isEqualTo("id2"); assertThat(r.active()).isFalse();
    }
    @Test void createRequest_withName() {
        CreateBudgetRequest req = new CreateBudgetRequest("new"); assertThat(req.name()).isEqualTo("new");
    }
    @Test void createRequest_null() {
        CreateBudgetRequest req = new CreateBudgetRequest(null); assertThat(req.name()).isNull();
    }
    @Test void dto_equality() {
        BudgetDto a = new BudgetDto("id", "t", "n", true, null, null);
        BudgetDto b = new BudgetDto("id", "t", "n", true, null, null);
        assertThat(a).isEqualTo(b); assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
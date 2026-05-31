package com.gogidix.ecommerce.inventory.sync.application.dto;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

class InventorySyncDtoTest {

    @Test void dto_allFields() {
        Instant now = Instant.now();
        InventorySyncDto dto = new InventorySyncDto("id1", "t1", "name", "desc", true, now, now);
        assertThat(dto.id()).isEqualTo("id1");
        assertThat(dto.tenantId()).isEqualTo("t1");
        assertThat(dto.name()).isEqualTo("name");
        assertThat(dto.description()).isEqualTo("desc");
        assertThat(dto.active()).isTrue();
    }
    @Test void dto_nulls() {
        InventorySyncDto dto = new InventorySyncDto(null, null, null, null, false, null, null);
        assertThat(dto.id()).isNull(); assertThat(dto.active()).isFalse();
    }
    @Test void response_fromDto() {
        InventorySyncDto dto = new InventorySyncDto("id1", "t1", "name1", "desc1", true, null, null);
        InventorySyncResponse r = InventorySyncResponse.from(dto);
        assertThat(r.id()).isEqualTo("id1"); assertThat(r.name()).isEqualTo("name1");
        assertThat(r.description()).isEqualTo("desc1"); assertThat(r.active()).isTrue();
    }
    @Test void response_direct() {
        InventorySyncResponse r = new InventorySyncResponse("id2", "n2", "d2", false);
        assertThat(r.id()).isEqualTo("id2"); assertThat(r.active()).isFalse();
    }
    @Test void createRequest() { CreateInventorySyncRequest req = new CreateInventorySyncRequest("n", "d"); assertThat(req.name()).isEqualTo("n"); assertThat(req.description()).isEqualTo("d"); }
    @Test void createRequest_nulls() { CreateInventorySyncRequest req = new CreateInventorySyncRequest(null, null); assertThat(req.name()).isNull(); }
    @Test void equality() {
        InventorySyncDto a = new InventorySyncDto("id", "t", "n", "d", true, null, null);
        InventorySyncDto b = new InventorySyncDto("id", "t", "n", "d", true, null, null);
        assertThat(a).isEqualTo(b); assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
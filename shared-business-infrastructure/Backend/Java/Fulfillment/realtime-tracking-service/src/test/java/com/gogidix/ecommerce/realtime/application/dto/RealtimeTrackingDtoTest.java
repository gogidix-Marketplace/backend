package com.gogidix.ecommerce.realtime.application.dto;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

class RealtimeTrackingDtoTest {

    @Test void dto_allFields() {
        Instant now = Instant.now();
        RealtimeTrackingDto dto = new RealtimeTrackingDto("id1", "t1", "name", true, now, now);
        assertThat(dto.id()).isEqualTo("id1");
        assertThat(dto.tenantId()).isEqualTo("t1");
        assertThat(dto.name()).isEqualTo("name");
        assertThat(dto.active()).isTrue();
        assertThat(dto.createdAt()).isEqualTo(now);
    }
    @Test void dto_nulls() {
        RealtimeTrackingDto dto = new RealtimeTrackingDto(null, null, null, false, null, null);
        assertThat(dto.id()).isNull(); assertThat(dto.active()).isFalse();
    }
    @Test void response_fromDto() {
        RealtimeTrackingDto dto = new RealtimeTrackingDto("id1", "t1", "name1", true, null, null);
        RealtimeTrackingResponse r = RealtimeTrackingResponse.from(dto);
        assertThat(r.id()).isEqualTo("id1"); assertThat(r.name()).isEqualTo("name1"); assertThat(r.active()).isTrue();
    }
    @Test void response_direct() {
        RealtimeTrackingResponse r = new RealtimeTrackingResponse("id2", "n2", false);
        assertThat(r.id()).isEqualTo("id2"); assertThat(r.active()).isFalse();
    }
    @Test void createRequest() {
        CreateRealtimeTrackingRequest req = new CreateRealtimeTrackingRequest("new");
        assertThat(req.name()).isEqualTo("new");
    }
    @Test void createRequest_null() {
        CreateRealtimeTrackingRequest req = new CreateRealtimeTrackingRequest(null);
        assertThat(req.name()).isNull();
    }
    @Test void dto_equality() {
        RealtimeTrackingDto a = new RealtimeTrackingDto("id", "t", "n", true, null, null);
        RealtimeTrackingDto b = new RealtimeTrackingDto("id", "t", "n", true, null, null);
        assertThat(a).isEqualTo(b); assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
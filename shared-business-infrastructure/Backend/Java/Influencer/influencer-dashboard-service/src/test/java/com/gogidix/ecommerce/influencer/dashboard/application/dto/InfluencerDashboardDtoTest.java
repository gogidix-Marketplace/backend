package com.gogidix.ecommerce.influencer.dashboard.application.dto;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

class InfluencerDashboardDtoTest {

    @Test void dto_allFields() {
        Instant now = Instant.now();
        InfluencerDashboardDto dto = new InfluencerDashboardDto("id1", "t1", "name", "desc", true, now, now);
        assertThat(dto.id()).isEqualTo("id1");
        assertThat(dto.tenantId()).isEqualTo("t1");
        assertThat(dto.name()).isEqualTo("name");
        assertThat(dto.description()).isEqualTo("desc");
        assertThat(dto.active()).isTrue();
    }
    @Test void dto_nulls() {
        InfluencerDashboardDto dto = new InfluencerDashboardDto(null, null, null, null, false, null, null);
        assertThat(dto.id()).isNull(); assertThat(dto.active()).isFalse();
    }
    @Test void response_fromDto() {
        InfluencerDashboardDto dto = new InfluencerDashboardDto("id1", "t1", "name1", "desc1", true, null, null);
        InfluencerDashboardResponse r = InfluencerDashboardResponse.from(dto);
        assertThat(r.id()).isEqualTo("id1"); assertThat(r.name()).isEqualTo("name1");
        assertThat(r.description()).isEqualTo("desc1"); assertThat(r.active()).isTrue();
    }
    @Test void response_direct() {
        InfluencerDashboardResponse r = new InfluencerDashboardResponse("id2", "n2", "d2", false);
        assertThat(r.id()).isEqualTo("id2"); assertThat(r.active()).isFalse();
    }
    @Test void createRequest() { CreateInfluencerDashboardRequest req = new CreateInfluencerDashboardRequest("n", "d"); assertThat(req.name()).isEqualTo("n"); assertThat(req.description()).isEqualTo("d"); }
    @Test void createRequest_nulls() { CreateInfluencerDashboardRequest req = new CreateInfluencerDashboardRequest(null, null); assertThat(req.name()).isNull(); }
    @Test void equality() {
        InfluencerDashboardDto a = new InfluencerDashboardDto("id", "t", "n", "d", true, null, null);
        InfluencerDashboardDto b = new InfluencerDashboardDto("id", "t", "n", "d", true, null, null);
        assertThat(a).isEqualTo(b); assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
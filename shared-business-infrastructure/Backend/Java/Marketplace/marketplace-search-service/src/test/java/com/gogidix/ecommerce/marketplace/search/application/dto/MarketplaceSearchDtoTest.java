package com.gogidix.ecommerce.marketplace.search.application.dto;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

class MarketplaceSearchDtoTest {

    @Test void dto_allFields() {
        Instant now = Instant.now();
        MarketplaceSearchDto dto = new MarketplaceSearchDto("id1", "t1", "name", "desc", true, now, now);
        assertThat(dto.id()).isEqualTo("id1");
        assertThat(dto.tenantId()).isEqualTo("t1");
        assertThat(dto.name()).isEqualTo("name");
        assertThat(dto.description()).isEqualTo("desc");
        assertThat(dto.active()).isTrue();
        assertThat(dto.createdAt()).isEqualTo(now);
        assertThat(dto.updatedAt()).isEqualTo(now);
    }

    @Test void dto_nullFields() {
        MarketplaceSearchDto dto = new MarketplaceSearchDto(null, null, null, null, false, null, null);
        assertThat(dto.id()).isNull();
        assertThat(dto.active()).isFalse();
    }

    @Test void response_fromDto() {
        MarketplaceSearchDto dto = new MarketplaceSearchDto("id1", "t1", "name1", "desc1", true, null, null);
        MarketplaceSearchResponse r = MarketplaceSearchResponse.from(dto);
        assertThat(r.id()).isEqualTo("id1");
        assertThat(r.name()).isEqualTo("name1");
        assertThat(r.description()).isEqualTo("desc1");
        assertThat(r.active()).isTrue();
    }

    @Test void response_direct() {
        MarketplaceSearchResponse r = new MarketplaceSearchResponse("id2", "n2", "d2", false);
        assertThat(r.id()).isEqualTo("id2");
        assertThat(r.active()).isFalse();
    }

    @Test void createRequest() {
        CreateMarketplaceSearchRequest req = new CreateMarketplaceSearchRequest("name", "desc");
        assertThat(req.name()).isEqualTo("name");
        assertThat(req.description()).isEqualTo("desc");
    }

    @Test void createRequest_nulls() {
        CreateMarketplaceSearchRequest req = new CreateMarketplaceSearchRequest(null, null);
        assertThat(req.name()).isNull();
    }

    @Test void updateRequest() {
        UpdateMarketplaceSearchRequest req = new UpdateMarketplaceSearchRequest("n", "d", true);
        assertThat(req.name()).isEqualTo("n");
        assertThat(req.description()).isEqualTo("d");
        assertThat(req.active()).isTrue();
    }

    @Test void updateRequest_nulls() {
        UpdateMarketplaceSearchRequest req = new UpdateMarketplaceSearchRequest(null, null, false);
        assertThat(req.name()).isNull();
        assertThat(req.active()).isFalse();
    }

    @Test void dto_equality() {
        MarketplaceSearchDto a = new MarketplaceSearchDto("id", "t", "n", "d", true, null, null);
        MarketplaceSearchDto b = new MarketplaceSearchDto("id", "t", "n", "d", true, null, null);
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
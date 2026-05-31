package com.gogidix.ecommerce.vendor.dropship.application.mapper;

import com.gogidix.ecommerce.vendor.dropship.application.dto.CreateDropshipRequest;
import com.gogidix.ecommerce.vendor.dropship.application.dto.DropshipResponse;
import com.gogidix.ecommerce.vendor.dropship.domain.model.Dropship;
import com.gogidix.ecommerce.vendor.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.vendor.shared.requestcontext.RequestContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

class DropshipMapperTest {

    private DropshipMapper mapper = new DropshipMapper();

    @BeforeEach void setUp() {
        RequestContextHolder.set(RequestContext.builder().tenantId("t1").customerId("c1").build());
    }
    @AfterEach void tearDown() { RequestContextHolder.clear(); }

    @Test void toResponse() {
        Dropship d = new Dropship();
        d.setId("id1"); d.setTenantId("t1");
        d.setCreatedAt(Instant.now()); d.setUpdatedAt(Instant.now());
        DropshipResponse r = mapper.toResponse(d);
        assertThat(r.id()).isEqualTo("id1");
        assertThat(r.tenantId()).isEqualTo("t1");
    }

    @Test void toEntity() {
        CreateDropshipRequest req = new CreateDropshipRequest("order1");
        Dropship d = mapper.toEntity(req);
        assertThat(d.getTenantId()).isEqualTo("t1");
        assertThat(d.getName()).isEqualTo("order1");
    }
}
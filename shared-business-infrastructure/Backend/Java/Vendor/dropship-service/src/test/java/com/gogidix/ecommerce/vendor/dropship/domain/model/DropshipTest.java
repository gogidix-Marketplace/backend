package com.gogidix.ecommerce.vendor.dropship.domain.model;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

class DropshipTest {

    @Test void allFields() {
        Dropship d = new Dropship();
        d.setId("id1"); d.setTenantId("t1"); d.setVendorId("v1");
        d.setName("Order1"); d.setStatus(Dropship.DropshipStatus.PENDING);
        d.setCreatedAt(Instant.now()); d.setUpdatedAt(Instant.now());
        assertThat(d.getId()).isEqualTo("id1");
        assertThat(d.getTenantId()).isEqualTo("t1");
        assertThat(d.getVendorId()).isEqualTo("v1");
        assertThat(d.getName()).isEqualTo("Order1");
        assertThat(d.getStatus()).isEqualTo(Dropship.DropshipStatus.PENDING);
    }

    @Test void nullDefaults() {
        Dropship d = new Dropship();
        assertThat(d.getId()).isNull(); assertThat(d.getTenantId()).isNull();
        assertThat(d.getStatus()).isNull(); assertThat(d.getName()).isNull();
    }

    @Test void statusEnum() {
        assertThat(Dropship.DropshipStatus.values()).hasSize(6);
        assertThat(Dropship.DropshipStatus.valueOf("SHIPPED")).isEqualTo(Dropship.DropshipStatus.SHIPPED);
    }
}
package com.gogidix.ecommerce.inventory.sync.application.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateInventorySyncRequestTest {

    @Test
    void create() {
        UpdateInventorySyncRequest req = new UpdateInventorySyncRequest("name", "desc", true);
        assertThat(req.name()).isEqualTo("name");
        assertThat(req.description()).isEqualTo("desc");
        assertThat(req.active()).isTrue();
    }
}

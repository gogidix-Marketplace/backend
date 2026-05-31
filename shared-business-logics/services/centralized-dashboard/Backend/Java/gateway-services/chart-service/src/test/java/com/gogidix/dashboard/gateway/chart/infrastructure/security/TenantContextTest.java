package com.gogidix.dashboard.gateway.chart.infrastructure.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TenantContextTest {

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void setAndGetTenantId() {
        TenantContext.setTenantId("my-tenant");
        assertEquals("my-tenant", TenantContext.getTenantId());
    }

    @Test
    void getTenantId_whenNotSet_returnsNull() {
        assertNull(TenantContext.getTenantId());
    }

    @Test
    void clear_removesTenantId() {
        TenantContext.setTenantId("tenant-x");
        assertNotNull(TenantContext.getTenantId());

        TenantContext.clear();
        assertNull(TenantContext.getTenantId());
    }

    @Test
    void setTenantId_overwritesPrevious() {
        TenantContext.setTenantId("first");
        TenantContext.setTenantId("second");
        assertEquals("second", TenantContext.getTenantId());
    }
}

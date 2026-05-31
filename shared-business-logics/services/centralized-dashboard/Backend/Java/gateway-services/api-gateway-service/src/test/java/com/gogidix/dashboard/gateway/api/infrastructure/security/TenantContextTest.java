package com.gogidix.dashboard.gateway.api.infrastructure.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TenantContextTest {

    @BeforeEach
    @AfterEach
    void clearContext() {
        TenantContext.clear();
    }

    @Nested
    @DisplayName("ThreadLocal tests")
    class ThreadLocalTests {
        @Test
        void setTenantId_andGetTenantId_work() {
            TenantContext.setTenantId("t1");
            assertEquals("t1", TenantContext.getTenantId());
        }

        @Test
        void getTenantId_whenNotSet_returnsNull() {
            assertNull(TenantContext.getTenantId());
        }

        @Test
        void clear_removesTenantId() {
            TenantContext.setTenantId("t1");
            TenantContext.clear();
            assertNull(TenantContext.getTenantId());
        }
    }
}

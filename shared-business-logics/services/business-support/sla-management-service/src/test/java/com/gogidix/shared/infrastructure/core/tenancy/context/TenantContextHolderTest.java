package com.gogidix.shared.infrastructure.core.tenancy.context;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TenantContextHolder Tests")
class TenantContextHolderTest {

    @AfterEach
    void tearDown() {
        TenantContextHolder.clear();
    }

    @Test
    @DisplayName("Should set and get tenant ID")
    void shouldSetAndGetTenantId() {
        TenantContextHolder.setTenantId("tenant-123");
        assertEquals("tenant-123", TenantContextHolder.getTenantId());
    }

    @Test
    @DisplayName("Should return null when no tenant ID set")
    void shouldReturnNullWhenNoTenantIdSet() {
        assertNull(TenantContextHolder.getTenantId());
    }

    @Test
    @DisplayName("Should clear tenant ID")
    void shouldClearTenantId() {
        TenantContextHolder.setTenantId("tenant-123");
        TenantContextHolder.clear();
        assertNull(TenantContextHolder.getTenantId());
    }

    @Test
    @DisplayName("Should return true when tenant ID is set")
    void shouldReturnTrueWhenTenantIdIsSet() {
        TenantContextHolder.setTenantId("tenant-123");
        assertTrue(TenantContextHolder.hasTenantId());
    }

    @Test
    @DisplayName("Should return false when no tenant ID is set")
    void shouldReturnFalseWhenNoTenantIdIsSet() {
        assertFalse(TenantContextHolder.hasTenantId());
    }

    @Test
    @DisplayName("Should return required tenant ID when set")
    void shouldReturnRequiredTenantIdWhenSet() {
        TenantContextHolder.setTenantId("tenant-123");
        assertEquals("tenant-123", TenantContextHolder.getRequiredTenantId());
    }

    @Test
    @DisplayName("Should throw exception when required tenant ID not set")
    void shouldThrowExceptionWhenRequiredTenantIdNotSet() {
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                TenantContextHolder::getRequiredTenantId);
        assertTrue(ex.getMessage().contains("Tenant ID not set"));
    }

    @Test
    @DisplayName("Should handle multiple threads independently")
    void shouldHandleMultipleThreadsIndependently() throws Exception {
        TenantContextHolder.setTenantId("main-tenant");

        Thread t1 = new Thread(() -> {
            TenantContextHolder.setTenantId("thread-1-tenant");
            assertEquals("thread-1-tenant", TenantContextHolder.getTenantId());
        });
        t1.start();
        t1.join();

        assertEquals("main-tenant", TenantContextHolder.getTenantId());
    }

    @Test
    @DisplayName("Should overwrite existing tenant ID")
    void shouldOverwriteExistingTenantId() {
        TenantContextHolder.setTenantId("tenant-1");
        TenantContextHolder.setTenantId("tenant-2");
        assertEquals("tenant-2", TenantContextHolder.getTenantId());
    }
}

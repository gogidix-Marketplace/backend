package com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TenantNotFoundExceptionTest {

    @Test
    void shouldCreateWithTenantId() {
        TenantNotFoundException ex = new TenantNotFoundException("t1");
        assertTrue(ex.getMessage().contains("t1"));
    }

    @Test
    void shouldBeRuntimeException() {
        assertInstanceOf(RuntimeException.class, new TenantNotFoundException("x"));
    }
}

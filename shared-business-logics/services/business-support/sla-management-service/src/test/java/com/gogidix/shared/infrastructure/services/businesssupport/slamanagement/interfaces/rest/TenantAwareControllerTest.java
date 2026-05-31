package com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.interfaces.rest;

import com.gogidix.shared.multitenancy.context.TenantContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("TenantAwareController Tests")
class TenantAwareControllerTest {

    private TestTenantAwareController controller;

    static class TestTenantAwareController extends TenantAwareController {
    }

    @BeforeEach
    void setUp() {
        controller = new TestTenantAwareController();
        TenantContextHolder.clear();
    }

    @AfterEach
    void tearDown() {
        TenantContextHolder.clear();
    }

    @Test
    @DisplayName("Should validate tenant ID from header")
    void shouldValidateTenantIdFromHeader() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-Tenant-ID")).thenReturn("tenant-123");

        String result = controller.validateTenantId(request);

        assertEquals("tenant-123", result);
    }

    @Test
    @DisplayName("Should throw exception when tenant ID header is missing")
    void shouldThrowExceptionWhenTenantIdHeaderIsMissing() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-Tenant-ID")).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> controller.validateTenantId(request));
        assertTrue(ex.getMessage().contains("X-Tenant-ID"));
    }

    @Test
    @DisplayName("Should throw exception when tenant ID header is empty")
    void shouldThrowExceptionWhenTenantIdHeaderIsEmpty() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-Tenant-ID")).thenReturn("");

        assertThrows(IllegalArgumentException.class,
                () -> controller.validateTenantId(request));
    }

    @Test
    @DisplayName("Should trim tenant ID from header")
    void shouldTrimTenantIdFromHeader() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-Tenant-ID")).thenReturn("  tenant-123  ");

        String result = controller.validateTenantId(request);

        assertEquals("tenant-123", result);
    }

    @Test
    @DisplayName("Should get tenant ID from context")
    void shouldGetTenantIdFromContext() {
        TenantContextHolder.setTenantId("ctx-tenant");

        String result = controller.getTenantId();

        assertEquals("ctx-tenant", result);
    }

    @Test
    @DisplayName("Should throw exception when tenant ID not in context")
    void shouldThrowExceptionWhenTenantIdNotInContext() {
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> controller.getTenantId());
        assertTrue(ex.getMessage().contains("Tenant ID not set"));
    }
}

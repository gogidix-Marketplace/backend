package com.gogidix.shared.infrastructure.core.tenancy.interceptor;

import com.gogidix.shared.infrastructure.core.tenancy.context.TenantContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("TenantInterceptor Tests")
class TenantInterceptorTest {

    private TenantInterceptor interceptor;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        interceptor = new TenantInterceptor();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        TenantContextHolder.clear();
    }

    @AfterEach
    void tearDown() {
        TenantContextHolder.clear();
    }

    @Test
    @DisplayName("Should set tenant from header")
    void shouldSetTenantFromHeader() throws Exception {
        when(request.getHeader("X-Tenant-ID")).thenReturn("my-tenant");

        boolean result = interceptor.preHandle(request, response, new Object());

        assertTrue(result);
        assertEquals("my-tenant", TenantContextHolder.getTenantId());
    }

    @Test
    @DisplayName("Should use default tenant when header is null")
    void shouldUseDefaultTenantWhenHeaderIsNull() throws Exception {
        when(request.getHeader("X-Tenant-ID")).thenReturn(null);

        interceptor.preHandle(request, response, new Object());

        assertEquals("default", TenantContextHolder.getTenantId());
    }

    @Test
    @DisplayName("Should use default tenant when header is empty")
    void shouldUseDefaultTenantWhenHeaderIsEmpty() throws Exception {
        when(request.getHeader("X-Tenant-ID")).thenReturn("");

        interceptor.preHandle(request, response, new Object());

        assertEquals("default", TenantContextHolder.getTenantId());
    }

    @Test
    @DisplayName("Should use default tenant when header is blank")
    void shouldUseDefaultTenantWhenHeaderIsBlank() throws Exception {
        when(request.getHeader("X-Tenant-ID")).thenReturn("   ");

        interceptor.preHandle(request, response, new Object());

        assertEquals("default", TenantContextHolder.getTenantId());
    }

    @Test
    @DisplayName("Should clear tenant context after completion")
    void shouldClearTenantContextAfterCompletion() throws Exception {
        TenantContextHolder.setTenantId("my-tenant");

        interceptor.afterCompletion(request, response, new Object(), null);

        assertNull(TenantContextHolder.getTenantId());
    }

    @Test
    @DisplayName("Should clear tenant context after completion with exception")
    void shouldClearTenantContextAfterCompletionWithException() throws Exception {
        TenantContextHolder.setTenantId("my-tenant");

        interceptor.afterCompletion(request, response, new Object(), new RuntimeException("test"));

        assertNull(TenantContextHolder.getTenantId());
    }
}

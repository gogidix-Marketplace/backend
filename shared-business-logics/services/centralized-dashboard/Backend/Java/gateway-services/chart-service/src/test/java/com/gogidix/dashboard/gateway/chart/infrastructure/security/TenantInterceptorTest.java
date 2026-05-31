package com.gogidix.dashboard.gateway.chart.infrastructure.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

class TenantInterceptorTest {

    private final TenantInterceptor interceptor = new TenantInterceptor();

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void preHandle_withTenantHeader_setsTenantContext() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Tenant-ID", "tenant-abc");
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean result = interceptor.preHandle(request, response, new Object());

        assertTrue(result);
        assertEquals("tenant-abc", TenantContext.getTenantId());
    }

    @Test
    void preHandle_withoutTenantHeader_usesDefault() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        interceptor.preHandle(request, response, new Object());

        assertEquals("default", TenantContext.getTenantId());
    }

    @Test
    void preHandle_withEmptyTenantHeader_usesDefault() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Tenant-ID", "");
        MockHttpServletResponse response = new MockHttpServletResponse();

        interceptor.preHandle(request, response, new Object());

        assertEquals("default", TenantContext.getTenantId());
    }

    @Test
    void afterCompletion_clearsTenantContext() throws Exception {
        TenantContext.setTenantId("some-tenant");
        assertNotNull(TenantContext.getTenantId());

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        interceptor.afterCompletion(request, response, new Object(), null);

        assertNull(TenantContext.getTenantId());
    }

    @Test
    void afterCompletion_withException_stillClearsContext() throws Exception {
        TenantContext.setTenantId("tenant-x");

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        interceptor.afterCompletion(request, response, new Object(), new RuntimeException("test"));

        assertNull(TenantContext.getTenantId());
    }
}

package com.gogidix.dashboard.gateway.api.infrastructure.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TenantInterceptorTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private TenantInterceptor interceptor;

    @BeforeEach
    @AfterEach
    void clearContext() {
        TenantContext.clear();
    }

    @Nested
    @DisplayName("preHandle tests")
    class PreHandleTests {
        @Test
        void preHandle_withTenantHeader_setsContext() throws Exception {
            when(request.getHeader("X-Tenant-ID")).thenReturn("my-tenant");
            assertTrue(interceptor.preHandle(request, response, new Object()));
            assertEquals("my-tenant", TenantContext.getTenantId());
        }

        @Test
        void preHandle_withoutTenantHeader_usesDefault() throws Exception {
            when(request.getHeader("X-Tenant-ID")).thenReturn(null);
            assertTrue(interceptor.preHandle(request, response, new Object()));
            assertEquals("default", TenantContext.getTenantId());
        }

        @Test
        void preHandle_withEmptyTenantHeader_usesDefault() throws Exception {
            when(request.getHeader("X-Tenant-ID")).thenReturn("");
            assertTrue(interceptor.preHandle(request, response, new Object()));
            assertEquals("default", TenantContext.getTenantId());
        }
    }

    @Nested
    @DisplayName("afterCompletion tests")
    class AfterCompletionTests {
        @Test
        void afterCompletion_clearsContext() throws Exception {
            TenantContext.setTenantId("t1");
            interceptor.afterCompletion(request, response, new Object(), null);
            assertNull(TenantContext.getTenantId());
        }

        @Test
        void afterCompletion_withException_clearsContext() throws Exception {
            TenantContext.setTenantId("t1");
            interceptor.afterCompletion(request, response, new Object(), new RuntimeException("test"));
            assertNull(TenantContext.getTenantId());
        }
    }
}

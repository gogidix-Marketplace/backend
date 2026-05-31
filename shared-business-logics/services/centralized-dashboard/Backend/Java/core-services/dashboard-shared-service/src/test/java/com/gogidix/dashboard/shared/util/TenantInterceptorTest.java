package com.gogidix.dashboard.shared.util;

import com.gogidix.dashboard.shared.dto.TenantContext;
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
    void setUp() {
        TenantContext.clearContext();
    }

    @AfterEach
    void tearDown() {
        TenantContext.clearContext();
    }

    @Nested
    @DisplayName("preHandle tests")
    class PreHandleTests {

        @Test
        void preHandle_withTenantHeader_setsContext() throws Exception {
            when(request.getHeader("X-Tenant-ID")).thenReturn("tenant-1");
            when(request.getHeader("X-Correlation-ID")).thenReturn("corr-1");

            boolean result = interceptor.preHandle(request, response, new Object());

            assertTrue(result);
            assertNotNull(TenantContext.getContext());
            assertEquals("tenant-1", TenantContext.getCurrentTenantId());
            assertEquals("corr-1", TenantContext.getContext().getCorrelationId());
        }

        @Test
        void preHandle_withoutTenantHeader_usesDefault() throws Exception {
            when(request.getHeader("X-Tenant-ID")).thenReturn(null);
            when(request.getHeader("X-Correlation-ID")).thenReturn("corr-1");

            interceptor.preHandle(request, response, new Object());

            assertEquals("default", TenantContext.getCurrentTenantId());
        }

        @Test
        void preHandle_withEmptyTenantHeader_usesDefault() throws Exception {
            when(request.getHeader("X-Tenant-ID")).thenReturn("");
            when(request.getHeader("X-Correlation-ID")).thenReturn("corr-1");

            interceptor.preHandle(request, response, new Object());

            assertEquals("default", TenantContext.getCurrentTenantId());
        }

        @Test
        void preHandle_withoutCorrelationHeader_generatesOne() throws Exception {
            when(request.getHeader("X-Tenant-ID")).thenReturn("t1");
            when(request.getHeader("X-Correlation-ID")).thenReturn(null);

            interceptor.preHandle(request, response, new Object());

            assertNotNull(TenantContext.getContext().getCorrelationId());
            assertFalse(TenantContext.getContext().getCorrelationId().isEmpty());
        }

        @Test
        void preHandle_setsRequestTimestamp() throws Exception {
            when(request.getHeader("X-Tenant-ID")).thenReturn("t1");
            when(request.getHeader("X-Correlation-ID")).thenReturn("c1");

            interceptor.preHandle(request, response, new Object());

            assertNotNull(TenantContext.getContext().getRequestTimestamp());
        }
    }

    @Nested
    @DisplayName("afterCompletion tests")
    class AfterCompletionTests {

        @Test
        void afterCompletion_clearsContext() throws Exception {
            TenantContext.setContext(TenantContext.builder("t1").build());
            assertNotNull(TenantContext.getContext());

            interceptor.afterCompletion(request, response, new Object(), null);

            assertNull(TenantContext.getContext());
        }

        @Test
        void afterCompletion_withException_clearsContext() throws Exception {
            TenantContext.setContext(TenantContext.builder("t1").build());

            interceptor.afterCompletion(request, response, new Object(), new RuntimeException("test"));

            assertNull(TenantContext.getContext());
        }
    }
}

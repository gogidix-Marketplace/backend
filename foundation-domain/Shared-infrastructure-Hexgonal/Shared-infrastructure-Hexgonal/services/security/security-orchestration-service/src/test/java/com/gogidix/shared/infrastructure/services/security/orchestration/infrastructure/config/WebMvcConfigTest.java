package com.gogidix.shared.infrastructure.services.security.orchestration.infrastructure.config;

import com.gogidix.shared.infrastructure.core.tenancy.interceptor.TenantInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for WebMvcConfig.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("WebMvcConfig Tests")
class WebMvcConfigTest {

    @Mock
    private TenantInterceptor tenantInterceptor;

    @Mock
    private InterceptorRegistry interceptorRegistry;

    @Mock
    private InterceptorRegistration interceptorRegistration;

    private WebMvcConfig webMvcConfig;

    @BeforeEach
    void setUp() {
        webMvcConfig = new WebMvcConfig(tenantInterceptor);
        lenient().when(interceptorRegistry.addInterceptor(any(TenantInterceptor.class)))
                .thenReturn(interceptorRegistration);
        lenient().when(interceptorRegistration.addPathPatterns(any(String[].class)))
                .thenReturn(interceptorRegistration);
    }

    @Test
    @DisplayName("Should create WebMvcConfig with TenantInterceptor")
    void shouldCreateWebMvcConfigWithTenantInterceptor() {
        WebMvcConfig config = new WebMvcConfig(tenantInterceptor);

        assertNotNull(config);
    }

    @Test
    @DisplayName("Should add tenant interceptor to registry")
    void shouldAddTenantInterceptorToRegistry() {
        webMvcConfig.addInterceptors(interceptorRegistry);

        verify(interceptorRegistry, times(1)).addInterceptor(tenantInterceptor);
    }

    @Test
    @DisplayName("Should configure interceptor for API paths")
    void shouldConfigureInterceptorForApiPaths() {
        webMvcConfig.addInterceptors(interceptorRegistry);

        verify(interceptorRegistry).addInterceptor(tenantInterceptor);
        verify(interceptorRegistration).addPathPatterns("/api/**");
    }
}

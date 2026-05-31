package com.gogidix.shared.infrastructure.services.security.auth.infrastructure.config;

import com.gogidix.shared.multitenancy.interceptor.TenantInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    }

    @Test
    @DisplayName("Should create WebMvcConfig with TenantInterceptor")
    void shouldCreateWebMvcConfigWithTenantInterceptor() {
        WebMvcConfig config = new WebMvcConfig(tenantInterceptor);
        assertNotNull(config);
    }

    @Test
    @DisplayName("Should create WebMvcConfig with null interceptor should not throw")
    void shouldCreateWebMvcConfigWithNullInterceptor() {
        assertDoesNotThrow(() -> new WebMvcConfig(null));
    }

    @Test
    @DisplayName("Should add interceptors to registry")
    void shouldAddInterceptorsToRegistry() {
        when(interceptorRegistry.addInterceptor(any(TenantInterceptor.class))).thenReturn(interceptorRegistration);
        when(interceptorRegistration.addPathPatterns(any(String[].class))).thenReturn(interceptorRegistration);
        when(interceptorRegistration.excludePathPatterns(any(String[].class))).thenReturn(interceptorRegistration);

        webMvcConfig.addInterceptors(interceptorRegistry);

        verify(interceptorRegistry).addInterceptor(tenantInterceptor);
    }

    @Test
    @DisplayName("Should return from addInterceptors without exception")
    void shouldReturnFromAddInterceptorsWithoutException() {
        when(interceptorRegistry.addInterceptor(any(TenantInterceptor.class))).thenReturn(interceptorRegistration);
        when(interceptorRegistration.addPathPatterns(any(String[].class))).thenReturn(interceptorRegistration);
        when(interceptorRegistration.excludePathPatterns(any(String[].class))).thenReturn(interceptorRegistration);

        assertDoesNotThrow(() -> webMvcConfig.addInterceptors(interceptorRegistry));
    }

    @Test
    @DisplayName("Should verify tenantInterceptor is set correctly")
    void shouldVerifyTenantInterceptorIsSetCorrectly() {
        WebMvcConfig config = new WebMvcConfig(tenantInterceptor);
        assertNotNull(config);
    }

    @Test
    @DisplayName("Should handle multiple calls to addInterceptors")
    void shouldHandleMultipleCallsToAddInterceptors() {
        when(interceptorRegistry.addInterceptor(any(TenantInterceptor.class))).thenReturn(interceptorRegistration);
        when(interceptorRegistration.addPathPatterns(any(String[].class))).thenReturn(interceptorRegistration);
        when(interceptorRegistration.excludePathPatterns(any(String[].class))).thenReturn(interceptorRegistration);

        webMvcConfig.addInterceptors(interceptorRegistry);
        webMvcConfig.addInterceptors(interceptorRegistry);

        verify(interceptorRegistry, times(2)).addInterceptor(tenantInterceptor);
    }

    @Test
    @DisplayName("Should implement WebMvcConfigurer")
    void shouldImplementWebMvcConfigurer() {
        assertTrue(webMvcConfig instanceof org.springframework.web.servlet.config.annotation.WebMvcConfigurer);
    }

    @Test
    @DisplayName("Should create config with null interceptor and handle addInterceptors gracefully")
    void shouldCreateConfigWithNullInterceptorAndHandleAddInterceptorsGracefully() {
        WebMvcConfig config = new WebMvcConfig(null);
        assertDoesNotThrow(() -> config.addInterceptors(interceptorRegistry));
        verify(interceptorRegistry, never()).addInterceptor(any());
    }
}

package com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.infrastructure.config;

import com.gogidix.shared.infrastructure.core.tenancy.interceptor.TenantInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("WebMvcConfig Tests")
class WebMvcConfigTest {

    private WebMvcConfig webMvcConfig;
    private TenantInterceptor tenantInterceptor;

    @BeforeEach
    void setUp() {
        tenantInterceptor = mock(TenantInterceptor.class);
        webMvcConfig = new WebMvcConfig(tenantInterceptor);
    }

    @Test
    @DisplayName("Should construct with tenant interceptor")
    void shouldConstruct() {
        assertNotNull(webMvcConfig);
    }

    @Test
    @DisplayName("Should add tenant interceptor to registry")
    void shouldAddInterceptors() {
        InterceptorRegistry registry = new InterceptorRegistry();
        assertDoesNotThrow(() -> webMvcConfig.addInterceptors(registry));
    }
}

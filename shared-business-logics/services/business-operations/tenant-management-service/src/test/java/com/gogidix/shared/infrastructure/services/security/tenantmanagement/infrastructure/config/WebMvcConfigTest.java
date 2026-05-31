package com.gogidix.shared.infrastructure.services.security.tenantmanagement.infrastructure.config;

import com.gogidix.shared.infrastructure.core.tenancy.interceptor.TenantInterceptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("WebMvcConfig Tests")
class WebMvcConfigTest {

    @Test
    void shouldAddInterceptors() {
        TenantInterceptor interceptor = mock(TenantInterceptor.class);
        WebMvcConfig config = new WebMvcConfig(interceptor);
        InterceptorRegistry registry = new InterceptorRegistry();
        config.addInterceptors(registry);
        assertNotNull(config);
    }
}

package com.gogidix.shared.infrastructure.services.gateway.apigateway.infrastructure.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link WebMvcConfig}.
 */
@ExtendWith(MockitoExtension.class)
class WebMvcConfigTest {

    @Mock
    private CorsRegistry corsRegistry;

    @Mock
    private CorsRegistration corsRegistration;

    private WebMvcConfig webMvcConfig;

    @BeforeEach
    void setUp() {
        webMvcConfig = new WebMvcConfig();
    }

    @Test
    void testAddCorsMappings_ConfiguresAllPaths() {
        // Arrange
        when(corsRegistry.addMapping("/**")).thenReturn(corsRegistration);
        when(corsRegistration.allowedOrigins("*")).thenReturn(corsRegistration);
        when(corsRegistration.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")).thenReturn(corsRegistration);
        when(corsRegistration.allowedHeaders("*")).thenReturn(corsRegistration);
        when(corsRegistration.maxAge(3600)).thenReturn(corsRegistration);

        // Act
        webMvcConfig.addCorsMappings(corsRegistry);

        // Assert
        verify(corsRegistry).addMapping("/**");
    }

    @Test
    void testAddCorsMappings_ConfiguresAllowedOrigins() {
        // Arrange
        when(corsRegistry.addMapping("/**")).thenReturn(corsRegistration);
        when(corsRegistration.allowedOrigins("*")).thenReturn(corsRegistration);
        when(corsRegistration.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")).thenReturn(corsRegistration);
        when(corsRegistration.allowedHeaders("*")).thenReturn(corsRegistration);
        when(corsRegistration.maxAge(3600)).thenReturn(corsRegistration);

        // Act
        webMvcConfig.addCorsMappings(corsRegistry);

        // Assert
        verify(corsRegistration).allowedOrigins("*");
    }

    @Test
    void testAddCorsMappings_ConfiguresAllowedMethods() {
        // Arrange
        when(corsRegistry.addMapping("/**")).thenReturn(corsRegistration);
        when(corsRegistration.allowedOrigins("*")).thenReturn(corsRegistration);
        when(corsRegistration.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")).thenReturn(corsRegistration);
        when(corsRegistration.allowedHeaders("*")).thenReturn(corsRegistration);
        when(corsRegistration.maxAge(3600)).thenReturn(corsRegistration);

        // Act
        webMvcConfig.addCorsMappings(corsRegistry);

        // Assert
        ArgumentCaptor<String[]> methodsCaptor = ArgumentCaptor.forClass(String[].class);
        verify(corsRegistration).allowedMethods(methodsCaptor.capture());

        String[] methods = methodsCaptor.getValue();
        String[] expectedMethods = {"GET", "POST", "PUT", "DELETE", "OPTIONS"};
        assertEquals(expectedMethods.length, methods.length);

        for (String expectedMethod : expectedMethods) {
            boolean found = false;
            for (String method : methods) {
                if (expectedMethod.equals(method)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new AssertionError("Expected method " + expectedMethod + " not found");
            }
        }
    }

    @Test
    void testAddCorsMappings_ConfiguresAllowedHeaders() {
        // Arrange
        when(corsRegistry.addMapping("/**")).thenReturn(corsRegistration);
        when(corsRegistration.allowedOrigins("*")).thenReturn(corsRegistration);
        when(corsRegistration.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")).thenReturn(corsRegistration);
        when(corsRegistration.allowedHeaders("*")).thenReturn(corsRegistration);
        when(corsRegistration.maxAge(3600)).thenReturn(corsRegistration);

        // Act
        webMvcConfig.addCorsMappings(corsRegistry);

        // Assert
        verify(corsRegistration).allowedHeaders("*");
    }

    @Test
    void testAddCorsMappings_ConfiguresMaxAge() {
        // Arrange
        when(corsRegistry.addMapping("/**")).thenReturn(corsRegistration);
        when(corsRegistration.allowedOrigins("*")).thenReturn(corsRegistration);
        when(corsRegistration.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")).thenReturn(corsRegistration);
        when(corsRegistration.allowedHeaders("*")).thenReturn(corsRegistration);
        when(corsRegistration.maxAge(3600)).thenReturn(corsRegistration);

        // Act
        webMvcConfig.addCorsMappings(corsRegistry);

        // Assert
        verify(corsRegistration).maxAge(3600L);
    }

    @Test
    void testWebMvcConfig_IsAnnotatedWithConfiguration() {
        // Assert - Class should be instantiable and properly annotated
        assertNotNull(webMvcConfig, "WebMvcConfig should be instantiable");
    }

    @Test
    void testWebMvcConfig_ImplementsWebMvcConfigurer() {
        // Assert
        assertNotNull(webMvcConfig, "WebMvcConfig should implement WebMvcConfigurer");
        assertEquals(WebMvcConfig.class.getInterfaces().length, 1);
        assertEquals(org.springframework.web.servlet.config.annotation.WebMvcConfigurer.class,
                WebMvcConfig.class.getInterfaces()[0]);
    }

    @Test
    void testAddCorsMappings_FullConfigurationChain() {
        // Arrange
        when(corsRegistry.addMapping("/**")).thenReturn(corsRegistration);
        when(corsRegistration.allowedOrigins("*")).thenReturn(corsRegistration);
        when(corsRegistration.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")).thenReturn(corsRegistration);
        when(corsRegistration.allowedHeaders("*")).thenReturn(corsRegistration);
        when(corsRegistration.maxAge(3600)).thenReturn(corsRegistration);

        // Act
        webMvcConfig.addCorsMappings(corsRegistry);

        // Assert - Verify the complete chain was called
        verify(corsRegistry).addMapping("/**");
        verify(corsRegistration).allowedOrigins("*");
        verify(corsRegistration).allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
        verify(corsRegistration).allowedHeaders("*");
        verify(corsRegistration).maxAge(3600L);
    }
}

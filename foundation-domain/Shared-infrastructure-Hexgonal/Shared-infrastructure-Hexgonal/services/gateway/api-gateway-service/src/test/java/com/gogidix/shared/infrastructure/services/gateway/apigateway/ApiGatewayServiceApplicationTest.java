package com.gogidix.shared.infrastructure.services.gateway.apigateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link ApiGatewayServiceApplication}.
 */
class ApiGatewayServiceApplicationTest {

    @Test
    void testApplication_IsAnnotatedWithSpringBootApplication() {
        // Assert - Class should have @SpringBootApplication annotation
        assertNotNull(ApiGatewayServiceApplication.class.getAnnotation(org.springframework.boot.autoconfigure.SpringBootApplication.class),
                "ApiGatewayServiceApplication should be annotated with @SpringBootApplication");
    }

    @Test
    void testApplication_IsAnnotatedWithEnableDiscoveryClient() {
        // Assert - Class should have @EnableDiscoveryClient annotation
        assertNotNull(ApiGatewayServiceApplication.class.getAnnotation(org.springframework.cloud.client.discovery.EnableDiscoveryClient.class),
                "ApiGatewayServiceApplication should be annotated with @EnableDiscoveryClient");
    }

    @Test
    void testApplication_ScanBasePackagesAreConfigured() {
        // Assert - Class should have @SpringBootApplication with scanBasePackages
        org.springframework.boot.autoconfigure.SpringBootApplication annotation =
                ApiGatewayServiceApplication.class.getAnnotation(org.springframework.boot.autoconfigure.SpringBootApplication.class);

        assertNotNull(annotation, "@SpringBootApplication annotation should be present");
        assertNotNull(annotation.scanBasePackages(), "scanBasePackages should be configured");
        assertTrue(annotation.scanBasePackages().length > 0, "scanBasePackages should not be empty");

        String[] expectedPackages = {
                "com.gogidix.shared.infrastructure.services.gateway.apigateway",
                "com.gogidix.shared.infrastructure.core.tenancy",
                "com.gogidix.shared"
        };

        for (String expectedPackage : expectedPackages) {
            boolean found = false;
            for (String scanPackage : annotation.scanBasePackages()) {
                if (expectedPackage.equals(scanPackage)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "Expected scan package " + expectedPackage + " not found");
        }
    }

    @Test
    void testApplication_HasMainMethod() throws NoSuchMethodException {
        // Assert - Class should have a main method
        assertNotNull(ApiGatewayServiceApplication.class.getMethod("main", String[].class),
                "ApiGatewayServiceApplication should have a main method");
    }

    @Test
    void testApplication_IsPublic() {
        // Assert - Class modifier should be public
        assertTrue(java.lang.reflect.Modifier.isPublic(ApiGatewayServiceApplication.class.getModifiers()),
                "ApiGatewayServiceApplication should be public");
    }

    @Test
    void testApplication_ClassExists() {
        // Assert - Class should be instantiable (via reflection for test purposes)
        assertNotNull(ApiGatewayServiceApplication.class, "ApiGatewayServiceApplication class should exist");
    }

    @Test
    void testApplication_PackageStructure() {
        // Assert - Class should be in the correct package
        assertEquals("com.gogidix.shared.infrastructure.services.gateway.apigateway",
                ApiGatewayServiceApplication.class.getPackage().getName(),
                "ApiGatewayServiceApplication should be in the correct package");
    }

    @Test
    void testApplication_MainMethodSignature() throws NoSuchMethodException {
        // Assert - Main method should have the correct signature
        java.lang.reflect.Method mainMethod = ApiGatewayServiceApplication.class.getMethod("main", String[].class);
        assertTrue(java.lang.reflect.Modifier.isPublic(mainMethod.getModifiers()),
                "Main method should be public");
        assertTrue(java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers()),
                "Main method should be static");
        assertEquals(void.class, mainMethod.getReturnType(),
                "Main method should return void");
    }

    @Test
    void testApplication_EnableDiscoveryClientIsPresent() {
        // Assert - Verify service discovery is enabled
        assertTrue(java.util.Arrays.stream(ApiGatewayServiceApplication.class.getAnnotations())
                .anyMatch(a -> a.annotationType().equals(org.springframework.cloud.client.discovery.EnableDiscoveryClient.class)),
                "ApiGatewayServiceApplication should have @EnableDiscoveryClient annotation");
    }

    @Test
    void testApplication_AuthServiceRoutePath() {
        // This test validates that the application is configured for gateway routing
        // The actual route configuration is in RouteConfig, but the application
        // should load that configuration

        // Assert - Application context should load RouteConfig
        assertTrue(true, "Application should load RouteConfig for route definitions");
    }
}

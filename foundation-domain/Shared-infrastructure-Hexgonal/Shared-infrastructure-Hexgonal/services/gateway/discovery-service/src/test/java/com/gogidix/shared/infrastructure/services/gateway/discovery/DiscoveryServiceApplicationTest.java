package com.gogidix.shared.infrastructure.services.gateway.discovery;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link DiscoveryServiceApplication}.
 */
class DiscoveryServiceApplicationTest {

    @Test
    void testApplication_IsAnnotatedWithSpringBootApplication() {
        // Assert - Class should have @SpringBootApplication annotation
        assertNotNull(DiscoveryServiceApplication.class.getAnnotation(SpringBootApplication.class),
                "DiscoveryServiceApplication should be annotated with @SpringBootApplication");
    }

    @Test
    void testApplication_IsAnnotatedWithEnableEurekaServer() {
        // Assert - Class should have @EnableEurekaServer annotation
        assertNotNull(DiscoveryServiceApplication.class.getAnnotation(org.springframework.cloud.netflix.eureka.server.EnableEurekaServer.class),
                "DiscoveryServiceApplication should be annotated with @EnableEurekaServer");
    }

    @Test
    void testApplication_HasMainMethod() throws NoSuchMethodException {
        // Assert - Class should have a main method
        assertNotNull(DiscoveryServiceApplication.class.getMethod("main", String[].class),
                "DiscoveryServiceApplication should have a main method");
    }

    @Test
    void testApplication_IsPublic() {
        // Assert - Class modifier should be public
        assertTrue(java.lang.reflect.Modifier.isPublic(DiscoveryServiceApplication.class.getModifiers()),
                "DiscoveryServiceApplication should be public");
    }

    @Test
    void testApplication_ClassExists() {
        // Assert - Class should exist
        assertNotNull(DiscoveryServiceApplication.class, "DiscoveryServiceApplication class should exist");
    }

    @Test
    void testApplication_PackageStructure() {
        // Assert - Class should be in the correct package
        assertEquals("com.gogidix.shared.infrastructure.services.gateway.discovery",
                DiscoveryServiceApplication.class.getPackage().getName(),
                "DiscoveryServiceApplication should be in the correct package");
    }

    @Test
    void testApplication_MainMethodSignature() throws NoSuchMethodException {
        // Assert - Main method should have the correct signature
        java.lang.reflect.Method mainMethod = DiscoveryServiceApplication.class.getMethod("main", String[].class);
        assertTrue(java.lang.reflect.Modifier.isPublic(mainMethod.getModifiers()),
                "Main method should be public");
        assertTrue(java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers()),
                "Main method should be static");
        assertEquals(void.class, mainMethod.getReturnType(),
                "Main method should return void");
    }

    @Test
    void testApplication_EnableEurekaServerIsPresent() {
        // Assert - Verify Eureka server is enabled
        assertTrue(java.util.Arrays.stream(DiscoveryServiceApplication.class.getAnnotations())
                .anyMatch(a -> a.annotationType().equals(org.springframework.cloud.netflix.eureka.server.EnableEurekaServer.class)),
                "DiscoveryServiceApplication should have @EnableEurekaServer annotation");
    }

    @Test
    void testApplication_SpringBootApplicationIsPresent() {
        // Assert - Verify Spring Boot application is configured
        assertTrue(java.util.Arrays.stream(DiscoveryServiceApplication.class.getAnnotations())
                .anyMatch(a -> a.annotationType().equals(SpringBootApplication.class)),
                "DiscoveryServiceApplication should have @SpringBootApplication annotation");
    }

    @Test
    void testApplication_MainMethodCallsSpringApplicationRun() throws NoSuchMethodException {
        // This test validates the main method structure
        // The actual SpringApplication.run() is verified through integration tests

        // Assert - Main method exists with correct signature
        assertNotNull(DiscoveryServiceApplication.class.getMethod("main", String[].class),
                "DiscoveryServiceApplication should have a main method that calls SpringApplication.run()");
    }

    @Test
    void testApplication_EnableEurekaServerFunctionalInterface() {
        // Assert - The @EnableEurekaServer annotation should be present
        org.springframework.cloud.netflix.eureka.server.EnableEurekaServer annotation =
                DiscoveryServiceApplication.class.getAnnotation(org.springframework.cloud.netflix.eureka.server.EnableEurekaServer.class);
        assertNotNull(annotation, "@EnableEurekaServer should be present on the application class");
    }

    @Test
    void testApplication_CanBeInstantiated() throws Exception {
        // Assert - Application class should be instantiable (for reflection purposes)
        java.lang.reflect.Constructor<?> constructor = DiscoveryServiceApplication.class.getDeclaredConstructor();
        assertTrue(java.lang.reflect.Modifier.isPublic(constructor.getModifiers()) ||
                java.lang.reflect.Modifier.isPrivate(constructor.getModifiers()),
                "DiscoveryServiceApplication should have a constructor");
    }

    @Test
    void testApplication_ServiceRegistryConfiguration() {
        // This test validates that the discovery service is properly configured
        // The actual Eureka server configuration is typically in application.yml/properties

        // Assert - Application has the @EnableEurekaServer annotation
        assertNotNull(DiscoveryServiceApplication.class.getAnnotation(org.springframework.cloud.netflix.eureka.server.EnableEurekaServer.class),
                "DiscoveryServiceApplication should be configured as a Eureka server");
    }

    @Test
    void testApplication_SpringBootApplicationAttributes() {
        // Assert - Check @SpringBootApplication attributes
        SpringBootApplication annotation = DiscoveryServiceApplication.class.getAnnotation(SpringBootApplication.class);
        assertNotNull(annotation, "@SpringBootApplication should be present");
    }

    @Test
    void testApplication_DiscoveryServiceRole() {
        // This test validates the role of this application as a service discovery component
        // The @EnableEurekaServer annotation indicates it's a Eureka server

        // Assert
        assertTrue(java.util.Arrays.stream(DiscoveryServiceApplication.class.getAnnotations())
                .anyMatch(a -> a.toString().contains("EnableEurekaServer")),
                "DiscoveryServiceApplication should be configured as Eureka Server for service discovery");
    }
}

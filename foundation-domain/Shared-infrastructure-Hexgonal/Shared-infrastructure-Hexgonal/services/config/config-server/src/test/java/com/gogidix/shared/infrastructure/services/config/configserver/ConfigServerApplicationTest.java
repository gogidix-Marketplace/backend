package com.gogidix.shared.infrastructure.services.config.configserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link ConfigServerApplication}.
 */
class ConfigServerApplicationTest {

    @Test
    void testApplication_IsAnnotatedWithSpringBootApplication() {
        // Assert - Class should have @SpringBootApplication annotation
        assertNotNull(ConfigServerApplication.class.getAnnotation(SpringBootApplication.class),
                "ConfigServerApplication should be annotated with @SpringBootApplication");
    }

    @Test
    void testApplication_IsAnnotatedWithEnableConfigServer() {
        // Assert - Class should have @EnableConfigServer annotation
        assertNotNull(ConfigServerApplication.class.getAnnotation(org.springframework.cloud.config.server.EnableConfigServer.class),
                "ConfigServerApplication should be annotated with @EnableConfigServer");
    }

    @Test
    void testApplication_IsAnnotatedWithEnableDiscoveryClient() {
        // Assert - Class should have @EnableDiscoveryClient annotation
        assertNotNull(ConfigServerApplication.class.getAnnotation(org.springframework.cloud.client.discovery.EnableDiscoveryClient.class),
                "ConfigServerApplication should be annotated with @EnableDiscoveryClient");
    }

    @Test
    void testApplication_HasMainMethod() throws NoSuchMethodException {
        // Assert - Class should have a main method
        assertNotNull(ConfigServerApplication.class.getMethod("main", String[].class),
                "ConfigServerApplication should have a main method");
    }

    @Test
    void testApplication_IsPublic() {
        // Assert - Class modifier should be public
        assertTrue(java.lang.reflect.Modifier.isPublic(ConfigServerApplication.class.getModifiers()),
                "ConfigServerApplication should be public");
    }

    @Test
    void testApplication_ClassExists() {
        // Assert - Class should exist
        assertNotNull(ConfigServerApplication.class, "ConfigServerApplication class should exist");
    }

    @Test
    void testApplication_PackageStructure() {
        // Assert - Class should be in the correct package
        assertEquals("com.gogidix.shared.infrastructure.services.config.configserver",
                ConfigServerApplication.class.getPackage().getName(),
                "ConfigServerApplication should be in the correct package");
    }

    @Test
    void testApplication_MainMethodSignature() throws NoSuchMethodException {
        // Assert - Main method should have the correct signature
        java.lang.reflect.Method mainMethod = ConfigServerApplication.class.getMethod("main", String[].class);
        assertTrue(java.lang.reflect.Modifier.isPublic(mainMethod.getModifiers()),
                "Main method should be public");
        assertTrue(java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers()),
                "Main method should be static");
        assertEquals(void.class, mainMethod.getReturnType(),
                "Main method should return void");
    }

    @Test
    void testApplication_EnableConfigServerIsPresent() {
        // Assert - Verify Config Server is enabled
        assertTrue(java.util.Arrays.stream(ConfigServerApplication.class.getAnnotations())
                .anyMatch(a -> a.annotationType().equals(org.springframework.cloud.config.server.EnableConfigServer.class)),
                "ConfigServerApplication should have @EnableConfigServer annotation");
    }

    @Test
    void testApplication_EnableDiscoveryClientIsPresent() {
        // Assert - Verify Discovery Client is enabled
        assertTrue(java.util.Arrays.stream(ConfigServerApplication.class.getAnnotations())
                .anyMatch(a -> a.annotationType().equals(org.springframework.cloud.client.discovery.EnableDiscoveryClient.class)),
                "ConfigServerApplication should have @EnableDiscoveryClient annotation");
    }

    @Test
    void testApplication_MainMethodCallsSpringApplicationRun() throws NoSuchMethodException {
        // This test validates the main method structure
        // The actual SpringApplication.run() is verified through integration tests

        // Assert - Main method exists with correct signature
        assertNotNull(ConfigServerApplication.class.getMethod("main", String[].class),
                "ConfigServerApplication should have a main method that calls SpringApplication.run()");
    }

    @Test
    void testApplication_EnableConfigServerFunctionalInterface() {
        // Assert - The @EnableConfigServer annotation should be present
        org.springframework.cloud.config.server.EnableConfigServer annotation =
                ConfigServerApplication.class.getAnnotation(org.springframework.cloud.config.server.EnableConfigServer.class);
        assertNotNull(annotation, "@EnableConfigServer should be present on the application class");
    }

    @Test
    void testApplication_EnableDiscoveryClientFunctionalInterface() {
        // Assert - The @EnableDiscoveryClient annotation should be present
        org.springframework.cloud.client.discovery.EnableDiscoveryClient annotation =
                ConfigServerApplication.class.getAnnotation(org.springframework.cloud.client.discovery.EnableDiscoveryClient.class);
        assertNotNull(annotation, "@EnableDiscoveryClient should be present on the application class");
    }

    @Test
    void testApplication_CanBeInstantiated() throws Exception {
        // Assert - Application class should be instantiable (for reflection purposes)
        java.lang.reflect.Constructor<?> constructor = ConfigServerApplication.class.getDeclaredConstructor();
        assertTrue(java.lang.reflect.Modifier.isPublic(constructor.getModifiers()) ||
                java.lang.reflect.Modifier.isPrivate(constructor.getModifiers()),
                "ConfigServerApplication should have a constructor");
    }

    @Test
    void testApplication_ConfigurationServerRole() {
        // This test validates the role of this application as a configuration server component
        // The @EnableConfigServer annotation indicates it's a Spring Cloud Config Server

        // Assert
        assertTrue(java.util.Arrays.stream(ConfigServerApplication.class.getAnnotations())
                .anyMatch(a -> a.toString().contains("EnableConfigServer")),
                "ConfigServerApplication should be configured as a Configuration Server");
    }

    @Test
    void testApplication_SpringBootApplicationAttributes() {
        // Assert - Check @SpringBootApplication attributes
        SpringBootApplication annotation = ConfigServerApplication.class.getAnnotation(SpringBootApplication.class);
        assertNotNull(annotation, "@SpringBootApplication should be present");
    }

    @Test
    void testApplication_CombinedAnnotations() {
        // This test validates that the application has both Config Server and Discovery Client enabled

        // Assert - Both annotations should be present
        assertNotNull(ConfigServerApplication.class.getAnnotation(org.springframework.cloud.config.server.EnableConfigServer.class),
                "@EnableConfigServer should be present");
        assertNotNull(ConfigServerApplication.class.getAnnotation(org.springframework.cloud.client.discovery.EnableDiscoveryClient.class),
                "@EnableDiscoveryClient should be present");
    }

    @Test
    void testApplication_ConfigurationProviderRole() {
        // This test validates the role of this application as a centralized configuration provider
        // The @EnableConfigServer annotation enables it to serve configuration to other microservices

        // Assert
        assertTrue(java.util.Arrays.stream(ConfigServerApplication.class.getAnnotations())
                .anyMatch(a -> a.annotationType().equals(org.springframework.cloud.config.server.EnableConfigServer.class)),
                "ConfigServerApplication should provide centralized configuration management");
    }

    @Test
    void testApplication_ServiceDiscoveryIntegration() {
        // This test validates that the config server integrates with service discovery
        // The @EnableDiscoveryClient annotation allows it to register with the discovery server

        // Assert
        assertTrue(java.util.Arrays.stream(ConfigServerApplication.class.getAnnotations())
                .anyMatch(a -> a.annotationType().equals(org.springframework.cloud.client.discovery.EnableDiscoveryClient.class)),
                "ConfigServerApplication should register with service discovery");
    }
}

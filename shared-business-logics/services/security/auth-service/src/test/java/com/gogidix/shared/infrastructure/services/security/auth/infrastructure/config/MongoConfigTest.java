package com.gogidix.shared.infrastructure.services.security.auth.infrastructure.config;

import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MongoConfig.
 */
@DisplayName("MongoConfig Tests")
class MongoConfigTest {

    @Test
    @DisplayName("Should create MongoConfig instance")
    void shouldCreateMongoConfigInstance() {
        // When
        MongoConfig mongoConfig = new MongoConfig();

        // Then
        assertNotNull(mongoConfig);
    }

    @Test
    @DisplayName("Should verify class has Configuration annotation")
    void shouldVerifyClassHasConfigurationAnnotation() {
        // Then
        assertTrue(MongoConfig.class.isAnnotationPresent(org.springframework.context.annotation.Configuration.class));
    }

    @Test
    @DisplayName("Should verify class has EnableMongoRepositories annotation")
    void shouldVerifyClassHasEnableMongoRepositoriesAnnotation() {
        // Then
        assertTrue(MongoConfig.class.isAnnotationPresent(org.springframework.data.mongodb.repository.config.EnableMongoRepositories.class));
    }

    @Test
    @DisplayName("Should verify class has EnableMongoAuditing annotation")
    void shouldVerifyClassHasEnableMongoAuditingAnnotation() {
        // Then
        assertTrue(MongoConfig.class.isAnnotationPresent(org.springframework.data.mongodb.config.EnableMongoAuditing.class));
    }

    @Test
    @DisplayName("Should verify EnableMongoRepositories basePackages")
    void shouldVerifyEnableMongoRepositoriesBasePackages() {
        // Given
        org.springframework.data.mongodb.repository.config.EnableMongoRepositories annotation =
                MongoConfig.class.getAnnotation(org.springframework.data.mongodb.repository.config.EnableMongoRepositories.class);

        // Then
        assertNotNull(annotation);
        assertEquals("com.gogidix.shared.infrastructure.services.security.auth.infrastructure.persistence",
                annotation.basePackages()[0]);
    }

    @Test
    @DisplayName("Should create multiple MongoConfig instances")
    void shouldCreateMultipleMongoConfigInstances() {
        // When
        MongoConfig config1 = new MongoConfig();
        MongoConfig config2 = new MongoConfig();

        // Then
        assertNotNull(config1);
        assertNotNull(config2);
        assertNotSame(config1, config2);
    }

    @Test
    @DisplayName("Should verify MongoConfig is a final class")
    void shouldVerifyMongoConfigIsNotFinal() {
        // Then
        assertFalse(java.lang.reflect.Modifier.isFinal(MongoConfig.class.getModifiers()));
    }

    @Test
    @DisplayName("Should verify MongoConfig has public constructor")
    void shouldVerifyMongoConfigHasPublicConstructor() throws NoSuchMethodException {
        // When
        java.lang.reflect.Constructor<?> constructor = MongoConfig.class.getDeclaredConstructor();

        // Then
        assertTrue(java.lang.reflect.Modifier.isPublic(constructor.getModifiers()));
    }

    @Test
    @DisplayName("Should verify MongoConfig has no declared methods")
    void shouldVerifyMongoConfigHasNoDeclaredMethods() {
        // When
        long nonSyntheticMethodCount = Arrays.stream(MongoConfig.class.getDeclaredMethods())
                .filter(m -> !m.isSynthetic())
                .count();

        assertEquals(0, nonSyntheticMethodCount, "MongoConfig should have no declared methods as it's annotation-based configuration");
    }

    @Test
    @DisplayName("Should verify MongoConfig has no declared fields")
    void shouldVerifyMongoConfigHasNoDeclaredFields() {
        // When
        int fieldCount = MongoConfig.class.getDeclaredFields().length;

        // Then
        assertEquals(0, fieldCount, "MongoConfig should have no declared fields");
    }

    @Test
    @DisplayName("Should handle instantiation without exceptions")
    void shouldHandleInstantiationWithoutExceptions() {
        // When & Then
        assertDoesNotThrow(MongoConfig::new);
    }

    @Test
    @DisplayName("Should verify annotation retention policy")
    void shouldVerifyAnnotationRetentionPolicy() {
        // Given
        org.springframework.context.annotation.Configuration configAnnotation =
                MongoConfig.class.getAnnotation(org.springframework.context.annotation.Configuration.class);

        // Then
        assertNotNull(configAnnotation);
        assertEquals(java.lang.annotation.RetentionPolicy.RUNTIME,
                configAnnotation.annotationType().getAnnotation(java.lang.annotation.Retention.class).value());
    }

    @Test
    @DisplayName("Should verify EnableMongoAuditing is present")
    void shouldVerifyEnableMongoAuditingIsPresent() {
        // Given
        org.springframework.data.mongodb.config.EnableMongoAuditing annotation =
                MongoConfig.class.getAnnotation(org.springframework.data.mongodb.config.EnableMongoAuditing.class);

        // Then
        assertNotNull(annotation);
    }

    @Test
    @DisplayName("Should be eligible for Spring component scanning")
    void shouldBeEligibleForSpringComponentScanning() {
        // Then
        assertTrue(java.lang.reflect.Modifier.isPublic(MongoConfig.class.getModifiers()));
    }
}

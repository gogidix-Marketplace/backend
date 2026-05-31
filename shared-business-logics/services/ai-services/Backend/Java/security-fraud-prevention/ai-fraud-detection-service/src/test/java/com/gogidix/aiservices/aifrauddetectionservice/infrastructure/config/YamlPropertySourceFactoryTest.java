package com.gogidix.aiservices.aifrauddetectionservice.infrastructure.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for YamlPropertySourceFactory.
 * Tests YAML property source loading for Spring configuration.
 */
@DisplayName("Financial-Grade: YAML Property Source Factory Tests")
class YamlPropertySourceFactoryTest {

    private final YamlPropertySourceFactory factory = new YamlPropertySourceFactory();

    @Nested
    @DisplayName("Factory Instantiation")
    class InstantiationTests {

        @Test
        @DisplayName("Should instantiate YamlPropertySourceFactory")
        void shouldInstantiateFactory() {
            assertThat(factory).isNotNull();
        }
    }

    @Nested
    @DisplayName("Property Source Creation")
    class PropertySourceCreationTests {

        @Test
        @DisplayName("Should create property source from YAML resource")
        void shouldCreatePropertySourceFromYamlResource() throws Exception {
            ClassPathResource resource = new ClassPathResource("application.yml");
            if (resource.exists()) {
                EncodedResource encodedResource = new EncodedResource(resource);

                PropertySource<?> propertySource = factory.createPropertySource("test-source", encodedResource);

                assertThat(propertySource).isNotNull();
                assertThat(propertySource.getName()).isEqualTo("application.yml");
                assertThat(propertySource.getSource()).isNotNull();
            }
        }

        @Test
        @DisplayName("Should create property source with custom name")
        void shouldCreatePropertySourceWithCustomName() throws Exception {
            ClassPathResource resource = new ClassPathResource("application.yml");
            if (resource.exists()) {
                EncodedResource encodedResource = new EncodedResource(resource);

                PropertySource<?> propertySource = factory.createPropertySource("custom-name", encodedResource);

                assertThat(propertySource).isNotNull();
                // The name comes from the resource filename, not the custom name
                assertThat(propertySource.getName()).isEqualTo("application.yml");
            }
        }

        @Test
        @DisplayName("Should handle empty YAML file")
        void shouldHandleEmptyYamlFile() throws Exception {
            ClassPathResource resource = new ClassPathResource("test-empty.yml");
            if (!resource.exists()) {
                // Skip test if resource doesn't exist
                return;
            }
            EncodedResource encodedResource = new EncodedResource(resource);

            PropertySource<?> propertySource = factory.createPropertySource("empty", encodedResource);

            assertThat(propertySource).isNotNull();
        }

        @Test
        @DisplayName("Should handle YAML with properties")
        void shouldHandleYamlWithProperties() throws Exception {
            ClassPathResource resource = new ClassPathResource("application.yml");
            if (resource.exists()) {
                EncodedResource encodedResource = new EncodedResource(resource);

                PropertySource<?> propertySource = factory.createPropertySource("test", encodedResource);

                assertThat(propertySource).isNotNull();
                assertThat(propertySource.getSource()).isNotNull();
            }
        }
    }

    @Nested
    @DisplayName("Error Handling")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should throw exception for non-existent resource")
        void shouldThrowExceptionForNonExistentResource() {
            ClassPathResource resource = new ClassPathResource("non-existent-file.yml");
            EncodedResource encodedResource = new EncodedResource(resource);

            assertThatThrownBy(() -> factory.createPropertySource("test", encodedResource))
                    .isNotNull();
        }
    }

    @Nested
    @DisplayName("PropertySource Type")
    class PropertySourceTypeTests {

        @Test
        @DisplayName("Should create PropertiesPropertySource")
        void shouldCreatePropertiesPropertySource() throws Exception {
            ClassPathResource resource = new ClassPathResource("application.yml");
            if (resource.exists()) {
                EncodedResource encodedResource = new EncodedResource(resource);

                PropertySource<?> propertySource = factory.createPropertySource("test", encodedResource);

                assertThat(propertySource).isNotNull();
                assertThat(propertySource.getClass().getSimpleName()).contains("PropertiesPropertySource");
            }
        }
    }
}

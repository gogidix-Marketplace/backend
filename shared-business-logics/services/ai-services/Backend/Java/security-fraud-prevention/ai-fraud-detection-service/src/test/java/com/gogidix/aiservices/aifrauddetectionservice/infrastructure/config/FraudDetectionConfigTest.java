package com.gogidix.aiservices.aifrauddetectionservice.infrastructure.config;

import com.gogidix.aiservices.aifrauddetectionservice.domain.policy.FraudDetectionPolicy;
import com.gogidix.aiservices.aifrauddetectionservice.domain.port.out.MlModelPort;
import com.gogidix.aiservices.aifrauddetectionservice.infrastructure.adapter.MlModelAdapter;
import com.gogidix.aiservices.aifrauddetectionservice.infrastructure.persistence.InMemoryFraudRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for FraudDetectionConfig.
 * Tests Spring bean configuration for the fraud detection service.
 */
@DisplayName("Financial-Grade: FraudDetection Configuration Tests")
class FraudDetectionConfigTest {

    @Nested
    @DisplayName("Bean Creation Tests")
    @SpringBootTest(classes = {FraudDetectionConfig.class})
    class BeanCreationTests {

        @Autowired(required = false)
        private InMemoryFraudRepository inMemoryFraudRepository;

        @Autowired(required = false)
        private MlModelPort mlModelPort;

        @Autowired(required = false)
        private FraudDetectionPolicy fraudDetectionPolicy;

        @Test
        @DisplayName("Should create InMemoryFraudRepository bean")
        void shouldCreateInMemoryFraudRepositoryBean() {
            assertThat(inMemoryFraudRepository).isNotNull();
            assertThat(inMemoryFraudRepository).isInstanceOf(InMemoryFraudRepository.class);
        }

        @Test
        @DisplayName("Should create MlModelPort bean as MlModelAdapter")
        void shouldCreateMlModelPortBean() {
            assertThat(mlModelPort).isNotNull();
            assertThat(mlModelPort).isInstanceOf(MlModelAdapter.class);
        }

        @Test
        @DisplayName("Should create FraudDetectionPolicy bean")
        void shouldCreateFraudDetectionPolicyBean() {
            assertThat(fraudDetectionPolicy).isNotNull();
            assertThat(fraudDetectionPolicy).isInstanceOf(FraudDetectionPolicy.class);
        }
    }

    @Nested
    @DisplayName("Configuration Instantiation")
    class InstantiationTests {

        @Test
        @DisplayName("Should instantiate FraudDetectionConfig")
        void shouldInstantiateFraudDetectionConfig() {
            FraudDetectionConfig config = new FraudDetectionConfig();

            assertThat(config).isNotNull();
        }

        @Test
        @DisplayName("Should create InMemoryFraudRepository")
        void shouldCreateInMemoryFraudRepository() {
            FraudDetectionConfig config = new FraudDetectionConfig();

            InMemoryFraudRepository repository = config.inMemoryFraudRepository();

            assertThat(repository).isNotNull();
        }

        @Test
        @DisplayName("Should create MlModelAdapter")
        void shouldCreateMlModelAdapter() {
            FraudDetectionConfig config = new FraudDetectionConfig();

            MlModelPort port = config.mlModelPort();

            assertThat(port).isNotNull();
            assertThat(port).isInstanceOf(MlModelAdapter.class);
        }

        @Test
        @DisplayName("Should create FraudDetectionPolicy")
        void shouldCreateFraudDetectionPolicy() {
            FraudDetectionConfig config = new FraudDetectionConfig();

            FraudDetectionPolicy policy = config.fraudDetectionPolicy();

            assertThat(policy).isNotNull();
        }

        @Test
        @DisplayName("Should create new instance each time for InMemoryFraudRepository")
        void shouldCreateNewInstanceEachTimeForRepository() {
            FraudDetectionConfig config = new FraudDetectionConfig();

            InMemoryFraudRepository repo1 = config.inMemoryFraudRepository();
            InMemoryFraudRepository repo2 = config.inMemoryFraudRepository();

            assertThat(repo1).isNotSameAs(repo2);
        }

        @Test
        @DisplayName("Should create new instance each time for MlModelPort")
        void shouldCreateNewInstanceEachTimeForMlModelPort() {
            FraudDetectionConfig config = new FraudDetectionConfig();

            MlModelPort port1 = config.mlModelPort();
            MlModelPort port2 = config.mlModelPort();

            assertThat(port1).isNotSameAs(port2);
        }

        @Test
        @DisplayName("Should create new instance each time for FraudDetectionPolicy")
        void shouldCreateNewInstanceEachTimeForPolicy() {
            FraudDetectionConfig config = new FraudDetectionConfig();

            FraudDetectionPolicy policy1 = config.fraudDetectionPolicy();
            FraudDetectionPolicy policy2 = config.fraudDetectionPolicy();

            assertThat(policy1).isNotSameAs(policy2);
        }
    }
}

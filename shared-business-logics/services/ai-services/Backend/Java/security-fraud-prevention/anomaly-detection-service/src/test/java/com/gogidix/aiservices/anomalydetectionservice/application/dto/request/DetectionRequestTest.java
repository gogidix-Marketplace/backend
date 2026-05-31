package com.gogidix.aiservices.anomalydetectionservice.application.dto.request;

import com.gogidix.aiservices.anomalydetectionservice.domain.model.DetectionAlgorithm;
import com.gogidix.aiservices.anomalydetectionservice.domain.model.SensitivityLevel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DetectionRequest DTO Tests")
class DetectionRequestTest {

    private Validator validator;
    private ValidatorFactory validatorFactory;

    @BeforeEach
    void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterEach
    void tearDown() {
        if (validatorFactory != null) {
            validatorFactory.close();
        }
    }

    @Nested
    @DisplayName("DataSource Field Tests")
    class DataSourceFieldTests {

        @Test
        @DisplayName("Should set and get data source")
        void shouldSetAndGetDataSource() {
            DetectionRequest request = new DetectionRequest();
            request.setDataSource("api-gateway-metrics");

            assertThat(request.getDataSource()).isEqualTo("api-gateway-metrics");
        }

        @Test
        @DisplayName("Should accept non-empty data source")
        void shouldAcceptNonEmptyDataSource() {
            DetectionRequest request = new DetectionRequest();
            request.setDataSource("database-logs");

            Set<ConstraintViolation<DetectionRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should reject empty data source")
        void shouldRejectEmptyDataSource() {
            DetectionRequest request = new DetectionRequest();
            request.setDataSource("");

            Set<ConstraintViolation<DetectionRequest>> violations = validator.validate(request);

            assertThat(violations).isNotEmpty();
            assertThat(violations).anyMatch(v -> v.getMessage().contains("NotBlank"));
        }

        @Test
        @DisplayName("Should reject null data source")
        void shouldRejectNullDataSource() {
            DetectionRequest request = new DetectionRequest();
            request.setDataSource(null);

            Set<ConstraintViolation<DetectionRequest>> violations = validator.validate(request);

            assertThat(violations).isNotEmpty();
            assertThat(violations).anyMatch(v -> v.getMessage().contains("NotBlank"));
        }

        @ParameterizedTest
        @ValueSource(strings = {"api-gateway-metrics", "database-logs", "application-traces", "network-flows"})
        @DisplayName("Should accept various data sources")
        void shouldAcceptVariousDataSources(String dataSource) {
            DetectionRequest request = new DetectionRequest();
            request.setDataSource(dataSource);

            Set<ConstraintViolation<DetectionRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }
    }

    @Nested
    @DisplayName("StartTime Field Tests")
    class StartTimeFieldTests {

        @Test
        @DisplayName("Should set and get start time")
        void shouldSetAndGetStartTime() {
            Instant startTime = Instant.now().minusSeconds(3600);
            DetectionRequest request = new DetectionRequest();
            request.setStartTime(startTime);

            assertThat(request.getStartTime()).isEqualTo(startTime);
        }

        @Test
        @DisplayName("Should accept null start time")
        void shouldAcceptNullStartTime() {
            DetectionRequest request = new DetectionRequest();
            request.setStartTime(null);
            request.setDataSource("test-source");

            Set<ConstraintViolation<DetectionRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should accept past start time")
        void shouldAcceptPastStartTime() {
            Instant pastTime = Instant.now().minusSeconds(7200);
            DetectionRequest request = new DetectionRequest();
            request.setStartTime(pastTime);

            assertThat(request.getStartTime()).isEqualTo(pastTime);
        }

        @Test
        @DisplayName("Should accept future start time")
        void shouldAcceptFutureStartTime() {
            Instant futureTime = Instant.now().plusSeconds(3600);
            DetectionRequest request = new DetectionRequest();
            request.setStartTime(futureTime);

            assertThat(request.getStartTime()).isEqualTo(futureTime);
        }
    }

    @Nested
    @DisplayName("EndTime Field Tests")
    class EndTimeFieldTests {

        @Test
        @DisplayName("Should set and get end time")
        void shouldSetAndGetEndTime() {
            Instant endTime = Instant.now();
            DetectionRequest request = new DetectionRequest();
            request.setEndTime(endTime);

            assertThat(request.getEndTime()).isEqualTo(endTime);
        }

        @Test
        @DisplayName("Should accept null end time")
        void shouldAcceptNullEndTime() {
            DetectionRequest request = new DetectionRequest();
            request.setEndTime(null);
            request.setDataSource("test-source");

            Set<ConstraintViolation<DetectionRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should accept past end time")
        void shouldAcceptPastEndTime() {
            Instant pastTime = Instant.now().minusSeconds(3600);
            DetectionRequest request = new DetectionRequest();
            request.setEndTime(pastTime);

            assertThat(request.getEndTime()).isEqualTo(pastTime);
        }

        @Test
        @DisplayName("Should accept future end time")
        void shouldAcceptFutureEndTime() {
            Instant futureTime = Instant.now().plusSeconds(3600);
            DetectionRequest request = new DetectionRequest();
            request.setEndTime(futureTime);

            assertThat(request.getEndTime()).isEqualTo(futureTime);
        }
    }

    @Nested
    @DisplayName("Sensitivity Field Tests")
    class SensitivityFieldTests {

        @ParameterizedTest
        @EnumSource(SensitivityLevel.class)
        @DisplayName("Should accept all sensitivity levels")
        void shouldAcceptAllSensitivityLevels(SensitivityLevel sensitivity) {
            DetectionRequest request = new DetectionRequest();
            request.setDataSource("test-source");
            request.setSensitivity(sensitivity);

            assertThat(request.getSensitivity()).isEqualTo(sensitivity);
        }

        @Test
        @DisplayName("Should have MEDIUM as default")
        void shouldHaveMEDIUMAsDefault() {
            DetectionRequest request = new DetectionRequest();

            assertThat(request.getSensitivity()).isEqualTo(SensitivityLevel.MEDIUM);
        }

        @ParameterizedTest
        @NullSource
        @DisplayName("Should accept null sensitivity")
        void shouldAcceptNullSensitivity(SensitivityLevel sensitivity) {
            DetectionRequest request = new DetectionRequest();
            request.setDataSource("test-source");
            request.setSensitivity(sensitivity);

            Set<ConstraintViolation<DetectionRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should set LOW sensitivity")
        void shouldSetLOWSensitivity() {
            DetectionRequest request = new DetectionRequest();
            request.setSensitivity(SensitivityLevel.LOW);

            assertThat(request.getSensitivity()).isEqualTo(SensitivityLevel.LOW);
        }

        @Test
        @DisplayName("Should set HIGH sensitivity")
        void shouldSetHIGHSensitivity() {
            DetectionRequest request = new DetectionRequest();
            request.setSensitivity(SensitivityLevel.HIGH);

            assertThat(request.getSensitivity()).isEqualTo(SensitivityLevel.HIGH);
        }
    }

    @Nested
    @DisplayName("Algorithms Field Tests")
    class AlgorithmsFieldTests {

        @Test
        @DisplayName("Should set and get algorithms")
        void shouldSetAndGetAlgorithms() {
            List<DetectionAlgorithm> algorithms = List.of(DetectionAlgorithm.ISOLATION_FOREST);
            DetectionRequest request = new DetectionRequest();
            request.setAlgorithms(algorithms);

            assertThat(request.getAlgorithms()).isEqualTo(algorithms);
        }

        @Test
        @DisplayName("Should accept null algorithms")
        void shouldAcceptNullAlgorithms() {
            DetectionRequest request = new DetectionRequest();
            request.setDataSource("test-source");
            request.setAlgorithms(null);

            Set<ConstraintViolation<DetectionRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should accept empty algorithms")
        void shouldAcceptEmptyAlgorithms() {
            DetectionRequest request = new DetectionRequest();
            request.setDataSource("test-source");
            request.setAlgorithms(List.of());

            Set<ConstraintViolation<DetectionRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should accept single algorithm")
        void shouldAcceptSingleAlgorithm() {
            List<DetectionAlgorithm> algorithms = List.of(DetectionAlgorithm.Z_SCORE);
            DetectionRequest request = new DetectionRequest();
            request.setAlgorithms(algorithms);

            assertThat(request.getAlgorithms()).hasSize(1);
        }

        @Test
        @DisplayName("Should accept multiple algorithms")
        void shouldAcceptMultipleAlgorithms() {
            List<DetectionAlgorithm> algorithms = List.of(
                    DetectionAlgorithm.ISOLATION_FOREST,
                    DetectionAlgorithm.Z_SCORE,
                    DetectionAlgorithm.LOCAL_OUTLIER_FACTOR
            );
            DetectionRequest request = new DetectionRequest();
            request.setAlgorithms(algorithms);

            assertThat(request.getAlgorithms()).hasSize(3);
        }

        @Test
        @DisplayName("Should accept all algorithm types")
        void shouldAcceptAllAlgorithmTypes() {
            List<DetectionAlgorithm> allAlgorithms = List.of(DetectionAlgorithm.values());
            DetectionRequest request = new DetectionRequest();
            request.setAlgorithms(allAlgorithms);

            assertThat(request.getAlgorithms()).hasSize(5);
        }
    }

    @Nested
    @DisplayName("Lombok Data Tests")
    class LombokDataTests {

        @Test
        @DisplayName("Should generate getters")
        void shouldGenerateGetters() {
            DetectionRequest request = new DetectionRequest();
            request.setDataSource("test-source");
            request.setSensitivity(SensitivityLevel.HIGH);

            assertThat(request.getDataSource()).isEqualTo("test-source");
            assertThat(request.getSensitivity()).isEqualTo(SensitivityLevel.HIGH);
        }

        @Test
        @DisplayName("Should generate setters")
        void shouldGenerateSetters() {
            DetectionRequest request = new DetectionRequest();

            request.setDataSource("source1");
            request.setSensitivity(SensitivityLevel.LOW);

            assertThat(request.getDataSource()).isEqualTo("source1");
            assertThat(request.getSensitivity()).isEqualTo(SensitivityLevel.LOW);

            request.setDataSource("source2");
            request.setSensitivity(SensitivityLevel.HIGH);

            assertThat(request.getDataSource()).isEqualTo("source2");
            assertThat(request.getSensitivity()).isEqualTo(SensitivityLevel.HIGH);
        }

        @Test
        @DisplayName("Should generate toString")
        void shouldGenerateToString() {
            DetectionRequest request = new DetectionRequest();
            request.setDataSource("test-source");

            String toString = request.toString();

            assertThat(toString).contains("test-source");
        }

        @Test
        @DisplayName("Should generate equals")
        void shouldGenerateEquals() {
            DetectionRequest request1 = new DetectionRequest();
            request1.setDataSource("test-source");
            request1.setSensitivity(SensitivityLevel.HIGH);

            DetectionRequest request2 = new DetectionRequest();
            request2.setDataSource("test-source");
            request2.setSensitivity(SensitivityLevel.HIGH);

            assertThat(request1).isEqualTo(request2);
        }

        @Test
        @DisplayName("Should generate hashCode")
        void shouldGenerateHashCode() {
            DetectionRequest request1 = new DetectionRequest();
            request1.setDataSource("test-source");

            DetectionRequest request2 = new DetectionRequest();
            request2.setDataSource("test-source");

            assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
        }
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create with no-args constructor")
        void shouldCreateWithNoArgsConstructor() {
            DetectionRequest request = new DetectionRequest();

            assertThat(request).isNotNull();
            assertThat(request.getSensitivity()).isEqualTo(SensitivityLevel.MEDIUM);
        }

        @Test
        @DisplayName("Should allow setting fields after construction")
        void shouldAllowSettingFieldsAfterConstruction() {
            DetectionRequest request = new DetectionRequest();

            request.setDataSource("test-source");
            request.setSensitivity(SensitivityLevel.HIGH);
            request.setAlgorithms(List.of(DetectionAlgorithm.Z_SCORE));

            assertThat(request.getDataSource()).isEqualTo("test-source");
            assertThat(request.getSensitivity()).isEqualTo(SensitivityLevel.HIGH);
            assertThat(request.getAlgorithms()).contains(DetectionAlgorithm.Z_SCORE);
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should pass validation with valid data")
        void shouldPassValidationWithValidData() {
            DetectionRequest request = new DetectionRequest();
            request.setDataSource("valid-source");

            Set<ConstraintViolation<DetectionRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should fail validation with blank data source")
        void shouldFailValidationWithBlankDataSource() {
            DetectionRequest request = new DetectionRequest();
            request.setDataSource("");

            Set<ConstraintViolation<DetectionRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
        }

        @Test
        @DisplayName("Should fail validation with null data source")
        void shouldFailValidationWithNullDataSource() {
            DetectionRequest request = new DetectionRequest();
            request.setDataSource(null);

            Set<ConstraintViolation<DetectionRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Time Range Tests")
    class TimeRangeTests {

        @Test
        @DisplayName("Should accept valid time range")
        void shouldAcceptValidTimeRange() {
            Instant start = Instant.now().minusSeconds(7200);
            Instant end = Instant.now();

            DetectionRequest request = new DetectionRequest();
            request.setDataSource("test-source");
            request.setStartTime(start);
            request.setEndTime(end);

            assertThat(request.getStartTime()).isBefore(request.getEndTime());
        }

        @Test
        @DisplayName("Should accept null time range")
        void shouldAcceptNullTimeRange() {
            DetectionRequest request = new DetectionRequest();
            request.setDataSource("test-source");
            request.setStartTime(null);
            request.setEndTime(null);

            Set<ConstraintViolation<DetectionRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }
    }

    @Nested
    @DisplayName("Use Case Tests")
    class UseCaseTests {

        @Test
        @DisplayName("Should support API monitoring request")
        void shouldSupportApiMonitoringRequest() {
            DetectionRequest request = new DetectionRequest();
            request.setDataSource("api-gateway-metrics");
            request.setSensitivity(SensitivityLevel.HIGH);
            request.setAlgorithms(List.of(DetectionAlgorithm.ISOLATION_FOREST));

            assertThat(request.getDataSource()).isEqualTo("api-gateway-metrics");
            assertThat(request.getSensitivity()).isEqualTo(SensitivityLevel.HIGH);
        }

        @Test
        @DisplayName("Should support database monitoring request")
        void shouldSupportDatabaseMonitoringRequest() {
            DetectionRequest request = new DetectionRequest();
            request.setDataSource("database-logs");
            request.setSensitivity(SensitivityLevel.MEDIUM);
            request.setAlgorithms(List.of(DetectionAlgorithm.Z_SCORE));

            assertThat(request.getDataSource()).isEqualTo("database-logs");
            assertThat(request.getSensitivity()).isEqualTo(SensitivityLevel.MEDIUM);
        }

        @Test
        @DisplayName("Should support application monitoring request")
        void shouldSupportApplicationMonitoringRequest() {
            DetectionRequest request = new DetectionRequest();
            request.setDataSource("application-traces");
            request.setSensitivity(SensitivityLevel.LOW);
            request.setAlgorithms(List.of(DetectionAlgorithm.AUTOENCODER));

            assertThat(request.getDataSource()).isEqualTo("application-traces");
            assertThat(request.getSensitivity()).isEqualTo(SensitivityLevel.LOW);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle very long data source name")
        void shouldHandleVeryLongDataSourceName() {
            String longDataSource = "source-" + "x".repeat(200);
            DetectionRequest request = new DetectionRequest();
            request.setDataSource(longDataSource);

            assertThat(request.getDataSource()).isEqualTo(longDataSource);
        }

        @Test
        @DisplayName("Should handle empty algorithms list")
        void shouldHandleEmptyAlgorithmsList() {
            DetectionRequest request = new DetectionRequest();
            request.setDataSource("test-source");
            request.setAlgorithms(List.of());

            Set<ConstraintViolation<DetectionRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }
    }
}

package com.gogidix.aiservices.aifrauddetectionservice.infrastructure.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for SloConfig.
 * Tests SLO configuration, threshold retrieval, and compliance checking.
 */
@DisplayName("Financial-Grade: SLO Configuration Tests")
class SloConfigTest {

    private SloConfig sloConfig;

    @BeforeEach
    void setUp() {
        sloConfig = new SloConfig();
    }

    @Nested
    @DisplayName("SLO List Management")
    class SloListTests {

        @Test
        @DisplayName("Should set and get SLO list")
        void shouldSetAndGetSloList() {
            List<SloConfig.SloDefinition> sloList = List.of(createTestSloDefinition("test-slo"));
            sloConfig.setSlo(sloList);

            assertThat(sloConfig.getSlo()).isNotNull();
            assertThat(sloConfig.getSlo()).hasSize(1);
            assertThat(sloConfig.getSlo().get(0).getName()).isEqualTo("test-slo");
        }

        @Test
        @DisplayName("Should handle empty SLO list")
        void shouldHandleEmptySloList() {
            sloConfig.setSlo(List.of());

            assertThat(sloConfig.getSlo()).isNotNull();
            assertThat(sloConfig.getSlo()).isEmpty();
        }

        @Test
        @DisplayName("Should handle null SLO list")
        void shouldHandleNullSloList() {
            sloConfig.setSlo(null);

            assertThat(sloConfig.getSlo()).isNull();
        }
    }

    @Nested
    @DisplayName("SLO Retrieval by Name")
    class SloRetrievalTests {

        @Test
        @DisplayName("Should find SLO by name")
        void shouldFindSloByName() {
            SloConfig.SloDefinition slo1 = createTestSloDefinition("p95-latency");
            SloConfig.SloDefinition slo2 = createTestSloDefinition("p99-latency");
            sloConfig.setSlo(List.of(slo1, slo2));

            SloConfig.SloDefinition found = sloConfig.getSloByName("p95-latency");

            assertThat(found).isNotNull();
            assertThat(found.getName()).isEqualTo("p95-latency");
        }

        @Test
        @DisplayName("Should return null when SLO not found")
        void shouldReturnNullWhenSloNotFound() {
            SloConfig.SloDefinition slo = createTestSloDefinition("other-slo");
            sloConfig.setSlo(List.of(slo));

            SloConfig.SloDefinition found = sloConfig.getSloByName("non-existent");

            assertThat(found).isNull();
        }

        @Test
        @DisplayName("Should throw NullPointerException when SLO list is null")
        void shouldThrowNullPointerExceptionWhenSloListIsNull() {
            sloConfig.setSlo(null);

            assertThatThrownBy(() -> sloConfig.getSloByName("any-slo"))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Latency Threshold Retrieval")
    class LatencyThresholdTests {

        @Test
        @DisplayName("Should get latency threshold from SLO definition")
        void shouldGetLatencyThreshold() {
            SloConfig.SloDefinition slo = createTestSloDefinitionWithThresholds("latency-slo",
                    Map.of("p95_ms", 500, "p99_ms", 1000));
            sloConfig.setSlo(List.of(slo));

            long threshold = sloConfig.getLatencyThreshold("latency-slo", "p95");

            assertThat(threshold).isEqualTo(500L);
        }

        @Test
        @DisplayName("Should get P99 latency threshold")
        void shouldGetP99LatencyThreshold() {
            SloConfig.SloDefinition slo = createTestSloDefinitionWithThresholds("latency-slo",
                    Map.of("p95_ms", 500, "p99_ms", 1000));
            sloConfig.setSlo(List.of(slo));

            long threshold = sloConfig.getLatencyThreshold("latency-slo", "p99");

            assertThat(threshold).isEqualTo(1000L);
        }

        @Test
        @DisplayName("Should return default threshold when SLO not found")
        void shouldReturnDefaultThresholdWhenSloNotFound() {
            sloConfig.setSlo(List.of());

            long threshold = sloConfig.getLatencyThreshold("non-existent", "p95");

            assertThat(threshold).isEqualTo(1000L); // Default
        }

        @Test
        @DisplayName("Should return default threshold when thresholds map is null")
        void shouldReturnDefaultThresholdWhenThresholdsIsNull() {
            SloConfig.SloDefinition slo = new SloConfig.SloDefinition();
            slo.setName("test-slo");
            slo.setThresholds(null);
            sloConfig.setSlo(List.of(slo));

            long threshold = sloConfig.getLatencyThreshold("test-slo", "p95");

            assertThat(threshold).isEqualTo(1000L); // Default
        }

        @Test
        @DisplayName("Should return default threshold when threshold type not found")
        void shouldReturnDefaultThresholdWhenTypeNotFound() {
            SloConfig.SloDefinition slo = createTestSloDefinitionWithThresholds("test-slo",
                    Map.of("other_ms", 200));
            sloConfig.setSlo(List.of(slo));

            long threshold = sloConfig.getLatencyThreshold("test-slo", "p95");

            assertThat(threshold).isEqualTo(1000L); // Default
        }

        @Test
        @DisplayName("Should return default threshold when value is not a Number")
        void shouldReturnDefaultThresholdWhenValueIsNotNumber() {
            Map<String, Object> thresholds = new HashMap<>();
            thresholds.put("p95_ms", "not-a-number");
            SloConfig.SloDefinition slo = createTestSloDefinitionWithThresholds("test-slo", thresholds);
            sloConfig.setSlo(List.of(slo));

            long threshold = sloConfig.getLatencyThreshold("test-slo", "p95");

            assertThat(threshold).isEqualTo(1000L); // Default
        }
    }

    @Nested
    @DisplayName("SLO Compliance Checking")
    class SloComplianceTests {

        @Test
        @DisplayName("Should return true when latency meets SLO")
        void shouldReturnTrueWhenLatencyMeetsSlo() {
            Map<String, Object> objective = new HashMap<>();
            objective.put("target_latency_ms", 500);
            SloConfig.SloDefinition slo = createTestSloDefinitionWithObjective("latency-slo", objective);
            sloConfig.setSlo(List.of(slo));

            boolean meets = sloConfig.meetsSlo("latency-slo", 400);

            assertThat(meets).isTrue();
        }

        @Test
        @DisplayName("Should return true when latency exactly equals SLO")
        void shouldReturnTrueWhenLatencyEqualsSlo() {
            Map<String, Object> objective = new HashMap<>();
            objective.put("target_latency_ms", 500);
            SloConfig.SloDefinition slo = createTestSloDefinitionWithObjective("latency-slo", objective);
            sloConfig.setSlo(List.of(slo));

            boolean meets = sloConfig.meetsSlo("latency-slo", 500);

            assertThat(meets).isTrue();
        }

        @Test
        @DisplayName("Should return false when latency exceeds SLO")
        void shouldReturnFalseWhenLatencyExceedsSlo() {
            Map<String, Object> objective = new HashMap<>();
            objective.put("target_latency_ms", 500);
            SloConfig.SloDefinition slo = createTestSloDefinitionWithObjective("latency-slo", objective);
            sloConfig.setSlo(List.of(slo));

            boolean meets = sloConfig.meetsSlo("latency-slo", 600);

            assertThat(meets).isFalse();
        }

        @Test
        @DisplayName("Should return true when SLO not found (default permissive)")
        void shouldReturnTrueWhenSloNotFound() {
            sloConfig.setSlo(List.of());

            boolean meets = sloConfig.meetsSlo("non-existent", 10000);

            assertThat(meets).isTrue();
        }

        @Test
        @DisplayName("Should return true when objective is null")
        void shouldReturnTrueWhenObjectiveIsNull() {
            SloConfig.SloDefinition slo = new SloConfig.SloDefinition();
            slo.setName("test-slo");
            slo.setObjective(null);
            sloConfig.setSlo(List.of(slo));

            boolean meets = sloConfig.meetsSlo("test-slo", 10000);

            assertThat(meets).isTrue();
        }

        @Test
        @DisplayName("Should return true when target_latency_ms is not a Number")
        void shouldReturnTrueWhenTargetIsNotNumber() {
            Map<String, Object> objective = new HashMap<>();
            objective.put("target_latency_ms", "not-a-number");
            SloConfig.SloDefinition slo = createTestSloDefinitionWithObjective("test-slo", objective);
            sloConfig.setSlo(List.of(slo));

            boolean meets = sloConfig.meetsSlo("test-slo", 10000);

            assertThat(meets).isTrue();
        }
    }

    @Nested
    @DisplayName("SloDefinition Inner Class")
    class SloDefinitionTests {

        @Test
        @DisplayName("Should set and get all properties")
        void shouldSetAndGetAllProperties() {
            SloConfig.SloDefinition definition = new SloConfig.SloDefinition();
            definition.setName("test-slo");
            definition.setDescription("Test SLO description");

            Map<String, Object> objective = Map.of("target_latency_ms", 500);
            definition.setObjective(objective);

            Map<String, Object> thresholds = Map.of("p95_ms", 500, "p99_ms", 1000);
            definition.setThresholds(thresholds);

            List<String> tags = List.of("latency", "critical");
            definition.setTags(tags);

            assertThat(definition.getName()).isEqualTo("test-slo");
            assertThat(definition.getDescription()).isEqualTo("Test SLO description");
            assertThat(definition.getObjective()).isEqualTo(objective);
            assertThat(definition.getThresholds()).isEqualTo(thresholds);
            assertThat(definition.getTags()).isEqualTo(tags);
        }

        @Test
        @DisplayName("Should handle null values in properties")
        void shouldHandleNullValuesInProperties() {
            SloConfig.SloDefinition definition = new SloConfig.SloDefinition();

            assertThat(definition.getName()).isNull();
            assertThat(definition.getDescription()).isNull();
            assertThat(definition.getObjective()).isNull();
            assertThat(definition.getThresholds()).isNull();
            assertThat(definition.getTags()).isNull();
        }
    }

    // Helper methods

    private SloConfig.SloDefinition createTestSloDefinition(String name) {
        SloConfig.SloDefinition definition = new SloConfig.SloDefinition();
        definition.setName(name);
        definition.setDescription("Test SLO: " + name);
        return definition;
    }

    private SloConfig.SloDefinition createTestSloDefinitionWithThresholds(String name, Map<String, Object> thresholds) {
        SloConfig.SloDefinition definition = new SloConfig.SloDefinition();
        definition.setName(name);
        definition.setThresholds(new HashMap<>(thresholds));
        return definition;
    }

    private SloConfig.SloDefinition createTestSloDefinitionWithObjective(String name, Map<String, Object> objective) {
        SloConfig.SloDefinition definition = new SloConfig.SloDefinition();
        definition.setName(name);
        definition.setObjective(new HashMap<>(objective));
        return definition;
    }
}

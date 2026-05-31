package com.gogidix.aiservices.aifrauddetectionservice.infrastructure.persistence.mongodb;

import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudAction;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudAnalysisResult;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudPattern;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.RiskLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Financial-Grade Entity Mapping Tests.
 *
 * Tests the bidirectional mapping between:
 * - Domain models (FraudAnalysisResult, FraudPattern)
 * - MongoDB entities (FraudAnalysisResultEntity, FraudPatternEntity)
 *
 * Coverage areas:
 * 1. Domain -> Entity construction
 * 2. Entity -> Domain model conversion
 * 3. Entity update from domain model
 * 4. Null and edge case handling
 * 5. Enum mapping (RiskLevel, FraudAction)
 */
@DisplayName("Financial-Grade: Entity Mapping Tests")
class EntityMappingTest {

    @Nested
    @DisplayName("FraudAnalysisResultEntity - Domain to Entity Mapping")
    class FraudAnalysisResultEntityMappingTests {

        @Test
        @DisplayName("Should construct entity from domain model with all fields")
        void shouldConstructEntityFromDomainModelWithAllFields() {
            Instant now = Instant.now();
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId("analysis-123")
                    .transactionId("txn-456")
                    .userId("user-789")
                    .tenantId("tenant-001")
                    .fraudScore(0.85)
                    .riskLevel(RiskLevel.HIGH)
                    .recommendedAction(FraudAction.BLOCK)
                    .reasons(List.of("Reason 1", "Reason 2"))
                    .timestamp(now)
                    .modelVersion("v1.0.0")
                    .build();

            FraudAnalysisResultEntity entity = new FraudAnalysisResultEntity(result);

            assertThat(entity.getAnalysisId()).isEqualTo("analysis-123");
            assertThat(entity.getTransactionId()).isEqualTo("txn-456");
            assertThat(entity.getUserId()).isEqualTo("user-789");
            assertThat(entity.getTenantId()).isEqualTo("tenant-001");
            assertThat(entity.getFraudScore()).isEqualTo(0.85);
            assertThat(entity.getRiskLevel()).isEqualTo("HIGH");
            assertThat(entity.getRecommendedAction()).isEqualTo("BLOCK");
            assertThat(entity.getReasons()).containsExactly("Reason 1", "Reason 2");
            assertThat(entity.getTimestamp()).isEqualTo(now);
            assertThat(entity.getModelVersion()).isEqualTo("v1.0.0");
            assertThat(entity.getCreatedAt()).isNotNull();
            assertThat(entity.getUpdatedAt()).isNotNull();
        }

        @Test
        @DisplayName("Should construct entity with null enums")
        void shouldConstructEntityWithNullEnums() {
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId("analysis-null")
                    .transactionId("txn-null")
                    .userId("user-null")
                    .tenantId("tenant-null")
                    .fraudScore(0.5)
                    .riskLevel(null)
                    .recommendedAction(null)
                    .reasons(List.of())
                    .timestamp(Instant.now())
                    .modelVersion("v1.0.0")
                    .build();

            FraudAnalysisResultEntity entity = new FraudAnalysisResultEntity(result);

            assertThat(entity.getRiskLevel()).isNull();
            assertThat(entity.getRecommendedAction()).isNull();
        }

        @Test
        @DisplayName("Should construct entity with null reasons list")
        void shouldConstructEntityWithNullReasonsList() {
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId("analysis-null-reasons")
                    .transactionId("txn-null-reasons")
                    .userId("user-null-reasons")
                    .tenantId("tenant-null-reasons")
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(null)
                    .timestamp(Instant.now())
                    .modelVersion("v1.0.0")
                    .build();

            FraudAnalysisResultEntity entity = new FraudAnalysisResultEntity(result);

            assertThat(entity.getReasons()).isNull();
        }
    }

    @Nested
    @DisplayName("FraudAnalysisResultEntity - Entity to Domain Mapping")
    class FraudAnalysisResultDomainMappingTests {

        @Test
        @DisplayName("Should convert entity to domain model with all fields")
        void shouldConvertEntityToDomainModelWithAllFields() {
            Instant now = Instant.now();
            FraudAnalysisResultEntity entity = new FraudAnalysisResultEntity();
            entity.setAnalysisId("analysis-123");
            entity.setTransactionId("txn-456");
            entity.setUserId("user-789");
            entity.setTenantId("tenant-001");
            entity.setFraudScore(0.85);
            entity.setRiskLevel("HIGH");
            entity.setRecommendedAction("BLOCK");
            entity.setReasons(List.of("Reason 1", "Reason 2"));
            entity.setTimestamp(now);
            entity.setModelVersion("v1.0.0");

            FraudAnalysisResult result = entity.toDomainModel();

            assertThat(result.getAnalysisId()).isEqualTo("analysis-123");
            assertThat(result.getTransactionId()).isEqualTo("txn-456");
            assertThat(result.getUserId()).isEqualTo("user-789");
            assertThat(result.getTenantId()).isEqualTo("tenant-001");
            assertThat(result.getFraudScore()).isEqualTo(0.85);
            assertThat(result.getRiskLevel()).isEqualTo(RiskLevel.HIGH);
            assertThat(result.getRecommendedAction()).isEqualTo(FraudAction.BLOCK);
            assertThat(result.getReasons()).containsExactly("Reason 1", "Reason 2");
            assertThat(result.getTimestamp()).isEqualTo(now);
            assertThat(result.getModelVersion()).isEqualTo("v1.0.0");
        }

        @Test
        @DisplayName("Should convert entity with null enum strings to null enums")
        void shouldConvertEntityWithNullEnumStringsToNullEnums() {
            FraudAnalysisResultEntity entity = new FraudAnalysisResultEntity();
            entity.setAnalysisId("analysis-null");
            entity.setTransactionId("txn-null");
            entity.setUserId("user-null");
            entity.setTenantId("tenant-null");
            entity.setFraudScore(0.5);
            entity.setRiskLevel(null);
            entity.setRecommendedAction(null);
            entity.setReasons(List.of());
            entity.setTimestamp(Instant.now());
            entity.setModelVersion("v1.0.0");

            FraudAnalysisResult result = entity.toDomainModel();

            assertThat(result.getRiskLevel()).isNull();
            assertThat(result.getRecommendedAction()).isNull();
        }

        @Test
        @DisplayName("Should convert all risk levels correctly")
        void shouldConvertAllRiskLevelsCorrectly() {
            FraudAnalysisResultEntity entity = new FraudAnalysisResultEntity();
            entity.setAnalysisId("analysis-risk");
            entity.setTransactionId("txn-risk");
            entity.setUserId("user-risk");
            entity.setTenantId("tenant-risk");
            entity.setFraudScore(0.5);
            entity.setReasons(List.of());
            entity.setTimestamp(Instant.now());
            entity.setModelVersion("v1.0.0");

            for (RiskLevel level : RiskLevel.values()) {
                entity.setRiskLevel(level.name());
                FraudAnalysisResult result = entity.toDomainModel();
                assertThat(result.getRiskLevel()).isEqualTo(level);
            }
        }

        @Test
        @DisplayName("Should convert all fraud actions correctly")
        void shouldConvertAllFraudActionsCorrectly() {
            FraudAnalysisResultEntity entity = new FraudAnalysisResultEntity();
            entity.setAnalysisId("analysis-action");
            entity.setTransactionId("txn-action");
            entity.setUserId("user-action");
            entity.setTenantId("tenant-action");
            entity.setFraudScore(0.5);
            entity.setReasons(List.of());
            entity.setTimestamp(Instant.now());
            entity.setModelVersion("v1.0.0");

            for (FraudAction action : FraudAction.values()) {
                entity.setRecommendedAction(action.name());
                FraudAnalysisResult result = entity.toDomainModel();
                assertThat(result.getRecommendedAction()).isEqualTo(action);
            }
        }
    }

    @Nested
    @DisplayName("FraudAnalysisResultEntity - Update Operations")
    class FraudAnalysisResultUpdateTests {

        @Test
        @DisplayName("Should update entity from domain model")
        void shouldUpdateEntityFromDomainModel() {
            FraudAnalysisResultEntity entity = new FraudAnalysisResultEntity();
            entity.setAnalysisId("analysis-update");
            entity.setFraudScore(0.3);
            entity.setRiskLevel("LOW");
            entity.setRecommendedAction("ALLOW");
            entity.setReasons(List.of("Old reason"));
            Instant originalUpdatedAt = entity.getUpdatedAt();

            FraudAnalysisResult update = FraudAnalysisResult.builder()
                    .analysisId("analysis-update")
                    .fraudScore(0.9)
                    .riskLevel(RiskLevel.HIGH)
                    .recommendedAction(FraudAction.BLOCK)
                    .reasons(List.of("New reason"))
                    .build();

            entity.updateFrom(update);

            assertThat(entity.getFraudScore()).isEqualTo(0.9);
            assertThat(entity.getRiskLevel()).isEqualTo("HIGH");
            assertThat(entity.getRecommendedAction()).isEqualTo("BLOCK");
            assertThat(entity.getReasons()).containsExactly("New reason");
            assertThat(entity.getUpdatedAt()).isNotNull();
            if (originalUpdatedAt != null) {
                assertThat(entity.getUpdatedAt()).isAfterOrEqualTo(originalUpdatedAt);
            }
        }

        @Test
        @DisplayName("Should handle update with null enums")
        void shouldHandleUpdateWithNullEnums() {
            FraudAnalysisResultEntity entity = new FraudAnalysisResultEntity();
            entity.setAnalysisId("analysis-null-update");
            entity.setRiskLevel("MEDIUM");
            entity.setRecommendedAction("REVIEW");

            FraudAnalysisResult update = FraudAnalysisResult.builder()
                    .analysisId("analysis-null-update")
                    .riskLevel(null)
                    .recommendedAction(null)
                    .reasons(List.of())
                    .build();

            entity.updateFrom(update);

            assertThat(entity.getRiskLevel()).isNull();
            assertThat(entity.getRecommendedAction()).isNull();
        }
    }

    @Nested
    @DisplayName("FraudPatternEntity - Domain to Entity Mapping")
    class FraudPatternEntityMappingTests {

        @Test
        @DisplayName("Should construct entity from domain model with all fields")
        void shouldConstructEntityFromDomainModelWithAllFields() {
            Instant now = Instant.now();
            FraudPattern pattern = FraudPattern.builder()
                    .patternId("pattern-123")
                    .tenantId("tenant-001")
                    .patternName("Test Pattern")
                    .description("Test description")
                    .confidenceScore(0.9)
                    .lastSeen(now)
                    .occurrenceCount(5)
                    .build();

            FraudPatternEntity entity = new FraudPatternEntity(pattern);

            assertThat(entity.getPatternId()).isEqualTo("pattern-123");
            assertThat(entity.getTenantId()).isEqualTo("tenant-001");
            assertThat(entity.getPatternName()).isEqualTo("Test Pattern");
            assertThat(entity.getDescription()).isEqualTo("Test description");
            assertThat(entity.getConfidenceScore()).isEqualTo(0.9);
            assertThat(entity.getLastSeen()).isEqualTo(now);
            assertThat(entity.getOccurrenceCount()).isEqualTo(5);
            assertThat(entity.isActive()).isTrue();
            assertThat(entity.getCreatedAt()).isNotNull();
            assertThat(entity.getUpdatedAt()).isNotNull();
        }

        @Test
        @DisplayName("Should set active to true on construction")
        void shouldSetActiveToTrueOnConstruction() {
            FraudPattern pattern = FraudPattern.builder()
                    .patternId("pattern-active")
                    .tenantId("tenant-001")
                    .patternName("Active Pattern")
                    .description("Description")
                    .confidenceScore(0.8)
                    .lastSeen(Instant.now())
                    .occurrenceCount(1)
                    .build();

            FraudPatternEntity entity = new FraudPatternEntity(pattern);

            assertThat(entity.isActive()).isTrue();
        }
    }

    @Nested
    @DisplayName("FraudPatternEntity - Entity to Domain Mapping")
    class FraudPatternDomainMappingTests {

        @Test
        @DisplayName("Should convert entity to domain model with all fields")
        void shouldConvertEntityToDomainModelWithAllFields() {
            Instant now = Instant.now();
            FraudPatternEntity entity = new FraudPatternEntity();
            entity.setPatternId("pattern-123");
            entity.setTenantId("tenant-001");
            entity.setPatternName("Test Pattern");
            entity.setDescription("Test description");
            entity.setConfidenceScore(0.9);
            entity.setLastSeen(now);
            entity.setOccurrenceCount(5);
            entity.setActive(true);

            FraudPattern pattern = entity.toDomainModel();

            assertThat(pattern.getPatternId()).isEqualTo("pattern-123");
            assertThat(pattern.getTenantId()).isEqualTo("tenant-001");
            assertThat(pattern.getPatternName()).isEqualTo("Test Pattern");
            assertThat(pattern.getDescription()).isEqualTo("Test description");
            assertThat(pattern.getConfidenceScore()).isEqualTo(0.9);
            assertThat(pattern.getLastSeen()).isEqualTo(now);
            assertThat(pattern.getOccurrenceCount()).isEqualTo(5);
        }

        @Test
        @DisplayName("Should convert entity with boundary confidence scores")
        void shouldConvertEntityWithBoundaryConfidenceScores() {
            FraudPatternEntity entity = new FraudPatternEntity();
            entity.setPatternId("pattern-boundary");
            entity.setTenantId("tenant-001");
            entity.setPatternName("Boundary Pattern");
            entity.setDescription("Description");
            entity.setLastSeen(Instant.now());
            entity.setOccurrenceCount(1);

            // Test minimum
            entity.setConfidenceScore(0.0);
            assertThat(entity.toDomainModel().getConfidenceScore()).isEqualTo(0.0);

            // Test maximum
            entity.setConfidenceScore(1.0);
            assertThat(entity.toDomainModel().getConfidenceScore()).isEqualTo(1.0);

            // Test mid-point
            entity.setConfidenceScore(0.5);
            assertThat(entity.toDomainModel().getConfidenceScore()).isEqualTo(0.5);
        }
    }

    @Nested
    @DisplayName("FraudPatternEntity - Update and Operations")
    class FraudPatternUpdateTests {

        @Test
        @DisplayName("Should update entity from domain model")
        void shouldUpdateEntityFromDomainModel() {
            FraudPatternEntity entity = new FraudPatternEntity();
            entity.setPatternId("pattern-update");
            entity.setPatternName("Old Name");
            entity.setDescription("Old Description");
            entity.setConfidenceScore(0.5);
            Instant originalUpdatedAt = entity.getUpdatedAt();

            FraudPattern update = FraudPattern.builder()
                    .patternId("pattern-update")
                    .patternName("New Name")
                    .description("New Description")
                    .confidenceScore(0.95)
                    .lastSeen(Instant.now())
                    .occurrenceCount(10)
                    .build();

            entity.updateFrom(update);

            assertThat(entity.getPatternName()).isEqualTo("New Name");
            assertThat(entity.getDescription()).isEqualTo("New Description");
            assertThat(entity.getConfidenceScore()).isEqualTo(0.95);
            assertThat(entity.getUpdatedAt()).isNotNull();
            if (originalUpdatedAt != null) {
                assertThat(entity.getUpdatedAt()).isAfterOrEqualTo(originalUpdatedAt);
            }
        }

        @Test
        @DisplayName("Should increment occurrence count")
        void shouldIncrementOccurrenceCount() {
            FraudPatternEntity entity = new FraudPatternEntity();
            entity.setPatternId("pattern-increment");
            entity.setOccurrenceCount(5);
            Instant originalLastSeen = entity.getLastSeen();

            entity.incrementOccurrence();

            assertThat(entity.getOccurrenceCount()).isEqualTo(6);
            assertThat(entity.getLastSeen()).isNotNull();
            if (originalLastSeen != null) {
                assertThat(entity.getLastSeen()).isAfterOrEqualTo(originalLastSeen);
            }
            assertThat(entity.getUpdatedAt()).isNotNull();
        }

        @Test
        @DisplayName("Should deactivate pattern")
        void shouldDeactivatePattern() {
            FraudPatternEntity entity = new FraudPatternEntity();
            entity.setPatternId("pattern-deactivate");
            entity.setActive(true);
            Instant originalUpdatedAt = entity.getUpdatedAt();

            entity.deactivate();

            assertThat(entity.isActive()).isFalse();
            assertThat(entity.getUpdatedAt()).isNotNull();
            if (originalUpdatedAt != null) {
                assertThat(entity.getUpdatedAt()).isAfterOrEqualTo(originalUpdatedAt);
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases and Null Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle double round-trip conversion")
        void shouldHandleDoubleRoundTripConversion() {
            FraudAnalysisResult original = FraudAnalysisResult.builder()
                    .analysisId("round-trip")
                    .transactionId("txn-round-trip")
                    .userId("user-round-trip")
                    .tenantId("tenant-round-trip")
                    .fraudScore(0.75)
                    .riskLevel(RiskLevel.MEDIUM)
                    .recommendedAction(FraudAction.REVIEW)
                    .reasons(List.of("Test"))
                    .timestamp(Instant.now())
                    .modelVersion("v1.0.0")
                    .build();

            FraudAnalysisResultEntity entity = new FraudAnalysisResultEntity(original);
            FraudAnalysisResult converted = entity.toDomainModel();

            assertThat(converted.getAnalysisId()).isEqualTo(original.getAnalysisId());
            assertThat(converted.getTransactionId()).isEqualTo(original.getTransactionId());
            assertThat(converted.getUserId()).isEqualTo(original.getUserId());
            assertThat(converted.getTenantId()).isEqualTo(original.getTenantId());
            assertThat(converted.getFraudScore()).isEqualTo(original.getFraudScore());
            assertThat(converted.getRiskLevel()).isEqualTo(original.getRiskLevel());
            assertThat(converted.getRecommendedAction()).isEqualTo(original.getRecommendedAction());
            assertThat(converted.getReasons()).isEqualTo(original.getReasons());
            assertThat(converted.getTimestamp()).isEqualTo(original.getTimestamp());
            assertThat(converted.getModelVersion()).isEqualTo(original.getModelVersion());
        }

        @Test
        @DisplayName("Should handle pattern round-trip conversion")
        void shouldHandlePatternRoundTripConversion() {
            FraudPattern original = FraudPattern.builder()
                    .patternId("pattern-round-trip")
                    .tenantId("tenant-round-trip")
                    .patternName("Round Trip Pattern")
                    .description("Testing round trip")
                    .confidenceScore(0.85)
                    .lastSeen(Instant.now())
                    .occurrenceCount(7)
                    .build();

            FraudPatternEntity entity = new FraudPatternEntity(original);
            FraudPattern converted = entity.toDomainModel();

            assertThat(converted.getPatternId()).isEqualTo(original.getPatternId());
            assertThat(converted.getTenantId()).isEqualTo(original.getTenantId());
            assertThat(converted.getPatternName()).isEqualTo(original.getPatternName());
            assertThat(converted.getDescription()).isEqualTo(original.getDescription());
            assertThat(converted.getConfidenceScore()).isEqualTo(original.getConfidenceScore());
            assertThat(converted.getLastSeen()).isEqualTo(original.getLastSeen());
            assertThat(converted.getOccurrenceCount()).isEqualTo(original.getOccurrenceCount());
        }
    }
}

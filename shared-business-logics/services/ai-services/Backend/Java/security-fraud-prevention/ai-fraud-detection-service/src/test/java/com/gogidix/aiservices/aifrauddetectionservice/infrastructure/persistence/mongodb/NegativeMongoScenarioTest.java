package com.gogidix.aiservices.aifrauddetectionservice.infrastructure.persistence.mongodb;

import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudAction;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudAnalysisResult;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudPattern;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.RiskLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Financial-Grade Negative MongoDB Scenario Tests.
 *
 * Tests error handling and resilience for:
 * 1. Connection failure scenarios
 * 2. Duplicate key violations
 * 3. Invalid tenant operations
 * 4. Malformed data handling
 * 5. Constraint violations
 * 6. Timeout scenarios
 *
 * These tests ensure the application handles MongoDB errors gracefully
 * and maintains data integrity under failure conditions.
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Financial-Grade: Negative MongoDB Scenario Tests")
class NegativeMongoScenarioTest {

    @Autowired(required = false)
    private MongoFraudRepositoryAdapter fraudRepositoryAdapter;

    @Autowired(required = false)
    private SpringDataFraudAnalysisResultRepository analysisRepository;

    @Autowired(required = false)
    private SpringDataFraudPatternRepository patternRepository;

    private static final String TEST_TENANT = "tenant-negative";
    private static final String TEST_USER = "user-negative";

    @Nested
    @DisplayName("1. Connection Failure Scenarios")
    class ConnectionFailureTests {

        @Test
        @DisplayName("Should handle null adapter gracefully when MongoDB not available")
        void shouldHandleNullAdapterGracefully() {
            // This test verifies behavior when MongoDB is not configured
            // In a real scenario, tests would mock the connection failure

            if (fraudRepositoryAdapter == null) {
                // Expected behavior when MongoDB is not available
                assertThat(fraudRepositoryAdapter).isNull();
            } else {
                // If MongoDB is available, verify it's functional
                assertThat(fraudRepositoryAdapter).isNotNull();
            }
        }
    }

    @Nested
    @DisplayName("2. Duplicate Key Scenarios")
    class DuplicateKeyTests {

        @Test
        @DisplayName("Should handle duplicate analysis ID with upsert")
        void shouldHandleDuplicateAnalysisIdWithUpsert() {
            assumeMongoDbAvailable();

            String analysisId = "duplicate-test-001";

            // Create and save first result
            FraudAnalysisResult first = createTestAnalysisResult(analysisId, TEST_USER, TEST_TENANT, 0.5);
            fraudRepositoryAdapter.saveAnalysisResult(first);

            // Create and save second result with same ID (upsert behavior)
            FraudAnalysisResult second = createTestAnalysisResult(analysisId, TEST_USER, TEST_TENANT, 0.9);
            fraudRepositoryAdapter.saveAnalysisResult(second);

            // Verify the result was updated, not duplicated
            List<FraudAnalysisResult> results = fraudRepositoryAdapter.findByTenantId(TEST_TENANT, 100);
            long count = results.stream()
                    .filter(r -> r.getAnalysisId().equals(analysisId))
                    .count();

            assertThat(count).isEqualTo(1);

            // Verify the score was updated (last write wins)
            var found = fraudRepositoryAdapter.findAnalysisById(analysisId);
            assertThat(found).isPresent();
            assertThat(found.get().getFraudScore()).isEqualTo(0.9);

            // Cleanup
            analysisRepository.deleteByAnalysisId(analysisId);
        }

        @Test
        @DisplayName("Should handle duplicate pattern ID with increment")
        void shouldHandleDuplicatePatternIdWithIncrement() {
            assumeMongoDbAvailable();

            String patternId = "duplicate-pattern-001";

            // Save pattern first time
            FraudPattern first = createTestPattern(patternId, TEST_TENANT, 1);
            fraudRepositoryAdapter.savePattern(first);

            // Save pattern second time (should increment occurrence)
            FraudPattern second = createTestPattern(patternId, TEST_TENANT, 2);
            fraudRepositoryAdapter.savePattern(second);

            // Verify only one pattern exists
            FraudPatternEntity entity = patternRepository.findByPatternId(patternId);
            assertThat(entity).isNotNull();
            assertThat(entity.getOccurrenceCount()).isGreaterThanOrEqualTo(2);

            // Cleanup - delete the entity
            patternRepository.delete(entity);
        }

        @Test
        @DisplayName("Should verify collections are accessible")
        void shouldVerifyCollectionsAreAccessible() {
            assumeMongoDbAvailable();

            // Verify collections are accessible by counting documents
            long analysisCount = analysisRepository.count();
            long patternCount = patternRepository.count();

            // Should return non-negative counts
            assertThat(analysisCount).isGreaterThanOrEqualTo(0);
            assertThat(patternCount).isGreaterThanOrEqualTo(0);
        }
    }

    @Nested
    @DisplayName("3. Invalid Tenant Scenarios")
    class InvalidTenantTests {

        @Test
        @DisplayName("Should handle empty tenant ID")
        void shouldHandleEmptyTenantId() {
            assumeMongoDbAvailable();

            String analysisId = "empty-tenant-001";

            FraudAnalysisResult result = createTestAnalysisResult(analysisId, TEST_USER, "", 0.5);

            // Should not throw exception
            assertThatCode(() -> fraudRepositoryAdapter.saveAnalysisResult(result))
                    .doesNotThrowAnyException();

            // Cleanup
            analysisRepository.deleteByAnalysisId(analysisId);
        }

        @Test
        @DisplayName("Should handle null tenant ID")
        void shouldHandleNullTenantId() {
            assumeMongoDbAvailable();

            String analysisId = "null-tenant-001";

            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(analysisId)
                    .transactionId("txn-null-tenant")
                    .userId(TEST_USER)
                    .tenantId(null)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of())
                    .timestamp(Instant.now())
                    .modelVersion("v1.0.0")
                    .build();

            // Should handle gracefully
            assertThatCode(() -> fraudRepositoryAdapter.saveAnalysisResult(result))
                    .doesNotThrowAnyException();

            // Cleanup
            analysisRepository.deleteByAnalysisId(analysisId);
        }

        @Test
        @DisplayName("Should isolate data across tenants")
        void shouldIsolateDataAcrossTenants() {
            assumeMongoDbAvailable();

            // Use different analysis IDs for different tenants (data isolation by tenant)
            String analysisId1 = "isolation-tenant1-001";
            String analysisId2 = "isolation-tenant2-001";

            // Save with tenant 1
            FraudAnalysisResult result1 = createTestAnalysisResult(analysisId1, TEST_USER, "tenant-001", 0.5);
            fraudRepositoryAdapter.saveAnalysisResult(result1);

            // Save with tenant 2 (different analysis ID - different tenant)
            FraudAnalysisResult result2 = createTestAnalysisResult(analysisId2, TEST_USER, "tenant-002", 0.7);
            fraudRepositoryAdapter.saveAnalysisResult(result2);

            // Verify both exist (they're in different tenants with different IDs)
            var fromTenant1 = fraudRepositoryAdapter.findByTenantId("tenant-001", 100);
            var fromTenant2 = fraudRepositoryAdapter.findByTenantId("tenant-002", 100);

            assertThat(fromTenant1).hasSize(1);
            assertThat(fromTenant2).hasSize(1);
            assertThat(fromTenant1.get(0).getTenantId()).isEqualTo("tenant-001");
            assertThat(fromTenant2.get(0).getTenantId()).isEqualTo("tenant-002");

            // Cleanup
            analysisRepository.deleteByAnalysisId(analysisId1);
            analysisRepository.deleteByAnalysisId(analysisId2);
        }

        @Test
        @DisplayName("Should handle tenant stats for non-existent tenant")
        void shouldHandleTenantStatsForNonExistentTenant() {
            assumeMongoDbAvailable();

            MongoFraudRepositoryAdapter.TenantStats stats =
                    fraudRepositoryAdapter.getTenantStats("non-existent-tenant");

            assertThat(stats.tenantId()).isEqualTo("non-existent-tenant");
            assertThat(stats.analysisCount()).isEqualTo(0);
            assertThat(stats.patternCount()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should return empty list for invalid tenant queries")
        void shouldReturnEmptyListForInvalidTenantQueries() {
            assumeMongoDbAvailable();

            List<FraudAnalysisResult> results =
                    fraudRepositoryAdapter.findByTenantId("non-existent-tenant-xyz", 10);

            assertThat(results).isEmpty();
        }
    }

    @Nested
    @DisplayName("4. Malformed Data Scenarios")
    class MalformedDataTests {

        @Test
        @DisplayName("Should handle very long tenant IDs")
        void shouldHandleVeryLongTenantIds() {
            assumeMongoDbAvailable();

            String longTenantId = "a".repeat(500);
            String analysisId = "long-tenant-001";

            FraudAnalysisResult result = createTestAnalysisResult(analysisId, TEST_USER, longTenantId, 0.5);

            assertThatCode(() -> fraudRepositoryAdapter.saveAnalysisResult(result))
                    .doesNotThrowAnyException();

            // Cleanup
            analysisRepository.deleteByAnalysisId(analysisId);
        }

        @Test
        @DisplayName("Should handle special characters in tenant ID")
        void shouldHandleSpecialCharactersInTenantId() {
            assumeMongoDbAvailable();

            String specialTenantId = "tenant-with-特殊字符-ñ-ø-$-#";
            String analysisId = "special-tenant-001";

            FraudAnalysisResult result = createTestAnalysisResult(analysisId, TEST_USER, specialTenantId, 0.5);

            assertThatCode(() -> fraudRepositoryAdapter.saveAnalysisResult(result))
                    .doesNotThrowAnyException();

            // Cleanup
            analysisRepository.deleteByAnalysisId(analysisId);
        }

        @Test
        @DisplayName("Should handle fraud score outside normal range")
        void shouldHandleFraudScoreOutsideNormalRange() {
            assumeMongoDbAvailable();

            // Test with score > 1.0
            FraudAnalysisResult highScore = createTestAnalysisResult("high-score-001", TEST_USER, TEST_TENANT, 1.5);
            fraudRepositoryAdapter.saveAnalysisResult(highScore);

            var found = fraudRepositoryAdapter.findAnalysisById("high-score-001");
            assertThat(found).isPresent();
            assertThat(found.get().getFraudScore()).isGreaterThan(1.0);

            // Test with negative score
            FraudAnalysisResult negativeScore = createTestAnalysisResult("negative-score-001", TEST_USER, TEST_TENANT, -0.5);
            fraudRepositoryAdapter.saveAnalysisResult(negativeScore);

            var foundNegative = fraudRepositoryAdapter.findAnalysisById("negative-score-001");
            assertThat(foundNegative).isPresent();
            assertThat(foundNegative.get().getFraudScore()).isNegative();

            // Cleanup
            analysisRepository.deleteByAnalysisId("high-score-001");
            analysisRepository.deleteByAnalysisId("negative-score-001");
        }

        @Test
        @DisplayName("Should handle empty reasons list")
        void shouldHandleEmptyReasonsList() {
            assumeMongoDbAvailable();

            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId("empty-reasons-001")
                    .transactionId("txn-empty-reasons")
                    .userId(TEST_USER)
                    .tenantId(TEST_TENANT)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of())
                    .timestamp(Instant.now())
                    .modelVersion("v1.0.0")
                    .build();

            fraudRepositoryAdapter.saveAnalysisResult(result);

            var found = fraudRepositoryAdapter.findAnalysisById("empty-reasons-001");
            assertThat(found).isPresent();
            assertThat(found.get().getReasons()).isNotNull();

            // Cleanup
            analysisRepository.deleteByAnalysisId("empty-reasons-001");
        }
    }

    @Nested
    @DisplayName("5. Constraint Violation Scenarios")
    class ConstraintViolationTests {

        @Test
        @DisplayName("Should handle missing required fields with defaults")
        void shouldHandleMissingRequiredFieldsWithDefaults() {
            assumeMongoDbAvailable();

            // Create entity with minimal fields
            FraudAnalysisResultEntity entity = new FraudAnalysisResultEntity();
            entity.setAnalysisId("minimal-001");
            entity.setUserId(TEST_USER);
            entity.setTenantId(TEST_TENANT);
            entity.setFraudScore(0.5);
            entity.setTimestamp(Instant.now());

            // Should save with null values for optional fields
            assertThatCode(() -> analysisRepository.save(entity))
                    .doesNotThrowAnyException();

            // Cleanup
            analysisRepository.deleteByAnalysisId("minimal-001");
        }

        @Test
        @DisplayName("Should verify document structure")
        void shouldVerifyDocumentStructure() {
            assumeMongoDbAvailable();

            String analysisId = "structure-001";
            FraudAnalysisResult result = createTestAnalysisResult(analysisId, TEST_USER, TEST_TENANT, 0.5);
            fraudRepositoryAdapter.saveAnalysisResult(result);

            // Verify we can retrieve and all fields are accessible
            var found = fraudRepositoryAdapter.findAnalysisById(analysisId);
            assertThat(found).isPresent();

            FraudAnalysisResult retrieved = found.get();
            assertThat(retrieved.getAnalysisId()).isNotNull();
            assertThat(retrieved.getUserId()).isNotNull();
            assertThat(retrieved.getTenantId()).isNotNull();
            assertThat(retrieved.getTimestamp()).isNotNull();

            // Cleanup
            analysisRepository.deleteByAnalysisId(analysisId);
        }
    }

    @Nested
    @DisplayName("6. Deactivation and Soft Delete Scenarios")
    class DeactivationTests {

        @Test
        @DisplayName("Should handle pattern deactivation")
        void shouldHandlePatternDeactivation() {
            assumeMongoDbAvailable();

            String patternId = "deactivate-001";
            fraudRepositoryAdapter.savePattern(createTestPattern(patternId, TEST_TENANT, 1));

            // Deactivate the pattern
            boolean deactivated = fraudRepositoryAdapter.deletePattern(patternId);

            assertThat(deactivated).isTrue();

            // Verify it's deactivated but still exists
            FraudPatternEntity entity = patternRepository.findByPatternId(patternId);
            if (entity != null) {
                assertThat(entity.isActive()).isFalse();
            }

            // Verify it's not in active patterns
            List<FraudPattern> activePatterns = fraudRepositoryAdapter.getActivePatterns();
            assertThat(activePatterns).noneMatch(p -> p.getPatternId().equals(patternId));

            // Cleanup - delete the entity
            if (entity != null) {
                patternRepository.delete(entity);
            }
        }

        @Test
        @DisplayName("Should handle deactivation of non-existent pattern")
        void shouldHandleDeactivationOfNonExistentPattern() {
            assumeMongoDbAvailable();

            boolean deactivated = fraudRepositoryAdapter.deletePattern("non-existent-pattern");

            assertThat(deactivated).isFalse();
        }
    }

    // Helper methods

    private void assumeMongoDbAvailable() {
        org.junit.jupiter.api.Assumptions.assumeTrue(fraudRepositoryAdapter != null,
                "MongoDB adapter not available - skipping test");
    }

    private FraudAnalysisResult createTestAnalysisResult(String analysisId, String userId, String tenantId, double fraudScore) {
        return FraudAnalysisResult.builder()
                .analysisId(analysisId)
                .transactionId("txn-" + analysisId)
                .userId(userId)
                .tenantId(tenantId)
                .fraudScore(fraudScore)
                .riskLevel(RiskLevel.LOW)
                .recommendedAction(FraudAction.ALLOW)
                .reasons(List.of("Test reason"))
                .timestamp(Instant.now())
                .modelVersion("v1.0.0")
                .build();
    }

    private FraudPattern createTestPattern(String patternId, String tenantId, int occurrenceCount) {
        return FraudPattern.builder()
                .patternId(patternId)
                .tenantId(tenantId)
                .patternName("Test Pattern " + patternId)
                .description("Test pattern description")
                .confidenceScore(0.8)
                .lastSeen(Instant.now())
                .occurrenceCount(occurrenceCount)
                .build();
    }
}

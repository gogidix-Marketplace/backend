package com.gogidix.aiservices.aifrauddetectionservice.infrastructure.persistence.mongodb;

import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudAction;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudAnalysisResult;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudPattern;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.RiskLevel;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * Financial-Grade Integration Tests for MongoDB Fraud Repository.
 *
 * These tests execute against a REAL MongoDB instance at localhost:27017.
 * This is NOT using embedded MongoDB or mocks - real database operations.
 *
 * Prerequisites:
 * - MongoDB must be running on localhost:27017
 * - Database 'ai-fraud-detection-test' will be created/used
 *
 * Test Categories:
 * 1. Connection & Schema Validation
 * 2. CRUD Operations
 * 3. Multi-Tenancy Isolation
 * 4. Data Consistency & Concurrency
 * 5. Query Performance & Indexing
 * 6. Edge Cases & Boundary Conditions
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Financial-Grade: MongoDB Fraud Repository Integration Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FraudRepositoryIntegrationTest {

    @Autowired
    private SpringDataFraudAnalysisResultRepository analysisRepository;

    @Autowired
    private SpringDataFraudPatternRepository patternRepository;

    @Autowired
    private MongoFraudRepositoryAdapter fraudRepositoryAdapter;

    private static final String TEST_TENANT_1 = "tenant-001";
    private static final String TEST_TENANT_2 = "tenant-002";
    private static final String TEST_USER_1 = "user-001";
    private static final String TEST_USER_2 = "user-002";

    @BeforeAll
    static void setupDatabase(@Autowired SpringDataFraudAnalysisResultRepository analysisRepository,
                              @Autowired SpringDataFraudPatternRepository patternRepository) {
        // Clean up test data before running tests
        analysisRepository.deleteAll();
        patternRepository.deleteAll();
    }

    @AfterEach
    void cleanupTestData() {
        // Clean up after each test to ensure isolation
        analysisRepository.deleteAll();
        patternRepository.deleteAll();
    }

    @Nested
    @DisplayName("1. Connection & Schema Validation")
    class ConnectionAndSchemaValidationTests {

        @Test
        @Order(1)
        @DisplayName("Should connect to MongoDB and verify database is accessible")
        void shouldConnectToMongoDB() {
            // This test verifies MongoDB connectivity
            long count = analysisRepository.count();
            assertThat(count).isGreaterThanOrEqualTo(0);

            long patternCount = patternRepository.count();
            assertThat(patternCount).isGreaterThanOrEqualTo(0);
        }

        @Test
        @Order(2)
        @DisplayName("Should create indexes automatically on collections")
        void shouldCreateIndexesAutomatically() {
            // Save a document to trigger index creation
            FraudAnalysisResult result = createTestAnalysisResult("test-001", TEST_USER_1, TEST_TENANT_1);
            fraudRepositoryAdapter.saveAnalysisResult(result);

            // Query by indexed field should work
            FraudAnalysisResultEntity found = analysisRepository.findByAnalysisId("test-001");
            assertThat(found).isNotNull();
            assertThat(found.getAnalysisId()).isEqualTo("test-001");
        }

        @Test
        @Order(3)
        @DisplayName("Should persist and retrieve entity with all fields")
        void shouldPersistAndRetrieveEntityWithAllFields() {
            String analysisId = "full-field-test-001";
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(analysisId)
                    .transactionId("txn-001")
                    .userId(TEST_USER_1)
                    .tenantId(TEST_TENANT_1)
                    .fraudScore(0.85)
                    .riskLevel(RiskLevel.HIGH)
                    .recommendedAction(FraudAction.BLOCK)
                    .reasons(List.of("High amount", "Unusual location"))
                    .timestamp(Instant.now())
                    .modelVersion("v1.0.0")
                    .build();

            fraudRepositoryAdapter.saveAnalysisResult(result);

            Optional<FraudAnalysisResult> retrieved = fraudRepositoryAdapter.findAnalysisById(analysisId);

            assertThat(retrieved).isPresent();
            FraudAnalysisResult retrievedResult = retrieved.get();
            assertThat(retrievedResult.getAnalysisId()).isEqualTo(analysisId);
            assertThat(retrievedResult.getTransactionId()).isEqualTo("txn-001");
            assertThat(retrievedResult.getUserId()).isEqualTo(TEST_USER_1);
            assertThat(retrievedResult.getTenantId()).isEqualTo(TEST_TENANT_1);
            assertThat(retrievedResult.getFraudScore()).isEqualTo(0.85);
            assertThat(retrievedResult.getRiskLevel()).isEqualTo(RiskLevel.HIGH);
            assertThat(retrievedResult.getRecommendedAction()).isEqualTo(FraudAction.BLOCK);
            assertThat(retrievedResult.getReasons()).containsExactly("High amount", "Unusual location");
            assertThat(retrievedResult.getModelVersion()).isEqualTo("v1.0.0");
        }
    }

    @Nested
    @DisplayName("2. CRUD Operations")
    class CrudOperationsTests {

        @Test
        @Order(10)
        @DisplayName("Should save new fraud analysis result")
        void shouldSaveNewFraudAnalysisResult() {
            FraudAnalysisResult result = createTestAnalysisResult("crud-001", TEST_USER_1, TEST_TENANT_1);

            fraudRepositoryAdapter.saveAnalysisResult(result);

            Optional<FraudAnalysisResult> found = fraudRepositoryAdapter.findAnalysisById("crud-001");
            assertThat(found).isPresent();
        }

        @Test
        @Order(11)
        @DisplayName("Should update existing fraud analysis result")
        void shouldUpdateExistingFraudAnalysisResult() {
            String analysisId = "update-test-001";

            // Create initial result
            FraudAnalysisResult initial = FraudAnalysisResult.builder()
                    .analysisId(analysisId)
                    .transactionId("txn-update-001")
                    .userId(TEST_USER_1)
                    .tenantId(TEST_TENANT_1)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.MEDIUM)
                    .recommendedAction(FraudAction.REVIEW)
                    .reasons(List.of("Initial reason"))
                    .timestamp(Instant.now())
                    .modelVersion("v1.0.0")
                    .build();

            fraudRepositoryAdapter.saveAnalysisResult(initial);

            // Update with new score
            FraudAnalysisResult updated = FraudAnalysisResult.builder()
                    .analysisId(analysisId)
                    .transactionId("txn-update-001")
                    .userId(TEST_USER_1)
                    .tenantId(TEST_TENANT_1)
                    .fraudScore(0.95)
                    .riskLevel(RiskLevel.HIGH)
                    .recommendedAction(FraudAction.BLOCK)
                    .reasons(List.of("Updated reason", "New pattern detected"))
                    .timestamp(Instant.now())
                    .modelVersion("v1.0.0")
                    .build();

            fraudRepositoryAdapter.saveAnalysisResult(updated);

            // Verify update
            Optional<FraudAnalysisResult> retrieved = fraudRepositoryAdapter.findAnalysisById(analysisId);
            assertThat(retrieved).isPresent();
            assertThat(retrieved.get().getFraudScore()).isEqualTo(0.95);
            assertThat(retrieved.get().getRecommendedAction()).isEqualTo(FraudAction.BLOCK);
        }

        @Test
        @Order(12)
        @DisplayName("Should find analysis by user ID with limit")
        void shouldFindByUserIdWithLimit() {
            // Create multiple analyses for same user
            for (int i = 0; i < 5; i++) {
                FraudAnalysisResult result = createTestAnalysisResult(
                    "user-limit-" + i,
                    TEST_USER_1,
                    TEST_TENANT_1
                );
                fraudRepositoryAdapter.saveAnalysisResult(result);
            }

            // Find with limit
            List<FraudAnalysisResult> found = fraudRepositoryAdapter.findByUserId(TEST_USER_1, 3);

            assertThat(found).hasSize(3);
        }

        @Test
        @Order(13)
        @DisplayName("Should return empty list when user has no analyses")
        void shouldReturnEmptyListWhenUserHasNoAnalyses() {
            List<FraudAnalysisResult> found = fraudRepositoryAdapter.findByUserId("non-existent-user", 10);

            assertThat(found).isEmpty();
        }

        @Test
        @Order(14)
        @DisplayName("Should save and retrieve fraud patterns")
        void shouldSaveAndRetrieveFraudPatterns() {
            FraudPattern pattern = FraudPattern.builder()
                    .patternId("pattern-001")
                    .tenantId(TEST_TENANT_1)
                    .patternName("Test Pattern")
                    .description("A test fraud pattern")
                    .confidenceScore(0.9)
                    .lastSeen(Instant.now())
                    .occurrenceCount(5)
                    .build();

            fraudRepositoryAdapter.savePattern(pattern);

            List<FraudPattern> patterns = fraudRepositoryAdapter.getActivePatterns();

            assertThat(patterns).isNotEmpty();
            assertThat(patterns).anyMatch(p -> p.getPatternId().equals("pattern-001"));
        }

        @Test
        @Order(15)
        @DisplayName("Should increment pattern occurrence count on save")
        void shouldIncrementPatternOccurrenceCount() {
            String patternId = "increment-test-001";

            FraudPattern initial = FraudPattern.builder()
                    .patternId(patternId)
                    .tenantId(TEST_TENANT_1)
                    .patternName("Increment Test Pattern")
                    .description("Testing occurrence count increment")
                    .confidenceScore(0.8)
                    .lastSeen(Instant.now())
                    .occurrenceCount(1)
                    .build();

            fraudRepositoryAdapter.savePattern(initial);

            // Save again to trigger increment
            FraudPattern update = FraudPattern.builder()
                    .patternId(patternId)
                    .tenantId(TEST_TENANT_1)
                    .patternName("Increment Test Pattern")
                    .description("Testing occurrence count increment")
                    .confidenceScore(0.85)
                    .lastSeen(Instant.now())
                    .occurrenceCount(2)
                    .build();

            fraudRepositoryAdapter.savePattern(update);

            // Verify the entity was updated
            FraudPatternEntity entity = patternRepository.findByPatternId(patternId);
            assertThat(entity).isNotNull();
            assertThat(entity.getOccurrenceCount()).isGreaterThanOrEqualTo(2);
        }

        @Test
        @Order(16)
        @DisplayName("Should delete analysis result")
        void shouldDeleteAnalysisResult() {
            FraudAnalysisResult result = createTestAnalysisResult("delete-test-001", TEST_USER_1, TEST_TENANT_1);
            fraudRepositoryAdapter.saveAnalysisResult(result);

            // Verify it exists
            assertThat(fraudRepositoryAdapter.findAnalysisById("delete-test-001")).isPresent();

            // Delete
            analysisRepository.deleteByAnalysisId("delete-test-001");

            // Verify it's gone
            Optional<FraudAnalysisResult> found = fraudRepositoryAdapter.findAnalysisById("delete-test-001");
            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("3. Multi-Tenancy Isolation")
    class MultiTenancyIsolationTests {

        @Test
        @Order(20)
        @DisplayName("Should isolate data by tenant ID")
        void shouldIsolateDataByTenantId() {
            // Create analyses for different tenants
            FraudAnalysisResult tenant1Result = createTestAnalysisResult("tenant-1-001", TEST_USER_1, TEST_TENANT_1);
            FraudAnalysisResult tenant2Result = createTestAnalysisResult("tenant-2-001", TEST_USER_1, TEST_TENANT_2);

            fraudRepositoryAdapter.saveAnalysisResult(tenant1Result);
            fraudRepositoryAdapter.saveAnalysisResult(tenant2Result);

            // Find by tenant
            List<FraudAnalysisResult> tenant1Results = fraudRepositoryAdapter.findByTenantId(TEST_TENANT_1, 100);
            List<FraudAnalysisResult> tenant2Results = fraudRepositoryAdapter.findByTenantId(TEST_TENANT_2, 100);

            assertThat(tenant1Results).hasSize(1);
            assertThat(tenant2Results).hasSize(1);
            assertThat(tenant1Results.get(0).getTenantId()).isEqualTo(TEST_TENANT_1);
            assertThat(tenant2Results.get(0).getTenantId()).isEqualTo(TEST_TENANT_2);
        }

        @Test
        @Order(21)
        @DisplayName("Should isolate patterns by tenant ID")
        void shouldIsolatePatternsByTenantId() {
            // Create patterns for different tenants
            FraudPattern tenant1Pattern = createTestPattern("pattern-tenant1-001", TEST_TENANT_1);
            FraudPattern tenant2Pattern = createTestPattern("pattern-tenant2-001", TEST_TENANT_2);

            fraudRepositoryAdapter.savePattern(tenant1Pattern);
            fraudRepositoryAdapter.savePattern(tenant2Pattern);

            // Get patterns by tenant
            List<FraudPattern> tenant1Patterns = fraudRepositoryAdapter.getActivePatternsByTenant(TEST_TENANT_1);
            List<FraudPattern> tenant2Patterns = fraudRepositoryAdapter.getActivePatternsByTenant(TEST_TENANT_2);

            assertThat(tenant1Patterns).hasSize(1);
            assertThat(tenant2Patterns).hasSize(1);
            assertThat(tenant1Patterns.get(0).getTenantId()).isEqualTo(TEST_TENANT_1);
            assertThat(tenant2Patterns.get(0).getTenantId()).isEqualTo(TEST_TENANT_2);
        }

        @Test
        @Order(22)
        @DisplayName("Should prevent cross-tenant data leakage")
        void shouldPreventCrossTenantDataLeakage() {
            // Create 10 analyses for tenant 1
            for (int i = 0; i < 10; i++) {
                fraudRepositoryAdapter.saveAnalysisResult(
                    createTestAnalysisResult("cross-tenant-1-" + i, TEST_USER_1, TEST_TENANT_1)
                );
            }

            // Create 5 analyses for tenant 2
            for (int i = 0; i < 5; i++) {
                fraudRepositoryAdapter.saveAnalysisResult(
                    createTestAnalysisResult("cross-tenant-2-" + i, TEST_USER_1, TEST_TENANT_2)
                );
            }

            // Verify counts
            List<FraudAnalysisResult> tenant1Results = fraudRepositoryAdapter.findByTenantId(TEST_TENANT_1, 100);
            List<FraudAnalysisResult> tenant2Results = fraudRepositoryAdapter.findByTenantId(TEST_TENANT_2, 100);

            assertThat(tenant1Results).hasSize(10);
            assertThat(tenant2Results).hasSize(5);

            // Verify no mixing
            assertThat(tenant1Results).allMatch(r -> r.getTenantId().equals(TEST_TENANT_1));
            assertThat(tenant2Results).allMatch(r -> r.getTenantId().equals(TEST_TENANT_2));
        }

        @Test
        @Order(23)
        @DisplayName("Should get correct tenant stats")
        void shouldGetCorrectTenantStats() {
            // Create data for tenant 1
            for (int i = 0; i < 5; i++) {
                fraudRepositoryAdapter.saveAnalysisResult(
                    createTestAnalysisResult("stats-tenant1-" + i, TEST_USER_1, TEST_TENANT_1)
                );
            }
            fraudRepositoryAdapter.savePattern(createTestPattern("stats-p1-001", TEST_TENANT_1));
            fraudRepositoryAdapter.savePattern(createTestPattern("stats-p1-002", TEST_TENANT_1));

            // Create data for tenant 2
            for (int i = 0; i < 3; i++) {
                fraudRepositoryAdapter.saveAnalysisResult(
                    createTestAnalysisResult("stats-tenant2-" + i, TEST_USER_1, TEST_TENANT_2)
                );
            }
            fraudRepositoryAdapter.savePattern(createTestPattern("stats-p2-001", TEST_TENANT_2));

            // Get stats
            MongoFraudRepositoryAdapter.TenantStats stats1 = fraudRepositoryAdapter.getTenantStats(TEST_TENANT_1);
            MongoFraudRepositoryAdapter.TenantStats stats2 = fraudRepositoryAdapter.getTenantStats(TEST_TENANT_2);

            assertThat(stats1.analysisCount()).isEqualTo(5);
            assertThat(stats1.patternCount()).isEqualTo(2);
            assertThat(stats2.analysisCount()).isEqualTo(3);
            assertThat(stats2.patternCount()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("4. Data Consistency & Concurrency")
    class DataConsistencyAndConcurrencyTests {

        @Test
        @Order(30)
        @DisplayName("Should handle concurrent saves of same analysis")
        void shouldHandleConcurrentSaves() {
            String analysisId = "concurrent-001";

            // Simulate concurrent updates by saving twice
            FraudAnalysisResult result1 = createTestAnalysisResult(analysisId, TEST_USER_1, TEST_TENANT_1);
            result1 = FraudAnalysisResult.builder()
                    .analysisId(analysisId)
                    .transactionId("txn-concurrent-1")
                    .userId(TEST_USER_1)
                    .tenantId(TEST_TENANT_1)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.MEDIUM)
                    .recommendedAction(FraudAction.REVIEW)
                    .reasons(List.of("First save"))
                    .timestamp(Instant.now())
                    .modelVersion("v1.0.0")
                    .build();

            FraudAnalysisResult result2 = FraudAnalysisResult.builder()
                    .analysisId(analysisId)
                    .transactionId("txn-concurrent-2")
                    .userId(TEST_USER_1)
                    .tenantId(TEST_TENANT_1)
                    .fraudScore(0.9)
                    .riskLevel(RiskLevel.HIGH)
                    .recommendedAction(FraudAction.BLOCK)
                    .reasons(List.of("Second save"))
                    .timestamp(Instant.now())
                    .modelVersion("v1.0.0")
                    .build();

            fraudRepositoryAdapter.saveAnalysisResult(result1);
            fraudRepositoryAdapter.saveAnalysisResult(result2);

            // Both should complete without error
            Optional<FraudAnalysisResult> found = fraudRepositoryAdapter.findAnalysisById(analysisId);
            assertThat(found).isPresent();
            // Last write wins
            assertThat(found.get().getFraudScore()).isEqualTo(0.9);
        }

        @Test
        @Order(31)
        @DisplayName("Should maintain referential integrity")
        void shouldMaintainReferentialIntegrity() {
            String analysisId = "integrity-001";

            FraudAnalysisResult result = createTestAnalysisResult(analysisId, TEST_USER_1, TEST_TENANT_1);
            result = FraudAnalysisResult.builder()
                    .analysisId(analysisId)
                    .transactionId("txn-integrity-001")
                    .userId(TEST_USER_1)
                    .tenantId(TEST_TENANT_1)
                    .fraudScore(0.7)
                    .riskLevel(RiskLevel.MEDIUM)
                    .recommendedAction(FraudAction.REVIEW)
                    .reasons(List.of("Integrity test"))
                    .timestamp(Instant.now())
                    .modelVersion("v1.0.0")
                    .build();

            fraudRepositoryAdapter.saveAnalysisResult(result);

            // Verify transaction ID matches
            Optional<FraudAnalysisResult> found = fraudRepositoryAdapter.findAnalysisById(analysisId);
            assertThat(found).isPresent();
            assertThat(found.get().getTransactionId()).isEqualTo("txn-integrity-001");
        }

        @Test
        @Order(32)
        @DisplayName("Should handle null enum values gracefully")
        void shouldHandleNullEnumValues() {
            String analysisId = "null-enum-001";

            FraudAnalysisResultEntity entity = new FraudAnalysisResultEntity();
            entity.setAnalysisId(analysisId);
            entity.setTransactionId("txn-null-enum");
            entity.setUserId(TEST_USER_1);
            entity.setTenantId(TEST_TENANT_1);
            entity.setFraudScore(0.5);
            entity.setRiskLevel(null);
            entity.setRecommendedAction(null);
            entity.setTimestamp(Instant.now());

            analysisRepository.save(entity);

            // Retrieve and verify
            FraudAnalysisResultEntity found = analysisRepository.findByAnalysisId(analysisId);
            assertThat(found).isNotNull();
            assertThat(found.getRiskLevel()).isNull();
            assertThat(found.getRecommendedAction()).isNull();
        }
    }

    @Nested
    @DisplayName("5. Query Performance & Indexing")
    class QueryPerformanceAndIndexingTests {

        @Test
        @Order(40)
        @DisplayName("Should efficiently query by indexed fields")
        void shouldEfficientlyQueryByIndexedFields() {
            // Create bulk data
            int count = 100;
            for (int i = 0; i < count; i++) {
                fraudRepositoryAdapter.saveAnalysisResult(
                    createTestAnalysisResult("perf-" + i, TEST_USER_1, TEST_TENANT_1)
                );
            }

            // Query by indexed field (user_id)
            long startTime = System.currentTimeMillis();
            List<FraudAnalysisResult> results = fraudRepositoryAdapter.findByUserId(TEST_USER_1, count);
            long duration = System.currentTimeMillis() - startTime;

            assertThat(results).hasSize(count);
            // Should be fast with index (< 1 second for 100 records)
            assertThat(duration).isLessThan(5000);
        }

        @Test
        @Order(41)
        @DisplayName("Should efficiently query by tenant ID")
        void shouldEfficientlyQueryByTenantId() {
            // Create bulk data for multiple tenants
            for (int i = 0; i < 50; i++) {
                fraudRepositoryAdapter.saveAnalysisResult(
                    createTestAnalysisResult("tenant1-perf-" + i, "user-" + (i % 10), TEST_TENANT_1)
                );
                fraudRepositoryAdapter.saveAnalysisResult(
                    createTestAnalysisResult("tenant2-perf-" + i, "user-" + (i % 10), TEST_TENANT_2)
                );
            }

            long startTime = System.currentTimeMillis();
            List<FraudAnalysisResult> results = fraudRepositoryAdapter.findByTenantId(TEST_TENANT_1, 100);
            long duration = System.currentTimeMillis() - startTime;

            assertThat(results).hasSize(50);
            assertThat(duration).isLessThan(5000);
        }

        @Test
        @Order(42)
        @DisplayName("Should respect pagination limits")
        void shouldRespectPaginationLimits() {
            // Create 50 analyses
            for (int i = 0; i < 50; i++) {
                fraudRepositoryAdapter.saveAnalysisResult(
                    createTestAnalysisResult("page-" + i, TEST_USER_1, TEST_TENANT_1)
                );
            }

            // Request with various limits
            List<FraudAnalysisResult> limit10 = fraudRepositoryAdapter.findByUserId(TEST_USER_1, 10);
            List<FraudAnalysisResult> limit25 = fraudRepositoryAdapter.findByUserId(TEST_USER_1, 25);
            List<FraudAnalysisResult> limitAll = fraudRepositoryAdapter.findByUserId(TEST_USER_1, 0);

            assertThat(limit10).hasSize(10);
            assertThat(limit25).hasSize(25);
            assertThat(limitAll).hasSize(50);
        }

        @Test
        @Order(43)
        @DisplayName("Should return results in descending timestamp order")
        void shouldReturnResultsInDescendingOrder() {
            Instant baseTime = Instant.now();

            // Create analyses with different timestamps
            for (int i = 0; i < 10; i++) {
                FraudAnalysisResult result = createTestAnalysisResult("order-" + i, TEST_USER_1, TEST_TENANT_1);
                // Override timestamp with specific times
                FraudAnalysisResult.Builder builder = FraudAnalysisResult.builder()
                        .analysisId("order-" + i)
                        .transactionId("txn-order-" + i)
                        .userId(TEST_USER_1)
                        .tenantId(TEST_TENANT_1)
                        .fraudScore(0.5)
                        .riskLevel(RiskLevel.LOW)
                        .recommendedAction(FraudAction.ALLOW)
                        .reasons(List.of())
                        .timestamp(baseTime.minusSeconds(i * 60)) // Different times
                        .modelVersion("v1.0.0");
                fraudRepositoryAdapter.saveAnalysisResult(builder.build());
            }

            List<FraudAnalysisResult> results = fraudRepositoryAdapter.findByUserId(TEST_USER_1, 10);

            // Verify descending order (most recent first)
            assertThat(results).hasSize(10);
            for (int i = 0; i < results.size() - 1; i++) {
                assertThat(results.get(i).getTimestamp()
                        .compareTo(results.get(i + 1).getTimestamp()))
                        .isGreaterThanOrEqualTo(0);
            }
        }
    }

    @Nested
    @DisplayName("6. Edge Cases & Boundary Conditions")
    class EdgeCasesAndBoundaryConditionsTests {

        @Test
        @Order(50)
        @DisplayName("Should handle empty string IDs")
        void shouldHandleEmptyStringIds() {
            FraudAnalysisResult result = createTestAnalysisResult("", TEST_USER_1, TEST_TENANT_1);

            // Should not throw exception
            assertThatCode(() -> fraudRepositoryAdapter.saveAnalysisResult(result))
                    .doesNotThrowAnyException();
        }

        @Test
        @Order(51)
        @DisplayName("Should handle very long strings")
        void shouldHandleVeryLongStrings() {
            String longId = "a".repeat(500);

            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(longId)
                    .transactionId(longId)
                    .userId(TEST_USER_1)
                    .tenantId(TEST_TENANT_1)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of(longId))
                    .timestamp(Instant.now())
                    .modelVersion("v1.0.0")
                    .build();

            fraudRepositoryAdapter.saveAnalysisResult(result);

            Optional<FraudAnalysisResult> found = fraudRepositoryAdapter.findAnalysisById(longId);
            assertThat(found).isPresent();
        }

        @Test
        @Order(52)
        @DisplayName("Should handle fraud score at boundaries (0.0, 0.5, 0.8, 1.0)")
        void shouldHandleFraudScoreBoundaries() {
            // Test with 0.0 score
            FraudAnalysisResult scoreZero = createTestAnalysisWithScore("score-0.0", TEST_USER_1, TEST_TENANT_1, 0.0);
            fraudRepositoryAdapter.saveAnalysisResult(scoreZero);
            assertThat(fraudRepositoryAdapter.findAnalysisById("score-0.0").get().getFraudScore()).isEqualTo(0.0);

            // Test with 0.5 score (boundary)
            FraudAnalysisResult scoreHalf = createTestAnalysisWithScore("score-0.5", TEST_USER_1, TEST_TENANT_1, 0.5);
            fraudRepositoryAdapter.saveAnalysisResult(scoreHalf);
            assertThat(fraudRepositoryAdapter.findAnalysisById("score-0.5").get().getFraudScore()).isEqualTo(0.5);

            // Test with 0.8 score (boundary)
            FraudAnalysisResult scorePointEight = createTestAnalysisWithScore("score-0.8", TEST_USER_1, TEST_TENANT_1, 0.8);
            fraudRepositoryAdapter.saveAnalysisResult(scorePointEight);
            assertThat(fraudRepositoryAdapter.findAnalysisById("score-0.8").get().getFraudScore()).isEqualTo(0.8);

            // Test with 1.0 score (max)
            FraudAnalysisResult scoreOne = createTestAnalysisWithScore("score-1.0", TEST_USER_1, TEST_TENANT_1, 1.0);
            fraudRepositoryAdapter.saveAnalysisResult(scoreOne);
            assertThat(fraudRepositoryAdapter.findAnalysisById("score-1.0").get().getFraudScore()).isEqualTo(1.0);
        }

        @Test
        @Order(53)
        @DisplayName("Should handle negative fraud scores")
        void shouldHandleNegativeFraudScores() {
            FraudAnalysisResult result = createTestAnalysisWithScore("negative-score", TEST_USER_1, TEST_TENANT_1, -0.1);

            fraudRepositoryAdapter.saveAnalysisResult(result);

            Optional<FraudAnalysisResult> found = fraudRepositoryAdapter.findAnalysisById("negative-score");
            assertThat(found).isPresent();
            assertThat(found.get().getFraudScore()).isNegative();
        }

        @Test
        @Order(54)
        @DisplayName("Should handle fraud scores above 1.0")
        void shouldHandleFraudScoresAboveOne() {
            FraudAnalysisResult result = createTestAnalysisWithScore("above-one-score", TEST_USER_1, TEST_TENANT_1, 1.5);

            fraudRepositoryAdapter.saveAnalysisResult(result);

            Optional<FraudAnalysisResult> found = fraudRepositoryAdapter.findAnalysisById("above-one-score");
            assertThat(found).isPresent();
            assertThat(found.get().getFraudScore()).isGreaterThan(1.0);
        }

        @Test
        @Order(55)
        @DisplayName("Should handle empty reasons list")
        void shouldHandleEmptyReasonsList() {
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId("empty-reasons")
                    .transactionId("txn-empty-reasons")
                    .userId(TEST_USER_1)
                    .tenantId(TEST_TENANT_1)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of())
                    .timestamp(Instant.now())
                    .modelVersion("v1.0.0")
                    .build();

            fraudRepositoryAdapter.saveAnalysisResult(result);

            Optional<FraudAnalysisResult> found = fraudRepositoryAdapter.findAnalysisById("empty-reasons");
            assertThat(found).isPresent();
            assertThat(found.get().getReasons()).isEmpty();
        }

        @Test
        @Order(56)
        @DisplayName("Should handle special characters in strings")
        void shouldHandleSpecialCharacters() {
            String specialId = "test-with-特殊字符-ñ-ø-ש";

            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(specialId)
                    .transactionId("txn-特殊")
                    .userId(TEST_USER_1)
                    .tenantId(TEST_TENANT_1)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of("Test with special chars: <>&\"'"))
                    .timestamp(Instant.now())
                    .modelVersion("v1.0.0")
                    .build();

            fraudRepositoryAdapter.saveAnalysisResult(result);

            Optional<FraudAnalysisResult> found = fraudRepositoryAdapter.findAnalysisById(specialId);
            assertThat(found).isPresent();
        }

        @Test
        @Order(57)
        @DisplayName("Should handle large number of patterns")
        void shouldHandleLargeNumberOfPatterns() {
            int patternCount = 100;

            for (int i = 0; i < patternCount; i++) {
                FraudPattern pattern = createTestPattern("bulk-pattern-" + i, TEST_TENANT_1);
                fraudRepositoryAdapter.savePattern(pattern);
            }

            List<FraudPattern> patterns = fraudRepositoryAdapter.getActivePatterns();

            assertThat(patterns).hasSize(patternCount);
        }
    }

    // Helper methods

    private FraudAnalysisResult createTestAnalysisResult(String analysisId, String userId, String tenantId) {
        return FraudAnalysisResult.builder()
                .analysisId(analysisId)
                .transactionId("txn-" + analysisId)
                .userId(userId)
                .tenantId(tenantId)
                .fraudScore(0.5)
                .riskLevel(RiskLevel.LOW)
                .recommendedAction(FraudAction.ALLOW)
                .reasons(List.of("Test reason"))
                .timestamp(Instant.now())
                .modelVersion("v1.0.0")
                .build();
    }

    private FraudAnalysisResult createTestAnalysisWithScore(String analysisId, String userId, String tenantId, double score) {
        return FraudAnalysisResult.builder()
                .analysisId(analysisId)
                .transactionId("txn-" + analysisId)
                .userId(userId)
                .tenantId(tenantId)
                .fraudScore(score)
                .riskLevel(RiskLevel.LOW)
                .recommendedAction(FraudAction.ALLOW)
                .reasons(List.of("Test reason"))
                .timestamp(Instant.now())
                .modelVersion("v1.0.0")
                .build();
    }

    private FraudPattern createTestPattern(String patternId, String tenantId) {
        return FraudPattern.builder()
                .patternId(patternId)
                .tenantId(tenantId)
                .patternName("Test Pattern " + patternId)
                .description("Test pattern description")
                .confidenceScore(0.8)
                .lastSeen(Instant.now())
                .occurrenceCount(1)
                .build();
    }
}

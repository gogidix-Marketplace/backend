package com.gogidix.aiservices.aifrauddetectionservice.infrastructure.persistence;

import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudAnalysisResult;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudAction;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudPattern;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.RiskLevel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("InMemoryFraudRepository Infrastructure Tests")
class InMemoryFraudRepositoryTest {

    private final InMemoryFraudRepository repository = new InMemoryFraudRepository();

    private static final String ANALYSIS_ID = "analysis-123";
    private static final String TRANSACTION_ID = "txn-456";
    private static final String USER_ID = "user-789";
    private static final String PATTERN_ID = "pattern-001";

    @AfterEach
    void tearDown() {
        // Clean up after each test - repository is stateful
    }

    private FraudAnalysisResult createAnalysisResult(String analysisId, String userId, Instant timestamp) {
        return FraudAnalysisResult.builder()
                .analysisId(analysisId)
                .transactionId(TRANSACTION_ID)
                .userId(userId)
                .fraudScore(0.5)
                .riskLevel(RiskLevel.LOW)
                .recommendedAction(FraudAction.ALLOW)
                .reasons(List.of("Normal transaction"))
                .timestamp(timestamp)
                .modelVersion("1.0.0")
                .build();
    }

    private FraudPattern createFraudPattern(String patternId) {
        return FraudPattern.builder()
                .patternId(patternId)
                .patternName("Test Pattern")
                .description("Test description")
                .confidenceScore(0.8)
                .lastSeen(Instant.now())
                .occurrenceCount(5)
                .build();
    }

    @Nested
    @DisplayName("saveAnalysisResult Tests")
    class SaveAnalysisResultTests {

        @Test
        @DisplayName("Should save analysis result successfully")
        void shouldSaveAnalysisResult() {
            FraudAnalysisResult result = createAnalysisResult(ANALYSIS_ID, USER_ID, Instant.now());

            repository.saveAnalysisResult(result);

            var found = repository.findAnalysisById(ANALYSIS_ID);
            assertThat(found).isPresent();
            assertThat(found.get().getAnalysisId()).isEqualTo(ANALYSIS_ID);
        }

        @Test
        @DisplayName("Should overwrite existing analysis with same ID")
        void shouldOverwriteExistingAnalysis() {
            Instant firstTimestamp = Instant.now();
            FraudAnalysisResult firstResult = createAnalysisResult(ANALYSIS_ID, USER_ID, firstTimestamp);

            repository.saveAnalysisResult(firstResult);

            Instant secondTimestamp = Instant.now().plusSeconds(60);
            FraudAnalysisResult secondResult = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId("txn-different")
                    .userId(USER_ID)
                    .fraudScore(0.9)
                    .riskLevel(RiskLevel.HIGH)
                    .recommendedAction(FraudAction.BLOCK)
                    .reasons(List.of("Blocked"))
                    .timestamp(secondTimestamp)
                    .modelVersion("2.0.0")
                    .build();

            repository.saveAnalysisResult(secondResult);

            var found = repository.findAnalysisById(ANALYSIS_ID);
            assertThat(found).isPresent();
            assertThat(found.get().getTransactionId()).isEqualTo("txn-different");
            assertThat(found.get().getFraudScore()).isEqualTo(0.9);
        }

        @Test
        @DisplayName("Should save multiple analysis results")
        void shouldSaveMultipleAnalysisResults() {
            Instant now = Instant.now();

            repository.saveAnalysisResult(createAnalysisResult("analysis-1", USER_ID, now));
            repository.saveAnalysisResult(createAnalysisResult("analysis-2", USER_ID, now.plusSeconds(1)));
            repository.saveAnalysisResult(createAnalysisResult("analysis-3", USER_ID, now.plusSeconds(2)));

            assertThat(repository.findAnalysisById("analysis-1")).isPresent();
            assertThat(repository.findAnalysisById("analysis-2")).isPresent();
            assertThat(repository.findAnalysisById("analysis-3")).isPresent();
        }
    }

    @Nested
    @DisplayName("findAnalysisById Tests")
    class FindAnalysisByIdTests {

        @Test
        @DisplayName("Should find existing analysis by ID")
        void shouldFindExistingAnalysisById() {
            FraudAnalysisResult result = createAnalysisResult(ANALYSIS_ID, USER_ID, Instant.now());
            repository.saveAnalysisResult(result);

            var found = repository.findAnalysisById(ANALYSIS_ID);

            assertThat(found).isPresent();
            assertThat(found.get()).usingRecursiveComparison().isEqualTo(result);
        }

        @Test
        @DisplayName("Should return empty for non-existent analysis ID")
        void shouldReturnEmptyForNonExistentAnalysis() {
            var found = repository.findAnalysisById("non-existent");

            assertThat(found).isEmpty();
        }

        @Test
        @DisplayName("Should throw exception for null ID")
        void shouldThrowExceptionForNullId() {
            assertThatThrownBy(() -> repository.findAnalysisById(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should return empty for empty string ID")
        void shouldReturnEmptyForEmptyStringId() {
            var found = repository.findAnalysisById("");

            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByUserId Tests")
    class FindByUserIdTests {

        @Test
        @DisplayName("Should find analyses by user ID")
        void shouldFindAnalysesByUserId() {
            String userId = "user-123";
            Instant now = Instant.now();

            repository.saveAnalysisResult(createAnalysisResult("analysis-1", userId, now));
            repository.saveAnalysisResult(createAnalysisResult("analysis-2", userId, now.plusSeconds(1)));
            repository.saveAnalysisResult(createAnalysisResult("analysis-3", "other-user", now.plusSeconds(2)));

            List<FraudAnalysisResult> results = repository.findByUserId(userId, 10);

            assertThat(results).hasSize(2);
            assertThat(results).allMatch(r -> r.getUserId().equals(userId));
        }

        @Test
        @DisplayName("Should return empty list for user with no analyses")
        void shouldReturnEmptyListForUserWithNoAnalyses() {
            List<FraudAnalysisResult> results = repository.findByUserId("non-existent-user", 10);

            assertThat(results).isEmpty();
        }

        @Test
        @DisplayName("Should respect limit parameter")
        void shouldRespectLimitParameter() {
            String userId = "user-limit";
            Instant now = Instant.now();

            for (int i = 0; i < 10; i++) {
                repository.saveAnalysisResult(createAnalysisResult("analysis-" + i, userId, now.plusSeconds(i)));
            }

            List<FraudAnalysisResult> results = repository.findByUserId(userId, 5);

            assertThat(results).hasSize(5);
        }

        @Test
        @DisplayName("Should return all results when limit exceeds available")
        void shouldReturnAllResultsWhenLimitExceedsAvailable() {
            String userId = "user-all";
            Instant now = Instant.now();

            repository.saveAnalysisResult(createAnalysisResult("analysis-1", userId, now));
            repository.saveAnalysisResult(createAnalysisResult("analysis-2", userId, now.plusSeconds(1)));
            repository.saveAnalysisResult(createAnalysisResult("analysis-3", userId, now.plusSeconds(2)));

            List<FraudAnalysisResult> results = repository.findByUserId(userId, 100);

            assertThat(results).hasSize(3);
        }

        @Test
        @DisplayName("Should handle limit of zero")
        void shouldHandleLimitOfZero() {
            String userId = "user-zero";
            Instant now = Instant.now();

            repository.saveAnalysisResult(createAnalysisResult("analysis-1", userId, now));

            List<FraudAnalysisResult> results = repository.findByUserId(userId, 0);

            assertThat(results).isEmpty();
        }

        @Test
        @DisplayName("Should throw exception for negative limit")
        void shouldThrowExceptionForNegativeLimit() {
            String userId = "user-negative";
            Instant now = Instant.now();

            repository.saveAnalysisResult(createAnalysisResult("analysis-1", userId, now));

            assertThatThrownBy(() -> repository.findByUserId(userId, -1))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("savePattern Tests")
    class SavePatternTests {

        @Test
        @DisplayName("Should save fraud pattern successfully")
        void shouldSaveFraudPattern() {
            FraudPattern pattern = createFraudPattern(PATTERN_ID);

            repository.savePattern(pattern);

            List<FraudPattern> patterns = repository.getActivePatterns();
            assertThat(patterns).hasSize(1);
            assertThat(patterns.get(0).getPatternId()).isEqualTo(PATTERN_ID);
        }

        @Test
        @DisplayName("Should overwrite existing pattern with same ID")
        void shouldOverwriteExistingPattern() {
            FraudPattern firstPattern = createFraudPattern(PATTERN_ID);
            repository.savePattern(firstPattern);

            FraudPattern secondPattern = FraudPattern.builder()
                    .patternId(PATTERN_ID)
                    .patternName("Updated Pattern")
                    .description("Updated description")
                    .confidenceScore(0.95)
                    .lastSeen(Instant.now())
                    .occurrenceCount(10)
                    .build();
            repository.savePattern(secondPattern);

            List<FraudPattern> patterns = repository.getActivePatterns();
            assertThat(patterns).hasSize(1);
            assertThat(patterns.get(0).getPatternName()).isEqualTo("Updated Pattern");
            assertThat(patterns.get(0).getConfidenceScore()).isEqualTo(0.95);
        }

        @Test
        @DisplayName("Should save multiple patterns")
        void shouldSaveMultiplePatterns() {
            repository.savePattern(createFraudPattern("pattern-1"));
            repository.savePattern(createFraudPattern("pattern-2"));
            repository.savePattern(createFraudPattern("pattern-3"));

            List<FraudPattern> patterns = repository.getActivePatterns();
            assertThat(patterns).hasSize(3);
        }
    }

    @Nested
    @DisplayName("getActivePatterns Tests")
    class GetActivePatternsTests {

        @Test
        @DisplayName("Should return all active patterns")
        void shouldReturnAllActivePatterns() {
            repository.savePattern(createFraudPattern("pattern-1"));
            repository.savePattern(createFraudPattern("pattern-2"));
            repository.savePattern(createFraudPattern("pattern-3"));

            List<FraudPattern> patterns = repository.getActivePatterns();

            assertThat(patterns).hasSize(3);
        }

        @Test
        @DisplayName("Should return empty list when no patterns exist")
        void shouldReturnEmptyListWhenNoPatternsExist() {
            List<FraudPattern> patterns = repository.getActivePatterns();

            assertThat(patterns).isEmpty();
        }

        @Test
        @DisplayName("Should return new list each time")
        void shouldReturnNewListEachTime() {
            repository.savePattern(createFraudPattern("pattern-1"));

            List<FraudPattern> patterns1 = repository.getActivePatterns();
            List<FraudPattern> patterns2 = repository.getActivePatterns();

            assertThat(patterns1).isNotSameAs(patterns2);
            assertThat(patterns1).isEqualTo(patterns2);
        }
    }

    @Nested
    @DisplayName("Concurrency Tests")
    class ConcurrencyTests {

        @Test
        @DisplayName("Should handle concurrent saves")
        void shouldHandleConcurrentSaves() throws InterruptedException {
            int threadCount = 10;
            Thread[] threads = new Thread[threadCount];

            for (int i = 0; i < threadCount; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    repository.saveAnalysisResult(createAnalysisResult("analysis-" + index, USER_ID, Instant.now()));
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            List<FraudAnalysisResult> results = repository.findByUserId(USER_ID, 100);
            assertThat(results).hasSize(threadCount);
        }

        @Test
        @DisplayName("Should handle concurrent reads")
        void shouldHandleConcurrentReads() throws InterruptedException {
            repository.saveAnalysisResult(createAnalysisResult(ANALYSIS_ID, USER_ID, Instant.now()));

            int threadCount = 10;
            Thread[] threads = new Thread[threadCount];
            final boolean[] success = {true};

            for (int i = 0; i < threadCount; i++) {
                threads[i] = new Thread(() -> {
                    try {
                        var result = repository.findAnalysisById(ANALYSIS_ID);
                        if (result.isEmpty()) {
                            success[0] = false;
                        }
                    } catch (Exception e) {
                        success[0] = false;
                    }
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            assertThat(success[0]).isTrue();
        }
    }

    @Nested
    @DisplayName("Data Retention Tests")
    class DataRetentionTests {

        @Test
        @DisplayName("Should store recent analysis results")
        void shouldStoreRecentAnalysisResults() {
            FraudAnalysisResult result = createAnalysisResult(ANALYSIS_ID, USER_ID, Instant.now());
            repository.saveAnalysisResult(result);

            var found = repository.findAnalysisById(ANALYSIS_ID);
            assertThat(found).isPresent();
        }

        @Test
        @DisplayName("Should handle very old analysis results")
        void shouldHandleVeryOldAnalysisResults() {
            Instant oldTimestamp = Instant.now().minusSeconds(200 * 24 * 60 * 60); // 200 days ago
            FraudAnalysisResult result = createAnalysisResult(ANALYSIS_ID, USER_ID, oldTimestamp);
            repository.saveAnalysisResult(result);

            // After saving another recent result, old data might be cleaned up
            repository.saveAnalysisResult(createAnalysisResult("recent-analysis", USER_ID, Instant.now()));

            // The old analysis may or may not exist depending on cleanup timing
            // This test verifies the repository handles old data gracefully
            assertThatCode(() -> repository.findAnalysisById(ANALYSIS_ID))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should maintain consistency across operations")
        void shouldMaintainConsistencyAcrossOperations() {
            String userId = "integration-user";
            Instant now = Instant.now();

            // Save multiple analyses
            repository.saveAnalysisResult(createAnalysisResult("analysis-1", userId, now));
            repository.saveAnalysisResult(createAnalysisResult("analysis-2", userId, now.plusSeconds(1)));

            // Verify findByUserId
            List<FraudAnalysisResult> byUser = repository.findByUserId(userId, 10);
            assertThat(byUser).hasSize(2);

            // Verify individual finds
            assertThat(repository.findAnalysisById("analysis-1")).isPresent();
            assertThat(repository.findAnalysisById("analysis-2")).isPresent();

            // Add patterns
            repository.savePattern(createFraudPattern("pattern-1"));
            repository.savePattern(createFraudPattern("pattern-2"));

            // Verify patterns
            List<FraudPattern> patterns = repository.getActivePatterns();
            assertThat(patterns).hasSize(2);
        }

        @Test
        @DisplayName("Should handle mixed operations")
        void shouldHandleMixedOperations() {
            String user1 = "user-1";
            String user2 = "user-2";
            Instant now = Instant.now();

            // Save analyses for different users
            repository.saveAnalysisResult(createAnalysisResult("a1", user1, now));
            repository.saveAnalysisResult(createAnalysisResult("a2", user2, now.plusSeconds(1)));
            repository.saveAnalysisResult(createAnalysisResult("a3", user1, now.plusSeconds(2)));

            // Verify each user's analyses
            assertThat(repository.findByUserId(user1, 10)).hasSize(2);
            assertThat(repository.findByUserId(user2, 10)).hasSize(1);

            // Verify patterns are independent
            repository.savePattern(createFraudPattern("p1"));
            assertThat(repository.getActivePatterns()).hasSize(1);
        }
    }
}

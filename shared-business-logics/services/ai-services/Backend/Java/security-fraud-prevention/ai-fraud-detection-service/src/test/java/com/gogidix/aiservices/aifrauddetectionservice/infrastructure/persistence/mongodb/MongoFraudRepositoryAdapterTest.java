package com.gogidix.aiservices.aifrauddetectionservice.infrastructure.persistence.mongodb;

import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudAction;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudAnalysisResult;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudPattern;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.RiskLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for MongoFraudRepositoryAdapter.
 * Tests MongoDB repository adapter operations.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("MongoFraudRepositoryAdapter Tests")
class MongoFraudRepositoryAdapterTest {

    @Mock
    private SpringDataFraudAnalysisResultRepository analysisRepository;

    @Mock
    private SpringDataFraudPatternRepository patternRepository;

    private MongoFraudRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new MongoFraudRepositoryAdapter(analysisRepository, patternRepository);
    }

    @Nested
    @DisplayName("Save Analysis Result")
    class SaveAnalysisResultTests {

        @Test
        @DisplayName("Should save new analysis result when not exists")
        void shouldSaveNewAnalysisResult() {
            FraudAnalysisResult result = createTestAnalysisResult("analysis-001", "user-001", "tenant-001", 0.5);

            when(analysisRepository.findByAnalysisId("analysis-001")).thenReturn(null);

            adapter.saveAnalysisResult(result);

            ArgumentCaptor<FraudAnalysisResultEntity> captor = ArgumentCaptor.forClass(FraudAnalysisResultEntity.class);
            verify(analysisRepository).save(captor.capture());

            FraudAnalysisResultEntity saved = captor.getValue();
            assertThat(saved.getAnalysisId()).isEqualTo("analysis-001");
            assertThat(saved.getUserId()).isEqualTo("user-001");
            assertThat(saved.getTenantId()).isEqualTo("tenant-001");
            assertThat(saved.getFraudScore()).isEqualTo(0.5);
        }

        @Test
        @DisplayName("Should update existing analysis result when exists")
        void shouldUpdateExistingAnalysisResult() {
            FraudAnalysisResult newResult = createTestAnalysisResult("analysis-001", "user-001", "tenant-001", 0.9);

            FraudAnalysisResultEntity existing = new FraudAnalysisResultEntity();
            existing.setId("existing-id");
            existing.setAnalysisId("analysis-001");

            when(analysisRepository.findByAnalysisId("analysis-001")).thenReturn(existing);

            adapter.saveAnalysisResult(newResult);

            ArgumentCaptor<FraudAnalysisResultEntity> captor = ArgumentCaptor.forClass(FraudAnalysisResultEntity.class);
            verify(analysisRepository).save(captor.capture());

            FraudAnalysisResultEntity saved = captor.getValue();
            assertThat(saved.getAnalysisId()).isEqualTo("analysis-001");
            assertThat(saved.getFraudScore()).isEqualTo(0.9);
        }
    }

    @Nested
    @DisplayName("Find Analysis By ID")
    class FindAnalysisByIdTests {

        @Test
        @DisplayName("Should return analysis result when found")
        void shouldReturnAnalysisResultWhenFound() {
            FraudAnalysisResultEntity entity = new FraudAnalysisResultEntity();
            entity.setAnalysisId("analysis-001");
            entity.setUserId("user-001");
            entity.setTenantId("tenant-001");
            entity.setFraudScore(0.5);
            entity.setRiskLevel(RiskLevel.LOW.name());
            entity.setRecommendedAction(FraudAction.ALLOW.name());
            entity.setReasons(List.of("Test reason"));
            entity.setTimestamp(Instant.now());
            entity.setModelVersion("v1.0.0");

            when(analysisRepository.findByAnalysisId("analysis-001")).thenReturn(entity);

            Optional<FraudAnalysisResult> result = adapter.findAnalysisById("analysis-001");

            assertThat(result).isPresent();
            assertThat(result.get().getAnalysisId()).isEqualTo("analysis-001");
            assertThat(result.get().getFraudScore()).isEqualTo(0.5);
        }

        @Test
        @DisplayName("Should return empty optional when not found")
        void shouldReturnEmptyOptionalWhenNotFound() {
            when(analysisRepository.findByAnalysisId("analysis-001")).thenReturn(null);

            Optional<FraudAnalysisResult> result = adapter.findAnalysisById("analysis-001");

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("Find By User ID")
    class FindByUserIdTests {

        @Test
        @DisplayName("Should return analyses for user with limit")
        void shouldReturnAnalysesWithLimit() {
            List<FraudAnalysisResultEntity> entities = List.of(
                    createTestEntity("analysis-001", "user-001", "tenant-001"),
                    createTestEntity("analysis-002", "user-001", "tenant-001")
            );

            when(analysisRepository.findByUserIdOrderByTimestampDesc("user-001", PageRequest.of(0, 10)))
                    .thenReturn(entities);

            List<FraudAnalysisResult> results = adapter.findByUserId("user-001", 10);

            assertThat(results).hasSize(2);
            assertThat(results.get(0).getUserId()).isEqualTo("user-001");
        }

        @Test
        @DisplayName("Should return all analyses when limit is zero")
        void shouldReturnAllAnalysesWhenLimitIsZero() {
            List<FraudAnalysisResultEntity> entities = List.of(
                    createTestEntity("analysis-001", "user-001", "tenant-001")
            );

            when(analysisRepository.findByUserIdOrderByTimestampDesc("user-001")).thenReturn(entities);

            List<FraudAnalysisResult> results = adapter.findByUserId("user-001", 0);

            assertThat(results).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Save Pattern")
    class SavePatternTests {

        @Test
        @DisplayName("Should save new pattern when not exists")
        void shouldSaveNewPatternWhenNotExists() {
            FraudPattern pattern = createTestPattern("pattern-001", "tenant-001");

            when(patternRepository.findByPatternId("pattern-001")).thenReturn(null);

            adapter.savePattern(pattern);

            ArgumentCaptor<FraudPatternEntity> captor = ArgumentCaptor.forClass(FraudPatternEntity.class);
            verify(patternRepository).save(captor.capture());

            FraudPatternEntity saved = captor.getValue();
            assertThat(saved.getPatternId()).isEqualTo("pattern-001");
        }

        @Test
        @DisplayName("Should update existing pattern and increment occurrence")
        void shouldUpdateExistingPatternAndIncrement() {
            FraudPattern pattern = createTestPattern("pattern-001", "tenant-001");

            FraudPatternEntity existing = new FraudPatternEntity(pattern);
            existing.setOccurrenceCount(1);

            when(patternRepository.findByPatternId("pattern-001")).thenReturn(existing);

            adapter.savePattern(pattern);

            ArgumentCaptor<FraudPatternEntity> captor = ArgumentCaptor.forClass(FraudPatternEntity.class);
            verify(patternRepository).save(captor.capture());

            FraudPatternEntity saved = captor.getValue();
            assertThat(saved.getOccurrenceCount()).isGreaterThanOrEqualTo(2);
        }
    }

    @Nested
    @DisplayName("Get Active Patterns")
    class GetActivePatternsTests {

        @Test
        @DisplayName("Should return active patterns only")
        void shouldReturnActivePatternsOnly() {
            List<FraudPatternEntity> entities = List.of(
                    createActivePatternEntity("pattern-001", "tenant-001"),
                    createActivePatternEntity("pattern-002", "tenant-001")
            );

            when(patternRepository.findByIsActiveTrueOrderByLastSeenDesc()).thenReturn(entities);

            List<FraudPattern> patterns = adapter.getActivePatterns();

            assertThat(patterns).hasSize(2);
        }

        @Test
        @DisplayName("Should return empty list when no active patterns")
        void shouldReturnEmptyListWhenNoActivePatterns() {
            when(patternRepository.findByIsActiveTrueOrderByLastSeenDesc()).thenReturn(List.of());

            List<FraudPattern> patterns = adapter.getActivePatterns();

            assertThat(patterns).isEmpty();
        }
    }

    @Nested
    @DisplayName("Get Active Patterns By Tenant")
    class GetActivePatternsByTenantTests {

        @Test
        @DisplayName("Should return active patterns for tenant")
        void shouldReturnActivePatternsForTenant() {
            List<FraudPatternEntity> entities = List.of(
                    createActivePatternEntity("pattern-001", "tenant-001")
            );

            when(patternRepository.findByTenantIdAndIsActiveTrueOrderByLastSeenDesc("tenant-001"))
                    .thenReturn(entities);

            List<FraudPattern> patterns = adapter.getActivePatternsByTenant("tenant-001");

            assertThat(patterns).hasSize(1);
            assertThat(patterns.get(0).getTenantId()).isEqualTo("tenant-001");
        }

        @Test
        @DisplayName("Should return empty list for tenant with no patterns")
        void shouldReturnEmptyListForTenantWithNoPatterns() {
            when(patternRepository.findByTenantIdAndIsActiveTrueOrderByLastSeenDesc("tenant-001"))
                    .thenReturn(List.of());

            List<FraudPattern> patterns = adapter.getActivePatternsByTenant("tenant-001");

            assertThat(patterns).isEmpty();
        }
    }

    @Nested
    @DisplayName("Find By Tenant ID")
    class FindByTenantIdTests {

        @Test
        @DisplayName("Should return analyses for tenant with limit")
        void shouldReturnAnalysesForTenantWithLimit() {
            List<FraudAnalysisResultEntity> entities = List.of(
                    createTestEntity("analysis-001", "user-001", "tenant-001")
            );

            when(analysisRepository.findByTenantIdOrderByTimestampDesc("tenant-001", PageRequest.of(0, 10)))
                    .thenReturn(entities);

            List<FraudAnalysisResult> results = adapter.findByTenantId("tenant-001", 10);

            assertThat(results).hasSize(1);
            assertThat(results.get(0).getTenantId()).isEqualTo("tenant-001");
        }

        @Test
        @DisplayName("Should return all analyses for tenant when limit is zero")
        void shouldReturnAllAnalysesForTenantWhenLimitIsZero() {
            List<FraudAnalysisResultEntity> entities = List.of(
                    createTestEntity("analysis-001", "user-001", "tenant-001")
            );

            when(analysisRepository.findByTenantIdOrderByTimestampDesc("tenant-001")).thenReturn(entities);

            List<FraudAnalysisResult> results = adapter.findByTenantId("tenant-001", 0);

            assertThat(results).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Delete Pattern")
    class DeletePatternTests {

        @Test
        @DisplayName("Should deactivate pattern when exists")
        void shouldDeactivatePatternWhenExists() {
            FraudPatternEntity entity = new FraudPatternEntity();
            entity.setPatternId("pattern-001");
            entity.setActive(true);

            when(patternRepository.findByPatternId("pattern-001")).thenReturn(entity);
            when(patternRepository.save(any())).thenReturn(entity);

            boolean deleted = adapter.deletePattern("pattern-001");

            assertThat(deleted).isTrue();
            assertThat(entity.isActive()).isFalse();
            verify(patternRepository).save(entity);
        }

        @Test
        @DisplayName("Should return false when pattern not found")
        void shouldReturnFalseWhenPatternNotFound() {
            when(patternRepository.findByPatternId("pattern-001")).thenReturn(null);

            boolean deleted = adapter.deletePattern("pattern-001");

            assertThat(deleted).isFalse();
            verify(patternRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Get Tenant Stats")
    class GetTenantStatsTests {

        @Test
        @DisplayName("Should return statistics for tenant")
        void shouldReturnStatisticsForTenant() {
            when(analysisRepository.countByTenantId("tenant-001")).thenReturn(100L);
            when(patternRepository.countByTenantIdAndIsActiveTrue("tenant-001")).thenReturn(5L);

            MongoFraudRepositoryAdapter.TenantStats stats = adapter.getTenantStats("tenant-001");

            assertThat(stats.tenantId()).isEqualTo("tenant-001");
            assertThat(stats.analysisCount()).isEqualTo(100L);
            assertThat(stats.patternCount()).isEqualTo(5L);
        }

        @Test
        @DisplayName("Should return zero stats for non-existent tenant")
        void shouldReturnZeroStatsForNonExistentTenant() {
            when(analysisRepository.countByTenantId("non-existent")).thenReturn(0L);
            when(patternRepository.countByTenantIdAndIsActiveTrue("non-existent")).thenReturn(0L);

            MongoFraudRepositoryAdapter.TenantStats stats = adapter.getTenantStats("non-existent");

            assertThat(stats.tenantId()).isEqualTo("non-existent");
            assertThat(stats.analysisCount()).isEqualTo(0L);
            assertThat(stats.patternCount()).isEqualTo(0L);
        }
    }

    // Helper methods

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

    private FraudAnalysisResultEntity createTestEntity(String analysisId, String userId, String tenantId) {
        FraudAnalysisResultEntity entity = new FraudAnalysisResultEntity();
        entity.setAnalysisId(analysisId);
        entity.setUserId(userId);
        entity.setTenantId(tenantId);
        entity.setFraudScore(0.5);
        entity.setRiskLevel(RiskLevel.LOW.name());
        entity.setRecommendedAction(FraudAction.ALLOW.name());
        entity.setReasons(List.of("Test reason"));
        entity.setTimestamp(Instant.now());
        entity.setModelVersion("v1.0.0");
        return entity;
    }

    private FraudPattern createTestPattern(String patternId, String tenantId) {
        return FraudPattern.builder()
                .patternId(patternId)
                .tenantId(tenantId)
                .patternName("Test Pattern")
                .description("Test pattern description")
                .confidenceScore(0.8)
                .lastSeen(Instant.now())
                .occurrenceCount(1)
                .build();
    }

    private FraudPatternEntity createActivePatternEntity(String patternId, String tenantId) {
        FraudPattern pattern = createTestPattern(patternId, tenantId);
        FraudPatternEntity entity = new FraudPatternEntity(pattern);
        entity.setActive(true);
        return entity;
    }
}

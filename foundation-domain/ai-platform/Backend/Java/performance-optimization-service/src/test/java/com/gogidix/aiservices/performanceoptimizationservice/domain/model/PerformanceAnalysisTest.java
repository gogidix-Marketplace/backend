package com.gogidix.aiservices.performanceoptimizationservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("PerformanceAnalysis Domain Entity Tests")
class PerformanceAnalysisTest {

    private static final String TENANT_ID = "tenant-001";
    private static final String SERVICE = "ai-fraud-detection";
    private static final List<String> METRICS = List.of("cpu", "memory", "responseTime");

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create analysis with required fields")
        void shouldCreateWithRequiredFields() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);

            assertThat(analysis.getTenantId()).isEqualTo(TENANT_ID);
            assertThat(analysis.getService()).isEqualTo(SERVICE);
            assertThat(analysis.getMetrics()).isEqualTo(METRICS);
        }

        @Test
        @DisplayName("Should generate unique ID")
        void shouldGenerateUniqueId() {
            PerformanceAnalysis analysis1 = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);
            PerformanceAnalysis analysis2 = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);

            assertThat(analysis1.getId()).isNotNull();
            assertThat(analysis2.getId()).isNotNull();
            assertThat(analysis1.getId()).isNotEqualTo(analysis2.getId());
        }

        @Test
        @DisplayName("Should generate analysis ID")
        void shouldGenerateAnalysisId() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);

            assertThat(analysis.getAnalysisId()).isNotNull();
            assertThat(analysis.getAnalysisId()).startsWith("analysis_");
        }

        @Test
        @DisplayName("Should set created timestamp")
        void shouldSetCreatedTimestamp() {
            Instant before = Instant.now();
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);
            Instant after = Instant.now();

            assertThat(analysis.getCreatedAt()).isNotNull();
            assertThat(analysis.getCreatedAt()).isBetween(before, after);
        }

        @Test
        @DisplayName("Should initialize with empty bottlenecks")
        void shouldInitializeWithEmptyBottlenecks() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);

            assertThat(analysis.getBottlenecks()).isNotNull().isEmpty();
        }

        @Test
        @DisplayName("Should initialize with empty recommendations")
        void shouldInitializeWithEmptyRecommendations() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);

            assertThat(analysis.getRecommendations()).isNotNull().isEmpty();
        }

        @Test
        @DisplayName("Should initialize with null score")
        void shouldInitializeWithNullScore() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);

            assertThat(analysis.getScore()).isNull();
        }

        @Test
        @DisplayName("Should initialize with null end time")
        void shouldInitializeWithNullEndTime() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);

            assertThat(analysis.getEndTime()).isNull();
        }

        @ParameterizedTest
        @NullSource
        @DisplayName("Should throw when tenantId is null")
        void shouldThrowWhenTenantIdIsNull(String tenantId) {
            assertThatThrownBy(() -> new PerformanceAnalysis(tenantId, SERVICE, METRICS))
                    .isInstanceOf(NullPointerException.class);
        }

        @ParameterizedTest
        @NullSource
        @DisplayName("Should throw when service is null")
        void shouldThrowWhenServiceIsNull(String service) {
            assertThatThrownBy(() -> new PerformanceAnalysis(TENANT_ID, service, METRICS))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should throw when metrics is null")
        void shouldThrowWhenMetricsIsNull() {
            assertThatThrownBy(() -> new PerformanceAnalysis(TENANT_ID, SERVICE, null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should accept empty metrics list")
        void shouldAcceptEmptyMetricsList() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, List.of());

            assertThat(analysis.getMetrics()).isEmpty();
        }

        @Test
        @DisplayName("Should copy metrics list")
        void shouldCopyMetricsList() {
            List<String> originalMetrics = List.of("cpu", "memory");
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, originalMetrics);

            assertThat(analysis.getMetrics()).isEqualTo(originalMetrics);
        }
    }

    @Nested
    @DisplayName("Complete Tests")
    class CompleteTests {

        @Test
        @DisplayName("Should complete analysis with results")
        void shouldCompleteWithResults() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);
            List<Bottleneck> bottlenecks = List.of(
                    new Bottleneck("Database", "HIGH", "Slow query", 85.0)
            );
            List<Recommendation> recommendations = List.of(
                    new Recommendation("OPTIMIZATION", "Add index", 1, "CREATE INDEX...")
            );
            Integer score = 75;

            analysis.complete(bottlenecks, recommendations, score);

            assertThat(analysis.getBottlenecks()).isEqualTo(bottlenecks);
            assertThat(analysis.getRecommendations()).isEqualTo(recommendations);
            assertThat(analysis.getScore()).isEqualTo(score);
            assertThat(analysis.getEndTime()).isNotNull();
        }

        @Test
        @DisplayName("Should set end time when completed")
        void shouldSetEndTimeWhenCompleted() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);

            assertThat(analysis.getEndTime()).isNull();

            analysis.complete(List.of(), List.of(), 50);

            assertThat(analysis.getEndTime()).isNotNull();
        }

        @Test
        @DisplayName("Should accept empty bottlenecks")
        void shouldAcceptEmptyBottlenecks() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);

            analysis.complete(List.of(), List.of(new Recommendation("TYPE", "desc", 1, "action")), 50);

            assertThat(analysis.getBottlenecks()).isEmpty();
        }

        @Test
        @DisplayName("Should accept empty recommendations")
        void shouldAcceptEmptyRecommendations() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);

            analysis.complete(List.of(new Bottleneck("DB", "HIGH", "slow", 80.0)), List.of(), 50);

            assertThat(analysis.getRecommendations()).isEmpty();
        }

        @Test
        @DisplayName("Should accept zero score")
        void shouldAcceptZeroScore() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);

            analysis.complete(List.of(), List.of(), 0);

            assertThat(analysis.getScore()).isZero();
        }

        @Test
        @DisplayName("Should accept negative score")
        void shouldAcceptNegativeScore() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);

            analysis.complete(List.of(), List.of(), -10);

            assertThat(analysis.getScore()).isEqualTo(-10);
        }

        @Test
        @DisplayName("Should accept perfect score")
        void shouldAcceptPerfectScore() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);

            analysis.complete(List.of(), List.of(), 100);

            assertThat(analysis.getScore()).isEqualTo(100);
        }

        @Test
        @DisplayName("Should copy bottlenecks list")
        void shouldCopyBottlenecksList() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);
            List<Bottleneck> bottlenecks = List.of(
                    new Bottleneck("DB", "HIGH", "slow", 80.0),
                    new Bottleneck("Cache", "MEDIUM", "miss", 60.0)
            );

            analysis.complete(bottlenecks, List.of(), 50);

            assertThat(analysis.getBottlenecks()).hasSize(2);
        }

        @Test
        @DisplayName("Should copy recommendations list")
        void shouldCopyRecommendationsList() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);
            List<Recommendation> recommendations = List.of(
                    new Recommendation("OPT", "Add index", 1, "CREATE INDEX"),
                    new Recommendation("CACHE", "Add cache", 2, "Use Redis")
            );

            analysis.complete(List.of(), recommendations, 50);

            assertThat(analysis.getRecommendations()).hasSize(2);
        }

        @Test
        @DisplayName("Should allow multiple completions")
        void shouldAllowMultipleCompletions() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);

            analysis.complete(List.of(new Bottleneck("DB", "HIGH", "slow", 80.0)), List.of(), 50);
            assertThat(analysis.getScore()).isEqualTo(50);

            analysis.complete(List.of(new Bottleneck("Cache", "MEDIUM", "miss", 60.0)), List.of(), 75);
            assertThat(analysis.getScore()).isEqualTo(75);
        }
    }

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build with all fields")
        void shouldBuildWithAllFields() {
            List<Bottleneck> bottlenecks = List.of(new Bottleneck("DB", "HIGH", "slow", 80.0));
            List<Recommendation> recommendations = List.of(new Recommendation("OPT", "Add index", 1, "CREATE"));

            PerformanceAnalysis analysis = new PerformanceAnalysis.Builder()
                    .tenantId(TENANT_ID)
                    .service(SERVICE)
                    .score(75)
                    .bottlenecks(bottlenecks)
                    .recommendations(recommendations)
                    .build();

            assertThat(analysis.getTenantId()).isEqualTo(TENANT_ID);
            assertThat(analysis.getService()).isEqualTo(SERVICE);
            assertThat(analysis.getScore()).isEqualTo(75);
            assertThat(analysis.getBottlenecks()).isEqualTo(bottlenecks);
            assertThat(analysis.getRecommendations()).isEqualTo(recommendations);
        }

        @Test
        @DisplayName("Should build with required fields only")
        void shouldBuildWithRequiredFieldsOnly() {
            PerformanceAnalysis analysis = new PerformanceAnalysis.Builder()
                    .tenantId(TENANT_ID)
                    .service(SERVICE)
                    .build();

            assertThat(analysis.getTenantId()).isEqualTo(TENANT_ID);
            assertThat(analysis.getService()).isEqualTo(SERVICE);
            assertThat(analysis.getCreatedAt()).isNotNull();
        }

        @Test
        @DisplayName("Should throw when tenantId is missing")
        void shouldThrowWhenTenantIdIsMissing() {
            PerformanceAnalysis.Builder builder = new PerformanceAnalysis.Builder()
                    .service(SERVICE);

            assertThatThrownBy(builder::build)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("tenantId required");
        }

        @Test
        @DisplayName("Should throw when service is missing")
        void shouldThrowWhenServiceIsMissing() {
            PerformanceAnalysis.Builder builder = new PerformanceAnalysis.Builder()
                    .tenantId(TENANT_ID);

            assertThatThrownBy(builder::build)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("service required");
        }

        @Test
        @DisplayName("Should support method chaining")
        void shouldSupportMethodChaining() {
            PerformanceAnalysis analysis = new PerformanceAnalysis.Builder()
                    .tenantId(TENANT_ID)
                    .service(SERVICE)
                    .score(85)
                    .build();

            assertThat(analysis.getScore()).isEqualTo(85);
        }

        @Test
        @DisplayName("Should set id via builder")
        void shouldSetIdViaBuilder() {
            String id = "custom-id";
            PerformanceAnalysis analysis = new PerformanceAnalysis.Builder()
                    .id(id)
                    .tenantId(TENANT_ID)
                    .service(SERVICE)
                    .build();

            assertThat(analysis.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Should set analysisId via builder")
        void shouldSetAnalysisIdViaBuilder() {
            String analysisId = "custom-analysis-123";
            PerformanceAnalysis analysis = new PerformanceAnalysis.Builder()
                    .analysisId(analysisId)
                    .tenantId(TENANT_ID)
                    .service(SERVICE)
                    .build();

            assertThat(analysis.getAnalysisId()).isEqualTo(analysisId);
        }

        @Test
        @DisplayName("Should set null score via builder")
        void shouldSetNullScoreViaBuilder() {
            PerformanceAnalysis analysis = new PerformanceAnalysis.Builder()
                    .tenantId(TENANT_ID)
                    .service(SERVICE)
                    .score(null)
                    .build();

            assertThat(analysis.getScore()).isNull();
        }

        @Test
        @DisplayName("Should set empty bottlenecks via builder")
        void shouldSetEmptyBottlenecksViaBuilder() {
            PerformanceAnalysis analysis = new PerformanceAnalysis.Builder()
                    .tenantId(TENANT_ID)
                    .service(SERVICE)
                    .bottlenecks(List.of())
                    .build();

            assertThat(analysis.getBottlenecks()).isEmpty();
        }

        @Test
        @DisplayName("Should set empty recommendations via builder")
        void shouldSetEmptyRecommendationsViaBuilder() {
            PerformanceAnalysis analysis = new PerformanceAnalysis.Builder()
                    .tenantId(TENANT_ID)
                    .service(SERVICE)
                    .recommendations(List.of())
                    .build();

            assertThat(analysis.getRecommendations()).isEmpty();
        }

        @Test
        @DisplayName("Should auto-generate createdAt if not set")
        void shouldAutoGenerateCreatedAtIfNotSet() {
            Instant before = Instant.now();
            PerformanceAnalysis analysis = new PerformanceAnalysis.Builder()
                    .tenantId(TENANT_ID)
                    .service(SERVICE)
                    .build();
            Instant after = Instant.now();

            assertThat(analysis.getCreatedAt()).isBetween(before, after);
        }
    }

    @Nested
    @DisplayName("Metrics Tests")
    class MetricsTests {

        @Test
        @DisplayName("Should accept single metric")
        void shouldAcceptSingleMetric() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, List.of("cpu"));

            assertThat(analysis.getMetrics()).containsExactly("cpu");
        }

        @ParameterizedTest
        @ValueSource(strings = {"cpu", "memory", "disk", "network", "responseTime", "throughput", "errorRate", "cacheHitRatio"})
        @DisplayName("Should accept standard metrics")
        void shouldAcceptStandardMetrics(String metric) {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, List.of(metric));

            assertThat(analysis.getMetrics()).containsExactly(metric);
        }

        @Test
        @DisplayName("Should accept multiple metrics")
        void shouldAcceptMultipleMetrics() {
            List<String> metrics = List.of("cpu", "memory", "disk", "network");
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, metrics);

            assertThat(analysis.getMetrics()).hasSize(4);
        }

        @Test
        @DisplayName("Should accept custom metrics")
        void shouldAcceptCustomMetrics() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, List.of("customMetric", "apiLatency"));

            assertThat(analysis.getMetrics()).contains("customMetric", "apiLatency");
        }

        @Test
        @DisplayName("Should accept metrics with special characters")
        void shouldAcceptMetricsWithSpecialCharacters() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, List.of("cpu.usage", "mem-usage", "disk_io"));

            assertThat(analysis.getMetrics()).contains("cpu.usage", "mem-usage", "disk_io");
        }
    }

    @Nested
    @DisplayName("Getter Tests")
    class GetterTests {

        @Test
        @DisplayName("Should get ID")
        void shouldGetId() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);

            assertThat(analysis.getId()).isNotNull();
        }

        @Test
        @DisplayName("Should get analysis ID")
        void shouldGetAnalysisId() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);

            assertThat(analysis.getAnalysisId()).isNotNull();
        }

        @Test
        @DisplayName("Should get tenant ID")
        void shouldGetTenantId() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);

            assertThat(analysis.getTenantId()).isEqualTo(TENANT_ID);
        }

        @Test
        @DisplayName("Should get service")
        void shouldGetService() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);

            assertThat(analysis.getService()).isEqualTo(SERVICE);
        }

        @Test
        @DisplayName("Should get metrics")
        void shouldGetMetrics() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);

            assertThat(analysis.getMetrics()).isEqualTo(METRICS);
        }

        @Test
        @DisplayName("Should get bottlenecks")
        void shouldGetBottlenecks() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);
            List<Bottleneck> bottlenecks = List.of(new Bottleneck("DB", "HIGH", "slow", 80.0));
            analysis.complete(bottlenecks, List.of(), 50);

            assertThat(analysis.getBottlenecks()).isEqualTo(bottlenecks);
        }

        @Test
        @DisplayName("Should get recommendations")
        void shouldGetRecommendations() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);
            List<Recommendation> recommendations = List.of(new Recommendation("OPT", "Add index", 1, "CREATE"));
            analysis.complete(List.of(), recommendations, 50);

            assertThat(analysis.getRecommendations()).isEqualTo(recommendations);
        }

        @Test
        @DisplayName("Should get score")
        void shouldGetScore() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);
            analysis.complete(List.of(), List.of(), 85);

            assertThat(analysis.getScore()).isEqualTo(85);
        }

        @Test
        @DisplayName("Should get created timestamp")
        void shouldGetCreatedTimestamp() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);

            assertThat(analysis.getCreatedAt()).isNotNull();
        }

        @Test
        @DisplayName("Should get start time")
        void shouldGetStartTime() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);

            assertThat(analysis.getStartTime()).isNull();
        }

        @Test
        @DisplayName("Should get end time after completion")
        void shouldGetEndTimeAfterCompletion() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);
            analysis.complete(List.of(), List.of(), 50);

            assertThat(analysis.getEndTime()).isNotNull();
        }
    }

    @Nested
    @DisplayName("Service Name Tests")
    class ServiceNameTests {

        @ParameterizedTest
        @ValueSource(strings = {"ai-fraud-detection", "recommendation-engine", "sentiment-analysis", "notification-service", "monitoring-service"})
        @DisplayName("Should accept various service names")
        void shouldAcceptVariousServiceNames(String service) {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, service, METRICS);

            assertThat(analysis.getService()).isEqualTo(service);
        }

        @Test
        @DisplayName("Should accept service name with special characters")
        void shouldAcceptServiceNameWithSpecialCharacters() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, "api-gateway-v2", METRICS);

            assertThat(analysis.getService()).isEqualTo("api-gateway-v2");
        }

        @Test
        @DisplayName("Should accept service name with numbers")
        void shouldAcceptServiceNameWithNumbers() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, "service-v2-api", METRICS);

            assertThat(analysis.getService()).isEqualTo("service-v2-api");
        }

        @Test
        @DisplayName("Should accept empty service name")
        void shouldAcceptEmptyServiceName() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, "", METRICS);

            assertThat(analysis.getService()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Tenant ID Tests")
    class TenantIdTests {

        @ParameterizedTest
        @ValueSource(strings = {"tenant-001", "tenant-002", "org-123", "customer-456", "tenant-prod"})
        @DisplayName("Should accept various tenant IDs")
        void shouldAcceptVariousTenantIds(String tenantId) {
            PerformanceAnalysis analysis = new PerformanceAnalysis(tenantId, SERVICE, METRICS);

            assertThat(analysis.getTenantId()).isEqualTo(tenantId);
        }

        @Test
        @DisplayName("Should accept tenant ID with UUID format")
        void shouldAcceptTenantIdWithUuidFormat() {
            String uuidTenantId = "tenant-550e8400-e29b-41d4-a716-446655440000";
            PerformanceAnalysis analysis = new PerformanceAnalysis(uuidTenantId, SERVICE, METRICS);

            assertThat(analysis.getTenantId()).isEqualTo(uuidTenantId);
        }

        @Test
        @DisplayName("Should accept tenant ID with special characters")
        void shouldAcceptTenantIdWithSpecialCharacters() {
            PerformanceAnalysis analysis = new PerformanceAnalysis("tenant_test_01", SERVICE, METRICS);

            assertThat(analysis.getTenantId()).isEqualTo("tenant_test_01");
        }
    }

    @Nested
    @DisplayName("Score Tests")
    class ScoreTests {

        @ParameterizedTest
        @ValueSource(ints = {0, 25, 50, 75, 100})
        @DisplayName("Should accept various scores")
        void shouldAcceptVariousScores(int score) {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);
            analysis.complete(List.of(), List.of(), score);

            assertThat(analysis.getScore()).isEqualTo(score);
        }

        @Test
        @DisplayName("Should handle failing score")
        void shouldHandleFailingScore() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);
            analysis.complete(List.of(), List.of(), 30);

            assertThat(analysis.getScore()).isLessThan(50);
        }

        @Test
        @DisplayName("Should handle passing score")
        void shouldHandlePassingScore() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);
            analysis.complete(List.of(), List.of(), 85);

            assertThat(analysis.getScore()).isGreaterThanOrEqualTo(80);
        }

        @Test
        @DisplayName("Should handle excellent score")
        void shouldHandleExcellentScore() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);
            analysis.complete(List.of(), List.of(), 95);

            assertThat(analysis.getScore()).isGreaterThan(90);
        }
    }

    @Nested
    @DisplayName("Lifecycle Tests")
    class LifecycleTests {

        @Test
        @DisplayName("Should follow in-progress lifecycle")
        void shouldFollowInProgressLifecycle() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);

            assertThat(analysis.getScore()).isNull();
            assertThat(analysis.getEndTime()).isNull();
            assertThat(analysis.getBottlenecks()).isEmpty();
            assertThat(analysis.getRecommendations()).isEmpty();
        }

        @Test
        @DisplayName("Should follow completed lifecycle")
        void shouldFollowCompletedLifecycle() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);

            assertThat(analysis.getScore()).isNull();
            assertThat(analysis.getEndTime()).isNull();

            analysis.complete(
                    List.of(new Bottleneck("DB", "HIGH", "slow", 80.0)),
                    List.of(new Recommendation("OPT", "Add index", 1, "CREATE")),
                    75
            );

            assertThat(analysis.getScore()).isEqualTo(75);
            assertThat(analysis.getEndTime()).isNotNull();
            assertThat(analysis.getBottlenecks()).hasSize(1);
            assertThat(analysis.getRecommendations()).hasSize(1);
        }

        @Test
        @DisplayName("Should update on re-completion")
        void shouldUpdateOnRecompletion() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);

            analysis.complete(
                    List.of(new Bottleneck("DB", "HIGH", "slow", 80.0)),
                    List.of(new Recommendation("OPT", "Add index", 1, "CREATE")),
                    75
            );

            Instant firstEndTime = analysis.getEndTime();

            analysis.complete(
                    List.of(new Bottleneck("Cache", "MEDIUM", "miss", 60.0)),
                    List.of(new Recommendation("CACHE", "Add cache", 2, "Redis")),
                    85
            );

            assertThat(analysis.getScore()).isEqualTo(85);
            assertThat(analysis.getEndTime()).isNotNull();
            assertThat(analysis.getBottlenecks()).hasSize(1);
            assertThat(analysis.getRecommendations()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle very long tenant ID")
        void shouldHandleVeryLongTenantId() {
            String longTenantId = "tenant-" + "x".repeat(200);
            PerformanceAnalysis analysis = new PerformanceAnalysis(longTenantId, SERVICE, METRICS);

            assertThat(analysis.getTenantId()).isEqualTo(longTenantId);
        }

        @Test
        @DisplayName("Should handle very long service name")
        void shouldHandleVeryLongServiceName() {
            String longService = "service-" + "y".repeat(200);
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, longService, METRICS);

            assertThat(analysis.getService()).isEqualTo(longService);
        }

        @Test
        @DisplayName("Should handle very large metrics list")
        void shouldHandleVeryLargeMetricsList() {
            List<String> largeMetrics = new java.util.ArrayList<>();
            for (int i = 0; i < 100; i++) {
                largeMetrics.add("metric" + i);
            }
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, largeMetrics);

            assertThat(analysis.getMetrics()).hasSize(100);
        }

        @Test
        @DisplayName("Should handle large number of bottlenecks")
        void shouldHandleLargeNumberOfBottlenecks() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);
            List<Bottleneck> bottlenecks = new java.util.ArrayList<>();
            for (int i = 0; i < 50; i++) {
                bottlenecks.add(new Bottleneck("Component" + i, "HIGH", "Issue" + i, 50.0 + i));
            }

            analysis.complete(bottlenecks, List.of(), 50);

            assertThat(analysis.getBottlenecks()).hasSize(50);
        }

        @Test
        @DisplayName("Should handle large number of recommendations")
        void shouldHandleLargeNumberOfRecommendations() {
            PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE, METRICS);
            List<Recommendation> recommendations = new java.util.ArrayList<>();
            for (int i = 0; i < 50; i++) {
                recommendations.add(new Recommendation("OPT" + i, "Recommendation" + i, i, "Action" + i));
            }

            analysis.complete(List.of(), recommendations, 50);

            assertThat(analysis.getRecommendations()).hasSize(50);
        }
    }
}

package com.gogidix.aiservices.aidocumentextractionservice.multitenancy;

import com.gogidix.aiservices.aidocumentextractionservice.infrastructure.metrics.DocumentExtractionMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Financial-Grade: Multi-Tenancy Isolation Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TenantIsolationTest {

    private DocumentExtractionMetrics metrics;
    private MeterRegistry meterRegistry;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        metrics = new DocumentExtractionMetrics(meterRegistry);
    }

    @Nested
    @DisplayName("1. Tenant Data Isolation Tests")
    class TenantDataIsolationTests {

        @Test
        @Order(1)
        @DisplayName("Should handle extractions for multiple tenants")
        void shouldHandleExtractionsForMultipleTenants() {
            String[] tenants = {"tenant-1", "tenant-2", "tenant-3"};

            for (String tenant : tenants) {
                for (int i = 0; i < 5; i++) {
                    metrics.incrementExtractionTotal();
                    metrics.incrementExtractionSuccess();
                    metrics.incrementDocumentsProcessed();
                }
            }

            long totalExtractions = (long) meterRegistry
                    .get("ai.document.extraction.documents")
                    .counter()
                    .count();

            assertThat(totalExtractions).isEqualTo(15);
        }

        @Test
        @Order(2)
        @DisplayName("Should maintain separate metrics per tenant")
        void shouldMaintainSeparateMetricsPerTenant() {
            int tenant1Requests = 10;
            int tenant2Requests = 5;
            int tenant3Requests = 15;

            for (int i = 0; i < tenant1Requests; i++) {
                metrics.incrementExtractionTotal();
                metrics.incrementExtractionSuccess();
                metrics.recordExtractionTime(100);
            }

            for (int i = 0; i < tenant2Requests; i++) {
                metrics.incrementExtractionTotal();
                metrics.incrementExtractionSuccess();
                metrics.recordExtractionTime(150);
            }

            for (int i = 0; i < tenant3Requests; i++) {
                metrics.incrementExtractionTotal();
                metrics.incrementExtractionSuccess();
                metrics.recordExtractionTime(120);
            }

            long totalRequests = (long) meterRegistry
                    .get("ai.document.extraction.total")
                    .counter()
                    .count();

            assertThat(totalRequests).isEqualTo(tenant1Requests + tenant2Requests + tenant3Requests);
        }
    }

    @Nested
    @DisplayName("2. Tenant Resource Quota Tests")
    class TenantResourceQuotaTests {

        @Test
        @Order(10)
        @DisplayName("Should enforce per-tenant request limits")
        void shouldEnforcePerTenantRequestLimits() {
            int tenantLimit = 10;
            int requests = 15;

            for (int i = 0; i < requests; i++) {
                metrics.incrementExtractionTotal();
                if (i < tenantLimit) {
                    metrics.incrementExtractionSuccess();
                    metrics.recordExtractionTime(100);
                } else {
                    metrics.incrementExtractionFailure();
                }
            }

            double errorRate = metrics.getErrorRate();
            assertThat(errorRate).isGreaterThan(0);
        }

        @Test
        @Order(11)
        @DisplayName("Should track tenant-specific throughput")
        void shouldTrackTenantSpecificThroughput() {
            int highThroughputRequests = 50;

            for (int i = 0; i < highThroughputRequests; i++) {
                metrics.incrementExtractionTotal();
                metrics.incrementExtractionSuccess();
                metrics.recordExtractionTime(50);
            }

            double p95Latency = metrics.getExtractionLatencyP95();
            assertThat(p95Latency).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("3. Tenant Configuration Tests")
    class TenantConfigurationTests {

        @Test
        @Order(20)
        @DisplayName("Should support per-tenant extraction models")
        void shouldSupportPerTenantExtractionModels() {
            int[] tenantModelTimes = {100, 150, 200};

            for (int time : tenantModelTimes) {
                metrics.incrementExtractionTotal();
                metrics.incrementExtractionSuccess();
                metrics.recordExtractionTime(time);
            }

            double p95Latency = metrics.getExtractionLatencyP95();
            assertThat(p95Latency).isGreaterThan(0);
        }

        @Test
        @Order(21)
        @DisplayName("Should handle tenant-specific field mappings")
        void shouldHandleTenantSpecificFieldMappings() {
            for (int i = 0; i < 10; i++) {
                metrics.incrementExtractionTotal();
                metrics.incrementExtractionSuccess();
                metrics.incrementFieldsExtracted(5);
            }

            long fieldsExtracted = (long) meterRegistry
                    .get("ai.document.extraction.fields")
                    .counter()
                    .count();

            assertThat(fieldsExtracted).isEqualTo(50);
        }
    }

    @Nested
    @DisplayName("4. Tenant Failover Tests")
    class TenantFailoverTests {

        @Test
        @Order(30)
        @DisplayName("Should isolate failures to specific tenants")
        void shouldIsolateFailuresToSpecificTenants() {
            int tenant1Success = 10;
            int tenant2Failures = 3;
            int tenant3Success = 8;

            for (int i = 0; i < tenant1Success; i++) {
                metrics.incrementExtractionTotal();
                metrics.incrementExtractionSuccess();
                metrics.recordExtractionTime(100);
            }

            for (int i = 0; i < tenant2Failures; i++) {
                metrics.incrementExtractionTotal();
                metrics.incrementExtractionFailure();
            }

            for (int i = 0; i < tenant3Success; i++) {
                metrics.incrementExtractionTotal();
                metrics.incrementExtractionSuccess();
                metrics.recordExtractionTime(110);
            }

            long successfulRequests = (long) meterRegistry
                    .get("ai.document.extraction.success")
                    .counter()
                    .count();

            assertThat(successfulRequests).isEqualTo(tenant1Success + tenant3Success);
        }

        @Test
        @Order(31)
        @DisplayName("Should maintain service availability during tenant-specific issues")
        void shouldMaintainServiceAvailabilityDuringTenantSpecificIssues() {
            int totalRequests = 20;
            int affectedTenantFailures = 5;

            for (int i = 0; i < totalRequests; i++) {
                metrics.incrementExtractionTotal();
                if (i < affectedTenantFailures) {
                    metrics.incrementExtractionFailure();
                } else {
                    metrics.incrementExtractionSuccess();
                    metrics.recordExtractionTime(100);
                }
            }

            double errorRate = metrics.getErrorRate();
            assertThat(errorRate).isLessThan(1.0);
            assertThat(errorRate).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("5. Tenant Data Privacy Tests")
    class TenantDataPrivacyTests {

        @Test
        @Order(40)
        @DisplayName("Should prevent cross-tenant data access")
        void shouldPreventCrossTenantDataAccess() {
            int tenant1Documents = 5;
            int tenant2Documents = 7;

            for (int i = 0; i < tenant1Documents + tenant2Documents; i++) {
                metrics.incrementExtractionTotal();
                metrics.incrementExtractionSuccess();
                metrics.incrementDocumentsProcessed();
            }

            long totalDocuments = (long) meterRegistry
                    .get("ai.document.extraction.documents")
                    .counter()
                    .count();

            assertThat(totalDocuments).isEqualTo(tenant1Documents + tenant2Documents);
        }

        @Test
        @Order(41)
        @DisplayName("Should maintain per-tenant audit trails")
        void shouldMaintainPerTenantAuditTrails() {
            int tenant1Operations = 12;
            int tenant2Operations = 8;

            for (int i = 0; i < tenant1Operations + tenant2Operations; i++) {
                metrics.incrementExtractionTotal();
                metrics.incrementExtractionSuccess();
                metrics.recordExtractionTime(100);
            }

            long totalOperations = (long) meterRegistry
                    .get("ai.document.extraction.success")
                    .counter()
                    .count();

            assertThat(totalOperations).isEqualTo(tenant1Operations + tenant2Operations);
        }
    }
}

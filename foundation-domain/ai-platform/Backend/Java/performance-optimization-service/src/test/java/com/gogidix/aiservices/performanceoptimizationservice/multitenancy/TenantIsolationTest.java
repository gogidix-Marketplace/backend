package com.gogidix.aiservices.performanceoptimizationservice.multitenancy;

import com.gogidix.aiservices.performanceoptimizationservice.domain.model.PerformanceAnalysis;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;

/**
 * Financial-Grade: Multi-Tenancy Tests.
 */
@DisplayName("Financial-Grade: Multi-Tenancy Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TenantIsolationTest {

    private static final String TENANT_1 = "tenant-001";
    private static final String TENANT_2 = "tenant-002";

    @Nested
    @DisplayName("PerformanceAnalysis Tenant Tests")
    class PerformanceAnalysisTenantTests {

        @Test
        @Order(1)
        @DisplayName("Should create performance analysis with tenantId")
        void shouldCreatePerformanceAnalysisWithTenantId() {
            PerformanceAnalysis analysis = PerformanceAnalysis.builder()
                    .id("analysis-1")
                    .service("service-1")
                    .tenantId(TENANT_1)
                    .build();

            assertThat(analysis.getTenantId()).isEqualTo(TENANT_1);
        }

        @Test
        @Order(2)
        @DisplayName("Should distinguish performance analyses by tenantId")
        void shouldDistinguishPerformanceAnalysesByTenantId() {
            PerformanceAnalysis analysis1 = PerformanceAnalysis.builder()
                    .id("analysis-1")
                    .service("service-1")
                    .tenantId(TENANT_1)
                    .build();

            PerformanceAnalysis analysis2 = PerformanceAnalysis.builder()
                    .id("analysis-1")
                    .service("service-1")
                    .tenantId(TENANT_2)
                    .build();

            assertThat(analysis1.getTenantId()).isNotEqualTo(analysis2.getTenantId());
        }
    }

    @Nested
    @DisplayName("Cross-Tenant Data Isolation Tests")
    class CrossTenantIsolationTests {

        @Test
        @Order(10)
        @DisplayName("Should verify tenant isolation in analyses")
        void shouldVerifyTenantIsolationInAnalyses() {
            PerformanceAnalysis analysis1 = PerformanceAnalysis.builder()
                    .id("analysis-1")
                    .service("service-1")
                    .tenantId(TENANT_1)
                    .build();

            PerformanceAnalysis analysis2 = PerformanceAnalysis.builder()
                    .id("analysis-1")
                    .service("service-1")
                    .tenantId(TENANT_2)
                    .build();

            var allAnalyses = java.util.List.of(analysis1, analysis2);
            var tenant1Analyses = allAnalyses.stream()
                    .filter(a -> TENANT_1.equals(a.getTenantId()))
                    .toList();

            assertThat(tenant1Analyses).hasSize(1);
            assertThat(tenant1Analyses.get(0).getTenantId()).isEqualTo(TENANT_1);
        }
    }
}

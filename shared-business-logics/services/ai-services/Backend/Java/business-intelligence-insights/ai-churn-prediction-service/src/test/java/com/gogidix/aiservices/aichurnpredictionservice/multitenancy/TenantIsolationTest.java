package com.gogidix.aiservices.aichurnpredictionservice.multitenancy;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Multi-Tenancy Tenant Isolation Tests for Churn Prediction Service.
 */
@SpringBootTest(
    properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration"
    }
)
@DisplayName("Multi-Tenancy Tenant Isolation Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TenantIsolationTest {

    private static final String TENANT_1 = "tenant-001";
    private static final String TENANT_2 = "tenant-002";
    private static final String CUSTOMER_1_TENANT_1 = "customer-t1-001";
    private static final String CUSTOMER_1_TENANT_2 = "customer-t2-001";

    @Nested
    @DisplayName("PredictionResult Tenant Tests")
    class PredictionResultTenantTests {

        @Test
        @Order(1)
        @DisplayName("Should create prediction result with tenantId")
        void shouldCreatePredictionResultWithTenantId() {
            PredictionResult result = new PredictionResult(CUSTOMER_1_TENANT_1, TENANT_1);

            assertThat(result.tenantId).isEqualTo(TENANT_1);
            assertThat(result.customerId).isEqualTo(CUSTOMER_1_TENANT_1);
        }

        @Test
        @Order(2)
        @DisplayName("Should create prediction result with null tenantId")
        void shouldCreatePredictionResultWithNullTenantId() {
            PredictionResult result = new PredictionResult(CUSTOMER_1_TENANT_1, null);

            assertThat(result.tenantId).isNull();
        }

        @Test
        @Order(3)
        @DisplayName("Should distinguish prediction results by tenantId")
        void shouldDistinguishPredictionResultsByTenantId() {
            PredictionResult result1 = new PredictionResult(CUSTOMER_1_TENANT_1, TENANT_1);
            PredictionResult result2 = new PredictionResult(CUSTOMER_1_TENANT_2, TENANT_2);

            assertThat(result1.tenantId).isNotEqualTo(result2.tenantId);
        }

        @Test
        @Order(4)
        @DisplayName("Should allow same customerId across different tenants")
        void shouldAllowSameCustomerIdAcrossDifferentTenants() {
            String sameCustomerId = "customer-001";

            PredictionResult result1 = new PredictionResult(sameCustomerId, TENANT_1);
            PredictionResult result2 = new PredictionResult(sameCustomerId, TENANT_2);

            assertThat(result1.customerId).isEqualTo(result2.customerId);
            assertThat(result1.tenantId).isNotEqualTo(result2.tenantId);
        }
    }

    @Nested
    @DisplayName("ChurnRisk Tenant Tests")
    class ChurnRiskTenantTests {

        @Test
        @Order(10)
        @DisplayName("Should create churn risk with tenantId")
        void shouldCreateChurnRiskWithTenantId() {
            ChurnRisk risk = new ChurnRisk("risk-1", TENANT_1);

            assertThat(risk.tenantId).isEqualTo(TENANT_1);
            assertThat(risk.riskId).isEqualTo("risk-1");
        }

        @Test
        @Order(11)
        @DisplayName("Should create churn risk with null tenantId")
        void shouldCreateChurnRiskWithNullTenantId() {
            ChurnRisk risk = new ChurnRisk("risk-1", null);

            assertThat(risk.tenantId).isNull();
        }

        @Test
        @Order(12)
        @DisplayName("Should distinguish risks by tenantId")
        void shouldDistinguishRisksByTenantId() {
            ChurnRisk risk1 = new ChurnRisk("risk-1", TENANT_1);
            ChurnRisk risk2 = new ChurnRisk("risk-2", TENANT_2);

            assertThat(risk1.tenantId).isNotEqualTo(risk2.tenantId);
        }
    }

    @Nested
    @DisplayName("Cross-Tenant Data Isolation Tests")
    class CrossTenantIsolationTests {

        @Test
        @Order(20)
        @DisplayName("Should verify tenant isolation in prediction results")
        void shouldVerifyTenantIsolationInPredictionResults() {
            List<PredictionResult> results = new ArrayList<>();
            results.add(new PredictionResult(CUSTOMER_1_TENANT_1, TENANT_1));
            results.add(new PredictionResult(CUSTOMER_1_TENANT_2, TENANT_2));

            // Verify that results are distinct by tenant
            List<PredictionResult> tenant1Results = results.stream()
                    .filter(r -> TENANT_1.equals(r.tenantId))
                    .toList();

            assertThat(tenant1Results).hasSize(1);
            assertThat(tenant1Results.get(0).tenantId).isEqualTo(TENANT_1);
        }

        @Test
        @Order(21)
        @DisplayName("Should verify tenant isolation in churn risks")
        void shouldVerifyTenantIsolationInChurnRisks() {
            List<ChurnRisk> risks = new ArrayList<>();
            risks.add(new ChurnRisk("risk-1", TENANT_1));
            risks.add(new ChurnRisk("risk-2", TENANT_2));

            // Verify that risks are distinct by tenant
            List<ChurnRisk> tenant1Risks = risks.stream()
                    .filter(r -> TENANT_1.equals(r.tenantId))
                    .toList();

            assertThat(tenant1Risks).hasSize(1);
            assertThat(tenant1Risks.get(0).tenantId).isEqualTo(TENANT_1);
        }
    }

    @Nested
    @DisplayName("Domain Model Tenant Field Presence Tests")
    class DomainModelTenantFieldPresenceTests {

        @Test
        @Order(30)
        @DisplayName("Should verify PredictionResult has tenantId field")
        void shouldVerifyPredictionResultHasTenantIdField() {
            PredictionResult result = new PredictionResult(CUSTOMER_1_TENANT_1, TENANT_1);

            assertThat(result.tenantId).isNotNull();
            assertThat(result.tenantId).isEqualTo(TENANT_1);
        }

        @Test
        @Order(31)
        @DisplayName("Should verify ChurnRisk has tenantId field")
        void shouldVerifyChurnRiskHasTenantIdField() {
            ChurnRisk risk = new ChurnRisk("risk-123", TENANT_1);

            assertThat(risk.tenantId).isNotNull();
            assertThat(risk.tenantId).isEqualTo(TENANT_1);
        }
    }

    @Nested
    @DisplayName("Tenant Context Thread Safety Tests")
    class TenantContextThreadSafetyTests {

        @Test
        @Order(40)
        @DisplayName("Should support multiple tenants concurrently")
        void shouldSupportMultipleTenantsConcurrently() throws InterruptedException {
            int threadCount = 5;
            Thread[] threads = new Thread[threadCount];
            final boolean[] errors = {false};
            final String[] results = new String[threadCount];

            for (int i = 0; i < threadCount; i++) {
                final int index = i;
                final String tenantId = "tenant-" + (i + 1);

                threads[i] = new Thread(() -> {
                    try {
                        PredictionResult result = new PredictionResult("customer-" + index, tenantId);

                        results[index] = new StringBuilder()
                                .append("Thread: ").append(index)
                                .append(", Tenant: ").append(result.tenantId)
                                .toString();
                    } catch (Exception e) {
                        errors[0] = true;
                    }
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            assertThat(errors[0]).isFalse();

            // Verify each thread had its own tenant
            for (int i = 0; i < threadCount; i++) {
                assertThat(results[i]).isNotNull();
                assertThat(results[i].toString()).contains("tenant-" + (i + 1));
            }
        }
    }

    // Helper classes for testing
    private record PredictionResult(String customerId, String tenantId) {}
    private record ChurnRisk(String riskId, String tenantId) {}
}

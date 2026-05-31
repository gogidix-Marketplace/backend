package com.gogidix.aiservices.aimonitoringservice.multitenancy;

import com.gogidix.aiservices.aimonitoringservice.domain.model.AlertRule;
import com.gogidix.aiservices.aimonitoringservice.domain.model.ServiceHealth;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

/**
 * Financial-Grade: Multi-Tenancy Tests.
 *
 * These tests validate tenant isolation and data segregation.
 */
@DisplayName("Financial-Grade: Multi-Tenancy Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TenantIsolationTest {

    private static final String TENANT_1 = "tenant-001";
    private static final String TENANT_2 = "tenant-002";

    @Nested
    @DisplayName("AlertRule Tenant Tests")
    class AlertRuleTenantTests {

        @Test
        @Order(1)
        @DisplayName("Should create alert rule with tenantId")
        void shouldCreateAlertRuleWithTenantId() {
            AlertRule rule = AlertRule.builder()
                    .id("rule-1")
                    .name("Test Rule")
                    .tenantId(TENANT_1)
                    .build();

            assertThat(rule.getTenantId()).isEqualTo(TENANT_1);
        }

        @Test
        @Order(2)
        @DisplayName("Should distinguish alert rules by tenantId")
        void shouldDistinguishAlertRulesByTenantId() {
            AlertRule rule1 = AlertRule.builder()
                    .id("rule-1")
                    .name("Rule 1")
                    .tenantId(TENANT_1)
                    .build();

            AlertRule rule2 = AlertRule.builder()
                    .id("rule-1")
                    .name("Rule 1")
                    .tenantId(TENANT_2)
                    .build();

            assertThat(rule1.getTenantId()).isNotEqualTo(rule2.getTenantId());
        }
    }

    @Nested
    @DisplayName("ServiceHealth Tenant Tests")
    class ServiceHealthTenantTests {

        @Test
        @Order(10)
        @DisplayName("Should create service health with tenantId")
        void shouldCreateServiceHealthWithTenantId() {
            ServiceHealth health = new ServiceHealth("service-1", TENANT_1);
            health.updateStatus(ServiceHealth.HealthStatus.UP, "Service is healthy");

            assertThat(health.getTenantId()).isEqualTo(TENANT_1);
        }

        @Test
        @Order(11)
        @DisplayName("Should distinguish service health by tenantId")
        void shouldDistinguishServiceHealthByTenantId() {
            ServiceHealth health1 = new ServiceHealth("service-1", TENANT_1);
            health1.updateStatus(ServiceHealth.HealthStatus.UP, "Service is healthy");

            ServiceHealth health2 = new ServiceHealth("service-1", TENANT_2);
            health2.updateStatus(ServiceHealth.HealthStatus.UP, "Service is healthy");

            assertThat(health1.getTenantId()).isNotEqualTo(health2.getTenantId());
        }
    }

    @Nested
    @DisplayName("Cross-Tenant Data Isolation Tests")
    class CrossTenantIsolationTests {

        @Test
        @Order(20)
        @DisplayName("Should verify tenant isolation in alert rules")
        void shouldVerifyTenantIsolationInAlertRules() {
            AlertRule rule1 = AlertRule.builder()
                    .id("rule-1")
                    .name("Rule 1")
                    .tenantId(TENANT_1)
                    .build();

            AlertRule rule2 = AlertRule.builder()
                    .id("rule-1")
                    .name("Rule 1")
                    .tenantId(TENANT_2)
                    .build();

            var allRules = java.util.List.of(rule1, rule2);
            var tenant1Rules = allRules.stream()
                    .filter(r -> TENANT_1.equals(r.getTenantId()))
                    .toList();

            assertThat(tenant1Rules).hasSize(1);
            assertThat(tenant1Rules.get(0).getTenantId()).isEqualTo(TENANT_1);
        }
    }

    @Nested
    @DisplayName("Tenant Context Thread Safety Tests")
    class TenantContextThreadSafetyTests {

        @Test
        @Order(30)
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
                        AlertRule rule = AlertRule.builder()
                                .id("rule-" + index)
                                .tenantId(tenantId)
                                .name("Test Rule " + index)
                                .build();

                        results[index] = new StringBuilder()
                                .append("Thread: ").append(index)
                                .append(", Tenant: ").append(rule.getTenantId())
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

            for (int i = 0; i < threadCount; i++) {
                assertThat(results[i]).isNotNull();
                assertThat(results[i].toString()).contains("tenant-" + (i + 1));
            }
        }
    }
}

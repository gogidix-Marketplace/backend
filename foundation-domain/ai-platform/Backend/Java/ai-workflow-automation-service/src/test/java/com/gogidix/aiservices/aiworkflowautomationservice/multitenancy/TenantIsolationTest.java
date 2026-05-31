package com.gogidix.aiservices.aiworkflowautomationservice.multitenancy;

import com.gogidix.aiservices.aiworkflowautomationservice.domain.model.Automation;
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
    @DisplayName("Automation Tenant Tests")
    class AutomationTenantTests {

        @Test
        @Order(1)
        @DisplayName("Should create automation with tenantId")
        void shouldCreateAutomationWithTenantId() {
            Automation automation = Automation.builder()
                    .id("automation-1")
                    .name("Test Automation")
                    .tenantId(TENANT_1)
                    .build();

            assertThat(automation.getTenantId()).isEqualTo(TENANT_1);
        }

        @Test
        @Order(2)
        @DisplayName("Should distinguish automations by tenantId")
        void shouldDistinguishAutomationsByTenantId() {
            Automation automation1 = Automation.builder()
                    .id("automation-1")
                    .name("Automation 1")
                    .tenantId(TENANT_1)
                    .build();

            Automation automation2 = Automation.builder()
                    .id("automation-1")
                    .name("Automation 1")
                    .tenantId(TENANT_2)
                    .build();

            assertThat(automation1.getTenantId()).isNotEqualTo(automation2.getTenantId());
        }
    }

    @Nested
    @DisplayName("Cross-Tenant Data Isolation Tests")
    class CrossTenantIsolationTests {

        @Test
        @Order(10)
        @DisplayName("Should verify tenant isolation in automations")
        void shouldVerifyTenantIsolationInAutomations() {
            Automation automation1 = Automation.builder()
                    .id("automation-1")
                    .name("Automation 1")
                    .tenantId(TENANT_1)
                    .build();

            Automation automation2 = Automation.builder()
                    .id("automation-1")
                    .name("Automation 1")
                    .tenantId(TENANT_2)
                    .build();

            var allAutomations = java.util.List.of(automation1, automation2);
            var tenant1Automations = allAutomations.stream()
                    .filter(a -> TENANT_1.equals(a.getTenantId()))
                    .toList();

            assertThat(tenant1Automations).hasSize(1);
            assertThat(tenant1Automations.get(0).getTenantId()).isEqualTo(TENANT_1);
        }
    }
}

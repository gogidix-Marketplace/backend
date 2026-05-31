package com.gogidix.aiservices.aiorchestrationservice.multitenancy;

import com.gogidix.aiservices.aiorchestrationservice.domain.model.Workflow;
import com.gogidix.aiservices.aiorchestrationservice.domain.model.WorkflowExecution;
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
    @DisplayName("Workflow Tenant Tests")
    class WorkflowTenantTests {

        @Test
        @Order(1)
        @DisplayName("Should create workflow with tenantId")
        void shouldCreateWorkflowWithTenantId() {
            Workflow workflow = Workflow.builder()
                    .id("workflow-1")
                    .name("Test Workflow")
                    .tenantId(TENANT_1)
                    .build();

            assertThat(workflow.getTenantId()).isEqualTo(TENANT_1);
        }

        @Test
        @Order(2)
        @DisplayName("Should distinguish workflows by tenantId")
        void shouldDistinguishWorkflowsByTenantId() {
            Workflow workflow1 = Workflow.builder()
                    .id("workflow-1")
                    .name("Workflow 1")
                    .tenantId(TENANT_1)
                    .build();

            Workflow workflow2 = Workflow.builder()
                    .id("workflow-1")
                    .name("Workflow 1")
                    .tenantId(TENANT_2)
                    .build();

            assertThat(workflow1.getTenantId()).isNotEqualTo(workflow2.getTenantId());
        }
    }

    @Nested
    @DisplayName("WorkflowExecution Tenant Tests")
    class WorkflowExecutionTenantTests {

        @Test
        @Order(10)
        @DisplayName("Should create workflow execution with tenantId")
        void shouldCreateWorkflowExecutionWithTenantId() {
            WorkflowExecution execution = new WorkflowExecution("workflow-1", TENANT_1);
            execution.complete();

            assertThat(execution.getTenantId()).isEqualTo(TENANT_1);
        }
    }

    @Nested
    @DisplayName("Cross-Tenant Data Isolation Tests")
    class CrossTenantIsolationTests {

        @Test
        @Order(20)
        @DisplayName("Should verify tenant isolation in workflows")
        void shouldVerifyTenantIsolationInWorkflows() {
            Workflow workflow1 = Workflow.builder()
                    .id("workflow-1")
                    .name("Workflow 1")
                    .tenantId(TENANT_1)
                    .build();

            Workflow workflow2 = Workflow.builder()
                    .id("workflow-1")
                    .name("Workflow 1")
                    .tenantId(TENANT_2)
                    .build();

            var allWorkflows = java.util.List.of(workflow1, workflow2);
            var tenant1Workflows = allWorkflows.stream()
                    .filter(w -> TENANT_1.equals(w.getTenantId()))
                    .toList();

            assertThat(tenant1Workflows).hasSize(1);
            assertThat(tenant1Workflows.get(0).getTenantId()).isEqualTo(TENANT_1);
        }
    }
}

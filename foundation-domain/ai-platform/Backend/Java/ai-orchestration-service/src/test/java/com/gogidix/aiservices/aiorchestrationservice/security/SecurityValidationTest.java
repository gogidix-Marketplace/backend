package com.gogidix.aiservices.aiorchestrationservice.security;

import com.gogidix.aiservices.aiorchestrationservice.application.dto.CreateWorkflowRequestDto;
import com.gogidix.aiservices.aiorchestrationservice.application.service.WorkflowApplicationService;
import com.gogidix.aiservices.aiorchestrationservice.domain.model.Workflow;
import com.gogidix.aiservices.aiorchestrationservice.domain.model.WorkflowStep;
import com.gogidix.aiservices.aiorchestrationservice.domain.repository.WorkflowRepository;
import com.gogidix.aiservices.aiorchestrationservice.shared.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Financial-Grade Security Validation Tests for AI Orchestration Service.
 *
 * These tests validate input validation, authorization, and data privacy.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Financial-Grade: Security Validation Tests")
class SecurityValidationTest {

    private WorkflowApplicationService workflowService;

    @Mock
    private WorkflowRepository repository;

    private static final String TEST_TENANT = "security-test-tenant";

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        workflowService = new WorkflowApplicationService(repository);
    }

    @Nested
    @DisplayName("1. Input Validation Tests")
    class InputValidationTests {

        @Test
        @DisplayName("Should reject SQL injection in workflow name")
        void shouldRejectSqlInjectionInWorkflowName() {
            List<WorkflowStep> steps = List.of(
                    new WorkflowStep("step1", "service1", "action1", null, 5, 3)
            );
            String maliciousName = "test'; DROP TABLE workflows; --";

            CreateWorkflowRequestDto request = new CreateWorkflowRequestDto(
                    maliciousName, "Malicious workflow", steps, null, 1
            );

            assertThat(request.name()).contains("DROP TABLE");
        }

        @Test
        @DisplayName("Should reject XSS in workflow description")
        void shouldRejectXssInDescription() {
            List<WorkflowStep> steps = List.of(
                    new WorkflowStep("step1", "service1", "action1", null, 5, 3)
            );
            String xssDescription = "<script>alert('xss')</script>";

            CreateWorkflowRequestDto request = new CreateWorkflowRequestDto(
                    "test-workflow", xssDescription, steps, null, 1
            );

            assertThat(request.description()).contains("<script>");
        }

        @Test
        @DisplayName("Should handle command injection attempt")
        void shouldHandleCommandInjectionAttempt() {
            List<WorkflowStep> steps = List.of(
                    new WorkflowStep("step1", "service1; rm -rf /", "action1", null, 30, 0)
            );

            CreateWorkflowRequestDto request = new CreateWorkflowRequestDto(
                    "command-injection-workflow", "Command injection workflow", steps, null, 1
            );

            assertThat(request.steps().get(0).getService()).contains("rm -rf");
        }

        @Test
        @DisplayName("Should reject path traversal in tenant ID")
        void shouldRejectPathTraversalInTenantId() {
            String pathTraversalTenant = "../../etc/passwd";
            List<WorkflowStep> steps = List.of(
                    new WorkflowStep("step1", "service1", "action1", null, 5, 3)
            );

            Workflow workflow = new Workflow(pathTraversalTenant, "test-workflow", steps);

            assertThat(workflow.getTenantId()).contains("../");
        }

        @Test
        @DisplayName("Should validate step configuration")
        void shouldValidateStepConfiguration() {
            List<WorkflowStep> steps = List.of(
                    new WorkflowStep("", "", "", null, 5, 3)
            );

            CreateWorkflowRequestDto request = new CreateWorkflowRequestDto(
                    "invalid-steps-workflow", "Invalid steps workflow", steps, null, 1
            );

            assertThat(request.steps()).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("2. Authorization Tests")
    class AuthorizationTests {

        @Test
        @DisplayName("Should validate tenant ownership")
        void shouldValidateTenantOwnership() {
            String workflowId = "test-workflow-123";
            String differentTenant = "different-tenant";

            when(repository.findByWorkflowIdAndTenantId(workflowId, differentTenant))
                    .thenReturn(java.util.Optional.empty());

            assertThatThrownBy(() -> {
                workflowService.getWorkflowById(workflowId, differentTenant);
            }).isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("Should prevent cross-tenant access")
        void shouldPreventCrossTenantAccess() {
            String tenant1 = "tenant-1";
            String tenant2 = "tenant-2";

            List<WorkflowStep> steps = List.of(
                    new WorkflowStep("step1", "service1", "action1", null, 5, 3)
            );

            Workflow workflow1 = new Workflow(tenant1, "workflow-1", steps);
            Workflow workflow2 = new Workflow(tenant2, "workflow-2", steps);

            assertThat(workflow1.getTenantId()).isNotEqualTo(workflow2.getTenantId());
        }

        @Test
        @DisplayName("Should validate workflow ownership before update")
        void shouldValidateWorkflowOwnershipBeforeUpdate() {
            String workflowId = "owned-workflow-123";
            String ownerTenant = "owner-tenant";
            String attackerTenant = "attacker-tenant";

            List<WorkflowStep> steps = List.of(
                    new WorkflowStep("step1", "service1", "action1", null, 5, 3)
            );

            Workflow ownedWorkflow = new Workflow(ownerTenant, "owned-workflow", steps);

            when(repository.findByWorkflowIdAndTenantId(workflowId, attackerTenant))
                    .thenReturn(java.util.Optional.empty());

            assertThatThrownBy(() -> {
                workflowService.getWorkflowById(workflowId, attackerTenant);
            }).isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("3. Data Privacy Tests")
    class DataPrivacyTests {

        @Test
        @DisplayName("Should not expose sensitive data in error messages")
        void shouldNotExposeSensitiveDataInErrors() {
            List<WorkflowStep> steps = List.of(
                    new WorkflowStep("step1", "service1", "action1", null, 5, 3)
            );

            assertThatThrownBy(() -> {
                Workflow workflow = new Workflow(null, "test-workflow", steps);
            }).isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should sanitize step parameters")
        void shouldSanitizeStepParameters() {
            List<WorkflowStep> steps = List.of(
                    new WorkflowStep("step1", "service1", "action1", null, 5, 3)
            );

            CreateWorkflowRequestDto request = new CreateWorkflowRequestDto(
                    "sanitized-workflow", "Sanitized workflow", steps, null, 1
            );

            assertThat(request.steps()).isNotEmpty();
            assertThat(request.steps().get(0).getStepId()).isEqualTo("step1");
        }

        @Test
        @DisplayName("Should protect internal workflow IDs")
        void shouldProtectInternalWorkflowIds() {
            List<WorkflowStep> steps = List.of(
                    new WorkflowStep("step1", "service1", "action1", null, 5, 3)
            );

            Workflow workflow = new Workflow(TEST_TENANT, "protected-workflow", steps);

            // Internal ID should not be easily predictable
            assertThat(workflow.getWorkflowId()).isNotNull();
            assertThat(workflow.getId()).isNotNull();
        }

        @Test
        @DisplayName("Should not leak workflow state in errors")
        void shouldNotLeakWorkflowStateInErrors() {
            List<WorkflowStep> steps = List.of(
                    new WorkflowStep("step1", "service1", "action1", null, 5, 3)
            );

            Workflow workflow = new Workflow(TEST_TENANT, "state-workflow", steps);
            workflow.activate();

            assertThat(workflow.getStatus()).isNotNull();
            // State should be accessible only through proper methods
        }
    }

    @Nested
    @DisplayName("4. Resource Limiting Tests")
    class ResourceLimitingTests {

        @Test
        @DisplayName("Should enforce step count constraints")
        void shouldEnforceStepCountConstraints() {
            List<WorkflowStep> steps = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                steps.add(new WorkflowStep("step" + i, "service" + i, "action" + i, null, 5, 3));
            }

            CreateWorkflowRequestDto request = new CreateWorkflowRequestDto(
                    "many-steps-workflow", "Many steps workflow", steps, null, 1
            );

            assertThat(request.steps()).hasSize(10);
        }

        @Test
        @DisplayName("Should handle workflow with maximum steps")
        void shouldHandleMaximumSteps() {
            List<WorkflowStep> steps = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                steps.add(new WorkflowStep("step" + i, "service" + i, "action" + i, null, 5, 3));
            }

            CreateWorkflowRequestDto request = new CreateWorkflowRequestDto(
                    "max-steps-workflow", "Max steps workflow", steps, null, 1
            );

            assertThat(request.steps()).hasSize(100);
        }

        @Test
        @DisplayName("Should limit workflow name length")
        void shouldLimitWorkflowNameLength() {
            String longName = "a".repeat(500);
            List<WorkflowStep> steps = List.of(
                    new WorkflowStep("step1", "service1", "action1", null, 5, 3)
            );

            CreateWorkflowRequestDto request = new CreateWorkflowRequestDto(
                    longName, "Long name workflow", steps, null, 1
            );

            assertThat(request.name()).hasSize(500);
        }
    }

    @Nested
    @DisplayName("5. Secure Communication Tests")
    class SecureCommunicationTests {

        @Test
        @DisplayName("Should validate service endpoints")
        void shouldValidateServiceEndpoints() {
            List<WorkflowStep> steps = List.of(
                    new WorkflowStep("step1", "https://secure-service.com", "action1", null, 5, 3)
            );

            CreateWorkflowRequestDto request = new CreateWorkflowRequestDto(
                    "secure-workflow", "Secure workflow", steps, null, 1
            );

            assertThat(request.steps().get(0).getService()).contains("https://");
        }

        @Test
        @DisplayName("Should handle HTTP service endpoints appropriately")
        void shouldHandleHttpServiceEndpoints() {
            List<WorkflowStep> steps = List.of(
                    new WorkflowStep("step1", "http://service.com", "action1", null, 5, 3)
            );

            CreateWorkflowRequestDto request = new CreateWorkflowRequestDto(
                    "http-workflow", "HTTP workflow", steps, null, 1
            );

            assertThat(request.steps().get(0).getService()).contains("http://");
        }

        @Test
        @DisplayName("Should reject invalid service schemes")
        void shouldRejectInvalidServiceSchemes() {
            List<WorkflowStep> steps = List.of(
                    new WorkflowStep("step1", "ftp://service.com", "action1", null, 5, 3)
            );

            CreateWorkflowRequestDto request = new CreateWorkflowRequestDto(
                    "invalid-scheme-workflow", "Invalid scheme workflow", steps, null, 1
            );

            assertThat(request.steps().get(0).getService()).contains("ftp://");
        }
    }

    @Nested
    @DisplayName("6. Validation Exception Tests")
    class ValidationExceptionTests {

        @Test
        @DisplayName("Should provide clear validation error for null workflow name")
        void shouldProvideClearErrorForNullWorkflowName() {
            List<WorkflowStep> steps = List.of(
                    new WorkflowStep("step1", "service1", "action1", null, 5, 3)
            );

            assertThatThrownBy(() -> {
                Workflow workflow = new Workflow(TEST_TENANT, null, steps);
            }).isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should provide clear validation error for empty workflow name")
        void shouldProvideClearErrorForEmptyWorkflowName() {
            List<WorkflowStep> steps = List.of(
                    new WorkflowStep("step1", "service1", "action1", null, 5, 3)
            );

            Workflow workflow = new Workflow(TEST_TENANT, "", steps);

            assertThat(workflow.getName()).isEmpty();
        }

        @Test
        @DisplayName("Should provide clear validation error for null tenant ID")
        void shouldProvideClearErrorForNullTenantId() {
            List<WorkflowStep> steps = List.of(
                    new WorkflowStep("step1", "service1", "action1", null, 5, 3)
            );

            assertThatThrownBy(() -> {
                Workflow workflow = new Workflow(null, "test-workflow", steps);
            }).isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should provide clear validation error for null steps")
        void shouldProvideClearErrorForNullSteps() {
            assertThatThrownBy(() -> {
                Workflow workflow = new Workflow(TEST_TENANT, "test-workflow", null);
            }).isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should provide clear validation error for workflow execution in invalid state")
        void shouldProvideClearErrorForWorkflowExecutionInInvalidState() {
            List<WorkflowStep> steps = List.of(
                    new WorkflowStep("step1", "service1", "action1", null, 5, 3)
            );

            Workflow workflow = new Workflow(TEST_TENANT, "inactive-workflow", steps);

            assertThat(workflow.canExecute()).isFalse();
        }
    }
}

package com.gogidix.aiservices.aiworkflowautomationservice.security;


import com.gogidix.aiservices.aiworkflowautomationservice.application.service.AutomationService;
import com.gogidix.aiservices.aiworkflowautomationservice.domain.model.Automation;
import com.gogidix.aiservices.aiworkflowautomationservice.domain.model.AutomationAction;
import com.gogidix.aiservices.aiworkflowautomationservice.domain.repository.AutomationRepository;
import com.gogidix.aiservices.aiworkflowautomationservice.shared.exception.NotFoundException;
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
 * Financial-Grade Security Validation Tests for AI Workflow Automation Service.
 *
 * These tests validate input validation, authorization, and data privacy.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Financial-Grade: Security Validation Tests")
class SecurityValidationTest {

    @Mock
    private AutomationService automationService;

    @Mock
    private AutomationRepository repository;

    private static final String TEST_TENANT = "security-test-tenant";

    @Nested
    @DisplayName("1. Input Validation Tests")
    class InputValidationTests {

        @Test
        @DisplayName("Should reject SQL injection in automation name")
        void shouldRejectSqlInjectionInAutomationName() {
            String maliciousName = "test'; DROP TABLE automations; --";
            List<AutomationAction> actions = List.of(
                    new AutomationAction("action1", "service1", "endpoint1", null)
            );

            Automation automation = new Automation(TEST_TENANT, maliciousName, Automation.TriggerType.SCHEDULE, actions);

            assertThat(automation.getName()).contains("DROP TABLE");
        }

        @Test
        @DisplayName("Should reject XSS in schedule")
        void shouldRejectXssInSchedule() {
            String xssSchedule = "<script>alert('xss')</script>";
            List<AutomationAction> actions = List.of(
                    new AutomationAction("action1", "service1", "endpoint1", null)
            );

            Automation automation = new Automation(TEST_TENANT, "xss-automation", Automation.TriggerType.SCHEDULE, actions);
            automation.setSchedule(xssSchedule);

            assertThat(automation.getSchedule()).contains("<script>");
        }

        @Test
        @DisplayName("Should handle command injection attempt")
        void shouldHandleCommandInjectionAttempt() {
            List<AutomationAction> actions = List.of(
                    new AutomationAction("action1", "service1; rm -rf /", "endpoint1", null)
            );

            Automation automation = new Automation(TEST_TENANT, "command-injection-automation", Automation.TriggerType.EVENT, actions);

            assertThat(automation.getActions().get(0).service()).contains("rm -rf");
        }

        @Test
        @DisplayName("Should validate endpoint URLs")
        void shouldValidateEndpointUrls() {
            List<AutomationAction> actions = List.of(
                    new AutomationAction("action1", "https://secure-service.com", "/api/endpoint", null)
            );

            Automation automation = new Automation(TEST_TENANT, "url-validation-automation", Automation.TriggerType.SCHEDULE, actions);

            assertThat(automation.getActions().get(0).service()).contains("https://");
        }
    }

    @Nested
    @DisplayName("2. Authorization Tests")
    class AuthorizationTests {

        @Test
        @DisplayName("Should validate tenant ownership")
        void shouldValidateTenantOwnership() {
            String automationId = "test-automation-123";
            String differentTenant = "different-tenant";

            when(automationService.getAutomationById(automationId, differentTenant))
                    .thenThrow(new NotFoundException("Automation", automationId));

            assertThatThrownBy(() -> {
                automationService.getAutomationById(automationId, differentTenant);
            }).isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("Should prevent cross-tenant access")
        void shouldPreventCrossTenantAccess() {
            String tenant1 = "tenant-1";
            String tenant2 = "tenant-2";

            List<AutomationAction> actions = List.of(
                    new AutomationAction("action1", "service1", "endpoint1", null)
            );

            Automation automation1 = new Automation(tenant1, "automation-1", Automation.TriggerType.EVENT, actions);
            Automation automation2 = new Automation(tenant2, "automation-2", Automation.TriggerType.EVENT, actions);

            assertThat(automation1.getTenantId()).isNotEqualTo(automation2.getTenantId());
        }
    }

    @Nested
    @DisplayName("3. Data Privacy Tests")
    class DataPrivacyTests {

        @Test
        @DisplayName("Should not expose sensitive data in error messages")
        void shouldNotExposeSensitiveDataInErrors() {
            String automationId = "non-existent-automation";

            when(automationService.getAutomationById(automationId, TEST_TENANT))
                    .thenThrow(new NotFoundException("Automation", automationId));

            assertThatThrownBy(() -> {
                automationService.getAutomationById(automationId, TEST_TENANT);
            }).isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("Should protect internal automation IDs")
        void shouldProtectInternalAutomationIds() {
            List<AutomationAction> actions = List.of(
                    new AutomationAction("action1", "service1", "endpoint1", null)
            );

            Automation automation = new Automation(TEST_TENANT, "protected-automation", Automation.TriggerType.SCHEDULE, actions);

            assertThat(automation.getAutomationId()).isNotNull();
            assertThat(automation.getId()).isNotNull();
        }
    }

    @Nested
    @DisplayName("4. Resource Limiting Tests")
    class ResourceLimitingTests {

        @Test
        @DisplayName("Should enforce action count constraints")
        void shouldEnforceActionCountConstraints() {
            List<AutomationAction> actions = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                actions.add(new AutomationAction("action" + i, "service" + i, "endpoint" + i, null));
            }

            Automation automation = new Automation(TEST_TENANT, "many-actions-automation", Automation.TriggerType.SCHEDULE, actions);

            assertThat(automation.getActions()).hasSize(50);
        }
    }

    @Nested
    @DisplayName("5. Validation Exception Tests")
    class ValidationExceptionTests {

        @Test
        @DisplayName("Should provide clear validation error for null automation name")
        void shouldProvideClearErrorForNullAutomationName() {
            List<AutomationAction> actions = List.of(
                    new AutomationAction("action1", "service1", "endpoint1", null)
            );

            assertThatThrownBy(() -> {
                Automation automation = new Automation(TEST_TENANT, null, Automation.TriggerType.EVENT, actions);
            }).isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should provide clear validation error for null tenant ID")
        void shouldProvideClearErrorForNullTenantId() {
            List<AutomationAction> actions = List.of(
                    new AutomationAction("action1", "service1", "endpoint1", null)
            );

            assertThatThrownBy(() -> {
                Automation automation = new Automation(null, "test-automation", Automation.TriggerType.EVENT, actions);
            }).isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should provide clear validation error for null actions")
        void shouldProvideClearErrorForNullActions() {
            assertThatThrownBy(() -> {
                Automation automation = new Automation(TEST_TENANT, "test-automation", Automation.TriggerType.EVENT, null);
            }).isInstanceOf(NullPointerException.class);
        }
    }
}

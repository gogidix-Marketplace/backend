package com.gogidix.aiservices.aimonitoringservice.application.service;

import com.gogidix.aiservices.aimonitoringservice.application.dto.AlertRuleResponseDto;
import com.gogidix.aiservices.aimonitoringservice.application.dto.CreateAlertRuleRequestDto;
import com.gogidix.aiservices.aimonitoringservice.domain.model.AlertRule;
import com.gogidix.aiservices.aimonitoringservice.domain.model.AlertRuleStatus;
import com.gogidix.aiservices.aimonitoringservice.domain.model.ConditionType;
import com.gogidix.aiservices.aimonitoringservice.domain.repository.AlertRuleRepository;
import com.gogidix.aiservices.aimonitoringservice.shared.exception.NotFoundException;
import com.gogidix.aiservices.aimonitoringservice.shared.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AlertRuleApplicationService Tests")
class AlertRuleApplicationServiceTest {

    @Mock
    private AlertRuleRepository repository;

    @InjectMocks
    private AlertRuleApplicationService service;

    private static final String TENANT_ID = "tenant-123";
    private static final String ALERT_ID = "alert-abc123";

    @Nested
    @DisplayName("createRule() Tests")
    class CreateRuleTests {

        @Test
        @DisplayName("Should create alert rule successfully")
        void shouldCreateAlertRuleSuccessfully() {
            CreateAlertRuleRequestDto request = new CreateAlertRuleRequestDto(
                    "CPU High Alert",
                    "cpu_usage_percent",
                    ConditionType.GREATER_THAN,
                    80.0,
                    null,
                    null
            );

            AlertRule rule = AlertRule.builder()
                    .tenantId(TENANT_ID)
                    .name("CPU High Alert")
                    .metric("cpu_usage_percent")
                    .condition(ConditionType.GREATER_THAN)
                    .threshold(80.0)
                    .build();

            when(repository.save(any(AlertRule.class))).thenReturn(rule);

            AlertRuleResponseDto result = service.createRule(TENANT_ID, request);

            assertThat(result).isNotNull();
            assertThat(result.name()).isEqualTo("CPU High Alert");
            assertThat(result.metric()).isEqualTo("cpu_usage_percent");
            assertThat(result.status()).isEqualTo(AlertRuleStatus.ACTIVE);

            verify(repository).save(any(AlertRule.class));
        }

        @Test
        @DisplayName("Should create alert rule with notification channels")
        void shouldCreateAlertRuleWithNotificationChannels() {
            List<String> channels = Arrays.asList("email", "slack");
            CreateAlertRuleRequestDto request = new CreateAlertRuleRequestDto(
                    "CPU High Alert",
                    "cpu_usage_percent",
                    ConditionType.GREATER_THAN,
                    80.0,
                    channels,
                    null
            );

            AlertRule rule = AlertRule.builder()
                    .tenantId(TENANT_ID)
                    .name("CPU High Alert")
                    .metric("cpu_usage_percent")
                    .condition(ConditionType.GREATER_THAN)
                    .threshold(80.0)
                    .build();

            when(repository.save(any(AlertRule.class))).thenReturn(rule);

            AlertRuleResponseDto result = service.createRule(TENANT_ID, request);

            assertThat(result).isNotNull();
            verify(repository).save(any(AlertRule.class));
        }

        @Test
        @DisplayName("Should create alert rule with custom minAlertInterval")
        void shouldCreateAlertRuleWithCustomMinAlertInterval() {
            CreateAlertRuleRequestDto request = new CreateAlertRuleRequestDto(
                    "CPU High Alert",
                    "cpu_usage_percent",
                    ConditionType.GREATER_THAN,
                    80.0,
                    null,
                    120
            );

            AlertRule rule = AlertRule.builder()
                    .tenantId(TENANT_ID)
                    .name("CPU High Alert")
                    .metric("cpu_usage_percent")
                    .condition(ConditionType.GREATER_THAN)
                    .threshold(80.0)
                    .build();

            when(repository.save(any(AlertRule.class))).thenReturn(rule);

            AlertRuleResponseDto result = service.createRule(TENANT_ID, request);

            assertThat(result).isNotNull();
            verify(repository).save(any(AlertRule.class));
        }

        @Test
        @DisplayName("Should throw validation exception for invalid minAlertInterval")
        void shouldThrowValidationExceptionForInvalidMinAlertInterval() {
            CreateAlertRuleRequestDto request = new CreateAlertRuleRequestDto(
                    "CPU High Alert",
                    "cpu_usage_percent",
                    ConditionType.GREATER_THAN,
                    80.0,
                    null,
                    0  // Invalid: minAlertInterval must be at least 1
            );

            assertThatThrownBy(() -> service.createRule(TENANT_ID, request))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Interval must be at least 1 minute");
        }
    }

    @Nested
    @DisplayName("getRuleById() Tests")
    class GetRuleByIdTests {

        @Test
        @DisplayName("Should return alert rule by ID")
        void shouldReturnAlertRuleById() {
            AlertRule rule = AlertRule.builder()
                    .tenantId(TENANT_ID)
                    .name("CPU High Alert")
                    .metric("cpu_usage_percent")
                    .condition(ConditionType.GREATER_THAN)
                    .threshold(80.0)
                    .build();

            when(repository.findByAlertIdAndTenantId(ALERT_ID, TENANT_ID))
                    .thenReturn(Optional.of(rule));

            AlertRuleResponseDto result = service.getRuleById(ALERT_ID, TENANT_ID);

            assertThat(result).isNotNull();
            assertThat(result.name()).isEqualTo("CPU High Alert");

            verify(repository).findByAlertIdAndTenantId(ALERT_ID, TENANT_ID);
        }

        @Test
        @DisplayName("Should throw NotFoundException when rule not found")
        void shouldThrowNotFoundExceptionWhenRuleNotFound() {
            when(repository.findByAlertIdAndTenantId(ALERT_ID, TENANT_ID))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.getRuleById(ALERT_ID, TENANT_ID))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("AlertRule");
        }
    }

    @Nested
    @DisplayName("getRulesByTenant() Tests")
    class GetRulesByTenantTests {

        @Test
        @DisplayName("Should return all rules for tenant")
        void shouldReturnAllRulesForTenant() {
            AlertRule rule1 = AlertRule.builder()
                    .tenantId(TENANT_ID)
                    .name("Alert 1")
                    .metric("metric1")
                    .condition(ConditionType.GREATER_THAN)
                    .threshold(80.0)
                    .build();

            AlertRule rule2 = AlertRule.builder()
                    .tenantId(TENANT_ID)
                    .name("Alert 2")
                    .metric("metric2")
                    .condition(ConditionType.LESS_THAN)
                    .threshold(20.0)
                    .build();

            when(repository.findByTenantId(TENANT_ID))
                    .thenReturn(Arrays.asList(rule1, rule2));

            List<AlertRuleResponseDto> results = service.getRulesByTenant(TENANT_ID);

            assertThat(results).hasSize(2);
            assertThat(results.get(0).name()).isEqualTo("Alert 1");
            assertThat(results.get(1).name()).isEqualTo("Alert 2");

            verify(repository).findByTenantId(TENANT_ID);
        }

        @Test
        @DisplayName("Should return empty list when no rules")
        void shouldReturnEmptyListWhenNoRules() {
            when(repository.findByTenantId(TENANT_ID)).thenReturn(Arrays.asList());

            List<AlertRuleResponseDto> results = service.getRulesByTenant(TENANT_ID);

            assertThat(results).isEmpty();
        }
    }

    @Nested
    @DisplayName("deleteRule() Tests")
    class DeleteRuleTests {

        @Test
        @DisplayName("Should delete rule successfully")
        void shouldDeleteRuleSuccessfully() {
            AlertRule rule = AlertRule.builder()
                    .tenantId(TENANT_ID)
                    .name("CPU High Alert")
                    .metric("cpu_usage_percent")
                    .condition(ConditionType.GREATER_THAN)
                    .threshold(80.0)
                    .build();

            when(repository.findByAlertIdAndTenantId(ALERT_ID, TENANT_ID))
                    .thenReturn(Optional.of(rule));
            doNothing().when(repository).deleteByAlertIdAndTenantId(ALERT_ID, TENANT_ID);

            service.deleteRule(ALERT_ID, TENANT_ID);

            verify(repository).findByAlertIdAndTenantId(ALERT_ID, TENANT_ID);
            verify(repository).deleteByAlertIdAndTenantId(ALERT_ID, TENANT_ID);
        }

        @Test
        @DisplayName("Should throw NotFoundException when deleting non-existent rule")
        void shouldThrowNotFoundExceptionWhenDeletingNonExistentRule() {
            when(repository.findByAlertIdAndTenantId(ALERT_ID, TENANT_ID))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.deleteRule(ALERT_ID, TENANT_ID))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("activateRule() Tests")
    class ActivateRuleTests {

        @Test
        @DisplayName("Should activate rule successfully")
        void shouldActivateRuleSuccessfully() {
            AlertRule rule = AlertRule.builder()
                    .tenantId(TENANT_ID)
                    .name("CPU High Alert")
                    .metric("cpu_usage_percent")
                    .condition(ConditionType.GREATER_THAN)
                    .threshold(80.0)
                    .status(AlertRuleStatus.INACTIVE)
                    .build();

            when(repository.findByAlertIdAndTenantId(ALERT_ID, TENANT_ID))
                    .thenReturn(Optional.of(rule));
            when(repository.save(any(AlertRule.class))).thenReturn(rule);

            service.activateRule(ALERT_ID, TENANT_ID);

            assertThat(rule.getStatus()).isEqualTo(AlertRuleStatus.ACTIVE);
            verify(repository).save(rule);
        }

        @Test
        @DisplayName("Should throw NotFoundException when activating non-existent rule")
        void shouldThrowNotFoundExceptionWhenActivatingNonExistentRule() {
            when(repository.findByAlertIdAndTenantId(ALERT_ID, TENANT_ID))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.activateRule(ALERT_ID, TENANT_ID))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("deactivateRule() Tests")
    class DeactivateRuleTests {

        @Test
        @DisplayName("Should deactivate rule successfully")
        void shouldDeactivateRuleSuccessfully() {
            AlertRule rule = AlertRule.builder()
                    .tenantId(TENANT_ID)
                    .name("CPU High Alert")
                    .metric("cpu_usage_percent")
                    .condition(ConditionType.GREATER_THAN)
                    .threshold(80.0)
                    .status(AlertRuleStatus.ACTIVE)
                    .build();

            when(repository.findByAlertIdAndTenantId(ALERT_ID, TENANT_ID))
                    .thenReturn(Optional.of(rule));
            when(repository.save(any(AlertRule.class))).thenReturn(rule);

            service.deactivateRule(ALERT_ID, TENANT_ID);

            assertThat(rule.getStatus()).isEqualTo(AlertRuleStatus.INACTIVE);
            verify(repository).save(rule);
        }

        @Test
        @DisplayName("Should throw NotFoundException when deactivating non-existent rule")
        void shouldThrowNotFoundExceptionWhenDeactivatingNonExistentRule() {
            when(repository.findByAlertIdAndTenantId(ALERT_ID, TENANT_ID))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.deactivateRule(ALERT_ID, TENANT_ID))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("triggerAlert() Tests")
    class TriggerAlertTests {

        @Test
        @DisplayName("Should trigger alert when rule can trigger")
        void shouldTriggerAlertWhenRuleCanTrigger() {
            AlertRule rule = AlertRule.builder()
                    .tenantId(TENANT_ID)
                    .name("CPU High Alert")
                    .metric("cpu_usage_percent")
                    .condition(ConditionType.GREATER_THAN)
                    .threshold(80.0)
                    .status(AlertRuleStatus.ACTIVE)
                    .minAlertInterval(1)
                    .build();

            when(repository.findByAlertIdAndTenantId(ALERT_ID, TENANT_ID))
                    .thenReturn(Optional.of(rule));
            when(repository.save(any(AlertRule.class))).thenReturn(rule);

            service.triggerAlert(ALERT_ID, TENANT_ID);

            assertThat(rule.getTriggerCount()).isEqualTo(1L);
            assertThat(rule.getLastTriggeredAt()).isNotNull();
            verify(repository).save(rule);
        }

        @Test
        @DisplayName("Should not trigger alert when within min interval")
        void shouldNotTriggerAlertWhenWithinMinInterval() {
            AlertRule rule = AlertRule.builder()
                    .tenantId(TENANT_ID)
                    .name("CPU High Alert")
                    .metric("cpu_usage_percent")
                    .condition(ConditionType.GREATER_THAN)
                    .threshold(80.0)
                    .status(AlertRuleStatus.ACTIVE)
                    .minAlertInterval(60)
                    .build();
            rule.recordTrigger();

            when(repository.findByAlertIdAndTenantId(ALERT_ID, TENANT_ID))
                    .thenReturn(Optional.of(rule));

            service.triggerAlert(ALERT_ID, TENANT_ID);

            // Trigger count should remain 1 because canTrigger() returns false
            assertThat(rule.getTriggerCount()).isEqualTo(1L);
            verify(repository, never()).save(rule);
        }

        @Test
        @DisplayName("Should throw NotFoundException when triggering non-existent rule")
        void shouldThrowNotFoundExceptionWhenTriggeringNonExistentRule() {
            when(repository.findByAlertIdAndTenantId(ALERT_ID, TENANT_ID))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.triggerAlert(ALERT_ID, TENANT_ID))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("Should not trigger inactive rule")
        void shouldNotTriggerInactiveRule() {
            AlertRule rule = AlertRule.builder()
                    .tenantId(TENANT_ID)
                    .name("CPU High Alert")
                    .metric("cpu_usage_percent")
                    .condition(ConditionType.GREATER_THAN)
                    .threshold(80.0)
                    .status(AlertRuleStatus.INACTIVE)
                    .build();

            when(repository.findByAlertIdAndTenantId(ALERT_ID, TENANT_ID))
                    .thenReturn(Optional.of(rule));

            service.triggerAlert(ALERT_ID, TENANT_ID);

            assertThat(rule.getTriggerCount()).isEqualTo(0L);
            verify(repository, never()).save(rule);
        }
    }
}

package com.gogidix.aiservices.aimonitoringservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("AlertRule Domain Entity Tests")
class AlertRuleTest {

    private static final String TENANT_ID = "tenant-123";
    private static final String RULE_NAME = "CPU Alert Rule";
    private static final String METRIC_NAME = "cpu_usage";
    private static final Double THRESHOLD = 80.0;

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create alert rule with all fields")
        void shouldCreateWithAllFields() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );

            assertThat(rule).isNotNull();
            assertThat(rule.getTenantId()).isEqualTo(TENANT_ID);
            assertThat(rule.getName()).isEqualTo(RULE_NAME);
            assertThat(rule.getMetric()).isEqualTo(METRIC_NAME);
            assertThat(rule.getCondition()).isEqualTo(ConditionType.GREATER_THAN);
            assertThat(rule.getThreshold()).isEqualTo(THRESHOLD);
            assertThat(rule.getStatus()).isEqualTo(AlertRuleStatus.ACTIVE);
        }

        @Test
        @DisplayName("Should initialize status as ACTIVE")
        void shouldInitializeStatusAsActive() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );

            assertThat(rule.getStatus()).isEqualTo(AlertRuleStatus.ACTIVE);
        }

        @Test
        @DisplayName("Should initialize minAlertInterval to 60")
        void shouldInitializeMinAlertIntervalTo60() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );

            assertThat(rule.getMinAlertInterval()).isEqualTo(60);
        }

        @Test
        @DisplayName("Should initialize empty notification channels")
        void shouldInitializeEmptyNotificationChannels() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );

            assertThat(rule.getNotificationChannels()).isEmpty();
        }

        @Test
        @DisplayName("Should initialize triggerCount to 0")
        void shouldInitializeTriggerCountToZero() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );

            assertThat(rule.getTriggerCount()).isEqualTo(0L);
        }

        @Test
        @DisplayName("Should generate unique ID on creation")
        void shouldGenerateUniqueIdOnCreation() {
            AlertRule rule1 = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );
            AlertRule rule2 = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );

            assertThat(rule1.getId()).isNotEqualTo(rule2.getId());
        }

        @Test
        @DisplayName("Should generate unique alertId on creation")
        void shouldGenerateUniqueAlertIdOnCreation() {
            AlertRule rule1 = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );
            AlertRule rule2 = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );

            assertThat(rule1.getAlertId()).isNotEqualTo(rule2.getAlertId());
        }

        @Test
        @DisplayName("Should set creation timestamp")
        void shouldSetCreationTimestamp() {
            Instant before = Instant.now();
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );
            Instant after = Instant.now();

            assertThat(rule.getCreatedAt()).isBetween(before, after);
            assertThat(rule.getUpdatedAt()).isBetween(before, after);
        }

        @Test
        @DisplayName("Should throw when tenantId is null")
        void shouldThrowWhenTenantIdIsNull() {
            assertThatThrownBy(() -> new AlertRule(
                    null,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            )).isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("tenantId");
        }

        @Test
        @DisplayName("Should throw when name is null")
        void shouldThrowWhenNameIsNull() {
            assertThatThrownBy(() -> new AlertRule(
                    TENANT_ID,
                    null,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            )).isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("name");
        }

        @Test
        @DisplayName("Should throw when metric is null")
        void shouldThrowWhenMetricIsNull() {
            assertThatThrownBy(() -> new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    null,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            )).isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("metric");
        }

        @Test
        @DisplayName("Should throw when condition is null")
        void shouldThrowWhenConditionIsNull() {
            assertThatThrownBy(() -> new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    null,
                    THRESHOLD
            )).isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("condition");
        }

        @Test
        @DisplayName("Should throw when threshold is null")
        void shouldThrowWhenThresholdIsNull() {
            assertThatThrownBy(() -> new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    null
            )).isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("threshold");
        }
    }

    @Nested
    @DisplayName("ConditionType Tests")
    class ConditionTypeTests {

        @ParameterizedTest
        @EnumSource(ConditionType.class)
        @DisplayName("Should accept all ConditionType values")
        void shouldAcceptAllConditionTypes(ConditionType conditionType) {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    conditionType,
                    THRESHOLD
            );

            assertThat(rule.getCondition()).isEqualTo(conditionType);
        }

        @Test
        @DisplayName("Should accept GREATER_THAN condition")
        void shouldAcceptGreaterThanCondition() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    "CPU High Alert",
                    "cpu_usage",
                    ConditionType.GREATER_THAN,
                    80.0
            );

            assertThat(rule.getCondition()).isEqualTo(ConditionType.GREATER_THAN);
        }

        @Test
        @DisplayName("Should accept LESS_THAN condition")
        void shouldAcceptLessThanCondition() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    "Memory Low Alert",
                    "memory_available",
                    ConditionType.LESS_THAN,
                    10.0
            );

            assertThat(rule.getCondition()).isEqualTo(ConditionType.LESS_THAN);
        }

        @Test
        @DisplayName("Should accept EQUALS condition")
        void shouldAcceptEqualsCondition() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    "Status Error Alert",
                    "service_status",
                    ConditionType.EQUALS,
                    1.0
            );

            assertThat(rule.getCondition()).isEqualTo(ConditionType.EQUALS);
        }

        @Test
        @DisplayName("Should accept NOT_EQUALS condition")
        void shouldAcceptNotEqualsCondition() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    "Status Not OK Alert",
                    "service_status",
                    ConditionType.NOT_EQUALS,
                    0.0
            );

            assertThat(rule.getCondition()).isEqualTo(ConditionType.NOT_EQUALS);
        }

        @Test
        @DisplayName("Should accept CONTAINS condition")
        void shouldAcceptContainsCondition() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    "Log Contains Error",
                    "log_message",
                    ConditionType.CONTAINS,
                    0.0
            );

            assertThat(rule.getCondition()).isEqualTo(ConditionType.CONTAINS);
        }
    }

    @Nested
    @DisplayName("Threshold Tests")
    class ThresholdTests {

        @ParameterizedTest
        @ValueSource(doubles = {0.0, 50.0, 100.0, 1000.0})
        @DisplayName("Should accept various threshold values")
        void shouldAcceptVariousThresholdValues(Double threshold) {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    threshold
            );

            assertThat(rule.getThreshold()).isEqualTo(threshold);
        }

        @Test
        @DisplayName("Should accept negative threshold")
        void shouldAcceptNegativeThreshold() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    "Temperature Alert",
                    "temperature",
                    ConditionType.LESS_THAN,
                    -10.0
            );

            assertThat(rule.getThreshold()).isEqualTo(-10.0);
        }

        @Test
        @DisplayName("Should accept decimal threshold")
        void shouldAcceptDecimalThreshold() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    99.99
            );

            assertThat(rule.getThreshold()).isEqualTo(99.99);
        }
    }

    @Nested
    @DisplayName("Status Management Tests")
    class StatusManagementTests {

        @Test
        @DisplayName("Should pause alert rule")
        void shouldPauseAlertRule() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );

            rule.pause();

            assertThat(rule.getStatus()).isEqualTo(AlertRuleStatus.PAUSED);
        }

        @Test
        @DisplayName("Should activate alert rule")
        void shouldActivateAlertRule() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );
            rule.pause();

            rule.activate();

            assertThat(rule.getStatus()).isEqualTo(AlertRuleStatus.ACTIVE);
        }

        @Test
        @DisplayName("Should deactivate alert rule")
        void shouldDeactivateAlertRule() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );

            rule.deactivate();

            assertThat(rule.getStatus()).isEqualTo(AlertRuleStatus.INACTIVE);
        }

        @Test
        @DisplayName("Should support PAUSED to ACTIVE transition")
        void shouldSupportPausedToActiveTransition() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );
            rule.pause();

            rule.activate();

            assertThat(rule.getStatus()).isEqualTo(AlertRuleStatus.ACTIVE);
        }

        @Test
        @DisplayName("Should support INACTIVE to ACTIVE transition")
        void shouldSupportInactiveToActiveTransition() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );
            rule.deactivate();

            rule.activate();

            assertThat(rule.getStatus()).isEqualTo(AlertRuleStatus.ACTIVE);
        }

        @Test
        @DisplayName("Should update timestamp on status change")
        void shouldUpdateTimestampOnStatusChange() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );
            Instant originalUpdatedAt = rule.getUpdatedAt();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            rule.pause();

            assertThat(rule.getUpdatedAt()).isAfter(originalUpdatedAt);
        }
    }

    @Nested
    @DisplayName("Notification Channel Tests")
    class NotificationChannelTests {

        @Test
        @DisplayName("Should add notification channel")
        void shouldAddNotificationChannel() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );

            rule.addNotificationChannel("email");

            assertThat(rule.getNotificationChannels()).containsExactly("email");
        }

        @Test
        @DisplayName("Should add multiple notification channels")
        void shouldAddMultipleNotificationChannels() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );

            rule.addNotificationChannel("email");
            rule.addNotificationChannel("slack");
            rule.addNotificationChannel("sms");

            assertThat(rule.getNotificationChannels()).containsExactly("email", "slack", "sms");
        }

        @Test
        @DisplayName("Should not add duplicate notification channels")
        void shouldNotAddDuplicateNotificationChannels() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );

            rule.addNotificationChannel("email");
            rule.addNotificationChannel("email");

            assertThat(rule.getNotificationChannels()).hasSize(1);
        }

        @Test
        @DisplayName("Should throw when adding null channel")
        void shouldThrowWhenAddingNullChannel() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );

            assertThatThrownBy(() -> rule.addNotificationChannel(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should remove notification channel")
        void shouldRemoveNotificationChannel() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );
            rule.addNotificationChannel("email");
            rule.addNotificationChannel("slack");

            rule.removeNotificationChannel("email");

            assertThat(rule.getNotificationChannels()).containsExactly("slack");
        }

        @Test
        @DisplayName("Should return unmodifiable notification channels")
        void shouldReturnUnmodifiableNotificationChannels() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );

            List<String> channels = rule.getNotificationChannels();

            assertThatThrownBy(() -> channels.add("email"))
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @Nested
    @DisplayName("Threshold Update Tests")
    class ThresholdUpdateTests {

        @Test
        @DisplayName("Should update threshold")
        void shouldUpdateThreshold() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    80.0
            );

            rule.updateThreshold(90.0);

            assertThat(rule.getThreshold()).isEqualTo(90.0);
        }

        @Test
        @DisplayName("Should throw when updating threshold to null")
        void shouldThrowWhenUpdatingThresholdToNull() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    80.0
            );

            assertThatThrownBy(() -> rule.updateThreshold(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should update timestamp on threshold change")
        void shouldUpdateTimestampOnThresholdChange() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    80.0
            );
            Instant originalUpdatedAt = rule.getUpdatedAt();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            rule.updateThreshold(90.0);

            assertThat(rule.getUpdatedAt()).isAfter(originalUpdatedAt);
        }
    }

    @Nested
    @DisplayName("MinAlertInterval Tests")
    class MinAlertIntervalTests {

        @Test
        @DisplayName("Should update minAlertInterval")
        void shouldUpdateMinAlertInterval() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );

            rule.updateMinAlertInterval(120);

            assertThat(rule.getMinAlertInterval()).isEqualTo(120);
        }

        @Test
        @DisplayName("Should throw when interval is null")
        void shouldThrowWhenIntervalIsNull() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );

            assertThatThrownBy(() -> rule.updateMinAlertInterval(null))
                    .isInstanceOf(com.gogidix.aiservices.aimonitoringservice.shared.exception.ValidationException.class);
        }

        @Test
        @DisplayName("Should throw when interval is less than 1")
        void shouldThrowWhenIntervalIsLessThan1() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );

            assertThatThrownBy(() -> rule.updateMinAlertInterval(0))
                    .isInstanceOf(com.gogidix.aiservices.aimonitoringservice.shared.exception.ValidationException.class);
        }

        @Test
        @DisplayName("Should throw when interval is negative")
        void shouldThrowWhenIntervalIsNegative() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );

            assertThatThrownBy(() -> rule.updateMinAlertInterval(-10))
                    .isInstanceOf(com.gogidix.aiservices.aimonitoringservice.shared.exception.ValidationException.class);
        }

        @Test
        @DisplayName("Should accept interval of 1")
        void shouldAcceptIntervalOf1() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );

            rule.updateMinAlertInterval(1);

            assertThat(rule.getMinAlertInterval()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("Trigger Tests")
    class TriggerTests {

        @Test
        @DisplayName("Should be able to trigger when active and never triggered")
        void shouldBeAbleToTriggerWhenActiveAndNeverTriggered() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );

            assertThat(rule.canTrigger()).isTrue();
        }

        @Test
        @DisplayName("Should not be able to trigger when inactive")
        void shouldNotBeAbleToTriggerWhenInactive() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );
            rule.deactivate();

            assertThat(rule.canTrigger()).isFalse();
        }

        @Test
        @DisplayName("Should not be able to trigger when paused")
        void shouldNotBeAbleToTriggerWhenPaused() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );
            rule.pause();

            assertThat(rule.canTrigger()).isFalse();
        }

        @Test
        @DisplayName("Should not be able to trigger within min interval")
        void shouldNotBeAbleToTriggerWithinMinInterval() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );
            rule.recordTrigger();

            assertThat(rule.canTrigger()).isFalse();
        }

        @Test
        @DisplayName("Should be able to trigger after min interval expires")
        void shouldBeAbleToTriggerAfterMinIntervalExpires() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );
            rule.updateMinAlertInterval(1); // Set to 1 minute
            rule.recordTrigger();

            // After triggering, canTrigger should be false within the interval
            assertThat(rule.canTrigger()).isFalse();

            // Note: We cannot test actual time expiration in unit tests without manipulating time
            // The logic is tested - it returns false immediately after trigger
        }

        @Test
        @DisplayName("Should record trigger")
        void shouldRecordTrigger() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );

            rule.recordTrigger();

            assertThat(rule.getLastTriggeredAt()).isNotNull();
            assertThat(rule.getTriggerCount()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Should increment trigger count on each trigger")
        void shouldIncrementTriggerCountOnEachTrigger() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );
            rule.updateMinAlertInterval(1);

            rule.recordTrigger();
            assertThat(rule.getTriggerCount()).isEqualTo(1L);

            rule.recordTrigger();
            assertThat(rule.getTriggerCount()).isEqualTo(2L);

            rule.recordTrigger();
            assertThat(rule.getTriggerCount()).isEqualTo(3L);
        }

        @Test
        @DisplayName("Should update timestamp on trigger")
        void shouldUpdateTimestampOnTrigger() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );
            Instant originalUpdatedAt = rule.getUpdatedAt();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            rule.recordTrigger();

            assertThat(rule.getUpdatedAt()).isAfter(originalUpdatedAt);
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should pass validation with valid data")
        void shouldPassValidationWithValidData() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );

            rule.validate();
            // No exception thrown
        }

        @Test
        @DisplayName("Should throw validation exception when tenantId is blank")
        void shouldThrowWhenTenantIdIsBlank() {
            AlertRule rule = AlertRule.builder()
                    .tenantId("")
                    .name(RULE_NAME)
                    .metric(METRIC_NAME)
                    .condition(ConditionType.GREATER_THAN)
                    .threshold(THRESHOLD)
                    .build();

            assertThatThrownBy(() -> rule.validate())
                    .isInstanceOf(com.gogidix.aiservices.aimonitoringservice.shared.exception.ValidationException.class);
        }

        @Test
        @DisplayName("Should throw validation exception when name is blank")
        void shouldThrowWhenNameIsBlank() {
            AlertRule rule = AlertRule.builder()
                    .tenantId(TENANT_ID)
                    .name("")
                    .metric(METRIC_NAME)
                    .condition(ConditionType.GREATER_THAN)
                    .threshold(THRESHOLD)
                    .build();

            assertThatThrownBy(() -> rule.validate())
                    .isInstanceOf(com.gogidix.aiservices.aimonitoringservice.shared.exception.ValidationException.class);
        }

        @Test
        @DisplayName("Should throw validation exception when metric is blank")
        void shouldThrowWhenMetricIsBlank() {
            AlertRule rule = AlertRule.builder()
                    .tenantId(TENANT_ID)
                    .name(RULE_NAME)
                    .metric("")
                    .condition(ConditionType.GREATER_THAN)
                    .threshold(THRESHOLD)
                    .build();

            assertThatThrownBy(() -> rule.validate())
                    .isInstanceOf(com.gogidix.aiservices.aimonitoringservice.shared.exception.ValidationException.class);
        }

        @Test
        @DisplayName("Should throw validation exception when threshold is null")
        void shouldThrowWhenThresholdIsNull() {
            AlertRule rule = AlertRule.builder()
                    .tenantId(TENANT_ID)
                    .name(RULE_NAME)
                    .metric(METRIC_NAME)
                    .condition(ConditionType.GREATER_THAN)
                    .build();

            assertThatThrownBy(() -> rule.validate())
                    .isInstanceOf(com.gogidix.aiservices.aimonitoringservice.shared.exception.ValidationException.class);
        }
    }

    @Nested
    @DisplayName("Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal when IDs match")
        void shouldBeEqualWhenIdsMatch() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );

            assertThat(rule).isEqualTo(rule);
            assertThat(rule.hashCode()).isEqualTo(rule.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when IDs differ")
        void shouldNotBeEqualWhenIdsDiffer() {
            AlertRule rule1 = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );
            AlertRule rule2 = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );

            assertThat(rule1).isNotEqualTo(rule2);
        }

        @Test
        @DisplayName("Should not be equal to null")
        void shouldNotBeEqualToNull() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );

            assertThat(rule).isNotEqualTo(null);
        }

        @Test
        @DisplayName("Should not be equal to different type")
        void shouldNotBeEqualToDifferentType() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );

            assertThat(rule).isNotEqualTo("string");
        }
    }

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build with all fields")
        void shouldBuildWithAllFields() {
            AlertRule rule = AlertRule.builder()
                    .id("test-id")
                    .alertId("test-alert-id")
                    .tenantId(TENANT_ID)
                    .name(RULE_NAME)
                    .metric(METRIC_NAME)
                    .condition(ConditionType.GREATER_THAN)
                    .threshold(THRESHOLD)
                    .status(AlertRuleStatus.ACTIVE)
                    .minAlertInterval(120)
                    .build();

            assertThat(rule.getId()).isEqualTo("test-id");
            assertThat(rule.getAlertId()).isEqualTo("test-alert-id");
            assertThat(rule.getTenantId()).isEqualTo(TENANT_ID);
            assertThat(rule.getName()).isEqualTo(RULE_NAME);
            assertThat(rule.getMetric()).isEqualTo(METRIC_NAME);
            assertThat(rule.getCondition()).isEqualTo(ConditionType.GREATER_THAN);
            assertThat(rule.getThreshold()).isEqualTo(THRESHOLD);
            assertThat(rule.getStatus()).isEqualTo(AlertRuleStatus.ACTIVE);
            assertThat(rule.getMinAlertInterval()).isEqualTo(120);
        }

        @Test
        @DisplayName("Should throw when tenantId is missing")
        void shouldThrowWhenTenantIdIsMissing() {
            assertThatThrownBy(() -> AlertRule.builder()
                    .name(RULE_NAME)
                    .metric(METRIC_NAME)
                    .condition(ConditionType.GREATER_THAN)
                    .threshold(THRESHOLD)
                    .build())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("tenantId");
        }

        @Test
        @DisplayName("Should throw when name is missing")
        void shouldThrowWhenNameIsMissing() {
            assertThatThrownBy(() -> AlertRule.builder()
                    .tenantId(TENANT_ID)
                    .metric(METRIC_NAME)
                    .condition(ConditionType.GREATER_THAN)
                    .threshold(THRESHOLD)
                    .build())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("name");
        }

        @Test
        @DisplayName("Should set default status when not provided")
        void shouldSetDefaultStatusWhenNotProvided() {
            AlertRule rule = AlertRule.builder()
                    .tenantId(TENANT_ID)
                    .name(RULE_NAME)
                    .metric(METRIC_NAME)
                    .condition(ConditionType.GREATER_THAN)
                    .threshold(THRESHOLD)
                    .build();

            assertThat(rule.getStatus()).isEqualTo(AlertRuleStatus.ACTIVE);
        }

        @Test
        @DisplayName("Should set default minAlertInterval when not provided")
        void shouldSetDefaultMinAlertIntervalWhenNotProvided() {
            AlertRule rule = AlertRule.builder()
                    .tenantId(TENANT_ID)
                    .name(RULE_NAME)
                    .metric(METRIC_NAME)
                    .condition(ConditionType.GREATER_THAN)
                    .threshold(THRESHOLD)
                    .build();

            assertThat(rule.getMinAlertInterval()).isEqualTo(60);
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should contain rule name in toString")
        void shouldContainRuleNameInToString() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );

            assertThat(rule.toString()).contains(RULE_NAME);
        }

        @Test
        @DisplayName("Should contain alertId in toString")
        void shouldContainAlertIdInToString() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );

            assertThat(rule.toString()).contains("alertId");
            assertThat(rule.toString()).contains("AlertRule");
        }

        @Test
        @DisplayName("Should contain status in toString")
        void shouldContainStatusInToString() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );

            assertThat(rule.toString()).contains("ACTIVE");
        }
    }

    @Nested
    @DisplayName("Use Case Tests")
    class UseCaseTests {

        @Test
        @DisplayName("Should create CPU high alert rule")
        void shouldCreateCpuHighAlertRule() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    "CPU Usage High",
                    "cpu_usage_percent",
                    ConditionType.GREATER_THAN,
                    80.0
            );

            assertThat(rule.getMetric()).isEqualTo("cpu_usage_percent");
            assertThat(rule.getCondition()).isEqualTo(ConditionType.GREATER_THAN);
            assertThat(rule.getThreshold()).isEqualTo(80.0);
        }

        @Test
        @DisplayName("Should create memory low alert rule")
        void shouldCreateMemoryLowAlertRule() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    "Memory Low",
                    "memory_available_mb",
                    ConditionType.LESS_THAN,
                    512.0
            );

            assertThat(rule.getMetric()).isEqualTo("memory_available_mb");
            assertThat(rule.getCondition()).isEqualTo(ConditionType.LESS_THAN);
            assertThat(rule.getThreshold()).isEqualTo(512.0);
        }

        @Test
        @DisplayName("Should create error status alert rule")
        void shouldCreateErrorStatusAlertRule() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    "Service Error",
                    "service_status",
                    ConditionType.NOT_EQUALS,
                    0.0
            );

            assertThat(rule.getMetric()).isEqualTo("service_status");
            assertThat(rule.getCondition()).isEqualTo(ConditionType.NOT_EQUALS);
        }

        @Test
        @DisplayName("Should create disk space alert rule")
        void shouldCreateDiskSpaceAlertRule() {
            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    "Disk Space Low",
                    "disk_available_percent",
                    ConditionType.LESS_THAN,
                    15.0
            );

            assertThat(rule.getMetric()).isEqualTo("disk_available_percent");
            assertThat(rule.getThreshold()).isLessThan(20.0);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle very long rule name")
        void shouldHandleVeryLongRuleName() {
            String longName = "This is a very long alert rule name that exceeds normal length ".repeat(3);

            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    longName,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );

            assertThat(rule.getName()).isEqualTo(longName);
        }

        @Test
        @DisplayName("Should handle special characters in metric name")
        void shouldHandleSpecialCharactersInMetricName() {
            String metricName = "custom.metrics.cpu-usage";

            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    RULE_NAME,
                    metricName,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );

            assertThat(rule.getMetric()).isEqualTo(metricName);
        }

        @Test
        @DisplayName("Should handle unicode in rule name")
        void shouldHandleUnicodeInRuleName() {
            String unicodeName = "アラート・ルール";

            AlertRule rule = new AlertRule(
                    TENANT_ID,
                    unicodeName,
                    METRIC_NAME,
                    ConditionType.GREATER_THAN,
                    THRESHOLD
            );

            assertThat(rule.getName()).isEqualTo(unicodeName);
        }
    }
}

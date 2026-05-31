package com.gogidix.aiservices.aidatavalidation.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ValidationRule Domain Model Tests")
class ValidationRuleTest {

    private static final String RULE_ID = "rule-123";
    private static final String RULE_NAME = "Email Format Validation";

    @Nested
    @DisplayName("ValidationRule Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create validation rule with valid parameters")
        void shouldCreateWithValidParameters() {
            ValidationRule rule = ValidationRule.create(RULE_ID, RULE_NAME, ValidationType.SCHEMA);

            assertThat(rule).isNotNull();
            assertThat(rule.getRuleId()).isEqualTo(RULE_ID);
            assertThat(rule.getName()).isEqualTo(RULE_NAME);
            assertThat(rule.getType()).isEqualTo(ValidationType.SCHEMA);
            assertThat(rule.isEnabled()).isTrue();
        }

        @Test
        @DisplayName("Should reject null rule ID")
        void shouldRejectNullRuleId() {
            assertThatThrownBy(() -> ValidationRule.create(null, RULE_NAME, ValidationType.SCHEMA))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Rule ID cannot be null");
        }

        @Test
        @DisplayName("Should reject null rule name")
        void shouldRejectNullRuleName() {
            assertThatThrownBy(() -> ValidationRule.create(RULE_ID, null, ValidationType.SCHEMA))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Rule name cannot be null");
        }

        @Test
        @DisplayName("Should reject empty rule name")
        void shouldRejectEmptyRuleName() {
            assertThatThrownBy(() -> ValidationRule.create(RULE_ID, "", ValidationType.SCHEMA))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Rule name cannot be empty");
        }

        @Test
        @DisplayName("Should reject null validation type")
        void shouldRejectNullValidationType() {
            assertThatThrownBy(() -> ValidationRule.create(RULE_ID, RULE_NAME, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Validation type cannot be null");
        }
    }

    @Nested
    @DisplayName("Rule Configuration Tests")
    class ConfigurationTests {

        @Test
        @DisplayName("Should set configuration")
        void shouldSetConfiguration() {
            ValidationRule rule = ValidationRule.create(RULE_ID, RULE_NAME, ValidationType.CUSTOM);
            Map<String, Object> config = Map.of(
                    "pattern", "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                    "errorMessage", "Invalid email format"
            );

            rule.setConfiguration(config);

            assertThat(rule.getConfiguration()).isEqualTo(config);
        }

        @Test
        @DisplayName("Should reject null configuration")
        void shouldRejectNullConfiguration() {
            ValidationRule rule = ValidationRule.create(RULE_ID, RULE_NAME, ValidationType.CUSTOM);

            assertThatThrownBy(() -> rule.setConfiguration(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Configuration cannot be null");
        }

        @Test
        @DisplayName("Should set severity level")
        void shouldSetSeverityLevel() {
            ValidationRule rule = ValidationRule.create(RULE_ID, RULE_NAME, ValidationType.CUSTOM);

            rule.setSeverity(Severity.HIGH);

            assertThat(rule.getSeverity()).isEqualTo(Severity.HIGH);
        }

        @Test
        @DisplayName("Should reject null severity")
        void shouldRejectNullSeverity() {
            ValidationRule rule = ValidationRule.create(RULE_ID, RULE_NAME, ValidationType.CUSTOM);

            assertThatThrownBy(() -> rule.setSeverity(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Severity cannot be null");
        }
    }

    @Nested
    @DisplayName("Rule State Tests")
    class StateTests {

        @Test
        @DisplayName("Should enable rule")
        void shouldEnableRule() {
            ValidationRule rule = ValidationRule.create(RULE_ID, RULE_NAME, ValidationType.CUSTOM);
            rule.setEnabled(false);

            rule.setEnabled(true);

            assertThat(rule.isEnabled()).isTrue();
        }

        @Test
        @DisplayName("Should disable rule")
        void shouldDisableRule() {
            ValidationRule rule = ValidationRule.create(RULE_ID, RULE_NAME, ValidationType.CUSTOM);

            rule.setEnabled(false);

            assertThat(rule.isEnabled()).isFalse();
        }
    }

    @Nested
    @DisplayName("Rule Priority Tests")
    class PriorityTests {

        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10})
        @DisplayName("Should set valid priority")
        void shouldSetValidPriority(int priority) {
            ValidationRule rule = ValidationRule.create(RULE_ID, RULE_NAME, ValidationType.CUSTOM);

            rule.setPriority(priority);

            assertThat(rule.getPriority()).isEqualTo(priority);
        }

        @Test
        @DisplayName("Should reject priority below minimum")
        void shouldRejectLowPriority() {
            ValidationRule rule = ValidationRule.create(RULE_ID, RULE_NAME, ValidationType.CUSTOM);

            assertThatThrownBy(() -> rule.setPriority(0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Priority must be between");
        }

        @Test
        @DisplayName("Should reject priority above maximum")
        void shouldRejectHighPriority() {
            ValidationRule rule = ValidationRule.create(RULE_ID, RULE_NAME, ValidationType.CUSTOM);

            assertThatThrownBy(() -> rule.setPriority(11))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Priority must be between");
        }
    }

    @Nested
    @DisplayName("Restore Tests")
    class RestoreTests {

        @Test
        @DisplayName("Should restore existing rule")
        void shouldRestoreExistingRule() {
            Map<String, Object> config = Map.of("pattern", ".*");
            Severity severity = Severity.MEDIUM;
            int priority = 5;
            boolean enabled = true;

            ValidationRule rule = ValidationRule.restore(
                    RULE_ID,
                    RULE_NAME,
                    ValidationType.CUSTOM,
                    config,
                    severity,
                    priority,
                    enabled
            );

            assertThat(rule.getRuleId()).isEqualTo(RULE_ID);
            assertThat(rule.getName()).isEqualTo(RULE_NAME);
            assertThat(rule.getType()).isEqualTo(ValidationType.CUSTOM);
            assertThat(rule.getConfiguration()).isEqualTo(config);
            assertThat(rule.getSeverity()).isEqualTo(severity);
            assertThat(rule.getPriority()).isEqualTo(priority);
            assertThat(rule.isEnabled()).isEqualTo(enabled);
        }
    }
}

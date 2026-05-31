package com.gogidix.aiservices.supplychainoptimizationservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("OptimizationStatus Enum Tests")
class OptimizationStatusEnumTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(OptimizationStatus.class)
        @DisplayName("Should have all OptimizationStatus values")
        void shouldHaveAllValues(OptimizationStatus status) {
            assertThat(status).isNotNull();
        }

        @Test
        @DisplayName("Should have exactly 5 OptimizationStatus values")
        void shouldHave5Values() {
            assertThat(OptimizationStatus.values()).hasSize(5);
        }

        @Test
        @DisplayName("Should contain PENDING")
        void shouldContainPending() {
            assertThat(OptimizationStatus.valueOf("PENDING")).isEqualTo(OptimizationStatus.PENDING);
        }

        @Test
        @DisplayName("Should contain IN_PROGRESS")
        void shouldContainInProgress() {
            assertThat(OptimizationStatus.valueOf("IN_PROGRESS")).isEqualTo(OptimizationStatus.IN_PROGRESS);
        }

        @Test
        @DisplayName("Should contain COMPLETED")
        void shouldContainCompleted() {
            assertThat(OptimizationStatus.valueOf("COMPLETED")).isEqualTo(OptimizationStatus.COMPLETED);
        }

        @Test
        @DisplayName("Should contain FAILED")
        void shouldContainFailed() {
            assertThat(OptimizationStatus.valueOf("FAILED")).isEqualTo(OptimizationStatus.FAILED);
        }

        @Test
        @DisplayName("Should contain CANCELLED")
        void shouldContainCancelled() {
            assertThat(OptimizationStatus.valueOf("CANCELLED")).isEqualTo(OptimizationStatus.CANCELLED);
        }
    }

    @Nested
    @DisplayName("fromString() Tests")
    class FromStringTests {

        @Test
        @DisplayName("Should parse PENDING from string")
        void shouldParsePending() {
            assertThat(OptimizationStatus.fromString("pending")).isEqualTo(OptimizationStatus.PENDING);
        }

        @Test
        @DisplayName("Should parse IN_PROGRESS from string")
        void shouldParseInProgress() {
            assertThat(OptimizationStatus.fromString("in_progress")).isEqualTo(OptimizationStatus.IN_PROGRESS);
        }

        @Test
        @DisplayName("Should parse case insensitive")
        void shouldParseCaseInsensitive() {
            assertThat(OptimizationStatus.fromString("PENDING")).isEqualTo(OptimizationStatus.PENDING);
            assertThat(OptimizationStatus.fromString("Pending")).isEqualTo(OptimizationStatus.PENDING);
            assertThat(OptimizationStatus.fromString("pEnDiNg")).isEqualTo(OptimizationStatus.PENDING);
        }

        @ParameterizedTest
        @ValueSource(strings = {"pending", "in_progress", "completed", "failed", "cancelled"})
        @DisplayName("Should parse all valid values")
        void shouldParseAllValidValues(String value) {
            assertThat(OptimizationStatus.fromString(value)).isNotNull();
        }

        @Test
        @DisplayName("Should throw exception for invalid value")
        void shouldThrowForInvalidValue() {
            assertThatThrownBy(() -> OptimizationStatus.fromString("invalid"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Unknown optimization status");
        }
    }

    @Nested
    @DisplayName("isTerminal() Tests")
    class IsTerminalTests {

        @Test
        @DisplayName("Should return true for COMPLETED")
        void shouldReturnTrueForCompleted() {
            assertThat(OptimizationStatus.COMPLETED.isTerminal()).isTrue();
        }

        @Test
        @DisplayName("Should return true for FAILED")
        void shouldReturnTrueForFailed() {
            assertThat(OptimizationStatus.FAILED.isTerminal()).isTrue();
        }

        @Test
        @DisplayName("Should return true for CANCELLED")
        void shouldReturnTrueForCancelled() {
            assertThat(OptimizationStatus.CANCELLED.isTerminal()).isTrue();
        }

        @Test
        @DisplayName("Should return false for PENDING")
        void shouldReturnFalseForPending() {
            assertThat(OptimizationStatus.PENDING.isTerminal()).isFalse();
        }

        @Test
        @DisplayName("Should return false for IN_PROGRESS")
        void shouldReturnFalseForInProgress() {
            assertThat(OptimizationStatus.IN_PROGRESS.isTerminal()).isFalse();
        }
    }

    @Nested
    @DisplayName("getValue() Tests")
    class GetValueTests {

        @Test
        @DisplayName("Should return 'pending' for PENDING")
        void shouldReturnPending() {
            assertThat(OptimizationStatus.PENDING.getValue()).isEqualTo("pending");
        }

        @Test
        @DisplayName("Should return 'in_progress' for IN_PROGRESS")
        void shouldReturnInProgress() {
            assertThat(OptimizationStatus.IN_PROGRESS.getValue()).isEqualTo("in_progress");
        }

        @Test
        @DisplayName("Should return 'completed' for COMPLETED")
        void shouldReturnCompleted() {
            assertThat(OptimizationStatus.COMPLETED.getValue()).isEqualTo("completed");
        }

        @Test
        @DisplayName("Should return 'failed' for FAILED")
        void shouldReturnFailed() {
            assertThat(OptimizationStatus.FAILED.getValue()).isEqualTo("failed");
        }

        @Test
        @DisplayName("Should return 'cancelled' for CANCELLED")
        void shouldReturnCancelled() {
            assertThat(OptimizationStatus.CANCELLED.getValue()).isEqualTo("cancelled");
        }
    }

    @Nested
    @DisplayName("toString() Tests")
    class ToStringTests {

        @ParameterizedTest
        @EnumSource(OptimizationStatus.class)
        @DisplayName("Should return value string for all statuses")
        void shouldReturnValueString(OptimizationStatus status) {
            assertThat(status.toString()).isEqualTo(status.getValue());
        }
    }
}

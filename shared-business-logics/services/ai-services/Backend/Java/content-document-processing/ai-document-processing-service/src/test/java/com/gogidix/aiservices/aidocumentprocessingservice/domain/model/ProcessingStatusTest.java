package com.gogidix.aiservices.aidocumentprocessingservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ProcessingStatus Domain Model Tests")
class ProcessingStatusTest {

    @Nested
    @DisplayName("Status State Transition Tests")
    class StateTransitionTests {

        @Test
        @DisplayName("Should allow transition from PROCESSING to COMPLETED")
        void shouldAllowProcessingToCompleted() {
            assertThat(ProcessingStatus.PROCESSING.canTransitionTo(ProcessingStatus.COMPLETED))
                    .isTrue();
        }

        @Test
        @DisplayName("Should allow transition from PROCESSING to FAILED")
        void shouldAllowProcessingToFailed() {
            assertThat(ProcessingStatus.PROCESSING.canTransitionTo(ProcessingStatus.FAILED))
                    .isTrue();
        }

        @Test
        @DisplayName("Should not allow transition from COMPLETED to PROCESSING")
        void shouldNotAllowCompletedToProcessing() {
            assertThat(ProcessingStatus.COMPLETED.canTransitionTo(ProcessingStatus.PROCESSING))
                    .isFalse();
        }

        @Test
        @DisplayName("Should not allow transition from FAILED to COMPLETED")
        void shouldNotAllowFailedToCompleted() {
            assertThat(ProcessingStatus.FAILED.canTransitionTo(ProcessingStatus.COMPLETED))
                    .isFalse();
        }

        @Test
        @DisplayName("Should allow transition from PROCESSING to VALIDATING")
        void shouldAllowProcessingToValidating() {
            assertThat(ProcessingStatus.PROCESSING.canTransitionTo(ProcessingStatus.VALIDATING))
                    .isTrue();
        }

        @Test
        @DisplayName("Should allow transition from VALIDATING to COMPLETED")
        void shouldAllowValidatingToCompleted() {
            assertThat(ProcessingStatus.VALIDATING.canTransitionTo(ProcessingStatus.COMPLETED))
                    .isTrue();
        }
    }

    @Nested
    @DisplayName("Status Properties Tests")
    class StatusPropertiesTests {

        @ParameterizedTest
        @EnumSource(ProcessingStatus.class)
        @DisplayName("Should have valid status names")
        void shouldHaveValidStatusNames(ProcessingStatus status) {
            assertThat(status.name()).isIn("PROCESSING", "COMPLETED", "FAILED", "VALIDATING", "PENDING");
        }

        @Test
        @DisplayName("Should identify terminal statuses")
        void shouldIdentifyTerminalStatuses() {
            assertThat(ProcessingStatus.COMPLETED.isTerminal()).isTrue();
            assertThat(ProcessingStatus.FAILED.isTerminal()).isTrue();
            assertThat(ProcessingStatus.PROCESSING.isTerminal()).isFalse();
            assertThat(ProcessingStatus.VALIDATING.isTerminal()).isFalse();
            assertThat(ProcessingStatus.PENDING.isTerminal()).isFalse();
        }

        @Test
        @DisplayName("Should identify active statuses")
        void shouldIdentifyActiveStatuses() {
            assertThat(ProcessingStatus.PROCESSING.isActive()).isTrue();
            assertThat(ProcessingStatus.VALIDATING.isActive()).isTrue();
            assertThat(ProcessingStatus.COMPLETED.isActive()).isFalse();
            assertThat(ProcessingStatus.FAILED.isActive()).isFalse();
            assertThat(ProcessingStatus.PENDING.isActive()).isFalse();
        }

        @Test
        @DisplayName("Should check if status is final")
        void shouldCheckIfFinal() {
            assertThat(ProcessingStatus.COMPLETED.isFinal()).isTrue();
            assertThat(ProcessingStatus.FAILED.isFinal()).isTrue();
            assertThat(ProcessingStatus.PROCESSING.isFinal()).isFalse();
        }
    }

    @Nested
    @DisplayName("Status Parsing Tests")
    class ParsingTests {

        @Test
        @DisplayName("Should parse status from string")
        void shouldParseFromString() {
            assertThat(ProcessingStatus.fromString("PROCESSING")).isEqualTo(ProcessingStatus.PROCESSING);
            assertThat(ProcessingStatus.fromString("COMPLETED")).isEqualTo(ProcessingStatus.COMPLETED);
            assertThat(ProcessingStatus.fromString("FAILED")).isEqualTo(ProcessingStatus.FAILED);
        }

        @Test
        @DisplayName("Should be case insensitive")
        void shouldBeCaseInsensitive() {
            assertThat(ProcessingStatus.fromString("processing")).isEqualTo(ProcessingStatus.PROCESSING);
            assertThat(ProcessingStatus.fromString("Processing")).isEqualTo(ProcessingStatus.PROCESSING);
        }

        @Test
        @DisplayName("Should return PENDING for invalid string")
        void shouldReturnPendingForInvalid() {
            assertThat(ProcessingStatus.fromString("INVALID")).isEqualTo(ProcessingStatus.PENDING);
            assertThat(ProcessingStatus.fromString(null)).isEqualTo(ProcessingStatus.PENDING);
        }
    }
}

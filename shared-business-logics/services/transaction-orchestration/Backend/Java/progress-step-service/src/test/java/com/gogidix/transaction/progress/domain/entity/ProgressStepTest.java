package com.gogidix.transaction.progress.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProgressStep Tests")
class ProgressStepTest {

    @Test
    @DisplayName("Should create with builder")
    void shouldCreateWithBuilder() {
        UUID txId = UUID.randomUUID();
        ProgressStep s = ProgressStep.builder()
            .transactionId(txId).stepOrder(1).stepName("Validate")
            .stepType(ProgressStep.StepType.VALIDATION).build();
        assertEquals(txId, s.getTransactionId());
        assertEquals(1, s.getStepOrder());
        assertEquals("Validate", s.getStepName());
        assertEquals(ProgressStep.StepStatus.PENDING, s.getStatus());
    }

    @Test
    @DisplayName("Should use no-args constructor")
    void shouldUseNoArgsConstructor() {
        ProgressStep s = new ProgressStep();
        assertNotNull(s.getId());
        assertNull(s.getTransactionId());
    }

    @Test
    @DisplayName("Should test enums")
    void shouldTestEnums() {
        assertEquals(9, ProgressStep.StepType.values().length);
        assertEquals(7, ProgressStep.StepStatus.values().length);
    }

    @Test
    @DisplayName("Should set and get fields")
    void shouldSetAndGetFields() {
        ProgressStep s = new ProgressStep();
        UUID id = UUID.randomUUID();
        s.setId(id); s.setStepOrder(2); s.setStepDescription("desc");
        s.setStatus(ProgressStep.StepStatus.IN_PROGRESS);
        s.setRetryCount(1); s.setMaxRetries(5);
        assertEquals(id, s.getId());
        assertEquals(2, s.getStepOrder());
        assertEquals(ProgressStep.StepStatus.IN_PROGRESS, s.getStatus());
    }

    @Test
    @DisplayName("Should canRetry when under max")
    void shouldCanRetry() {
        ProgressStep s = ProgressStep.builder().retryCount(2).maxRetries(3).build();
        assertTrue(s.canRetry());
    }

    @Test
    @DisplayName("Should not canRetry when at max")
    void shouldNotCanRetry() {
        ProgressStep s = ProgressStep.builder().retryCount(3).maxRetries(3).build();
        assertFalse(s.canRetry());
    }

    @Test
    @DisplayName("Should hasTimedOut return false when no startedAt")
    void shouldHasTimedOutNoStartedAt() {
        ProgressStep s = ProgressStep.builder().executionTimeoutSeconds(10).build();
        assertFalse(s.hasTimedOut());
    }

    @Test
    @DisplayName("Should hasTimedOut return true when past timeout")
    void shouldHasTimedOutTrue() {
        ProgressStep s = ProgressStep.builder().executionTimeoutSeconds(1).build();
        s.setStartedAt(LocalDateTime.now().minusSeconds(5));
        assertTrue(s.hasTimedOut());
    }

    @Test
    @DisplayName("Should calculateDuration")
    void shouldCalculateDuration() {
        ProgressStep s = new ProgressStep();
        s.setStartedAt(LocalDateTime.now().minusSeconds(2));
        s.setCompletedAt(LocalDateTime.now());
        s.calculateDuration();
        assertNotNull(s.getDurationMilliseconds());
        assertTrue(s.getDurationMilliseconds() >= 1000);
    }

    @Test
    @DisplayName("Should isTerminal for terminal states")
    void shouldIsTerminal() {
        assertTrue(ProgressStep.builder().status(ProgressStep.StepStatus.COMPLETED).build().isTerminal());
        assertTrue(ProgressStep.builder().status(ProgressStep.StepStatus.FAILED).build().isTerminal());
        assertFalse(ProgressStep.builder().status(ProgressStep.StepStatus.PENDING).build().isTerminal());
        assertFalse(ProgressStep.builder().status(ProgressStep.StepStatus.IN_PROGRESS).build().isTerminal());
    }

    @Test
    @DisplayName("Should canExecute for PENDING and RETRYING")
    void shouldCanExecute() {
        assertTrue(ProgressStep.builder().status(ProgressStep.StepStatus.PENDING).build().canExecute());
        assertTrue(ProgressStep.builder().status(ProgressStep.StepStatus.RETRYING).build().canExecute());
        assertFalse(ProgressStep.builder().status(ProgressStep.StepStatus.COMPLETED).build().canExecute());
    }

    @Test
    @DisplayName("Should set dates on onCreate")
    void shouldSetDatesOnCreate() {
        ProgressStep s = new ProgressStep();
        s.onCreate();
        assertNotNull(s.getCreatedAt());
        assertNotNull(s.getUpdatedAt());
    }

    @Test
    @DisplayName("Should update updatedAt on onUpdate")
    void shouldUpdateOnUpdate() {
        ProgressStep s = new ProgressStep();
        s.onUpdate();
        assertNotNull(s.getUpdatedAt());
    }
}

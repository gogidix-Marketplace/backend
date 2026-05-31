package com.gogidix.transaction.progress.service;

import com.gogidix.transaction.progress.domain.entity.ProgressStep;
import com.gogidix.transaction.progress.domain.repository.ProgressStepRepository;
import com.gogidix.transaction.progress.dto.*;
import com.gogidix.transaction.progress.exception.StepNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProgressStepService Tests")
class ProgressStepServiceTest {

    @Mock private ProgressStepRepository stepRepository;
    @InjectMocks private ProgressStepService service;

    private ProgressStep buildStep(UUID id, ProgressStep.StepStatus status) {
        return ProgressStep.builder().id(id).transactionId(UUID.randomUUID())
            .stepOrder(1).stepName("Step1").stepType(ProgressStep.StepType.VALIDATION)
            .status(status).maxRetries(3).retryCount(0).build();
    }

    @Test
    @DisplayName("Should create step")
    void shouldCreateStep() {
        ProgressStepCreateRequest req = ProgressStepCreateRequest.builder()
            .transactionId(UUID.randomUUID()).stepOrder(1).stepName("S")
            .stepType(ProgressStep.StepType.VALIDATION).build();
        when(stepRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        ProgressStepResponse res = service.createStep(req);
        assertNotNull(res);
        assertEquals("S", res.getStepName());
    }

    @Test
    @DisplayName("Should create steps batch")
    void shouldCreateStepsBatch() {
        UUID txId = UUID.randomUUID();
        List<ProgressStepCreateRequest> reqs = List.of(
            ProgressStepCreateRequest.builder().transactionId(txId).stepOrder(1).stepName("A").stepType(ProgressStep.StepType.INITIALIZATION).build(),
            ProgressStepCreateRequest.builder().transactionId(txId).stepOrder(2).stepName("B").stepType(ProgressStep.StepType.FINALIZATION).build()
        );
        when(stepRepository.saveAll(any())).thenAnswer(inv -> {
            List<ProgressStep> steps = inv.getArgument(0);
            steps.forEach(s -> s.setId(UUID.randomUUID()));
            return steps;
        });
        List<ProgressStepResponse> res = service.createSteps(txId, reqs);
        assertEquals(2, res.size());
    }

    @Test
    @DisplayName("Should start step")
    void shouldStartStep() {
        UUID id = UUID.randomUUID();
        ProgressStep step = buildStep(id, ProgressStep.StepStatus.PENDING);
        when(stepRepository.findById(id)).thenReturn(Optional.of(step));
        when(stepRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        ProgressStepResponse res = service.startStep(id);
        assertEquals(ProgressStep.StepStatus.IN_PROGRESS, res.getStatus());
        assertNotNull(res.getStartedAt());
    }

    @Test
    @DisplayName("Should throw when starting non-pending step")
    void shouldThrowWhenStartingNonPending() {
        UUID id = UUID.randomUUID();
        ProgressStep step = buildStep(id, ProgressStep.StepStatus.COMPLETED);
        when(stepRepository.findById(id)).thenReturn(Optional.of(step));
        assertThrows(IllegalStateException.class, () -> service.startStep(id));
    }

    @Test
    @DisplayName("Should complete step")
    void shouldCompleteStep() {
        UUID id = UUID.randomUUID();
        ProgressStep step = buildStep(id, ProgressStep.StepStatus.IN_PROGRESS);
        step.setStartedAt(LocalDateTime.now().minusSeconds(2));
        when(stepRepository.findById(id)).thenReturn(Optional.of(step));
        when(stepRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(stepRepository.findByTransactionIdOrderByStepOrderAsc(any())).thenReturn(Collections.emptyList());
        ProgressStepResponse res = service.completeStep(id, Map.of("key", "val"));
        assertEquals(ProgressStep.StepStatus.COMPLETED, res.getStatus());
        assertNotNull(res.getCompletedAt());
        assertNotNull(res.getDurationMilliseconds());
    }

    @Test
    @DisplayName("Should fail step")
    void shouldFailStep() {
        UUID id = UUID.randomUUID();
        ProgressStep step = buildStep(id, ProgressStep.StepStatus.IN_PROGRESS);
        step.setStartedAt(LocalDateTime.now().minusSeconds(1));
        when(stepRepository.findById(id)).thenReturn(Optional.of(step));
        when(stepRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        ProgressStepResponse res = service.failStep(id, "error occurred");
        assertEquals(ProgressStep.StepStatus.FAILED, res.getStatus());
        assertEquals("error occurred", res.getErrorMessage());
    }

    @Test
    @DisplayName("Should skip step")
    void shouldSkipStep() {
        UUID id = UUID.randomUUID();
        ProgressStep step = buildStep(id, ProgressStep.StepStatus.PENDING);
        when(stepRepository.findById(id)).thenReturn(Optional.of(step));
        when(stepRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(stepRepository.findByTransactionIdOrderByStepOrderAsc(any())).thenReturn(Collections.emptyList());
        ProgressStepResponse res = service.skipStep(id, "not needed");
        assertEquals(ProgressStep.StepStatus.SKIPPED, res.getStatus());
    }

    @Test
    @DisplayName("Should throw StepNotFoundException")
    void shouldThrowNotFound() {
        UUID id = UUID.randomUUID();
        when(stepRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(StepNotFoundException.class, () -> service.getStepById(id));
    }

    @Test
    @DisplayName("Should get execution summary")
    void shouldGetExecutionSummary() {
        UUID txId = UUID.randomUUID();
        List<ProgressStep> steps = List.of(
            buildStep(UUID.randomUUID(), ProgressStep.StepStatus.COMPLETED),
            buildStep(UUID.randomUUID(), ProgressStep.StepStatus.PENDING),
            buildStep(UUID.randomUUID(), ProgressStep.StepStatus.IN_PROGRESS),
            buildStep(UUID.randomUUID(), ProgressStep.StepStatus.FAILED)
        );
        when(stepRepository.findByTransactionIdOrderByStepOrderAsc(txId)).thenReturn(steps);
        StepExecutionSummary sum = service.getExecutionSummary(txId);
        assertEquals(4, sum.getTotalSteps());
        assertEquals(1, sum.getCompletedSteps());
        assertEquals(1, sum.getFailedSteps());
        assertEquals(25, sum.getProgressPercentage());
        assertFalse(sum.getIsComplete());
    }

    @Test
    @DisplayName("Should get steps by transaction")
    void shouldGetStepsByTransaction() {
        UUID txId = UUID.randomUUID();
        when(stepRepository.findByTransactionIdOrderByStepOrderAsc(txId)).thenReturn(Collections.emptyList());
        List<ProgressStepResponse> res = service.getStepsByTransactionId(txId);
        assertTrue(res.isEmpty());
    }

    @Test
    @DisplayName("Should execute compensation")
    void shouldExecuteCompensation() {
        UUID id = UUID.randomUUID();
        ProgressStep step = buildStep(id, ProgressStep.StepStatus.FAILED);
        step.setCompensationAction("refund");
        when(stepRepository.findById(id)).thenReturn(Optional.of(step));
        assertDoesNotThrow(() -> service.executeCompensation(id));
    }

    @Test
    @DisplayName("Should throw when compensation step not found")
    void shouldThrowWhenCompensationNotFound() {
        UUID id = UUID.randomUUID();
        when(stepRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(StepNotFoundException.class, () -> service.executeCompensation(id));
    }
}

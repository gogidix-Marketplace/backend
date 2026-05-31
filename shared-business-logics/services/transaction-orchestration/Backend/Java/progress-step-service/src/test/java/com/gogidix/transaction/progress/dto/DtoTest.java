package com.gogidix.transaction.progress.dto;

import com.gogidix.transaction.progress.domain.entity.ProgressStep;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DTO Tests")
class DtoTest {

    @Test
    @DisplayName("Should build ProgressStepCreateRequest")
    void shouldBuildCreateRequest() {
        UUID txId = UUID.randomUUID();
        ProgressStepCreateRequest req = ProgressStepCreateRequest.builder()
            .transactionId(txId).stepOrder(1).stepName("Step1")
            .stepType(ProgressStep.StepType.VALIDATION).build();
        assertEquals(txId, req.getTransactionId());
        assertEquals("Step1", req.getStepName());
    }

    @Test
    @DisplayName("Should use no-args and setters for CreateRequest")
    void shouldUseNoArgsConstructor() {
        ProgressStepCreateRequest req = new ProgressStepCreateRequest();
        req.setStepName("test");
        assertEquals("test", req.getStepName());
    }

    @Test
    @DisplayName("Should build ProgressStepResponse")
    void shouldBuildResponse() {
        UUID id = UUID.randomUUID();
        ProgressStepResponse res = ProgressStepResponse.builder()
            .id(id).stepName("Step1").status(ProgressStep.StepStatus.COMPLETED)
            .durationMilliseconds(500L).build();
        assertEquals(id, res.getId());
        assertEquals(ProgressStep.StepStatus.COMPLETED, res.getStatus());
        assertEquals(500L, res.getDurationMilliseconds());
    }

    @Test
    @DisplayName("Should build StepExecutionSummary")
    void shouldBuildSummary() {
        UUID txId = UUID.randomUUID();
        StepExecutionSummary sum = StepExecutionSummary.builder()
            .transactionId(txId).totalSteps(10L).completedSteps(5L)
            .progressPercentage(50).isComplete(false).build();
        assertEquals(txId, sum.getTransactionId());
        assertEquals(10L, sum.getTotalSteps());
        assertEquals(50, sum.getProgressPercentage());
        assertFalse(sum.getIsComplete());
    }

    @Test
    @DisplayName("Should test ProgressStepMapper toEntity")
    void shouldTestMapperToEntity() {
        UUID txId = UUID.randomUUID();
        ProgressStepCreateRequest req = ProgressStepCreateRequest.builder()
            .transactionId(txId).stepOrder(1).stepName("Step")
            .stepType(ProgressStep.StepType.NOTIFICATION).build();
        ProgressStep entity = com.gogidix.transaction.progress.mapper.ProgressStepMapper.toEntity(req);
        assertEquals(txId, entity.getTransactionId());
        assertEquals("Step", entity.getStepName());
        assertFalse(entity.getCanExecuteParallel());
        assertEquals(3, entity.getMaxRetries());
    }

    @Test
    @DisplayName("Should test ProgressStepMapper toResponse")
    void shouldTestMapperToResponse() {
        ProgressStep entity = ProgressStep.builder()
            .transactionId(UUID.randomUUID()).stepOrder(1).stepName("S")
            .stepType(ProgressStep.StepType.FINALIZATION)
            .status(ProgressStep.StepStatus.COMPLETED).build();
        ProgressStepResponse res = com.gogidix.transaction.progress.mapper.ProgressStepMapper.toResponse(entity);
        assertEquals("S", res.getStepName());
        assertEquals(ProgressStep.StepStatus.COMPLETED, res.getStatus());
    }
}

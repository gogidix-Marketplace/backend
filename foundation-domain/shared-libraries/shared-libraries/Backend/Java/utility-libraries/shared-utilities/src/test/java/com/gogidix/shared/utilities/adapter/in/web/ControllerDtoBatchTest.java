package com.gogidix.shared.utilities.adapter.in.web;

import com.gogidix.shared.utilities.application.port.in.UtilityOperationUseCase;
import com.gogidix.shared.utilities.application.port.in.UtilityOperationUseCase.*;
import com.gogidix.shared.utilities.domain.model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.*;

class ControllerDtoBatchTest {

    @Test
    void executeOperationDto_gettersSetters() {
        UtilityController.ExecuteOperationDto dto = new UtilityController.ExecuteOperationDto();
        dto.setType(UtilityType.JSON_PROCESSING);
        dto.setName("test");
        dto.setDescription("desc");
        dto.setParameters(Map.of("k", "v"));
        dto.setPriority(OperationPriority.HIGH);
        dto.setUserId("user1");
        dto.setSessionId("sess1");
        dto.setCached(true);
        dto.setCacheKey("key");
        dto.setCacheTtlSeconds(300L);
        dto.setTimeoutSeconds(60L);
        dto.setMetadata(Map.of("m", "v"));

        assertEquals(UtilityType.JSON_PROCESSING, dto.getType());
        assertEquals("test", dto.getName());
        assertEquals("desc", dto.getDescription());
        assertEquals(Map.of("k", "v"), dto.getParameters());
        assertEquals(OperationPriority.HIGH, dto.getPriority());
        assertEquals("user1", dto.getUserId());
        assertEquals("sess1", dto.getSessionId());
        assertTrue(dto.isCached());
        assertEquals("key", dto.getCacheKey());
        assertEquals(300L, dto.getCacheTtlSeconds());
        assertEquals(60L, dto.getTimeoutSeconds());
        assertEquals(Map.of("m", "v"), dto.getMetadata());
    }

    @Test
    void batchExecuteDto() {
        UtilityController.BatchExecuteDto dto = new UtilityController.BatchExecuteDto();
        dto.setOperations(List.of(new UtilityController.ExecuteOperationDto()));
        assertEquals(1, dto.getOperations().size());
    }

    @Test
    void cancelOperationDto() {
        UtilityController.CancelOperationDto dto = new UtilityController.CancelOperationDto();
        dto.setReason("user requested");
        assertEquals("user requested", dto.getReason());
    }

    @Test
    void validateParametersDto() {
        UtilityController.ValidateParametersDto dto = new UtilityController.ValidateParametersDto();
        dto.setType(UtilityType.JSON_PROCESSING);
        dto.setParameters(Map.of());
        assertEquals(UtilityType.JSON_PROCESSING, dto.getType());
        assertTrue(dto.getParameters().isEmpty());
    }

    @Test
    void operationResponse_from_success() {
        UtilityOperation op = createTestOp();
        OperationResult result = OperationResult.success(op, Map.of("r", "v"), "ok");
        UtilityController.OperationResponse response = UtilityController.OperationResponse.from(result);

        assertTrue(response.isSuccess());
        assertNotNull(response.getOperationId());
        assertNotNull(response.getStatus());
        assertEquals("ok", response.getMessage());
        assertNull(response.getErrorCode());
    }

    @Test
    void operationResponse_from_nullOperation() {
        OperationResult result = OperationResult.failure(null, "fail", "ERR", null);
        UtilityController.OperationResponse response = UtilityController.OperationResponse.from(result);

        assertFalse(response.isSuccess());
        assertNull(response.getOperationId());
        assertNull(response.getStatus());
        assertEquals("fail", response.getMessage());
        assertEquals("ERR", response.getErrorCode());
    }

    @Test
    void asyncOperationResponse() {
        UtilityController.AsyncOperationResponse resp = new UtilityController.AsyncOperationResponse("id", "msg", "/url");
        assertEquals("id", resp.getOperationId());
        assertEquals("msg", resp.getMessage());
        assertEquals("/url", resp.getStatusUrl());
    }

    @Test
    void batchOperationResponse_from() {
        List<OperationResult> results = List.of(
            OperationResult.success(null, Map.of(), "ok"),
            OperationResult.failure(null, "fail", "ERR", null)
        );
        BatchOperationResult batchResult = new BatchOperationResult(2, 1, 1, results, 100L);
        UtilityController.BatchOperationResponse response = UtilityController.BatchOperationResponse.from(batchResult);

        assertEquals(2, response.getTotalOperations());
        assertEquals(1, response.getSuccessfulOperations());
        assertEquals(1, response.getFailedOperations());
        assertEquals(0.5, response.getSuccessRate(), 0.01);
        assertEquals(100L, response.getTotalExecutionTimeMs());
        assertEquals(2, response.getResults().size());
    }

    @Test
    void operationDetailsResponse_from() {
        UtilityOperation op = createTestOp();
        UtilityController.OperationDetailsResponse resp = UtilityController.OperationDetailsResponse.from(op);

        assertEquals("op-1", resp.getOperationId());
        assertEquals("Test Op", resp.getName());
        assertEquals("Test Desc", resp.getDescription());
        assertEquals(UtilityType.JSON_PROCESSING, resp.getType());
        assertNotNull(resp.getStatus());
        assertNotNull(resp.getPriority());
        assertNotNull(resp.getStartTime());
        assertEquals("user1", resp.getUserId());
    }

    @Test
    void operationStatisticsResponse_from() {
        OperationStatistics stats = new OperationStatistics("u1", 100, 80, 20, 200L,
            Map.of(), Map.of(), LocalDateTime.now());
        UtilityController.OperationStatisticsResponse resp = UtilityController.OperationStatisticsResponse.from(stats);

        assertEquals("u1", resp.getUserId());
        assertEquals(100, resp.getTotalOperations());
        assertEquals(80, resp.getSuccessfulOperations());
        assertEquals(20, resp.getFailedOperations());
        assertEquals(0.8, resp.getSuccessRate(), 0.01);
        assertEquals(200L, resp.getAverageExecutionTimeMs());
    }

    @Test
    void cleanupResponse_from() {
        CleanupResult result = new CleanupResult(10, 7, 50L, "cleaned");
        UtilityController.CleanupResponse resp = UtilityController.CleanupResponse.from(result);
        assertEquals(10, resp.getOperationsFound());
        assertEquals(7, resp.getOperationsRemoved());
        assertEquals(50L, resp.getCleanupTimeMs());
        assertEquals("cleaned", resp.getSummary());
    }

    @Test
    void utilityTypeInfoResponse_from() {
        UtilityTypeInfo info = new UtilityTypeInfo(
            UtilityType.JSON_PROCESSING, "JSON", true, true, 30L, 1048576L, false, List.of("data"));
        UtilityController.UtilityTypeInfoResponse resp = UtilityController.UtilityTypeInfoResponse.from(info);

        assertEquals(UtilityType.JSON_PROCESSING, resp.getType());
        assertEquals("JSON Processing", resp.getDisplayName());
        assertTrue(resp.isCacheable());
        assertTrue(resp.isRetryable());
        assertEquals(30L, resp.getDefaultTimeoutSeconds());
        assertEquals(1048576L, resp.getMaxInputSizeBytes());
        assertFalse(resp.isRequiresAuthentication());
        assertEquals(1, resp.getRequiredParameters().size());
    }

    @Test
    void validationResponse_from() {
        ValidationResult valid = ValidationResult.valid("ok");
        UtilityController.ValidationResponse resp = UtilityController.ValidationResponse.from(valid);
        assertTrue(resp.isValid());
        assertTrue(resp.getErrors().isEmpty());
        assertEquals("ok", resp.getMessage());

        ValidationResult invalid = ValidationResult.invalid(List.of("err"), "bad");
        UtilityController.ValidationResponse resp2 = UtilityController.ValidationResponse.from(invalid);
        assertFalse(resp2.isValid());
        assertEquals(1, resp2.getErrors().size());
    }

    @Test
    void queueStatusResponse_from() {
        QueueStatus status = new QueueStatus(5, 2, 10, 1, 200.0, 8, 4);
        UtilityController.QueueStatusResponse resp = UtilityController.QueueStatusResponse.from(status);

        assertEquals(5, resp.getQueuedOperations());
        assertEquals(2, resp.getRunningOperations());
        assertEquals(10, resp.getCompletedOperations());
        assertEquals(1, resp.getFailedOperations());
        assertEquals(200.0, resp.getAverageWaitTimeMs());
        assertEquals(12, resp.getTotalWorkers());
        assertFalse(resp.isOverloaded());
    }

    @Test
    void processingResponse_from() {
        ProcessingResult result = new ProcessingResult(5, 2, 100L, "done");
        UtilityController.ProcessingResponse resp = UtilityController.ProcessingResponse.from(result);
        assertEquals(5, resp.getProcessedOperations());
        assertEquals(2, resp.getSkippedOperations());
        assertEquals(100L, resp.getProcessingTimeMs());
        assertEquals("done", resp.getSummary());
    }

    private UtilityOperation createTestOp() {
        LocalDateTime now = LocalDateTime.now();
        return new UtilityOperation(
            UUID.randomUUID(), 1L, now, "SYSTEM", now, "SYSTEM",
            false, null, null, null,
            "op-1", UtilityType.JSON_PROCESSING, OperationStatus.COMPLETED,
            "Test Op", "Test Desc",
            Map.of("in", "data"), Map.of("out", "result"),
            Map.of(), Map.of(),
            now, now.plusSeconds(5), now, 5000L, 100L, 5100L,
            "user1", "sess1", "corr1", "req1", "client1",
            OperationPriority.NORMAL, 0, 3, 1.5, false,
            null, null, null, List.of(), null,
            false, null, null, "default", false,
            1048576L, 0L, 4, 0.0, 300000L,
            100, "DONE", 50L, 100L, "standard"
        );
    }
}

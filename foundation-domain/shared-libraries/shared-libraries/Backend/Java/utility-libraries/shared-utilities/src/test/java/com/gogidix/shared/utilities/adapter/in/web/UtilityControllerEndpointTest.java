package com.gogidix.shared.utilities.adapter.in.web;

import com.gogidix.shared.utilities.application.port.in.UtilityOperationUseCase;
import com.gogidix.shared.utilities.application.port.in.UtilityOperationUseCase.*;
import com.gogidix.shared.utilities.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

class UtilityControllerEndpointTest {

    private UtilityController controller;
    private StubUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new StubUseCase();
        controller = new UtilityController(useCase);
    }

    @Test
    void executeOperation() {
        UtilityController.ExecuteOperationDto dto = new UtilityController.ExecuteOperationDto();
        dto.setType(UtilityType.JSON_PROCESSING);
        dto.setName("test");
        dto.setDescription("desc");
        dto.setParameters(Map.of());
        dto.setPriority(OperationPriority.NORMAL);
        dto.setUserId("user1");
        dto.setSessionId("sess1");
        dto.setCached(false);
        dto.setMetadata(Map.of());

        var response = controller.executeOperation(dto);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void executeOperationAsync() {
        UtilityController.ExecuteOperationDto dto = new UtilityController.ExecuteOperationDto();
        dto.setType(UtilityType.JSON_PROCESSING);
        dto.setName("async-test");
        dto.setDescription("desc");
        dto.setParameters(Map.of());
        dto.setPriority(OperationPriority.NORMAL);
        dto.setUserId("user1");
        dto.setSessionId("sess1");
        dto.setCached(false);
        dto.setMetadata(Map.of());

        var response = controller.executeOperationAsync(dto);
        assertEquals(202, response.getStatusCode().value());
        assertNotNull(response.getBody().getOperationId());
        assertNotNull(response.getBody().getMessage());
        assertNotNull(response.getBody().getStatusUrl());
    }

    @Test
    void executeBatchOperations() {
        UtilityController.ExecuteOperationDto dto1 = new UtilityController.ExecuteOperationDto();
        dto1.setType(UtilityType.JSON_PROCESSING);
        dto1.setName("batch1");
        dto1.setDescription("d");
        dto1.setParameters(Map.of());
        dto1.setPriority(OperationPriority.NORMAL);
        dto1.setUserId("u1");
        dto1.setSessionId("s1");
        dto1.setCached(false);
        dto1.setMetadata(Map.of());

        UtilityController.BatchExecuteDto batch = new UtilityController.BatchExecuteDto();
        batch.setOperations(List.of(dto1));

        var response = controller.executeBatchOperations(batch);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void getOperation_found() {
        useCase.operationToReturn = Optional.of(createTestOp());
        var response = controller.getOperation("op-1");
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void getOperation_notFound() {
        var response = controller.getOperation("nonexistent");
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void getUserOperations() {
        useCase.userOperations = List.of(createTestOp());
        var response = controller.getUserOperations("user1", 10);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getOperations_byStatus() {
        useCase.statusOperations = List.of(createTestOp());
        var response = controller.getOperations(OperationStatus.COMPLETED, null, 50);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void getOperations_byType() {
        useCase.typeOperations = List.of(createTestOp());
        var response = controller.getOperations(null, UtilityType.JSON_PROCESSING, 50);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void getOperations_noFilter() {
        useCase.statusOperations = List.of(createTestOp());
        var response = controller.getOperations(null, null, 50);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void cancelOperation() {
        UtilityController.CancelOperationDto dto = new UtilityController.CancelOperationDto();
        dto.setReason("user request");
        var response = controller.cancelOperation("op-1", dto);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void retryOperation() {
        var response = controller.retryOperation("op-1");
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void pauseOperation() {
        var response = controller.pauseOperation("op-1");
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void resumeOperation() {
        var response = controller.resumeOperation("op-1");
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void getOperationStatistics() {
        var response = controller.getOperationStatistics("user1");
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void getGlobalStatistics() {
        var response = controller.getGlobalStatistics();
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void cleanupCompletedOperations() {
        var response = controller.cleanupCompletedOperations(30);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void getAvailableUtilityTypes() {
        var response = controller.getAvailableUtilityTypes();
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void validateOperationParameters() {
        UtilityController.ValidateParametersDto dto = new UtilityController.ValidateParametersDto();
        dto.setType(UtilityType.JSON_PROCESSING);
        dto.setParameters(Map.of());
        var response = controller.validateOperationParameters(dto);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void getQueueStatus() {
        var response = controller.getQueueStatus();
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void processQueue() {
        var response = controller.processQueue();
        assertEquals(200, response.getStatusCode().value());
    }

    private UtilityOperation createTestOp() {
        LocalDateTime now = LocalDateTime.now();
        return new UtilityOperation(
            UUID.randomUUID(), 1L, now, "SYSTEM", now, "SYSTEM",
            false, null, null, null,
            "op-1", UtilityType.JSON_PROCESSING, OperationStatus.COMPLETED,
            "Test Op", "Test Desc",
            Map.of(), Map.of(), Map.of(), Map.of(),
            now, now.plusSeconds(5), now, 5000L, 100L, 5100L,
            "user1", "sess1", "corr1", "req1", "client1",
            OperationPriority.NORMAL, 0, 3, 1.5, false,
            null, null, null, List.of(), null,
            false, null, null, "default", false,
            1048576L, 0L, 4, 0.0, 300000L,
            100, "DONE", 50L, 100L, "standard"
        );
    }

    static class StubUseCase implements UtilityOperationUseCase {
        Optional<UtilityOperation> operationToReturn = Optional.empty();
        List<UtilityOperation> userOperations = List.of();
        List<UtilityOperation> statusOperations = List.of();
        List<UtilityOperation> typeOperations = List.of();

        public OperationResult executeOperation(ExecuteOperationRequest req) {
            return OperationResult.success(null, Map.of(), "ok");
        }
        public CompletableFuture<OperationResult> executeOperationAsync(ExecuteOperationRequest req) {
            return CompletableFuture.completedFuture(OperationResult.success(null, Map.of(), "async-ok"));
        }
        public BatchOperationResult executeBatchOperations(List<ExecuteOperationRequest> reqs) {
            return new BatchOperationResult(reqs.size(), reqs.size(), 0, List.of(), 100L);
        }
        public Optional<UtilityOperation> getOperation(String id) { return operationToReturn; }
        public List<UtilityOperation> getUserOperations(String userId, int limit) { return userOperations; }
        public List<UtilityOperation> getOperationsByStatus(OperationStatus status, int limit) { return statusOperations; }
        public List<UtilityOperation> getOperationsByType(UtilityType type, int limit) { return typeOperations; }
        public OperationResult cancelOperation(String id, String reason) { return OperationResult.success(null, Map.of(), "cancelled"); }
        public OperationResult retryOperation(String id) { return OperationResult.success(null, Map.of(), "retried"); }
        public OperationResult pauseOperation(String id) { return OperationResult.success(null, Map.of(), "paused"); }
        public OperationResult resumeOperation(String id) { return OperationResult.success(null, Map.of(), "resumed"); }
        public OperationStatistics getOperationStatistics(String userId) {
            return new OperationStatistics(userId, 10, 8, 2, 150L, Map.of(), Map.of(), null);
        }
        public OperationStatistics getGlobalStatistics() {
            return new OperationStatistics(null, 100, 80, 20, 200L, Map.of(), Map.of(), null);
        }
        public CleanupResult cleanupCompletedOperations(int maxAge) { return new CleanupResult(5, 3, 50L, "cleaned"); }
        public List<UtilityTypeInfo> getAvailableUtilityTypes() { return List.of(); }
        public ValidationResult validateOperationParameters(UtilityType type, Map<String, Object> params) {
            return ValidationResult.valid("ok");
        }
        public QueueStatus getQueueStatus() { return new QueueStatus(0, 1, 5, 0, 10.0, 4, 1); }
        public ProcessingResult processQueue() { return new ProcessingResult(3, 0, 50L, "processed 3"); }
    }
}

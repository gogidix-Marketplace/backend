package com.gogidix.shared.utilities.application.service;

import com.gogidix.shared.utilities.application.port.in.UtilityOperationUseCase;
import com.gogidix.shared.utilities.domain.model.UtilityOperation;
import com.gogidix.shared.utilities.domain.model.UtilityType;
import com.gogidix.shared.utilities.domain.model.OperationStatus;
import com.gogidix.shared.utilities.domain.model.OperationPriority;
import com.gogidix.shared.utilities.domain.port.out.UtilityOperationRepository;
import com.gogidix.shared.utilities.domain.port.out.UtilityExecutor;
import com.gogidix.shared.utilities.domain.port.out.CacheService;
import com.gogidix.shared.utilities.domain.port.out.EventPublisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Application service implementing utility operation use cases.
 * Orchestrates domain operations and coordinates with infrastructure adapters.
 */
@Service
@Transactional
public class UtilityOperationService implements UtilityOperationUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(UtilityOperationService.class);
    
    private final UtilityOperationRepository repository;
    private final UtilityExecutor executor;
    private final CacheService cacheService;
    private final EventPublisher eventPublisher;
    private final Executor asyncExecutor;
    
    public UtilityOperationService(UtilityOperationRepository repository,
                                  UtilityExecutor executor,
                                  CacheService cacheService,
                                  EventPublisher eventPublisher,
                                  Executor asyncExecutor) {
        this.repository = repository;
        this.executor = executor;
        this.cacheService = cacheService;
        this.eventPublisher = eventPublisher;
        this.asyncExecutor = asyncExecutor;
    }
    
    @Override
    public OperationResult executeOperation(ExecuteOperationRequest request) {
        logger.debug("Executing operation: {} for user: {}", request.getName(), request.getUserId());
        
        try {
            // Create operation entity
            UtilityOperation operation = createUtilityOperation(request);
            
            // Check cache if enabled
            if (request.isCached() && operation.shouldUseCache()) {
                String cacheKey = operation.generateCacheKey();
                Optional<Map<String, Object>> cachedResult = cacheService.get(cacheKey, (Class<Map<String, Object>>)(Class<?>)Map.class);
                
                if (cachedResult.isPresent()) {
                    logger.debug("Returning cached result for operation: {}", operation.getOperationId());
                    
                    UtilityOperation completedOperation = operation.completeOperation(cachedResult.get(), "CACHE");
                    repository.save(completedOperation);
                    
                    return OperationResult.success(completedOperation, cachedResult.get(), "Result from cache");
                }
            }
            
            // Save initial operation
            operation = repository.save(operation);
            
            // Start execution
            UtilityOperation runningOperation = operation.startOperation("SYSTEM");
            runningOperation = repository.save(runningOperation);
            
            // Publish started event
            eventPublisher.publishOperationStarted(runningOperation);
            
            // Execute the operation
            UtilityExecutor.ExecutionResult executionResult = executor.execute(
                request.getType(), request.getParameters());
            
            if (executionResult.isSuccess()) {
                // Complete operation successfully
                UtilityOperation completedOperation = runningOperation.completeOperation(
                    executionResult.getResult(), "SYSTEM");
                completedOperation = repository.save(completedOperation);
                
                // Cache result if enabled
                if (request.isCached() && completedOperation.shouldUseCache()) {
                    Duration ttl = request.getCacheTtlSeconds() != null ? 
                        Duration.ofSeconds(request.getCacheTtlSeconds()) : Duration.ofHours(1);
                    cacheService.put(completedOperation.generateCacheKey(), executionResult.getResult(), ttl);
                }
                
                // Publish completed event
                eventPublisher.publishOperationCompleted(completedOperation);
                
                logger.info("Operation completed successfully: {}", completedOperation.getOperationId());
                return OperationResult.success(completedOperation, executionResult.getResult(), 
                                             "Operation completed successfully");
                
            } else {
                // Handle failure
                UtilityOperation failedOperation = runningOperation.failOperation(
                    executionResult.getErrorMessage(),
                    executionResult.getErrorCode(),
                    executionResult.getError() != null ? executionResult.getError().toString() : null,
                    "SYSTEM"
                );
                failedOperation = repository.save(failedOperation);
                
                // Publish failed event
                eventPublisher.publishOperationFailed(failedOperation, 
                                                    executionResult.getErrorMessage(), 
                                                    executionResult.getErrorCode());
                
                logger.error("Operation failed: {} - {}", failedOperation.getOperationId(), 
                           executionResult.getErrorMessage());
                return OperationResult.failure(failedOperation, executionResult.getErrorMessage(),
                                             executionResult.getErrorCode(), executionResult.getError());
            }
            
        } catch (Exception e) {
            logger.error("Error executing operation: {}", request.getName(), e);
            throw new RuntimeException("Failed to execute operation: " + e.getMessage(), e);
        }
    }
    
    @Override
    public CompletableFuture<OperationResult> executeOperationAsync(ExecuteOperationRequest request) {
        logger.debug("Executing operation asynchronously: {} for user: {}", request.getName(), request.getUserId());
        
        return CompletableFuture.supplyAsync(() -> executeOperation(request), asyncExecutor);
    }
    
    @Override
    public BatchOperationResult executeBatchOperations(List<ExecuteOperationRequest> requests) {
        logger.debug("Executing batch operations: {} operations", requests.size());
        
        long startTime = System.currentTimeMillis();
        List<OperationResult> results = new ArrayList<>();
        AtomicInteger successful = new AtomicInteger(0);
        AtomicInteger failed = new AtomicInteger(0);
        
        // Execute operations in parallel for better performance
        List<CompletableFuture<OperationResult>> futures = requests.stream()
            .map(this::executeOperationAsync)
            .collect(Collectors.toList());
        
        // Wait for all to complete and collect results
        for (CompletableFuture<OperationResult> future : futures) {
            try {
                OperationResult result = future.get();
                results.add(result);
                
                if (result.isSuccess()) {
                    successful.incrementAndGet();
                } else {
                    failed.incrementAndGet();
                }
            } catch (Exception e) {
                logger.error("Error in batch operation", e);
                failed.incrementAndGet();
                // Create failure result for the exception
                results.add(OperationResult.failure(null, "Batch operation error: " + e.getMessage(), 
                                                  "BATCH_ERROR", e));
            }
        }
        
        long totalTime = System.currentTimeMillis() - startTime;
        
        logger.info("Batch operations completed: {} total, {} successful, {} failed in {}ms",
                   requests.size(), successful.get(), failed.get(), totalTime);
        
        return new BatchOperationResult(requests.size(), successful.get(), failed.get(), results, totalTime);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<UtilityOperation> getOperation(String operationId) {
        return repository.findById(operationId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UtilityOperation> getUserOperations(String userId, int limit) {
        return repository.findByUserId(userId, Math.min(limit, 100)); // Cap at 100
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UtilityOperation> getOperationsByStatus(OperationStatus status, int limit) {
        return repository.findByStatus(status, Math.min(limit, 100)); // Cap at 100
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UtilityOperation> getOperationsByType(UtilityType type, int limit) {
        return repository.findByType(type, Math.min(limit, 100)); // Cap at 100
    }
    
    @Override
    public OperationResult cancelOperation(String operationId, String reason) {
        logger.debug("Cancelling operation: {} with reason: {}", operationId, reason);
        
        Optional<UtilityOperation> operationOpt = repository.findById(operationId);
        if (operationOpt.isEmpty()) {
            return OperationResult.failure(null, "Operation not found", "NOT_FOUND", null);
        }
        
        UtilityOperation operation = operationOpt.get();
        
        if (!operation.getOperationStatus().canCancel()) {
            return OperationResult.failure(operation, "Operation cannot be cancelled in current status", 
                                         "INVALID_STATUS", null);
        }
        
        try {
            // Try to cancel execution if running
            if (operation.isInProgress()) {
                executor.cancelExecution(operation.getOperationId());
            }
            
            // Update operation status
            UtilityOperation cancelledOperation = operation.cancelOperation(reason, "SYSTEM");
            cancelledOperation = repository.save(cancelledOperation);
            
            // Publish cancelled event
            eventPublisher.publishOperationCancelled(cancelledOperation, reason);
            
            logger.info("Operation cancelled: {}", operationId);
            return OperationResult.success(cancelledOperation, null, "Operation cancelled successfully");
            
        } catch (Exception e) {
            logger.error("Error cancelling operation: {}", operationId, e);
            return OperationResult.failure(operation, "Failed to cancel operation: " + e.getMessage(), 
                                         "CANCEL_ERROR", e);
        }
    }
    
    @Override
    public OperationResult retryOperation(String operationId) {
        logger.debug("Retrying operation: {}", operationId);
        
        Optional<UtilityOperation> operationOpt = repository.findById(operationId);
        if (operationOpt.isEmpty()) {
            return OperationResult.failure(null, "Operation not found", "NOT_FOUND", null);
        }
        
        UtilityOperation operation = operationOpt.get();
        
        if (!operation.canRetry()) {
            return OperationResult.failure(operation, "Operation cannot be retried", "CANNOT_RETRY", null);
        }
        
        try {
            // Create retry operation
            UtilityOperation retryOperation = operation.retryOperation("SYSTEM");
            retryOperation = repository.save(retryOperation);
            
            logger.info("Operation queued for retry: {}", operationId);
            return OperationResult.success(retryOperation, null, "Operation queued for retry");
            
        } catch (Exception e) {
            logger.error("Error retrying operation: {}", operationId, e);
            return OperationResult.failure(operation, "Failed to retry operation: " + e.getMessage(), 
                                         "RETRY_ERROR", e);
        }
    }
    
    @Override
    public OperationResult pauseOperation(String operationId) {
        logger.debug("Pausing operation: {}", operationId);
        
        Optional<UtilityOperation> operationOpt = repository.findById(operationId);
        if (operationOpt.isEmpty()) {
            return OperationResult.failure(null, "Operation not found", "NOT_FOUND", null);
        }
        
        UtilityOperation operation = operationOpt.get();
        
        if (!operation.getOperationStatus().canPause()) {
            return OperationResult.failure(operation, "Operation cannot be paused", "CANNOT_PAUSE", null);
        }
        
        try {
            // Try to pause execution
            boolean paused = executor.pauseExecution(operation.getOperationId());
            
            if (paused) {
                // Update operation status to paused
                UtilityOperation updatedOperation = repository.updateStatus(operationId, OperationStatus.PAUSED);
                
                logger.info("Operation paused: {}", operationId);
                return OperationResult.success(updatedOperation, null, "Operation paused successfully");
            } else {
                return OperationResult.failure(operation, "Failed to pause operation", "PAUSE_FAILED", null);
            }
            
        } catch (Exception e) {
            logger.error("Error pausing operation: {}", operationId, e);
            return OperationResult.failure(operation, "Failed to pause operation: " + e.getMessage(), 
                                         "PAUSE_ERROR", e);
        }
    }
    
    @Override
    public OperationResult resumeOperation(String operationId) {
        logger.debug("Resuming operation: {}", operationId);
        
        Optional<UtilityOperation> operationOpt = repository.findById(operationId);
        if (operationOpt.isEmpty()) {
            return OperationResult.failure(null, "Operation not found", "NOT_FOUND", null);
        }
        
        UtilityOperation operation = operationOpt.get();
        
        if (!operation.getOperationStatus().canResume()) {
            return OperationResult.failure(operation, "Operation cannot be resumed", "CANNOT_RESUME", null);
        }
        
        try {
            // Try to resume execution
            boolean resumed = executor.resumeExecution(operation.getOperationId());
            
            if (resumed) {
                // Update operation status to running
                UtilityOperation updatedOperation = repository.updateStatus(operationId, OperationStatus.RUNNING);
                
                logger.info("Operation resumed: {}", operationId);
                return OperationResult.success(updatedOperation, null, "Operation resumed successfully");
            } else {
                return OperationResult.failure(operation, "Failed to resume operation", "RESUME_FAILED", null);
            }
            
        } catch (Exception e) {
            logger.error("Error resuming operation: {}", operationId, e);
            return OperationResult.failure(operation, "Failed to resume operation: " + e.getMessage(), 
                                         "RESUME_ERROR", e);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public OperationStatistics getOperationStatistics(String userId) {
        logger.debug("Getting operation statistics for user: {}", userId);
        
        long total = repository.countByUserId(userId);
        long successful = repository.countByUserIdAndStatus(userId, OperationStatus.COMPLETED);
        long failed = repository.countByUserIdAndStatus(userId, OperationStatus.FAILED) +
                     repository.countByUserIdAndStatus(userId, OperationStatus.ERROR) +
                     repository.countByUserIdAndStatus(userId, OperationStatus.TIMEOUT);
        
        double averageTime = repository.getAverageExecutionTimeByUserId(userId);
        
        // Get operations by type and status (simplified for now)
        Map<UtilityType, Long> operationsByType = Arrays.stream(UtilityType.values())
            .collect(Collectors.toMap(type -> type, type -> 0L));
        
        Map<OperationStatus, Long> operationsByStatus = Arrays.stream(OperationStatus.values())
            .collect(Collectors.toMap(status -> status, status -> 
                repository.countByUserIdAndStatus(userId, status)));
        
        // Get last operation time (simplified)
        LocalDateTime lastOperationTime = LocalDateTime.now().minusDays(1); // Mock for now
        
        return new OperationStatistics(userId, total, successful, failed, (long) averageTime,
                                      operationsByType, operationsByStatus, lastOperationTime);
    }
    
    @Override
    @Transactional(readOnly = true)
    public OperationStatistics getGlobalStatistics() {
        logger.debug("Getting global operation statistics");
        
        // Get global statistics across all users
        double averageTime = repository.getGlobalAverageExecutionTime();
        
        // Simplified implementation - in real scenario, would aggregate across all users
        Map<UtilityType, Long> operationsByType = Arrays.stream(UtilityType.values())
            .collect(Collectors.toMap(type -> type, type -> 0L));
        
        Map<OperationStatus, Long> operationsByStatus = Arrays.stream(OperationStatus.values())
            .collect(Collectors.toMap(status -> status, status -> repository.countByStatus(status)));
        
        long total = operationsByStatus.values().stream().mapToLong(Long::longValue).sum();
        long successful = operationsByStatus.get(OperationStatus.COMPLETED);
        long failed = operationsByStatus.get(OperationStatus.FAILED) +
                     operationsByStatus.get(OperationStatus.ERROR) +
                     operationsByStatus.get(OperationStatus.TIMEOUT);
        
        return new OperationStatistics("GLOBAL", total, successful, failed, (long) averageTime,
                                      operationsByType, operationsByStatus, LocalDateTime.now());
    }
    
    @Override
    public CleanupResult cleanupCompletedOperations(int maxAge) {
        logger.debug("Cleaning up operations older than {} days", maxAge);
        
        long startTime = System.currentTimeMillis();
        
        try {
            // Count operations before cleanup
            long beforeCount = repository.countByStatus(OperationStatus.COMPLETED);
            
            // Delete old operations
            int deletedCount = repository.deleteOlderThan(maxAge);
            
            long cleanupTime = System.currentTimeMillis() - startTime;
            String summary = String.format("Cleaned up %d completed operations older than %d days in %dms",
                                          deletedCount, maxAge, cleanupTime);
            
            logger.info(summary);
            return new CleanupResult((int) beforeCount, deletedCount, cleanupTime, summary);
            
        } catch (Exception e) {
            logger.error("Error during cleanup", e);
            long cleanupTime = System.currentTimeMillis() - startTime;
            return new CleanupResult(0, 0, cleanupTime, "Cleanup failed: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UtilityTypeInfo> getAvailableUtilityTypes() {
        return Arrays.stream(UtilityType.values())
            .map(type -> new UtilityTypeInfo(
                type,
                type.getDisplayName(),
                type.isCacheable(),
                type.isRetryable(),
                type.getDefaultTimeoutSeconds(),
                type.getMaxInputSizeBytes(),
                type.requiresAuthentication(),
                getRequiredParameters(type) // Mock implementation
            ))
            .collect(Collectors.toList());
    }
    
    @Override
    public ValidationResult validateOperationParameters(UtilityType type, Map<String, Object> parameters) {
        UtilityExecutor.ParameterValidationResult executorResult = executor.validateParameters(type, parameters);
        
        List<String> errors = new ArrayList<>(executorResult.getErrors().values());
        List<String> warnings = new ArrayList<>(executorResult.getWarnings().values());
        
        if (executorResult.isValid()) {
            return executorResult.hasWarnings() ? 
                ValidationResult.validWithWarnings(warnings, "Parameters valid with warnings") :
                ValidationResult.valid("Parameters are valid");
        } else {
            return ValidationResult.invalid(errors, "Parameter validation failed");
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public QueueStatus getQueueStatus() {
        // Mock implementation - in real scenario would check actual queue
        long queued = repository.countByStatus(OperationStatus.QUEUED);
        long running = repository.countByStatus(OperationStatus.RUNNING);
        long completed = repository.countByStatus(OperationStatus.COMPLETED);
        long failed = repository.countByStatus(OperationStatus.FAILED);
        
        return new QueueStatus((int) queued, (int) running, (int) completed, (int) failed,
                              500.0, // average wait time
                              10,    // available workers
                              5);    // busy workers
    }
    
    @Override
    public ProcessingResult processQueue() {
        logger.debug("Processing operation queue");
        
        long startTime = System.currentTimeMillis();
        
        // Mock implementation - in real scenario would process actual queue
        List<UtilityOperation> queuedOperations = repository.findByStatus(OperationStatus.QUEUED, 10);
        
        int processed = 0;
        int skipped = 0;
        
        for (UtilityOperation operation : queuedOperations) {
            try {
                // Update to running status
                repository.updateStatus(operation.getOperationId(), OperationStatus.RUNNING);
                processed++;
            } catch (Exception e) {
                logger.warn("Failed to process queued operation: {}", operation.getOperationId(), e);
                skipped++;
            }
        }
        
        long processingTime = System.currentTimeMillis() - startTime;
        String summary = String.format("Processed %d operations, skipped %d in %dms", 
                                      processed, skipped, processingTime);
        
        logger.info(summary);
        return new ProcessingResult(processed, skipped, processingTime, summary);
    }
    
    // Private helper methods
    
    private UtilityOperation createUtilityOperation(ExecuteOperationRequest request) {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        String operationId = "util_" + System.currentTimeMillis() + "_" + id.toString().substring(0, 8);
        
        return new UtilityOperation(
            id, 1L, now, "SYSTEM", now, "SYSTEM", 
            false, null, null, null,
            operationId, request.getType(), OperationStatus.QUEUED, 
            request.getName(), request.getDescription(),
            request.getParameters(), new HashMap<>(), new HashMap<>(), 
            request.getMetadata(),
            null, null, now, null, null, null,
            request.getUserId(), request.getSessionId(), UUID.randomUUID().toString(),
            UUID.randomUUID().toString(), "CLIENT",
            request.getPriority(), 0, 3, 1.5, false,
            null, null, null, new ArrayList<>(), null,
            request.isCached(), request.getCacheKey(), request.getCacheTtlSeconds(), 
            "default", false,
            request.getType().getMaxInputSizeBytes(), 0L, 4, 0.0, 
            request.getTimeoutSeconds() != null ? request.getTimeoutSeconds() * 1000 : 
                request.getType().getDefaultTimeoutSeconds() * 1000,
            0, "CREATED", 0L, 100L, "standard"
        );
    }
    
    private List<String> getRequiredParameters(UtilityType type) {
        // Mock implementation - would be based on actual utility requirements
        return switch (type) {
            case DATA_TRANSFORMATION -> List.of("inputData", "transformationType");
            case JSON_PROCESSING -> List.of("jsonData", "operation");
            case FILE_PROCESSING -> List.of("filePath", "operation");
            case ENCRYPTION -> List.of("data", "algorithm");
            default -> List.of();
        };
    }
}
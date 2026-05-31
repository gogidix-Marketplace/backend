package com.gogidix.shared.utilities.domain.port.out;

import com.gogidix.shared.utilities.domain.model.UtilityOperation;
import com.gogidix.shared.utilities.domain.model.UtilityType;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Port for executing utility operations
 * Defines the contract for executing different types of utility operations
 */
public interface UtilityExecutor {
    
    /**
     * Executes a utility operation synchronously
     */
    ExecutionResult execute(UtilityType type, Map<String, Object> parameters);
    
    /**
     * Executes a utility operation asynchronously
     */
    CompletableFuture<ExecutionResult> executeAsync(UtilityType type, Map<String, Object> parameters);
    
    /**
     * Checks if the executor supports the given utility type
     */
    boolean supports(UtilityType type);
    
    /**
     * Validates parameters for a utility operation
     */
    ParameterValidationResult validateParameters(UtilityType type, Map<String, Object> parameters);
    
    /**
     * Gets the expected execution time for a utility operation
     */
    long getEstimatedExecutionTime(UtilityType type, Map<String, Object> parameters);
    
    /**
     * Cancels a running operation
     */
    boolean cancelExecution(String executionId);
    
    /**
     * Pauses a running operation
     */
    boolean pauseExecution(String executionId);
    
    /**
     * Resumes a paused operation
     */
    boolean resumeExecution(String executionId);
    
    /**
     * Gets execution progress
     */
    ExecutionProgress getProgress(String executionId);
    
    /**
     * Result of utility execution
     */
    class ExecutionResult {
        private final boolean success;
        private final Map<String, Object> result;
        private final String executionId;
        private final long executionTimeMs;
        private final String errorMessage;
        private final String errorCode;
        private final Exception error;
        
        public ExecutionResult(boolean success, Map<String, Object> result, String executionId,
                              long executionTimeMs, String errorMessage, String errorCode, Exception error) {
            this.success = success;
            this.result = result;
            this.executionId = executionId;
            this.executionTimeMs = executionTimeMs;
            this.errorMessage = errorMessage;
            this.errorCode = errorCode;
            this.error = error;
        }
        
        public static ExecutionResult success(Map<String, Object> result, String executionId, long executionTimeMs) {
            return new ExecutionResult(true, result, executionId, executionTimeMs, null, null, null);
        }
        
        public static ExecutionResult failure(String executionId, long executionTimeMs, 
                                            String errorMessage, String errorCode, Exception error) {
            return new ExecutionResult(false, null, executionId, executionTimeMs, errorMessage, errorCode, error);
        }
        
        // Getters
        public boolean isSuccess() { return success; }
        public Map<String, Object> getResult() { return result; }
        public String getExecutionId() { return executionId; }
        public long getExecutionTimeMs() { return executionTimeMs; }
        public String getErrorMessage() { return errorMessage; }
        public String getErrorCode() { return errorCode; }
        public Exception getError() { return error; }
    }
    
    /**
     * Parameter validation result
     */
    class ParameterValidationResult {
        private final boolean valid;
        private final Map<String, String> errors;
        private final Map<String, String> warnings;
        
        public ParameterValidationResult(boolean valid, Map<String, String> errors, Map<String, String> warnings) {
            this.valid = valid;
            this.errors = errors;
            this.warnings = warnings;
        }
        
        public static ParameterValidationResult valid() {
            return new ParameterValidationResult(true, Map.of(), Map.of());
        }
        
        public static ParameterValidationResult invalid(Map<String, String> errors) {
            return new ParameterValidationResult(false, errors, Map.of());
        }
        
        public static ParameterValidationResult validWithWarnings(Map<String, String> warnings) {
            return new ParameterValidationResult(true, Map.of(), warnings);
        }
        
        public boolean isValid() { return valid; }
        public Map<String, String> getErrors() { return errors; }
        public Map<String, String> getWarnings() { return warnings; }
        public boolean hasWarnings() { return !warnings.isEmpty(); }
    }
    
    /**
     * Execution progress information
     */
    class ExecutionProgress {
        private final String executionId;
        private final int progressPercentage;
        private final String currentPhase;
        private final long itemsProcessed;
        private final long totalItemsToProcess;
        private final String message;
        private final boolean canCancel;
        private final boolean canPause;
        
        public ExecutionProgress(String executionId, int progressPercentage, String currentPhase,
                               long itemsProcessed, long totalItemsToProcess, String message,
                               boolean canCancel, boolean canPause) {
            this.executionId = executionId;
            this.progressPercentage = Math.max(0, Math.min(100, progressPercentage));
            this.currentPhase = currentPhase;
            this.itemsProcessed = itemsProcessed;
            this.totalItemsToProcess = totalItemsToProcess;
            this.message = message;
            this.canCancel = canCancel;
            this.canPause = canPause;
        }
        
        public String getExecutionId() { return executionId; }
        public int getProgressPercentage() { return progressPercentage; }
        public String getCurrentPhase() { return currentPhase; }
        public long getItemsProcessed() { return itemsProcessed; }
        public long getTotalItemsToProcess() { return totalItemsToProcess; }
        public String getMessage() { return message; }
        public boolean canCancel() { return canCancel; }
        public boolean canPause() { return canPause; }
        public boolean isComplete() { return progressPercentage >= 100; }
    }
}
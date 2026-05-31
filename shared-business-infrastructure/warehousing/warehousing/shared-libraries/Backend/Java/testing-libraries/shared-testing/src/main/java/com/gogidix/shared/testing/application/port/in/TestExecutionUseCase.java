package com.gogidix.shared.testing.application.port.in;

import com.gogidix.shared.testing.domain.model.TestSpecification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Input port for test execution operations.
 * Defines contracts for executing tests, managing test suites, and reporting results.
 */
public interface TestExecutionUseCase {
    
    /**
     * Executes a single test specification synchronously.
     */
    TestExecutionResult executeTest(ExecuteTestRequest request);
    
    /**
     * Executes a single test specification asynchronously.
     */
    CompletableFuture<TestExecutionResult> executeTestAsync(ExecuteTestRequest request);
    
    /**
     * Executes multiple tests in parallel.
     */
    BatchTestExecutionResult executeTestsInParallel(List<ExecuteTestRequest> requests);
    
    /**
     * Executes multiple tests sequentially.
     */
    BatchTestExecutionResult executeTestsSequentially(List<ExecuteTestRequest> requests);
    
    /**
     * Executes a complete test suite.
     */
    TestSuiteExecutionResult executeTestSuite(ExecuteTestSuiteRequest request);
    
    /**
     * Gets test execution status.
     */
    Optional<TestExecutionResult> getTestExecution(String executionId);
    
    /**
     * Gets all test executions for a specification.
     */
    List<TestExecutionResult> getExecutionsForTest(String testSpecificationId, int limit);
    
    /**
     * Gets test execution history.
     */
    List<TestExecutionResult> getExecutionHistory(String testSpecificationId, LocalDateTime since);
    
    /**
     * Cancels a running test execution.
     */
    CancellationResult cancelTestExecution(String executionId, String reason);
    
    /**
     * Retries a failed test execution.
     */
    TestExecutionResult retryTestExecution(String executionId);
    
    /**
     * Validates test specifications before execution.
     */
    ValidationResult validateTestSpecification(TestSpecification specification);
    
    /**
     * Validates multiple test specifications.
     */
    BatchValidationResult validateTestSpecifications(List<TestSpecification> specifications);
    
    /**
     * Gets test execution statistics.
     */
    TestExecutionStatistics getExecutionStatistics(StatisticsRequest request);
    
    /**
     * Gets test environment status.
     */
    TestEnvironmentStatus getEnvironmentStatus();
    
    /**
     * Creates test data for test execution.
     */
    TestDataResult createTestData(TestDataRequest request);
    
    /**
     * Cleans up test data after execution.
     */
    CleanupResult cleanupTestData(String executionId);
    
    /**
     * Request to execute a test.
     */
    class ExecuteTestRequest {
        private final TestSpecification testSpecification;
        private final Map<String, Object> parameters;
        private final String executedBy;
        private final boolean dryRun;
        private final int timeoutOverrideSeconds;
        private final Map<String, String> environmentVariables;
        private final List<String> tags;
        
        public ExecuteTestRequest(TestSpecification testSpecification, Map<String, Object> parameters,
                                String executedBy, boolean dryRun, int timeoutOverrideSeconds,
                                Map<String, String> environmentVariables, List<String> tags) {
            this.testSpecification = testSpecification;
            this.parameters = parameters;
            this.executedBy = executedBy;
            this.dryRun = dryRun;
            this.timeoutOverrideSeconds = timeoutOverrideSeconds;
            this.environmentVariables = environmentVariables;
            this.tags = tags;
        }
        
        public TestSpecification getTestSpecification() { return testSpecification; }
        public Map<String, Object> getParameters() { return parameters; }
        public String getExecutedBy() { return executedBy; }
        public boolean isDryRun() { return dryRun; }
        public int getTimeoutOverrideSeconds() { return timeoutOverrideSeconds; }
        public Map<String, String> getEnvironmentVariables() { return environmentVariables; }
        public List<String> getTags() { return tags; }
    }
    
    /**
     * Request to execute a test suite.
     */
    class ExecuteTestSuiteRequest {
        private final String suiteName;
        private final List<TestSpecification> tests;
        private final Map<String, Object> suiteParameters;
        private final String executedBy;
        private final boolean stopOnFirstFailure;
        private final boolean parallelExecution;
        private final int maxParallelTests;
        private final Map<String, String> environmentVariables;
        
        public ExecuteTestSuiteRequest(String suiteName, List<TestSpecification> tests,
                                     Map<String, Object> suiteParameters, String executedBy,
                                     boolean stopOnFirstFailure, boolean parallelExecution,
                                     int maxParallelTests, Map<String, String> environmentVariables) {
            this.suiteName = suiteName;
            this.tests = tests;
            this.suiteParameters = suiteParameters;
            this.executedBy = executedBy;
            this.stopOnFirstFailure = stopOnFirstFailure;
            this.parallelExecution = parallelExecution;
            this.maxParallelTests = maxParallelTests;
            this.environmentVariables = environmentVariables;
        }
        
        public String getSuiteName() { return suiteName; }
        public List<TestSpecification> getTests() { return tests; }
        public Map<String, Object> getSuiteParameters() { return suiteParameters; }
        public String getExecutedBy() { return executedBy; }
        public boolean isStopOnFirstFailure() { return stopOnFirstFailure; }
        public boolean isParallelExecution() { return parallelExecution; }
        public int getMaxParallelTests() { return maxParallelTests; }
        public Map<String, String> getEnvironmentVariables() { return environmentVariables; }
    }
    
    /**
     * Result of a test execution.
     */
    class TestExecutionResult {
        private final String executionId;
        private final TestSpecification testSpecification;
        private final TestExecutionStatus status;
        private final LocalDateTime startTime;
        private final LocalDateTime endTime;
        private final long executionTimeMs;
        private final List<TestStepResult> stepResults;
        private final List<AssertionResult> assertionResults;
        private final String errorMessage;
        private final Exception error;
        private final Map<String, Object> executionData;
        private final String executedBy;
        private final boolean dryRun;
        
        public TestExecutionResult(String executionId, TestSpecification testSpecification,
                                 TestExecutionStatus status, LocalDateTime startTime, LocalDateTime endTime,
                                 long executionTimeMs, List<TestStepResult> stepResults,
                                 List<AssertionResult> assertionResults, String errorMessage,
                                 Exception error, Map<String, Object> executionData,
                                 String executedBy, boolean dryRun) {
            this.executionId = executionId;
            this.testSpecification = testSpecification;
            this.status = status;
            this.startTime = startTime;
            this.endTime = endTime;
            this.executionTimeMs = executionTimeMs;
            this.stepResults = stepResults;
            this.assertionResults = assertionResults;
            this.errorMessage = errorMessage;
            this.error = error;
            this.executionData = executionData;
            this.executedBy = executedBy;
            this.dryRun = dryRun;
        }
        
        public String getExecutionId() { return executionId; }
        public TestSpecification getTestSpecification() { return testSpecification; }
        public TestExecutionStatus getStatus() { return status; }
        public LocalDateTime getStartTime() { return startTime; }
        public LocalDateTime getEndTime() { return endTime; }
        public long getExecutionTimeMs() { return executionTimeMs; }
        public List<TestStepResult> getStepResults() { return stepResults; }
        public List<AssertionResult> getAssertionResults() { return assertionResults; }
        public String getErrorMessage() { return errorMessage; }
        public Optional<Exception> getError() { return Optional.ofNullable(error); }
        public Map<String, Object> getExecutionData() { return executionData; }
        public String getExecutedBy() { return executedBy; }
        public boolean isDryRun() { return dryRun; }
        
        public boolean isSuccess() { return status == TestExecutionStatus.PASSED; }
        public boolean isFailure() { return status == TestExecutionStatus.FAILED; }
        public boolean isSkipped() { return status == TestExecutionStatus.SKIPPED; }
        public boolean isRunning() { return status == TestExecutionStatus.RUNNING; }
        
        public int getPassedStepsCount() {
            return stepResults != null ? (int) stepResults.stream()
                    .filter(result -> result.getStatus() == TestStepExecutionStatus.PASSED)
                    .count() : 0;
        }
        
        public int getFailedStepsCount() {
            return stepResults != null ? (int) stepResults.stream()
                    .filter(result -> result.getStatus() == TestStepExecutionStatus.FAILED)
                    .count() : 0;
        }
        
        public int getPassedAssertionsCount() {
            return assertionResults != null ? (int) assertionResults.stream()
                    .filter(AssertionResult::isPassed)
                    .count() : 0;
        }
        
        public int getFailedAssertionsCount() {
            return assertionResults != null ? (int) assertionResults.stream()
                    .filter(result -> !result.isPassed())
                    .count() : 0;
        }
    }
    
    /**
     * Result of batch test execution.
     */
    class BatchTestExecutionResult {
        private final List<TestExecutionResult> results;
        private final LocalDateTime startTime;
        private final LocalDateTime endTime;
        private final long totalExecutionTimeMs;
        private final int totalTests;
        private final int passedTests;
        private final int failedTests;
        private final int skippedTests;
        
        public BatchTestExecutionResult(List<TestExecutionResult> results, LocalDateTime startTime,
                                      LocalDateTime endTime, long totalExecutionTimeMs) {
            this.results = results;
            this.startTime = startTime;
            this.endTime = endTime;
            this.totalExecutionTimeMs = totalExecutionTimeMs;
            this.totalTests = results.size();
            this.passedTests = (int) results.stream().filter(TestExecutionResult::isSuccess).count();
            this.failedTests = (int) results.stream().filter(TestExecutionResult::isFailure).count();
            this.skippedTests = (int) results.stream().filter(TestExecutionResult::isSkipped).count();
        }
        
        public List<TestExecutionResult> getResults() { return results; }
        public LocalDateTime getStartTime() { return startTime; }
        public LocalDateTime getEndTime() { return endTime; }
        public long getTotalExecutionTimeMs() { return totalExecutionTimeMs; }
        public int getTotalTests() { return totalTests; }
        public int getPassedTests() { return passedTests; }
        public int getFailedTests() { return failedTests; }
        public int getSkippedTests() { return skippedTests; }
        
        public double getSuccessRate() {
            return totalTests > 0 ? (double) passedTests / totalTests : 0.0;
        }
        
        public boolean isAllPassed() { return failedTests == 0 && skippedTests == 0; }
        public boolean hasFailures() { return failedTests > 0; }
    }
    
    // Additional result classes and enums...
    
    enum TestExecutionStatus {
        PENDING, RUNNING, PASSED, FAILED, SKIPPED, CANCELLED, ERROR
    }
    
    enum TestStepExecutionStatus {
        PENDING, RUNNING, PASSED, FAILED, SKIPPED, ERROR
    }
    
    class TestStepResult {
        private final String stepName;
        private final TestStepExecutionStatus status;
        private final LocalDateTime startTime;
        private final LocalDateTime endTime;
        private final long executionTimeMs;
        private final String errorMessage;
        private final Map<String, Object> stepData;
        
        public TestStepResult(String stepName, TestStepExecutionStatus status, LocalDateTime startTime,
                            LocalDateTime endTime, long executionTimeMs, String errorMessage,
                            Map<String, Object> stepData) {
            this.stepName = stepName;
            this.status = status;
            this.startTime = startTime;
            this.endTime = endTime;
            this.executionTimeMs = executionTimeMs;
            this.errorMessage = errorMessage;
            this.stepData = stepData;
        }
        
        public String getStepName() { return stepName; }
        public TestStepExecutionStatus getStatus() { return status; }
        public LocalDateTime getStartTime() { return startTime; }
        public LocalDateTime getEndTime() { return endTime; }
        public long getExecutionTimeMs() { return executionTimeMs; }
        public String getErrorMessage() { return errorMessage; }
        public Map<String, Object> getStepData() { return stepData; }
    }
    
    class AssertionResult {
        private final String assertionName;
        private final boolean passed;
        private final String message;
        private final Object actualValue;
        private final Object expectedValue;
        
        public AssertionResult(String assertionName, boolean passed, String message,
                             Object actualValue, Object expectedValue) {
            this.assertionName = assertionName;
            this.passed = passed;
            this.message = message;
            this.actualValue = actualValue;
            this.expectedValue = expectedValue;
        }
        
        public String getAssertionName() { return assertionName; }
        public boolean isPassed() { return passed; }
        public String getMessage() { return message; }
        public Object getActualValue() { return actualValue; }
        public Object getExpectedValue() { return expectedValue; }
    }
    
    // Additional classes for other result types...
    class TestSuiteExecutionResult {
        // Implementation similar to BatchTestExecutionResult
    }
    
    class ValidationResult {
        private final boolean valid;
        private final List<String> errors;
        private final List<String> warnings;
        
        public ValidationResult(boolean valid, List<String> errors, List<String> warnings) {
            this.valid = valid;
            this.errors = errors;
            this.warnings = warnings;
        }
        
        public boolean isValid() { return valid; }
        public List<String> getErrors() { return errors; }
        public List<String> getWarnings() { return warnings; }
    }
    
    class BatchValidationResult {
        private final List<ValidationResult> results;
        private final int totalCount;
        private final int validCount;
        private final int invalidCount;
        
        public BatchValidationResult(List<ValidationResult> results) {
            this.results = results;
            this.totalCount = results.size();
            this.validCount = (int) results.stream().filter(ValidationResult::isValid).count();
            this.invalidCount = totalCount - validCount;
        }
        
        public List<ValidationResult> getResults() { return results; }
        public int getTotalCount() { return totalCount; }
        public int getValidCount() { return validCount; }
        public int getInvalidCount() { return invalidCount; }
    }
    
    class CancellationResult {
        private final boolean success;
        private final String message;
        
        public CancellationResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }
    
    class TestExecutionStatistics {
        // Implementation for statistics
    }
    
    class TestEnvironmentStatus {
        // Implementation for environment status
    }
    
    class TestDataResult {
        // Implementation for test data creation result
    }
    
    class TestDataRequest {
        // Implementation for test data creation request
    }
    
    class CleanupResult {
        // Implementation for cleanup result
    }
    
    class StatisticsRequest {
        // Implementation for statistics request
    }
}
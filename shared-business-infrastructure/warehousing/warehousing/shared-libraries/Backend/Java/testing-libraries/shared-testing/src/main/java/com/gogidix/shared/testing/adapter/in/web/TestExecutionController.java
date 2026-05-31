package com.gogidix.shared.testing.adapter.in.web;

import com.gogidix.shared.testing.application.port.in.TestExecutionUseCase;
import com.gogidix.shared.testing.domain.model.TestSpecification;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * REST controller for test execution operations.
 * Provides HTTP endpoints for running tests, managing test suites, and accessing results.
 */
@RestController
@RequestMapping("/api/v1/testing")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Test Execution", description = "Test execution and management operations")
public class TestExecutionController {
    
    private final TestExecutionUseCase testExecutionUseCase;
    
    public TestExecutionController(TestExecutionUseCase testExecutionUseCase) {
        this.testExecutionUseCase = testExecutionUseCase;
    }
    
    @PostMapping("/execute")
    @Operation(summary = "Execute test", description = "Executes a single test specification synchronously")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Test executed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid test specification"),
        @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
        @ApiResponse(responseCode = "422", description = "Test validation failed")
    })
    public ResponseEntity<TestExecutionResponse> executeTest(@Valid @RequestBody ExecuteTestRequestDto request) {
        
        TestExecutionUseCase.ExecuteTestRequest executeRequest = new TestExecutionUseCase.ExecuteTestRequest(
            request.getTestSpecification(),
            request.getParameters(),
            request.getExecutedBy(),
            request.isDryRun(),
            request.getTimeoutOverrideSeconds(),
            request.getEnvironmentVariables(),
            request.getTags()
        );
        
        TestExecutionUseCase.TestExecutionResult result = testExecutionUseCase.executeTest(executeRequest);
        
        return ResponseEntity.ok(TestExecutionResponse.from(result));
    }
    
    @PostMapping("/execute-async")
    @Operation(summary = "Execute test asynchronously", description = "Executes a test specification asynchronously")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "202", description = "Test queued for execution"),
        @ApiResponse(responseCode = "400", description = "Invalid test specification")
    })
    public ResponseEntity<AsyncTestExecutionResponse> executeTestAsync(@Valid @RequestBody ExecuteTestRequestDto request) {
        
        TestExecutionUseCase.ExecuteTestRequest executeRequest = new TestExecutionUseCase.ExecuteTestRequest(
            request.getTestSpecification(),
            request.getParameters(),
            request.getExecutedBy(),
            request.isDryRun(),
            request.getTimeoutOverrideSeconds(),
            request.getEnvironmentVariables(),
            request.getTags()
        );
        
        CompletableFuture<TestExecutionUseCase.TestExecutionResult> future = 
            testExecutionUseCase.executeTestAsync(executeRequest);
        
        // Mock execution ID for response
        String executionId = "async-" + System.currentTimeMillis();
        
        return ResponseEntity.accepted().body(new AsyncTestExecutionResponse(
            executionId, 
            "Test queued for execution",
            "/api/v1/testing/executions/" + executionId
        ));
    }
    
    @PostMapping("/execute-batch/parallel")
    @Operation(summary = "Execute tests in parallel", description = "Executes multiple tests simultaneously")
    @PreAuthorize("hasAuthority('TEST_BATCH_EXECUTE')")
    public ResponseEntity<BatchTestExecutionResponse> executeTestsInParallel(@Valid @RequestBody BatchExecuteTestRequestDto request) {
        
        List<TestExecutionUseCase.ExecuteTestRequest> executeRequests = request.getRequests().stream()
            .map(dto -> new TestExecutionUseCase.ExecuteTestRequest(
                dto.getTestSpecification(), dto.getParameters(), dto.getExecutedBy(),
                dto.isDryRun(), dto.getTimeoutOverrideSeconds(), dto.getEnvironmentVariables(), dto.getTags()))
            .toList();
        
        TestExecutionUseCase.BatchTestExecutionResult result = 
            testExecutionUseCase.executeTestsInParallel(executeRequests);
        
        return ResponseEntity.ok(BatchTestExecutionResponse.from(result));
    }
    
    @PostMapping("/execute-batch/sequential")
    @Operation(summary = "Execute tests sequentially", description = "Executes multiple tests one after another")
    @PreAuthorize("hasAuthority('TEST_BATCH_EXECUTE')")
    public ResponseEntity<BatchTestExecutionResponse> executeTestsSequentially(@Valid @RequestBody BatchExecuteTestRequestDto request) {
        
        List<TestExecutionUseCase.ExecuteTestRequest> executeRequests = request.getRequests().stream()
            .map(dto -> new TestExecutionUseCase.ExecuteTestRequest(
                dto.getTestSpecification(), dto.getParameters(), dto.getExecutedBy(),
                dto.isDryRun(), dto.getTimeoutOverrideSeconds(), dto.getEnvironmentVariables(), dto.getTags()))
            .toList();
        
        TestExecutionUseCase.BatchTestExecutionResult result = 
            testExecutionUseCase.executeTestsSequentially(executeRequests);
        
        return ResponseEntity.ok(BatchTestExecutionResponse.from(result));
    }
    
    @PostMapping("/execute-suite")
    @Operation(summary = "Execute test suite", description = "Executes a complete test suite")
    @PreAuthorize("hasAuthority('TEST_SUITE_EXECUTE')")
    public ResponseEntity<TestSuiteExecutionResponse> executeTestSuite(@Valid @RequestBody ExecuteTestSuiteRequestDto request) {
        
        TestExecutionUseCase.ExecuteTestSuiteRequest suiteRequest = new TestExecutionUseCase.ExecuteTestSuiteRequest(
            request.getSuiteName(),
            request.getTests(),
            request.getSuiteParameters(),
            request.getExecutedBy(),
            request.isStopOnFirstFailure(),
            request.isParallelExecution(),
            request.getMaxParallelTests(),
            request.getEnvironmentVariables()
        );
        
        TestExecutionUseCase.TestSuiteExecutionResult result = testExecutionUseCase.executeTestSuite(suiteRequest);
        
        return ResponseEntity.ok(TestSuiteExecutionResponse.from(result));
    }
    
    @GetMapping("/executions/{executionId}")
    @Operation(summary = "Get test execution", description = "Retrieves details of a specific test execution")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Execution found"),
        @ApiResponse(responseCode = "404", description = "Execution not found")
    })
    public ResponseEntity<TestExecutionResponse> getTestExecution(@PathVariable String executionId) {
        
        return testExecutionUseCase.getTestExecution(executionId)
            .map(execution -> ResponseEntity.ok(TestExecutionResponse.from(execution)))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/tests/{testId}/executions")
    @Operation(summary = "Get test execution history", description = "Gets execution history for a test")
    @PreAuthorize("hasAuthority('TEST_READ_HISTORY')")
    public ResponseEntity<List<TestExecutionResponse>> getExecutionsForTest(
            @PathVariable String testId,
            @RequestParam(defaultValue = "50") int limit) {
        
        List<TestExecutionUseCase.TestExecutionResult> executions = 
            testExecutionUseCase.getExecutionsForTest(testId, limit);
        
        List<TestExecutionResponse> response = executions.stream()
            .map(TestExecutionResponse::from)
            .toList();
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/validate")
    @Operation(summary = "Validate test specification", description = "Validates a test specification before execution")
    public ResponseEntity<ValidationResponse> validateTestSpecification(@Valid @RequestBody ValidateTestRequestDto request) {
        
        TestExecutionUseCase.ValidationResult result = 
            testExecutionUseCase.validateTestSpecification(request.getTestSpecification());
        
        return ResponseEntity.ok(ValidationResponse.from(result));
    }
    
    @PostMapping("/validate-batch")
    @Operation(summary = "Validate multiple test specifications", description = "Validates multiple test specifications")
    @PreAuthorize("hasAuthority('TEST_BATCH_VALIDATE')")
    public ResponseEntity<BatchValidationResponse> validateTestSpecifications(@Valid @RequestBody ValidateTestsRequestDto request) {
        
        TestExecutionUseCase.BatchValidationResult result = 
            testExecutionUseCase.validateTestSpecifications(request.getTestSpecifications());
        
        return ResponseEntity.ok(BatchValidationResponse.from(result));
    }
    
    @PostMapping("/executions/{executionId}/cancel")
    @Operation(summary = "Cancel test execution", description = "Cancels a running test execution")
    @PreAuthorize("hasAuthority('TEST_CANCEL')")
    public ResponseEntity<CancellationResponse> cancelTestExecution(
            @PathVariable String executionId,
            @Valid @RequestBody CancelTestRequestDto request) {
        
        TestExecutionUseCase.CancellationResult result = 
            testExecutionUseCase.cancelTestExecution(executionId, request.getReason());
        
        return ResponseEntity.ok(CancellationResponse.from(result));
    }
    
    @PostMapping("/executions/{executionId}/retry")
    @Operation(summary = "Retry test execution", description = "Retries a failed test execution")
    @PreAuthorize("hasAuthority('TEST_RETRY')")
    public ResponseEntity<TestExecutionResponse> retryTestExecution(@PathVariable String executionId) {
        
        TestExecutionUseCase.TestExecutionResult result = testExecutionUseCase.retryTestExecution(executionId);
        
        return ResponseEntity.ok(TestExecutionResponse.from(result));
    }
    
    @GetMapping("/statistics")
    @Operation(summary = "Get test execution statistics", description = "Gets test execution statistics")
    @PreAuthorize("hasAuthority('TEST_READ_STATS')")
    public ResponseEntity<TestExecutionStatisticsResponse> getExecutionStatistics(
            @RequestParam(required = false) String testId,
            @RequestParam(required = false) String suiteId,
            @RequestParam(required = false) LocalDateTime since) {
        
        TestExecutionUseCase.StatisticsRequest request = new TestExecutionUseCase.StatisticsRequest();
        TestExecutionUseCase.TestExecutionStatistics statistics = testExecutionUseCase.getExecutionStatistics(request);
        
        return ResponseEntity.ok(TestExecutionStatisticsResponse.from(statistics));
    }
    
    @GetMapping("/environment/status")
    @Operation(summary = "Get test environment status", description = "Gets current test environment status")
    @PreAuthorize("hasAuthority('TEST_READ_ENV_STATUS')")
    public ResponseEntity<TestEnvironmentStatusResponse> getEnvironmentStatus() {
        
        TestExecutionUseCase.TestEnvironmentStatus status = testExecutionUseCase.getEnvironmentStatus();
        
        return ResponseEntity.ok(TestEnvironmentStatusResponse.from(status));
    }
    
    // Request DTOs
    
    public static class ExecuteTestRequestDto {
        @NotNull private TestSpecification testSpecification;
        private Map<String, Object> parameters;
        @NotNull private String executedBy;
        private boolean dryRun;
        private int timeoutOverrideSeconds;
        private Map<String, String> environmentVariables;
        private List<String> tags;
        
        // Getters and setters
        public TestSpecification getTestSpecification() { return testSpecification; }
        public void setTestSpecification(TestSpecification testSpecification) { this.testSpecification = testSpecification; }
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
        public String getExecutedBy() { return executedBy; }
        public void setExecutedBy(String executedBy) { this.executedBy = executedBy; }
        public boolean isDryRun() { return dryRun; }
        public void setDryRun(boolean dryRun) { this.dryRun = dryRun; }
        public int getTimeoutOverrideSeconds() { return timeoutOverrideSeconds; }
        public void setTimeoutOverrideSeconds(int timeoutOverrideSeconds) { this.timeoutOverrideSeconds = timeoutOverrideSeconds; }
        public Map<String, String> getEnvironmentVariables() { return environmentVariables; }
        public void setEnvironmentVariables(Map<String, String> environmentVariables) { this.environmentVariables = environmentVariables; }
        public List<String> getTags() { return tags; }
        public void setTags(List<String> tags) { this.tags = tags; }
    }
    
    public static class BatchExecuteTestRequestDto {
        @NotNull private List<ExecuteTestRequestDto> requests;
        
        public List<ExecuteTestRequestDto> getRequests() { return requests; }
        public void setRequests(List<ExecuteTestRequestDto> requests) { this.requests = requests; }
    }
    
    public static class ExecuteTestSuiteRequestDto {
        @NotNull private String suiteName;
        @NotNull private List<TestSpecification> tests;
        private Map<String, Object> suiteParameters;
        @NotNull private String executedBy;
        private boolean stopOnFirstFailure;
        private boolean parallelExecution;
        private int maxParallelTests = 5;
        private Map<String, String> environmentVariables;
        
        // Getters and setters
        public String getSuiteName() { return suiteName; }
        public void setSuiteName(String suiteName) { this.suiteName = suiteName; }
        public List<TestSpecification> getTests() { return tests; }
        public void setTests(List<TestSpecification> tests) { this.tests = tests; }
        public Map<String, Object> getSuiteParameters() { return suiteParameters; }
        public void setSuiteParameters(Map<String, Object> suiteParameters) { this.suiteParameters = suiteParameters; }
        public String getExecutedBy() { return executedBy; }
        public void setExecutedBy(String executedBy) { this.executedBy = executedBy; }
        public boolean isStopOnFirstFailure() { return stopOnFirstFailure; }
        public void setStopOnFirstFailure(boolean stopOnFirstFailure) { this.stopOnFirstFailure = stopOnFirstFailure; }
        public boolean isParallelExecution() { return parallelExecution; }
        public void setParallelExecution(boolean parallelExecution) { this.parallelExecution = parallelExecution; }
        public int getMaxParallelTests() { return maxParallelTests; }
        public void setMaxParallelTests(int maxParallelTests) { this.maxParallelTests = maxParallelTests; }
        public Map<String, String> getEnvironmentVariables() { return environmentVariables; }
        public void setEnvironmentVariables(Map<String, String> environmentVariables) { this.environmentVariables = environmentVariables; }
    }
    
    public static class ValidateTestRequestDto {
        @NotNull private TestSpecification testSpecification;
        
        public TestSpecification getTestSpecification() { return testSpecification; }
        public void setTestSpecification(TestSpecification testSpecification) { this.testSpecification = testSpecification; }
    }
    
    public static class ValidateTestsRequestDto {
        @NotNull private List<TestSpecification> testSpecifications;
        
        public List<TestSpecification> getTestSpecifications() { return testSpecifications; }
        public void setTestSpecifications(List<TestSpecification> testSpecifications) { this.testSpecifications = testSpecifications; }
    }
    
    public static class CancelTestRequestDto {
        @NotNull private String reason;
        
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }
    
    // Response DTOs
    
    public static class TestExecutionResponse {
        private String executionId;
        private String testName;
        private String status;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private long executionTimeMs;
        private int passedSteps;
        private int failedSteps;
        private int passedAssertions;
        private int failedAssertions;
        private String errorMessage;
        private boolean dryRun;
        
        public static TestExecutionResponse from(TestExecutionUseCase.TestExecutionResult result) {
            TestExecutionResponse response = new TestExecutionResponse();
            response.executionId = result.getExecutionId();
            response.testName = result.getTestSpecification().getName();
            response.status = result.getStatus().name();
            response.startTime = result.getStartTime();
            response.endTime = result.getEndTime();
            response.executionTimeMs = result.getExecutionTimeMs();
            response.passedSteps = result.getPassedStepsCount();
            response.failedSteps = result.getFailedStepsCount();
            response.passedAssertions = result.getPassedAssertionsCount();
            response.failedAssertions = result.getFailedAssertionsCount();
            response.errorMessage = result.getErrorMessage();
            response.dryRun = result.isDryRun();
            return response;
        }
        
        // Getters
        public String getExecutionId() { return executionId; }
        public String getTestName() { return testName; }
        public String getStatus() { return status; }
        public LocalDateTime getStartTime() { return startTime; }
        public LocalDateTime getEndTime() { return endTime; }
        public long getExecutionTimeMs() { return executionTimeMs; }
        public int getPassedSteps() { return passedSteps; }
        public int getFailedSteps() { return failedSteps; }
        public int getPassedAssertions() { return passedAssertions; }
        public int getFailedAssertions() { return failedAssertions; }
        public String getErrorMessage() { return errorMessage; }
        public boolean isDryRun() { return dryRun; }
    }
    
    public static class AsyncTestExecutionResponse {
        private String executionId;
        private String message;
        private String statusUrl;
        
        public AsyncTestExecutionResponse(String executionId, String message, String statusUrl) {
            this.executionId = executionId;
            this.message = message;
            this.statusUrl = statusUrl;
        }
        
        public String getExecutionId() { return executionId; }
        public String getMessage() { return message; }
        public String getStatusUrl() { return statusUrl; }
    }
    
    public static class BatchTestExecutionResponse {
        private int totalTests;
        private int passedTests;
        private int failedTests;
        private int skippedTests;
        private double successRate;
        private long totalExecutionTimeMs;
        private List<TestExecutionResponse> results;
        
        public static BatchTestExecutionResponse from(TestExecutionUseCase.BatchTestExecutionResult result) {
            BatchTestExecutionResponse response = new BatchTestExecutionResponse();
            response.totalTests = result.getTotalTests();
            response.passedTests = result.getPassedTests();
            response.failedTests = result.getFailedTests();
            response.skippedTests = result.getSkippedTests();
            response.successRate = result.getSuccessRate();
            response.totalExecutionTimeMs = result.getTotalExecutionTimeMs();
            response.results = result.getResults().stream()
                .map(TestExecutionResponse::from)
                .toList();
            return response;
        }
        
        // Getters
        public int getTotalTests() { return totalTests; }
        public int getPassedTests() { return passedTests; }
        public int getFailedTests() { return failedTests; }
        public int getSkippedTests() { return skippedTests; }
        public double getSuccessRate() { return successRate; }
        public long getTotalExecutionTimeMs() { return totalExecutionTimeMs; }
        public List<TestExecutionResponse> getResults() { return results; }
    }
    
    public static class TestSuiteExecutionResponse {
        // Implementation similar to BatchTestExecutionResponse
        public static TestSuiteExecutionResponse from(TestExecutionUseCase.TestSuiteExecutionResult result) {
            return new TestSuiteExecutionResponse();
        }
    }
    
    public static class ValidationResponse {
        private boolean valid;
        private List<String> errors;
        private List<String> warnings;
        
        public static ValidationResponse from(TestExecutionUseCase.ValidationResult result) {
            ValidationResponse response = new ValidationResponse();
            response.valid = result.isValid();
            response.errors = result.getErrors();
            response.warnings = result.getWarnings();
            return response;
        }
        
        public boolean isValid() { return valid; }
        public List<String> getErrors() { return errors; }
        public List<String> getWarnings() { return warnings; }
    }
    
    public static class BatchValidationResponse {
        private int totalCount;
        private int validCount;
        private int invalidCount;
        private List<ValidationResponse> results;
        
        public static BatchValidationResponse from(TestExecutionUseCase.BatchValidationResult result) {
            BatchValidationResponse response = new BatchValidationResponse();
            response.totalCount = result.getTotalCount();
            response.validCount = result.getValidCount();
            response.invalidCount = result.getInvalidCount();
            response.results = result.getResults().stream()
                .map(ValidationResponse::from)
                .toList();
            return response;
        }
        
        public int getTotalCount() { return totalCount; }
        public int getValidCount() { return validCount; }
        public int getInvalidCount() { return invalidCount; }
        public List<ValidationResponse> getResults() { return results; }
    }
    
    public static class CancellationResponse {
        private boolean success;
        private String message;
        
        public static CancellationResponse from(TestExecutionUseCase.CancellationResult result) {
            CancellationResponse response = new CancellationResponse();
            response.success = result.isSuccess();
            response.message = result.getMessage();
            return response;
        }
        
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }
    
    public static class TestExecutionStatisticsResponse {
        // Implementation for statistics response
        public static TestExecutionStatisticsResponse from(TestExecutionUseCase.TestExecutionStatistics statistics) {
            return new TestExecutionStatisticsResponse();
        }
    }
    
    public static class TestEnvironmentStatusResponse {
        // Implementation for environment status response
        public static TestEnvironmentStatusResponse from(TestExecutionUseCase.TestEnvironmentStatus status) {
            return new TestEnvironmentStatusResponse();
        }
    }
}
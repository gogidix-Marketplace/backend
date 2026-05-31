package com.gogidix.shared.testing.application.service;

import com.gogidix.shared.testing.application.port.in.TestExecutionUseCase;
import com.gogidix.shared.testing.domain.model.TestSpecification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Application service implementing test execution operations.
 * Orchestrates test execution, manages test lifecycle, and handles results.
 */
@Service
public class TestExecutionService implements TestExecutionUseCase {
    
    private final Map<String, TestExecutionResult> executionResults = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    
    @Override
    public TestExecutionResult executeTest(ExecuteTestRequest request) {
        String executionId = generateExecutionId();
        LocalDateTime startTime = LocalDateTime.now();
        
        try {
            // Validate test specification
            ValidationResult validation = validateTestSpecification(request.getTestSpecification());
            if (!validation.isValid()) {
                return createFailedResult(executionId, request, startTime, 
                    "Test validation failed: " + String.join(", ", validation.getErrors()));
            }
            
            // Set up test environment
            Map<String, Object> testContext = initializeTestContext(request);
            
            // Execute test steps
            List<TestStepResult> stepResults = executeTestSteps(request.getTestSpecification(), testContext);
            
            // Execute assertions
            List<AssertionResult> assertionResults = executeAssertions(request.getTestSpecification(), testContext);
            
            // Determine overall result
            TestExecutionStatus status = determineExecutionStatus(stepResults, assertionResults);
            
            LocalDateTime endTime = LocalDateTime.now();
            long executionTimeMs = java.time.Duration.between(startTime, endTime).toMillis();
            
            TestExecutionResult result = new TestExecutionResult(
                executionId, request.getTestSpecification(), status, startTime, endTime,
                executionTimeMs, stepResults, assertionResults, null, null,
                testContext, request.getExecutedBy(), request.isDryRun()
            );
            
            executionResults.put(executionId, result);
            return result;
            
        } catch (Exception e) {
            return createFailedResult(executionId, request, startTime, "Test execution failed: " + e.getMessage(), e);
        }
    }
    
    @Override
    public CompletableFuture<TestExecutionResult> executeTestAsync(ExecuteTestRequest request) {
        return CompletableFuture.supplyAsync(() -> executeTest(request), executorService);
    }
    
    @Override
    public BatchTestExecutionResult executeTestsInParallel(List<ExecuteTestRequest> requests) {
        LocalDateTime startTime = LocalDateTime.now();
        
        List<CompletableFuture<TestExecutionResult>> futures = requests.stream()
                .map(this::executeTestAsync)
                .collect(Collectors.toList());
        
        List<TestExecutionResult> results = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
        
        LocalDateTime endTime = LocalDateTime.now();
        long totalExecutionTimeMs = java.time.Duration.between(startTime, endTime).toMillis();
        
        return new BatchTestExecutionResult(results, startTime, endTime, totalExecutionTimeMs);
    }
    
    @Override
    public BatchTestExecutionResult executeTestsSequentially(List<ExecuteTestRequest> requests) {
        LocalDateTime startTime = LocalDateTime.now();
        
        List<TestExecutionResult> results = requests.stream()
                .map(this::executeTest)
                .collect(Collectors.toList());
        
        LocalDateTime endTime = LocalDateTime.now();
        long totalExecutionTimeMs = java.time.Duration.between(startTime, endTime).toMillis();
        
        return new BatchTestExecutionResult(results, startTime, endTime, totalExecutionTimeMs);
    }
    
    @Override
    public TestSuiteExecutionResult executeTestSuite(ExecuteTestSuiteRequest request) {
        // Create individual test requests
        List<ExecuteTestRequest> testRequests = request.getTests().stream()
                .map(spec -> new ExecuteTestRequest(
                    spec, request.getSuiteParameters(), request.getExecutedBy(),
                    false, 0, request.getEnvironmentVariables(), List.of()))
                .collect(Collectors.toList());
        
        // Execute based on parallel preference
        BatchTestExecutionResult batchResult = request.isParallelExecution() ?
                executeTestsInParallel(testRequests) : executeTestsSequentially(testRequests);
        
        // Convert to suite result (simplified implementation)
        return new TestSuiteExecutionResult();
    }
    
    @Override
    public Optional<TestExecutionResult> getTestExecution(String executionId) {
        return Optional.ofNullable(executionResults.get(executionId));
    }
    
    @Override
    public List<TestExecutionResult> getExecutionsForTest(String testSpecificationId, int limit) {
        return executionResults.values().stream()
                .filter(result -> result.getTestSpecification().getId().toString().equals(testSpecificationId))
                .sorted((a, b) -> b.getStartTime().compareTo(a.getStartTime()))
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TestExecutionResult> getExecutionHistory(String testSpecificationId, LocalDateTime since) {
        return executionResults.values().stream()
                .filter(result -> result.getTestSpecification().getId().toString().equals(testSpecificationId))
                .filter(result -> result.getStartTime().isAfter(since))
                .sorted((a, b) -> b.getStartTime().compareTo(a.getStartTime()))
                .collect(Collectors.toList());
    }
    
    @Override
    public CancellationResult cancelTestExecution(String executionId, String reason) {
        TestExecutionResult result = executionResults.get(executionId);
        if (result == null) {
            return new CancellationResult(false, "Execution not found: " + executionId);
        }
        
        if (!result.isRunning()) {
            return new CancellationResult(false, "Execution is not running: " + result.getStatus());
        }
        
        // In a real implementation, would cancel the running execution
        return new CancellationResult(true, "Execution cancelled: " + reason);
    }
    
    @Override
    public TestExecutionResult retryTestExecution(String executionId) {
        TestExecutionResult originalResult = executionResults.get(executionId);
        if (originalResult == null) {
            throw new IllegalArgumentException("Execution not found: " + executionId);
        }
        
        // Create retry request from original
        ExecuteTestRequest retryRequest = new ExecuteTestRequest(
            originalResult.getTestSpecification(),
            originalResult.getExecutionData(),
            originalResult.getExecutedBy(),
            originalResult.isDryRun(),
            0, Map.of(), List.of()
        );
        
        return executeTest(retryRequest);
    }
    
    @Override
    public ValidationResult validateTestSpecification(TestSpecification specification) {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        // Basic validation
        if (specification == null) {
            errors.add("Test specification cannot be null");
            return new ValidationResult(false, errors, warnings);
        }
        
        if (!specification.isExecutable()) {
            errors.add("Test specification is not executable");
        }
        
        // Validate steps
        if (specification.getSteps() != null) {
            for (int i = 0; i < specification.getSteps().size(); i++) {
                var step = specification.getSteps().get(i);
                if (!step.isValid()) {
                    errors.add("Step " + i + " (" + step.getName() + ") is invalid");
                }
            }
        }
        
        // Validate assertions
        if (specification.getAssertions() != null) {
            for (int i = 0; i < specification.getAssertions().size(); i++) {
                var assertion = specification.getAssertions().get(i);
                if (!assertion.isValid()) {
                    errors.add("Assertion " + i + " (" + assertion.getName() + ") is invalid");
                }
            }
        }
        
        // Performance warnings
        if (specification.getComplexityScore() > 8) {
            warnings.add("Test has high complexity score: " + specification.getComplexityScore());
        }
        
        if (specification.getEstimatedExecutionTimeSeconds() > 300) {
            warnings.add("Test has long estimated execution time: " + specification.getEstimatedExecutionTimeSeconds() + "s");
        }
        
        return new ValidationResult(errors.isEmpty(), errors, warnings);
    }
    
    @Override
    public BatchValidationResult validateTestSpecifications(List<TestSpecification> specifications) {
        List<ValidationResult> results = specifications.stream()
                .map(this::validateTestSpecification)
                .collect(Collectors.toList());
        
        return new BatchValidationResult(results);
    }
    
    @Override
    public TestExecutionStatistics getExecutionStatistics(StatisticsRequest request) {
        // Implementation for statistics calculation
        return new TestExecutionStatistics();
    }
    
    @Override
    public TestEnvironmentStatus getEnvironmentStatus() {
        // Implementation for environment status check
        return new TestEnvironmentStatus();
    }
    
    @Override
    public TestDataResult createTestData(TestDataRequest request) {
        // Implementation for test data creation
        return new TestDataResult();
    }
    
    @Override
    public CleanupResult cleanupTestData(String executionId) {
        // Implementation for test data cleanup
        return new CleanupResult();
    }
    
    // Helper methods
    
    private String generateExecutionId() {
        return "exec_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);
    }
    
    private Map<String, Object> initializeTestContext(ExecuteTestRequest request) {
        Map<String, Object> context = new HashMap<>();
        
        // Add request parameters
        if (request.getParameters() != null) {
            context.putAll(request.getParameters());
        }
        
        // Add environment variables
        if (request.getEnvironmentVariables() != null) {
            context.put("env", request.getEnvironmentVariables());
        }
        
        // Add execution metadata
        context.put("executionId", generateExecutionId());
        context.put("executedBy", request.getExecutedBy());
        context.put("startTime", LocalDateTime.now());
        context.put("dryRun", request.isDryRun());
        
        return context;
    }
    
    private List<TestStepResult> executeTestSteps(TestSpecification specification, Map<String, Object> context) {
        if (specification.getSteps() == null) {
            return List.of();
        }
        
        List<TestStepResult> stepResults = new ArrayList<>();
        
        for (var step : specification.getSteps()) {
            LocalDateTime stepStartTime = LocalDateTime.now();
            
            try {
                // Skip step if conditions are met
                if (step.shouldSkip(context)) {
                    stepResults.add(new TestStepResult(
                        step.getName(), TestStepExecutionStatus.SKIPPED,
                        stepStartTime, LocalDateTime.now(), 0, "Step skipped", Map.of()
                    ));
                    continue;
                }
                
                // Execute step (simplified implementation)
                Map<String, Object> stepData = executeStep(step, context);
                
                LocalDateTime stepEndTime = LocalDateTime.now();
                long stepExecutionTimeMs = java.time.Duration.between(stepStartTime, stepEndTime).toMillis();
                
                stepResults.add(new TestStepResult(
                    step.getName(), TestStepExecutionStatus.PASSED,
                    stepStartTime, stepEndTime, stepExecutionTimeMs, null, stepData
                ));
                
            } catch (Exception e) {
                LocalDateTime stepEndTime = LocalDateTime.now();
                long stepExecutionTimeMs = java.time.Duration.between(stepStartTime, stepEndTime).toMillis();
                
                stepResults.add(new TestStepResult(
                    step.getName(), TestStepExecutionStatus.FAILED,
                    stepStartTime, stepEndTime, stepExecutionTimeMs, e.getMessage(), Map.of()
                ));
            }
        }
        
        return stepResults;
    }
    
    private Map<String, Object> executeStep(com.gogidix.shared.testing.domain.model.TestStep step, Map<String, Object> context) {
        // Simplified step execution - in real implementation would handle different step types
        Map<String, Object> stepData = new HashMap<>();
        stepData.put("executed", true);
        stepData.put("stepType", step.getStepType().name());
        
        // Simulate step execution time
        try {
            Thread.sleep(100); // Simulate work
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return stepData;
    }
    
    private List<AssertionResult> executeAssertions(TestSpecification specification, Map<String, Object> context) {
        if (specification.getAssertions() == null) {
            return List.of();
        }
        
        List<AssertionResult> assertionResults = new ArrayList<>();
        
        for (var assertion : specification.getAssertions()) {
            try {
                // Get actual value from context (simplified)
                Object actualValue = context.get(assertion.getTarget());
                
                // Execute assertion
                var executedAssertion = assertion.executeAssertion(actualValue);
                var result = executedAssertion.getResult();
                
                assertionResults.add(new AssertionResult(
                    assertion.getName(), result.isPassed(), result.getMessage(),
                    actualValue, assertion.getExpectedValue()
                ));
                
            } catch (Exception e) {
                assertionResults.add(new AssertionResult(
                    assertion.getName(), false, "Assertion execution failed: " + e.getMessage(),
                    null, assertion.getExpectedValue()
                ));
            }
        }
        
        return assertionResults;
    }
    
    private TestExecutionStatus determineExecutionStatus(List<TestStepResult> stepResults, List<AssertionResult> assertionResults) {
        // Check for failed steps
        boolean hasFailedSteps = stepResults.stream()
                .anyMatch(result -> result.getStatus() == TestStepExecutionStatus.FAILED);
        
        if (hasFailedSteps) {
            return TestExecutionStatus.FAILED;
        }
        
        // Check for failed assertions
        boolean hasFailedAssertions = assertionResults.stream()
                .anyMatch(result -> !result.isPassed());
        
        if (hasFailedAssertions) {
            return TestExecutionStatus.FAILED;
        }
        
        // Check if all steps were skipped
        boolean allStepsSkipped = stepResults.stream()
                .allMatch(result -> result.getStatus() == TestStepExecutionStatus.SKIPPED);
        
        if (allStepsSkipped && !stepResults.isEmpty()) {
            return TestExecutionStatus.SKIPPED;
        }
        
        return TestExecutionStatus.PASSED;
    }
    
    private TestExecutionResult createFailedResult(String executionId, ExecuteTestRequest request,
                                                  LocalDateTime startTime, String errorMessage) {
        return createFailedResult(executionId, request, startTime, errorMessage, null);
    }
    
    private TestExecutionResult createFailedResult(String executionId, ExecuteTestRequest request,
                                                  LocalDateTime startTime, String errorMessage, Exception error) {
        LocalDateTime endTime = LocalDateTime.now();
        long executionTimeMs = java.time.Duration.between(startTime, endTime).toMillis();
        
        return new TestExecutionResult(
            executionId, request.getTestSpecification(), TestExecutionStatus.FAILED,
            startTime, endTime, executionTimeMs, List.of(), List.of(),
            errorMessage, error, Map.of(), request.getExecutedBy(), request.isDryRun()
        );
    }
}
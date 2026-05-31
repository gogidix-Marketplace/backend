package com.gogidix.foundation.devtools.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.foundation.devtools.domain.entity.ApiTestCase;
import com.gogidix.foundation.devtools.domain.entity.ApiTestExecution;
import com.gogidix.foundation.devtools.domain.repository.ApiTestCaseRepository;
import com.gogidix.foundation.devtools.domain.repository.ApiTestExecutionRepository;
import com.gogidix.foundation.devtools.dto.ApiTestCaseDto;
import com.gogidix.foundation.devtools.dto.ApiTestExecutionDto;
import com.gogidix.foundation.devtools.dto.ApiTestRequest;
import com.gogidix.foundation.devtools.dto.ApiTestResult;
import com.gogidix.foundation.devtools.exception.ApiTestException;
import com.gogidix.foundation.devtools.mapper.ApiTestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Service for API testing functionality.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ApiTestingService {

    private final ApiTestCaseRepository testCaseRepository;
    private final ApiTestExecutionRepository executionRepository;
    private final ApiTestMapper mapper;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ScriptEngineManager scriptEngineManager = new ScriptEngineManager();

    @Value("${devtools.api-testing.timeout:30000}")
    private int defaultTimeout;

    @Value("${devtools.api-testing.max-concurrent:10}")
    private int maxConcurrentTests;

    private final Map<String, CompletableFuture<ApiTestResult>> runningTests = new HashMap<>();

    /**
     * Create a new API test case.
     */
    @Transactional
    public ApiTestCaseDto createTestCase(ApiTestCaseDto dto) {
        log.info("Creating API test case: {}", dto.getName());

        ApiTestCase entity = mapper.toEntity(dto);
        entity.setUuid(UUID.randomUUID());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        entity = testCaseRepository.save(entity);
        log.info("Created API test case with UUID: {}", entity.getUuid());

        return mapper.toDto(entity);
    }

    /**
     * Update an existing API test case.
     */
    @Transactional
    @CacheEvict(value = "apiTestCases", key = "#uuid")
    public ApiTestCaseDto updateTestCase(UUID uuid, ApiTestCaseDto dto) {
        log.info("Updating API test case: {}", uuid);

        ApiTestCase entity = testCaseRepository.findByUuid(uuid)
                .orElseThrow(() -> new ApiTestException("Test case not found: " + uuid));

        mapper.updateEntityFromDto(dto, entity);
        entity.setUpdatedAt(LocalDateTime.now());

        entity = testCaseRepository.save(entity);
        return mapper.toDto(entity);
    }

    /**
     * Get an API test case by UUID.
     */
    @Cacheable(value = "apiTestCases", key = "#uuid")
    @Transactional(readOnly = true)
    public ApiTestCaseDto getTestCase(UUID uuid) {
        ApiTestCase entity = testCaseRepository.findByUuid(uuid)
                .orElseThrow(() -> new ApiTestException("Test case not found: " + uuid));
        return mapper.toDto(entity);
    }

    /**
     * Get all test cases for a project.
     */
    @Transactional(readOnly = true)
    public Page<ApiTestCaseDto> getTestCasesByProject(String projectId, Pageable pageable) {
        return testCaseRepository.findByProjectId(projectId, pageable)
                .map(mapper::toDto);
    }

    /**
     * Delete an API test case.
     */
    @Transactional
    @CacheEvict(value = "apiTestCases", key = "#uuid")
    public void deleteTestCase(UUID uuid) {
        log.info("Deleting API test case: {}", uuid);
        testCaseRepository.findByUuid(uuid).orElseThrow(() -> new ApiTestException("Test case not found: " + uuid)); testCaseRepository.deleteById(testCaseRepository.findByUuid(uuid).get().getId());
    }

    /**
     * Execute an API test case.
     */
    @Async
    @Transactional
    public CompletableFuture<ApiTestResult> executeTestCase(Long testCaseId, String executedBy) {
        ApiTestCase testCase = testCaseRepository.findById(testCaseId)
                .orElseThrow(() -> new ApiTestException("Test case not found: " + testCaseId));

        return executeApiTest(testCase, executedBy);
    }

    /**
     * Execute an ad-hoc API test.
     */
    @Async
    @Transactional
    public CompletableFuture<ApiTestResult> executeAdHocTest(ApiTestRequest request, String executedBy) {
        ApiTestCase adHocTestCase = ApiTestCase.builder()
                .uuid(UUID.randomUUID())
                .name(request.getName())
                .method(request.getMethod())
                .url(request.getUrl())
                .headers(request.getHeaders() != null ? request.getHeaders().toString() : null)
                .requestBody(request.getBody())
                .expectedStatusCode(request.getExpectedStatusCode())
                .timeout(request.getTimeout() != null ? request.getTimeout() : defaultTimeout)
                .build();

        return executeApiTest(adHocTestCase, executedBy);
    }

    /**
     * Execute a batch of API tests.
     */
    public List<ApiTestResult> executeBatch(List<Long> testCaseIds, String executedBy) {
        List<ApiTestResult> results = new ArrayList<>();

        for (Long testCaseId : testCaseIds) {
            try {
                ApiTestResult result = executeTestCase(testCaseId, executedBy).get();
                results.add(result);
            } catch (Exception e) {
                log.error("Error executing test case: {}", testCaseId, e);
                results.add(ApiTestResult.builder()
                        .status("ERROR")
                        .errorMessage(e.getMessage())
                        .build());
            }
        }

        return results;
    }

    /**
     * Internal method to execute an API test.
     */
    private CompletableFuture<ApiTestResult> executeApiTest(ApiTestCase testCase, String executedBy) {
        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            ApiTestResult.ApiTestResultBuilder resultBuilder = ApiTestResult.builder()
                    .testCaseUuid(testCase.getUuid())
                    .testCaseName(testCase.getName())
                    .method(testCase.getMethod())
                    .url(testCase.getUrl());

            ApiTestExecution.ApiTestExecutionBuilder executionBuilder = ApiTestExecution.builder()
                    .uuid(UUID.randomUUID())
                    .testCaseId(testCase.getId())
                    .executedBy(executedBy)
                    .executedAt(LocalDateTime.now())
                    .environment(testCase.getEnvironment());

            try {
                log.info("Executing API test: {} - {} {}", testCase.getName(), testCase.getMethod(), testCase.getUrl());

                // Parse headers
                Map<String, String> headers = parseHeaders(testCase.getHeaders());

                // Create request entity
                org.springframework.http.HttpHeaders httpHeaders = new org.springframework.http.HttpHeaders();
                if (headers != null) {
                    headers.forEach(httpHeaders::add);
                }

                org.springframework.http.HttpEntity<String> requestEntity =
                        new org.springframework.http.HttpEntity<>(testCase.getRequestBody(), httpHeaders);

                // Execute request
                ResponseEntity<String> response = restTemplate.exchange(
                        testCase.getUrl(),
                        HttpMethod.valueOf(testCase.getMethod()),
                        requestEntity,
                        String.class
                );

                long responseTime = System.currentTimeMillis() - startTime;

                // Build result
                resultBuilder.statusCode(response.getStatusCode().value())
                        .responseBody(response.getBody())
                        .responseTime(responseTime);

                executionBuilder.status("COMPLETED")
                        .actualStatusCode(response.getStatusCode().value())
                        .responseBody(truncateResponse(response.getBody()))
                        .responseTime(responseTime);

                // Validate response
                boolean statusMatch = response.getStatusCode().value() == testCase.getExpectedStatusCode();
                boolean bodyValid = validateResponseBody(testCase, response.getBody());

                resultBuilder.statusMatch(statusMatch)
                        .bodyValid(bodyValid)
                        .passed(statusMatch && bodyValid);

                if (testCase.getValidationScript() != null && !testCase.getValidationScript().isBlank()) {
                    boolean scriptValid = runValidationScript(testCase.getValidationScript(), response.getBody());
                    resultBuilder.scriptValid(scriptValid);
                    resultBuilder.passed(resultBuilder.build().getPassed() && scriptValid);
                }

                resultBuilder.status("PASSED");

            } catch (Exception e) {
                log.error("API test execution failed", e);
                long responseTime = System.currentTimeMillis() - startTime;

                resultBuilder.status("FAILED")
                        .errorMessage(e.getMessage())
                        .responseTime(responseTime);

                executionBuilder.status("FAILED")
                        .errorMessage(e.getMessage())
                        .responseTime(responseTime);
            }

            ApiTestResult result = resultBuilder.build();
            ApiTestExecution execution = executionBuilder.build();

            // Save execution
            executionRepository.save(execution);

            return result;
        });
    }

    /**
     * Get test execution history for a test case.
     */
    @Transactional(readOnly = true)
    public List<ApiTestExecutionDto> getExecutionHistory(Long testCaseId, Pageable pageable) {
        return executionRepository.findByTestCaseId(testCaseId, pageable)
                .stream()
                .map(mapper::toExecutionDto)
                .toList();
    }

    /**
     * Get test execution by UUID.
     */
    @Transactional(readOnly = true)
    public ApiTestExecutionDto getExecution(UUID uuid) {
        ApiTestExecution execution = executionRepository.findByUuid(uuid)
                .orElseThrow(() -> new ApiTestException("Execution not found: " + uuid));
        return mapper.toExecutionDto(execution);
    }

    /**
     * Get test statistics for a project.
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getTestStatistics(String projectId) {
        List<ApiTestCase> testCases = testCaseRepository.findByProjectId(projectId);

        long totalTests = testCases.size();
        long enabledTests = testCases.stream().filter(ApiTestCase::getEnabled).count();

        Map<String, Long> statusCounts = new HashMap<>();
        for (ApiTestCase tc : testCases) {
            Long passedCount = executionRepository.countByTestCaseIdAndStatus(tc.getId(), "PASSED");
            Long failedCount = executionRepository.countByTestCaseIdAndStatus(tc.getId(), "FAILED");

            statusCounts.put("passed", statusCounts.getOrDefault("passed", 0L) + passedCount);
            statusCounts.put("failed", statusCounts.getOrDefault("failed", 0L) + failedCount);
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalTests", totalTests);
        stats.put("enabledTests", enabledTests);
        stats.put("disabledTests", totalTests - enabledTests);
        stats.put("executions", statusCounts);

        return stats;
    }

    private Map<String, String> parseHeaders(String headersJson) {
        try {
            if (headersJson == null || headersJson.isBlank()) {
                return new HashMap<>();
            }
            return objectMapper.readValue(headersJson,
                    objectMapper.getTypeFactory().constructMapType(Map.class, String.class, String.class));
        } catch (Exception e) {
            log.warn("Failed to parse headers JSON", e);
            return new HashMap<>();
        }
    }

    private boolean validateResponseBody(ApiTestCase testCase, String responseBody) {
        if (testCase.getExpectedResponseBody() == null || testCase.getExpectedResponseBody().isBlank()) {
            return true;
        }

        try {
            JsonNode expected = objectMapper.readTree(testCase.getExpectedResponseBody());
            JsonNode actual = objectMapper.readTree(responseBody);
            return expected.equals(actual);
        } catch (Exception e) {
            log.warn("Failed to validate response body", e);
            return false;
        }
    }

    private boolean runValidationScript(String script, String responseBody) {
        try {
            ScriptEngine engine = scriptEngineManager.getEngineByName("js");
            if (engine == null) {
                log.warn("JavaScript engine not available");
                return true;
            }

            engine.put("response", responseBody);
            Object result = engine.eval(script);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            log.warn("Validation script execution failed", e);
            return false;
        }
    }

    private String truncateResponse(String response) {
        if (response == null) {
            return null;
        }
        if (response.length() > 10000) {
            return response.substring(0, 10000) + "... [truncated]";
        }
        return response;
    }
}

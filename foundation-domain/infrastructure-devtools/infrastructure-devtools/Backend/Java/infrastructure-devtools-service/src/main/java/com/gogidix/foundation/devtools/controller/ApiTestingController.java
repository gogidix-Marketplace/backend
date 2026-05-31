package com.gogidix.foundation.devtools.controller;

import com.gogidix.foundation.devtools.dto.ApiTestCaseDto;
import com.gogidix.foundation.devtools.dto.ApiTestExecutionDto;
import com.gogidix.foundation.devtools.dto.ApiTestRequest;
import com.gogidix.foundation.devtools.dto.ApiTestResult;
import com.gogidix.foundation.devtools.service.ApiTestingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * REST controller for API testing functionality.
 */
@RestController
@RequestMapping("/api-testing")
@RequiredArgsConstructor
@Tag(name = "API Testing", description = "API testing and validation endpoints")
@CrossOrigin(origins = "${cors.allowed-origins:*}")
public class ApiTestingController {

    private final ApiTestingService apiTestingService;

    @PostMapping("/test-cases")
    @Operation(summary = "Create a new API test case")
    @PreAuthorize("hasRole('DEVELOPER') or hasRole('ADMIN')")
    public ResponseEntity<ApiTestCaseDto> createTestCase(@Valid @RequestBody ApiTestCaseDto dto) {
        ApiTestCaseDto created = apiTestingService.createTestCase(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/test-cases/{uuid}")
    @Operation(summary = "Update an existing API test case")
    @PreAuthorize("hasRole('DEVELOPER') or hasRole('ADMIN')")
    public ResponseEntity<ApiTestCaseDto> updateTestCase(
            @Parameter(description = "Test case UUID") @PathVariable UUID uuid,
            @Valid @RequestBody ApiTestCaseDto dto) {
        ApiTestCaseDto updated = apiTestingService.updateTestCase(uuid, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/test-cases/{uuid}")
    @Operation(summary = "Get an API test case by UUID")
    public ResponseEntity<ApiTestCaseDto> getTestCase(
            @Parameter(description = "Test case UUID") @PathVariable UUID uuid) {
        ApiTestCaseDto testCase = apiTestingService.getTestCase(uuid);
        return ResponseEntity.ok(testCase);
    }

    @GetMapping("/test-cases")
    @Operation(summary = "Get all test cases for a project")
    public ResponseEntity<Page<ApiTestCaseDto>> getTestCases(
            @Parameter(description = "Project ID") @RequestParam String projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ApiTestCaseDto> testCases = apiTestingService.getTestCasesByProject(projectId, pageable);
        return ResponseEntity.ok(testCases);
    }

    @DeleteMapping("/test-cases/{uuid}")
    @Operation(summary = "Delete an API test case")
    @PreAuthorize("hasRole('DEVELOPER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTestCase(
            @Parameter(description = "Test case UUID") @PathVariable UUID uuid) {
        apiTestingService.deleteTestCase(uuid);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/test-cases/{id}/execute")
    @Operation(summary = "Execute a saved API test case")
    @PreAuthorize("hasRole('DEVELOPER') or hasRole('ADMIN')")
    public CompletableFuture<ResponseEntity<ApiTestResult>> executeTestCase(
            @Parameter(description = "Test case ID") @PathVariable Long id,
            @RequestParam(defaultValue = "system") String executedBy) {

        return apiTestingService.executeTestCase(id, executedBy)
                .thenApply(result -> ResponseEntity.accepted().body(result));
    }

    @PostMapping("/execute")
    @Operation(summary = "Execute an ad-hoc API test")
    @PreAuthorize("hasRole('DEVELOPER') or hasRole('ADMIN')")
    public CompletableFuture<ResponseEntity<ApiTestResult>> executeAdHocTest(
            @Valid @RequestBody ApiTestRequest request,
            @RequestParam(defaultValue = "system") String executedBy) {

        return apiTestingService.executeAdHocTest(request, executedBy)
                .thenApply(result -> ResponseEntity.accepted().body(result));
    }

    @PostMapping("/test-cases/batch/execute")
    @Operation(summary = "Execute multiple API test cases")
    @PreAuthorize("hasRole('DEVELOPER') or hasRole('ADMIN')")
    public ResponseEntity<List<ApiTestResult>> executeBatch(
            @RequestBody List<Long> testCaseIds,
            @RequestParam(defaultValue = "system") String executedBy) {

        List<ApiTestResult> results = apiTestingService.executeBatch(testCaseIds, executedBy);
        return ResponseEntity.accepted().body(results);
    }

    @GetMapping("/test-cases/{id}/executions")
    @Operation(summary = "Get execution history for a test case")
    public ResponseEntity<List<ApiTestExecutionDto>> getExecutionHistory(
            @Parameter(description = "Test case ID") @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("executedAt").descending());
        List<ApiTestExecutionDto> history = apiTestingService.getExecutionHistory(id, pageable);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/executions/{uuid}")
    @Operation(summary = "Get a specific execution by UUID")
    public ResponseEntity<ApiTestExecutionDto> getExecution(
            @Parameter(description = "Execution UUID") @PathVariable UUID uuid) {

        ApiTestExecutionDto execution = apiTestingService.getExecution(uuid);
        return ResponseEntity.ok(execution);
    }

    @GetMapping("/statistics")
    @Operation(summary = "Get test statistics for a project")
    public ResponseEntity<Map<String, Object>> getStatistics(
            @Parameter(description = "Project ID") @RequestParam String projectId) {

        Map<String, Object> stats = apiTestingService.getTestStatistics(projectId);
        return ResponseEntity.ok(stats);
    }
}

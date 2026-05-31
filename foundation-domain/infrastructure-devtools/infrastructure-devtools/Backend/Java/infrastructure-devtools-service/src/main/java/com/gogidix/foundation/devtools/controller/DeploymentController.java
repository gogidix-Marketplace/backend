package com.gogidix.foundation.devtools.controller;

import com.gogidix.foundation.devtools.dto.DeploymentExecutionDto;
import com.gogidix.foundation.devtools.dto.DeploymentJobDto;
import com.gogidix.foundation.devtools.dto.DeploymentRequest;
import com.gogidix.foundation.devtools.dto.DeploymentResult;
import com.gogidix.foundation.devtools.service.DeploymentService;
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
 * REST controller for deployment functionality.
 */
@RestController
@RequestMapping("/deployment")
@RequiredArgsConstructor
@Tag(name = "Deployment", description = "Deployment management endpoints")
@CrossOrigin(origins = "${cors.allowed-origins}")
public class DeploymentController {

    private final DeploymentService deploymentService;

    @PostMapping("/jobs")
    @Operation(summary = "Create a new deployment job")
    @PreAuthorize("hasRole('DEVELOPER') or hasRole('ADMIN')")
    public ResponseEntity<DeploymentJobDto> createJob(@Valid @RequestBody DeploymentJobDto dto) {
        DeploymentJobDto created = deploymentService.createJob(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/jobs/{uuid}")
    @Operation(summary = "Update an existing deployment job")
    @PreAuthorize("hasRole('DEVELOPER') or hasRole('ADMIN')")
    public ResponseEntity<DeploymentJobDto> updateJob(
            @Parameter(description = "Job UUID") @PathVariable UUID uuid,
            @Valid @RequestBody DeploymentJobDto dto) {
        DeploymentJobDto updated = deploymentService.updateJob(uuid, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/jobs/{uuid}")
    @Operation(summary = "Get a deployment job by UUID")
    public ResponseEntity<DeploymentJobDto> getJob(
            @Parameter(description = "Job UUID") @PathVariable UUID uuid) {
        DeploymentJobDto job = deploymentService.getJob(uuid);
        return ResponseEntity.ok(job);
    }

    @GetMapping("/jobs")
    @Operation(summary = "Get all deployment jobs for a project")
    public ResponseEntity<Page<DeploymentJobDto>> getJobs(
            @Parameter(description = "Project ID") @RequestParam String projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<DeploymentJobDto> jobs = deploymentService.getJobsByProject(projectId, pageable);
        return ResponseEntity.ok(jobs);
    }

    @DeleteMapping("/jobs/{uuid}")
    @Operation(summary = "Delete a deployment job")
    @PreAuthorize("hasRole('DEVELOPER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteJob(
            @Parameter(description = "Job UUID") @PathVariable UUID uuid) {
        deploymentService.deleteJob(uuid);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/jobs/{id}/execute")
    @Operation(summary = "Execute a deployment job")
    @PreAuthorize("hasRole('DEVELOPER') or hasRole('ADMIN')")
    public CompletableFuture<ResponseEntity<DeploymentResult>> executeJob(
            @Parameter(description = "Job ID") @PathVariable Long id,
            @RequestBody DeploymentRequest request) {

        return deploymentService.executeJob(id, request)
                .thenApply(result -> ResponseEntity.accepted().body(result));
    }

    @GetMapping("/jobs/{id}/executions")
    @Operation(summary = "Get execution history for a deployment job")
    public ResponseEntity<List<DeploymentExecutionDto>> getExecutionHistory(
            @Parameter(description = "Job ID") @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("executedAt").descending());
        List<DeploymentExecutionDto> history = deploymentService.getExecutionHistory(id, pageable);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/executions/{uuid}")
    @Operation(summary = "Get a specific deployment execution by UUID")
    public ResponseEntity<DeploymentExecutionDto> getExecution(
            @Parameter(description = "Execution UUID") @PathVariable UUID uuid) {

        DeploymentExecutionDto execution = deploymentService.getExecution(uuid);
        return ResponseEntity.ok(execution);
    }

    @PostMapping("/executions/{id}/rollback")
    @Operation(summary = "Rollback a deployment")
    @PreAuthorize("hasRole('DEVELOPER') or hasRole('ADMIN')")
    public CompletableFuture<ResponseEntity<DeploymentResult>> rollbackDeployment(
            @Parameter(description = "Execution ID") @PathVariable Long id,
            @RequestParam(defaultValue = "system") String executedBy) {

        return deploymentService.rollbackDeployment(id, executedBy)
                .thenApply(result -> ResponseEntity.accepted().body(result));
    }

    @GetMapping("/statistics")
    @Operation(summary = "Get deployment statistics for a project")
    public ResponseEntity<Map<String, Object>> getStatistics(
            @Parameter(description = "Project ID") @RequestParam String projectId) {

        Map<String, Object> stats = deploymentService.getDeploymentStatistics(projectId);
        return ResponseEntity.ok(stats);
    }
}

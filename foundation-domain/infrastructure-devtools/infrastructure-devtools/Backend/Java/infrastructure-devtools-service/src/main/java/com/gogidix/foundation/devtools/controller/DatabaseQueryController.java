package com.gogidix.foundation.devtools.controller;

import com.gogidix.foundation.devtools.dto.DatabaseQueryDto;
import com.gogidix.foundation.devtools.dto.DatabaseQueryExecutionDto;
import com.gogidix.foundation.devtools.dto.QueryResult;
import com.gogidix.foundation.devtools.service.DatabaseQueryService;
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
 * REST controller for database query functionality.
 */
@RestController
@RequestMapping("/database")
@RequiredArgsConstructor
@Tag(name = "Database Query", description = "Database query and inspection endpoints")
@CrossOrigin(origins = "${cors.allowed-origins}")
public class DatabaseQueryController {

    private final DatabaseQueryService databaseQueryService;

    @PostMapping("/queries")
    @Operation(summary = "Create a new saved database query")
    @PreAuthorize("hasRole('DEVELOPER') or hasRole('ADMIN')")
    public ResponseEntity<DatabaseQueryDto> createQuery(@Valid @RequestBody DatabaseQueryDto dto) {
        DatabaseQueryDto created = databaseQueryService.createQuery(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/queries/{uuid}")
    @Operation(summary = "Update an existing database query")
    @PreAuthorize("hasRole('DEVELOPER') or hasRole('ADMIN')")
    public ResponseEntity<DatabaseQueryDto> updateQuery(
            @Parameter(description = "Query UUID") @PathVariable UUID uuid,
            @Valid @RequestBody DatabaseQueryDto dto) {
        DatabaseQueryDto updated = databaseQueryService.updateQuery(uuid, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/queries/{uuid}")
    @Operation(summary = "Get a database query by UUID")
    public ResponseEntity<DatabaseQueryDto> getQuery(
            @Parameter(description = "Query UUID") @PathVariable UUID uuid) {
        DatabaseQueryDto query = databaseQueryService.getQuery(uuid);
        return ResponseEntity.ok(query);
    }

    @GetMapping("/queries")
    @Operation(summary = "Get all queries for a project")
    public ResponseEntity<Page<DatabaseQueryDto>> getQueries(
            @Parameter(description = "Project ID") @RequestParam String projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<DatabaseQueryDto> queries = databaseQueryService.getQueriesByProject(projectId, pageable);
        return ResponseEntity.ok(queries);
    }

    @DeleteMapping("/queries/{uuid}")
    @Operation(summary = "Delete a database query")
    @PreAuthorize("hasRole('DEVELOPER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteQuery(
            @Parameter(description = "Query UUID") @PathVariable UUID uuid) {
        databaseQueryService.deleteQuery(uuid);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/queries/{id}/execute")
    @Operation(summary = "Execute a saved database query")
    @PreAuthorize("hasRole('DEVELOPER') or hasRole('ADMIN')")
    public CompletableFuture<ResponseEntity<QueryResult>> executeQuery(
            @Parameter(description = "Query ID") @PathVariable Long id,
            @RequestBody(required = false) Map<String, Object> parameters,
            @RequestParam(defaultValue = "system") String executedBy) {

        return databaseQueryService.executeQuery(id, parameters, executedBy)
                .thenApply(result -> ResponseEntity.accepted().body(result));
    }

    @PostMapping("/execute")
    @Operation(summary = "Execute an ad-hoc database query")
    @PreAuthorize("hasRole('DEVELOPER') or hasRole('ADMIN')")
    public CompletableFuture<ResponseEntity<QueryResult>> executeAdHocQuery(
            @RequestParam String sql,
            @RequestParam(defaultValue = "default") String databaseName,
            @RequestBody(required = false) Map<String, Object> parameters,
            @RequestParam(defaultValue = "system") String executedBy) {

        return databaseQueryService.executeAdHocQuery(sql, databaseName, parameters, executedBy)
                .thenApply(result -> ResponseEntity.accepted().body(result));
    }

    @PostMapping("/validate")
    @Operation(summary = "Validate a SQL query without executing")
    public ResponseEntity<Map<String, Object>> validateQuery(
            @RequestParam String sql,
            @RequestParam(defaultValue = "default") String databaseName) {

        Map<String, Object> result = databaseQueryService.validateQuery(sql, databaseName);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/queries/{id}/executions")
    @Operation(summary = "Get execution history for a query")
    public ResponseEntity<List<DatabaseQueryExecutionDto>> getExecutionHistory(
            @Parameter(description = "Query ID") @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("executedAt").descending());
        List<DatabaseQueryExecutionDto> history = databaseQueryService.getExecutionHistory(id, pageable);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/statistics")
    @Operation(summary = "Get query statistics for a project")
    public ResponseEntity<Map<String, Object>> getStatistics(
            @Parameter(description = "Project ID") @RequestParam String projectId) {

        Map<String, Object> stats = databaseQueryService.getQueryStatistics(projectId);
        return ResponseEntity.ok(stats);
    }
}

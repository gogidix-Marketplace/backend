package com.gogidix.foundation.devtools.controller;

import com.gogidix.foundation.devtools.dto.LogEntryDto;
import com.gogidix.foundation.devtools.dto.LogQuery;
import com.gogidix.foundation.devtools.dto.LogStatistics;
import com.gogidix.foundation.devtools.service.LoggingService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * REST controller for logging and debugging functionality.
 */
@RestController
@RequestMapping("/logging")
@RequiredArgsConstructor
@Tag(name = "Logging", description = "Logging and debugging endpoints")
@CrossOrigin(origins = "${cors.allowed-origins}")
public class LoggingController {

    private final LoggingService loggingService;

    @PostMapping("/entries")
    @Operation(summary = "Create a log entry")
    public ResponseEntity<LogEntryDto> createLogEntry(@Valid @RequestBody LogEntryDto dto) {
        LogEntryDto created = loggingService.createLogEntry(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/entries/batch")
    @Operation(summary = "Batch create log entries")
    public ResponseEntity<List<LogEntryDto>> createLogEntries(@Valid @RequestBody List<LogEntryDto> entries) {
        List<LogEntryDto> created = loggingService.createLogEntries(entries);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/entries/query")
    @Operation(summary = "Query log entries")
    public ResponseEntity<Page<LogEntryDto>> queryLogs(
            @RequestBody LogQuery query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<LogEntryDto> logs = loggingService.queryLogs(query, pageable);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/entries/session/{sessionId}")
    @Operation(summary = "Get logs by session ID")
    public ResponseEntity<List<LogEntryDto>> getLogsBySession(
            @Parameter(description = "Session ID") @PathVariable String sessionId) {

        List<LogEntryDto> logs = loggingService.getLogsBySession(sessionId);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/entries/request/{requestId}")
    @Operation(summary = "Get logs by request ID")
    public ResponseEntity<List<LogEntryDto>> getLogsByRequest(
            @Parameter(description = "Request ID") @PathVariable String requestId) {

        List<LogEntryDto> logs = loggingService.getLogsByRequest(requestId);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/statistics")
    @Operation(summary = "Get log statistics")
    public ResponseEntity<LogStatistics> getStatistics(
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate) {

        LogStatistics stats = loggingService.getStatistics(startDate, endDate);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/metadata")
    @Operation(summary = "Get logging metadata")
    public ResponseEntity<Map<String, Object>> getMetadata() {
        Map<String, Object> metadata = Map.of(
                "levels", loggingService.getLogLevels(),
                "sources", loggingService.getLogSources()
        );
        return ResponseEntity.ok(metadata);
    }

    @PostMapping("/export")
    @Operation(summary = "Export logs as JSON")
    @PreAuthorize("hasRole('DEVELOPER') or hasRole('ADMIN')")
    public ResponseEntity<String> exportLogs(@RequestBody LogQuery query) {
        String exported = loggingService.exportLogs(query);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=logs.json")
                .body(exported);
    }

    @DeleteMapping("/entries/cleanup")
    @Operation(summary = "Clean up old log entries")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> cleanupOldLogs() {
        loggingService.cleanupOldLogs();
        return ResponseEntity.ok(Map.of("status", "cleanup initiated"));
    }
}

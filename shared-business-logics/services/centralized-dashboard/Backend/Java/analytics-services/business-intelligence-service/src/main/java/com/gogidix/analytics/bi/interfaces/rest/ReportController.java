package com.gogidix.analytics.bi.interfaces.rest;

import com.gogidix.analytics.bi.application.service.ReportCommandService;
import com.gogidix.analytics.bi.domain.model.ReportDefinition;
import com.gogidix.analytics.bi.domain.model.ReportExecution;
import com.gogidix.analytics.bi.domain.port.in.CreateReportCommand;
import com.gogidix.analytics.bi.domain.port.in.ExecuteReportQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for Report API.
 */
@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@Tag(name = "Reports", description = "BI report management and execution API")
public class ReportController {

    private final ReportCommandService commandService;

    @PostMapping
    @Operation(summary = "Create a report definition")
    public ResponseEntity<ReportDefinition> createReport(@Valid @RequestBody CreateReportCommand command) {
        ReportDefinition report = commandService.createReport(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(report);
    }

    @PutMapping("/{reportId}")
    @Operation(summary = "Update a report definition")
    public ResponseEntity<ReportDefinition> updateReport(
        @PathVariable String reportId,
        @Valid @RequestBody CreateReportCommand command) {

        return ResponseEntity.ok(commandService.updateReport(reportId, command));
    }

    @DeleteMapping("/{reportId}")
    @Operation(summary = "Delete a report definition")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReport(@PathVariable String reportId) {
        commandService.deleteReport(reportId);
    }

    @PostMapping("/{reportId}/execute")
    @Operation(summary = "Execute a report")
    public ResponseEntity<ReportExecution> executeReport(
        @PathVariable String reportId,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
        @RequestParam(required = false, defaultValue = "true") boolean async) {

        ExecuteReportQuery query = ExecuteReportQuery.builder()
            .reportId(reportId)
            .startDate(startDate)
            .endDate(endDate)
            .async(async)
            .build();

        return ResponseEntity.ok(commandService.executeReport(query));
    }

    @PostMapping("/executions/{executionId}/cancel")
    @Operation(summary = "Cancel a report execution")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelExecution(@PathVariable String executionId) {
        commandService.cancelExecution(executionId);
    }

    @PutMapping("/{reportId}/toggle")
    @Operation(summary = "Enable/disable a report")
    public ResponseEntity<ReportDefinition> toggleReport(
        @PathVariable String reportId,
        @RequestParam boolean enabled) {

        return ResponseEntity.ok(commandService.toggleReport(reportId, enabled));
    }
}

package com.gogidix.customersupport.supportanalytics.interfaces.rest;

import com.gogidix.customersupport.supportanalytics.application.dto.AnalyticsReportRequestDto;
import com.gogidix.customersupport.supportanalytics.application.dto.AnalyticsReportResponseDto;
import com.gogidix.customersupport.supportanalytics.application.dto.ChannelPerformanceResponseDto;
import com.gogidix.customersupport.supportanalytics.application.dto.TicketTrendResponseDto;
import com.gogidix.customersupport.supportanalytics.application.service.SupportAnalyticsService;
import com.gogidix.customersupport.supportanalytics.domain.model.AnalyticsReport;
import com.gogidix.customersupport.supportanalytics.domain.model.ChannelPerformance;
import com.gogidix.customersupport.supportanalytics.domain.model.TicketTrend;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/support-analytics")
@RequiredArgsConstructor
@Tag(name = "Support Analytics", description = "APIs for support analytics and reporting")
public class SupportAnalyticsController {

    private final SupportAnalyticsService supportAnalyticsService;

    @GetMapping("/reports")
    @Operation(summary = "Get all analytics reports", description = "Retrieve all analytics reports for the tenant")
    public ResponseEntity<List<AnalyticsReportResponseDto>> getAllReports() {
        return ResponseEntity.ok(supportAnalyticsService.getAllReports());
    }

    @GetMapping("/reports/{id}")
    @Operation(summary = "Get report by ID", description = "Retrieve a specific analytics report by its ID")
    public ResponseEntity<AnalyticsReportResponseDto> getReportById(
            @Parameter(description = "Report ID") @PathVariable String id) {
        return ResponseEntity.ok(supportAnalyticsService.getReportById(id));
    }

    @GetMapping("/reports/latest")
    @Operation(summary = "Get latest report", description = "Retrieve the most recent analytics report")
    public ResponseEntity<AnalyticsReportResponseDto> getLatestReport() {
        return ResponseEntity.ok(supportAnalyticsService.getLatestReport());
    }

    @GetMapping("/reports/type/{reportType}")
    @Operation(summary = "Get reports by type", description = "Retrieve reports filtered by type")
    public ResponseEntity<List<AnalyticsReportResponseDto>> getReportsByType(
            @Parameter(description = "Report type") @PathVariable AnalyticsReport.ReportType reportType) {
        return ResponseEntity.ok(supportAnalyticsService.getReportsByType(reportType));
    }

    @GetMapping("/reports/daterange")
    @Operation(summary = "Get reports by date range", description = "Retrieve reports within a date range")
    public ResponseEntity<List<AnalyticsReportResponseDto>> getReportsByDateRange(
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(supportAnalyticsService.getReportsByDateRange(startDate, endDate));
    }

    @PostMapping("/reports")
    @Operation(summary = "Create a new report", description = "Create a new analytics report")
    public ResponseEntity<AnalyticsReportResponseDto> createReport(
            @Valid @RequestBody AnalyticsReportRequestDto request) {
        AnalyticsReportResponseDto created = supportAnalyticsService.createReport(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/reports/{id}")
    @Operation(summary = "Update a report", description = "Update an existing analytics report")
    public ResponseEntity<AnalyticsReportResponseDto> updateReport(
            @Parameter(description = "Report ID") @PathVariable String id,
            @Valid @RequestBody AnalyticsReportRequestDto request) {
        return ResponseEntity.ok(supportAnalyticsService.updateReport(id, request));
    }

    @DeleteMapping("/reports/{id}")
    @Operation(summary = "Delete a report", description = "Delete an analytics report")
    public ResponseEntity<Void> deleteReport(
            @Parameter(description = "Report ID") @PathVariable String id) {
        supportAnalyticsService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/trends")
    @Operation(summary = "Get ticket trends", description = "Retrieve ticket trends within a date range")
    public ResponseEntity<List<TicketTrendResponseDto>> getTicketTrends(
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(supportAnalyticsService.getTicketTrends(startDate, endDate));
    }

    @GetMapping("/trends/latest")
    @Operation(summary = "Get latest ticket trend", description = "Retrieve the most recent ticket trend")
    public ResponseEntity<TicketTrendResponseDto> getLatestTicketTrend() {
        return ResponseEntity.ok(supportAnalyticsService.getLatestTicketTrend());
    }

    @GetMapping("/trends/type/{periodType}")
    @Operation(summary = "Get trends by period type", description = "Retrieve trends filtered by period type")
    public ResponseEntity<List<TicketTrendResponseDto>> getTrendsByType(
            @Parameter(description = "Period type") @PathVariable TicketTrend.PeriodType periodType) {
        return ResponseEntity.ok(supportAnalyticsService.getTicketTrendsByType(periodType));
    }

    @GetMapping("/channel-performance")
    @Operation(summary = "Get channel performance", description = "Retrieve channel performance metrics within a date range")
    public ResponseEntity<List<ChannelPerformanceResponseDto>> getChannelPerformance(
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(supportAnalyticsService.getChannelPerformance(startDate, endDate));
    }

    @GetMapping("/channel-performance/all")
    @Operation(summary = "Get all channel performance", description = "Retrieve all channel performance metrics")
    public ResponseEntity<List<ChannelPerformanceResponseDto>> getAllChannelPerformance() {
        return ResponseEntity.ok(supportAnalyticsService.getAllChannelPerformance());
    }

    @GetMapping("/channel-performance/{channelType}")
    @Operation(summary = "Get channel performance by type", description = "Retrieve performance metrics for a specific channel")
    public ResponseEntity<List<ChannelPerformanceResponseDto>> getChannelPerformanceByType(
            @Parameter(description = "Channel type") @PathVariable ChannelPerformance.ChannelType channelType,
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(supportAnalyticsService.getChannelPerformanceByType(channelType, startDate, endDate));
    }
}

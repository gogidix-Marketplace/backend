package com.gogidix.dashboard.aggregation.interfaces.rest;

import com.gogidix.dashboard.aggregation.application.service.AggregationService;
import com.gogidix.dashboard.aggregation.domain.model.AggregationRequest;
import com.gogidix.dashboard.aggregation.domain.port.in.CreateAggregationCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * REST controller for data aggregation operations.
 * Provides API endpoints for cross-service data aggregation.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/aggregation")
@RequiredArgsConstructor
@Tag(name = "Data Aggregation", description = "APIs for cross-service data aggregation")
public class AggregationController {

    private final AggregationService aggregationService;

    /**
     * Get aggregated data across multiple domains
     */
    @GetMapping(value = "/cross-domain", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get cross-domain aggregated data",
        description = "Aggregates data from multiple source domains for the specified time range"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Aggregation completed successfully")
    })
    public ResponseEntity<Map<String, Object>> getCrossDomainData(
        @Parameter(description = "Tenant ID from header", required = true)
        @RequestHeader("X-Tenant-ID") String tenantId,
        @Parameter(description = "Source domains to aggregate", required = true,
                  example = "COURIER_SERVICE,WAREHOUSE,SOCIAL_COMMERCE")
        @RequestParam List<String> domains,
        @Parameter(description = "Start date for aggregation", example = "2024-01-01T00:00:00")
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @Parameter(description = "End date for aggregation", example = "2024-01-31T23:59:59")
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        log.info("GET /api/v1/aggregation/cross-domain - tenant={}, domains={}", tenantId, domains);

        Map<String, Object> result = aggregationService.getAggregatedData(
                tenantId, domains, startDate, endDate);

        return ResponseEntity.ok(result);
    }

    /**
     * Get cross-domain summary
     */
    @GetMapping(value = "/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get cross-domain summary",
        description = "Returns a summary of metrics across all domains"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Summary retrieved successfully")
    })
    public ResponseEntity<Map<String, Object>> getCrossDomainSummary(
        @Parameter(description = "Tenant ID from header", required = true)
        @RequestHeader("X-Tenant-ID") String tenantId
    ) {
        log.info("GET /api/v1/aggregation/summary - tenant={}", tenantId);

        Map<String, Object> summary = aggregationService.getCrossDomainSummary(tenantId);

        return ResponseEntity.ok(summary);
    }

    /**
     * Get real-time metrics across domains
     */
    @GetMapping(value = "/real-time", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get real-time metrics",
        description = "Returns real-time metrics from the specified domains"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Real-time metrics retrieved successfully")
    })
    public ResponseEntity<Map<String, Object>> getRealTimeMetrics(
        @Parameter(description = "Tenant ID from header", required = true)
        @RequestHeader("X-Tenant-ID") String tenantId,
        @Parameter(description = "Domains to fetch metrics from",
                  example = "COURIER_SERVICE,WAREHOUSE")
        @RequestParam(defaultValue = "COURIER_SERVICE,WAREHOUSE,SOCIAL_COMMERCE") List<String> domains
    ) {
        log.info("GET /api/v1/aggregation/real-time - tenant={}, domains={}", tenantId, domains);

        Map<String, Object> metrics = aggregationService.getRealTimeMetrics(tenantId, domains);

        return ResponseEntity.ok(metrics);
    }

    /**
     * Create and process an aggregation request
     */
    @PostMapping(value = "/requests", consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Create aggregation request",
        description = "Creates a new aggregation request that will be processed asynchronously"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Aggregation request created"),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<AggregationRequest> createAggregationRequest(
        @Parameter(description = "Tenant ID from header", required = true)
        @RequestHeader("X-Tenant-ID") String tenantId,
        @Parameter(description = "Aggregation request", required = true)
        @Valid @RequestBody CreateAggregationRequestDto request
    ) {
        log.info("POST /api/v1/aggregation/requests - tenant={}, name={}", tenantId, request.getName());

        CreateAggregationCommand command = CreateAggregationCommand.builder()
                .name(request.getName())
                .description(request.getDescription())
                .tenantId(tenantId)
                .sourceDomains(request.getSourceDomains())
                .kpiCodes(request.getKpiCodes())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .aggregationType(request.getAggregationType())
                .groupBy(request.getGroupBy())
                .filters(request.getFilters())
                .build();

        AggregationRequest result = aggregationService.createAndProcessAggregation(command);

        return ResponseEntity.status(201).body(result);
    }

    /**
     * Request DTO for creating aggregation
     */
    @Schema(description = "Request to create a data aggregation")
    public static class CreateAggregationRequestDto {
        @Schema(description = "Name of the aggregation", example = "Monthly Performance Report")
        private String name;

        @Schema(description = "Description of the aggregation")
        private String description;

        @Schema(description = "Source domains to aggregate from",
                example = "[\"COURIER_SERVICE\", \"WAREHOUSE\", \"SOCIAL_COMMERCE\"]")
        private List<String> sourceDomains;

        @Schema(description = "KPI codes to include")
        private List<String> kpiCodes;

        @Schema(description = "Start date for data range")
        private LocalDateTime startDate;

        @Schema(description = "End date for data range")
        private LocalDateTime endDate;

        @Schema(description = "Type of aggregation", example = "SUM")
        private String aggregationType;

        @Schema(description = "Group by field")
        private String groupBy;

        @Schema(description = "Filters as JSON string")
        private String filters;

        // Getters
        public String getName() { return name; }
        public String getDescription() { return description; }
        public List<String> getSourceDomains() { return sourceDomains; }
        public List<String> getKpiCodes() { return kpiCodes; }
        public LocalDateTime getStartDate() { return startDate; }
        public LocalDateTime getEndDate() { return endDate; }
        public String getAggregationType() { return aggregationType; }
        public String getGroupBy() { return groupBy; }
        public String getFilters() { return filters; }
    }
}

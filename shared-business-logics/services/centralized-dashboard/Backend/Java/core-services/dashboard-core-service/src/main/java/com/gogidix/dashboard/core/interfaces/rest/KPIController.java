package com.gogidix.dashboard.core.interfaces.rest;

import com.gogidix.dashboard.core.application.dto.request.CreateKPIRequestDto;
import com.gogidix.dashboard.core.application.dto.request.RecordKPIValueRequestDto;
import com.gogidix.dashboard.core.application.dto.request.UpdateKPIRequestDto;
import com.gogidix.dashboard.core.application.dto.response.KPIResponseDto;
import com.gogidix.dashboard.core.application.dto.response.PagedResponseDto;
import com.gogidix.dashboard.core.application.service.KPICommandService;
import com.gogidix.dashboard.core.application.service.KPIQueryService;
import com.gogidix.dashboard.core.domain.port.in.GetKPIQuery;
import com.gogidix.dashboard.core.domain.port.in.SearchKPIsQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/**
 * REST controller for KPI operations.
 * Provides API endpoints for managing dashboard KPIs.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/kpis")
@RequiredArgsConstructor
@Tag(name = "KPI Management", description = "APIs for managing dashboard Key Performance Indicators")
public class KPIController {

    private final KPICommandService commandService;
    private final KPIQueryService queryService;

    /**
     * Create a new KPI
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Create a new KPI",
        description = "Creates a new Key Performance Indicator with the specified configuration"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "KPI created successfully",
            headers = @Header(name = "X-Tenant-ID", description = "Tenant ID"),
            content = @Content(schema = @Schema(implementation = KPIResponseDto.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "409", description = "KPI with code already exists")
    })
    public ResponseEntity<KPIResponseDto> createKPI(
        @Parameter(description = "Tenant ID from header", required = true, example = "tenant1")
        @RequestHeader("X-Tenant-ID") String tenantId,
        @Parameter(description = "KPI creation request", required = true)
        @Valid @RequestBody CreateKPIRequestDto request
    ) {
        log.info("POST /api/v1/kpis - Creating KPI: code={}, tenant={}", request.getCode(), tenantId);

        KPIResponseDto response = commandService.createKPI(request, tenantId);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .location(URI.create("/api/v1/kpis/" + response.getId()))
            .body(response);
    }

    /**
     * Get KPI by ID
     */
    @GetMapping(value = "/{kpiId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get KPI by ID",
        description = "Retrieves detailed information about a specific KPI including historical values and targets"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "KPI found", content = @Content(schema = @Schema(implementation = KPIResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "KPI not found")
    })
    public ResponseEntity<KPIResponseDto> getKPI(
        @Parameter(description = "KPI ID", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable String kpiId,
        @Parameter(description = "Include historical values", example = "false")
        @RequestParam(defaultValue = "false") Boolean includeHistorical,
        @Parameter(description = "Include targets", example = "false")
        @RequestParam(defaultValue = "false") Boolean includeTargets,
        @Parameter(description = "Historical days to include", example = "30")
        @RequestParam(defaultValue = "30") Integer historicalDays
    ) {
        log.info("GET /api/v1/kpis/{} - Getting KPI", kpiId);

        var query = GetKPIQuery.builder()
            .kpiId(kpiId)
            .includeHistoricalValues(includeHistorical)
            .includeTargets(includeTargets)
            .historicalDays(historicalDays)
            .build();

        KPIResponseDto response = queryService.getKPIById(kpiId, query);
        return ResponseEntity.ok(response);
    }

    /**
     * Get KPI by code
     */
    @GetMapping(value = "/by-code/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get KPI by code",
        description = "Retrieves a KPI by its unique code"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "KPI found", content = @Content(schema = @Schema(implementation = KPIResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "KPI not found")
    })
    public ResponseEntity<KPIResponseDto> getKPIByCode(
        @Parameter(description = "KPI code", required = true, example = "TOTAL_ORDERS")
        @PathVariable String code,
        @Parameter(description = "Tenant ID from header", required = true)
        @RequestHeader("X-Tenant-ID") String tenantId
    ) {
        log.info("GET /api/v1/kpis/by-code/{} - Getting KPI by code", code);

        KPIResponseDto response = queryService.getKPIByCode(code, tenantId);
        return ResponseEntity.ok(response);
    }

    /**
     * Search for KPIs
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Search for KPIs",
        description = "Searches for KPIs with optional filters for category, source domain, and search query"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully")
    })
    public ResponseEntity<PagedResponseDto<KPIResponseDto>> searchKPIs(
        @Parameter(description = "Tenant ID from header", required = true)
        @RequestHeader("X-Tenant-ID") String tenantId,
        @Parameter(description = "Filter by category") @RequestParam(required = false) String category,
        @Parameter(description = "Filter by source domain") @RequestParam(required = false) String sourceDomain,
        @Parameter(description = "Search query") @RequestParam(required = false) String searchQuery,
        @Parameter(description = "Filter by active status") @RequestParam(required = false) Boolean isActive,
        @Parameter(description = "Page number (0-indexed)", example = "0") @RequestParam(defaultValue = "0") Integer page,
        @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") Integer size,
        @Parameter(description = "Sort field", example = "name") @RequestParam(defaultValue = "name") String sortBy,
        @Parameter(description = "Sort direction (ASC/DESC)", example = "ASC") @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        log.info("GET /api/v1/kpis - Searching KPIs: tenant={}, category={}", tenantId, category);

        var query = SearchKPIsQuery.builder()
            .tenantId(tenantId)
            .category(category)
            .sourceDomain(sourceDomain)
            .searchQuery(searchQuery)
            .isActive(isActive)
            .page(page)
            .size(size)
            .sortBy(sortBy)
            .sortDirection(sortDirection)
            .build();

        PagedResponseDto<KPIResponseDto> response = queryService.searchKPIs(query);
        return ResponseEntity.ok(response);
    }

    /**
     * Update a KPI
     */
    @PutMapping(value = "/{kpiId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Update a KPI",
        description = "Updates an existing KPI with the provided data"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "KPI updated successfully", content = @Content(schema = @Schema(implementation = KPIResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "KPI not found")
    })
    public ResponseEntity<KPIResponseDto> updateKPI(
        @Parameter(description = "KPI ID", required = true)
        @PathVariable String kpiId,
        @Parameter(description = "KPI update request", required = true)
        @Valid @RequestBody UpdateKPIRequestDto request
    ) {
        log.info("PUT /api/v1/kpis/{} - Updating KPI", kpiId);

        KPIResponseDto response = commandService.updateKPI(kpiId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a KPI
     */
    @DeleteMapping(value = "/{kpiId}")
    @Operation(
        summary = "Delete a KPI",
        description = "Permanently deletes a KPI and all its associated data"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "KPI deleted successfully"),
        @ApiResponse(responseCode = "404", description = "KPI not found")
    })
    public ResponseEntity<Void> deleteKPI(
        @Parameter(description = "KPI ID", required = true)
        @PathVariable String kpiId
    ) {
        log.info("DELETE /api/v1/kpis/{} - Deleting KPI", kpiId);

        commandService.deleteKPI(kpiId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Record a KPI value
     */
    @PostMapping(value = "/{kpiId}/values", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Record a KPI value",
        description = "Records a new value for the specified KPI"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Value recorded successfully", content = @Content(schema = @Schema(implementation = KPIResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "KPI not found")
    })
    public ResponseEntity<KPIResponseDto> recordKPIValue(
        @Parameter(description = "KPI ID", required = true)
        @PathVariable String kpiId,
        @Parameter(description = "Value recording request", required = true)
        @Valid @RequestBody RecordKPIValueRequestDto request
    ) {
        log.info("POST /api/v1/kpis/{}/values - Recording value: {}", kpiId, request.getValue());

        KPIResponseDto response = commandService.recordKPIValue(kpiId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Calculate KPI
     */
    @PostMapping(value = "/calculate/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Calculate KPI value",
        description = "Triggers calculation of a KPI based on its formula and data sources"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "KPI calculated successfully", content = @Content(schema = @Schema(implementation = KPIResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "KPI not found")
    })
    public ResponseEntity<KPIResponseDto> calculateKPI(
        @Parameter(description = "KPI code", required = true)
        @PathVariable String code,
        @Parameter(description = "Tenant ID from header", required = true)
        @RequestHeader("X-Tenant-ID") String tenantId
    ) {
        log.info("POST /api/v1/kpis/calculate/{} - Calculating KPI", code);

        KPIResponseDto response = commandService.calculateKPI(code, tenantId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get KPIs by category
     */
    @GetMapping(value = "/category/{category}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get KPIs by category",
        description = "Retrieves all KPIs belonging to a specific category"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "KPIs retrieved successfully")
    })
    public ResponseEntity<java.util.List<KPIResponseDto>> getKPIsByCategory(
        @Parameter(description = "Category", required = true)
        @PathVariable String category,
        @Parameter(description = "Tenant ID from header", required = true)
        @RequestHeader("X-Tenant-ID") String tenantId
    ) {
        log.info("GET /api/v1/kpis/category/{} - Getting KPIs by category", category);

        java.util.List<KPIResponseDto> response = queryService.getKPIsByCategory(tenantId, category);
        return ResponseEntity.ok(response);
    }

    /**
     * Get KPIs by source domain
     */
    @GetMapping(value = "/source-domain/{sourceDomain}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get KPIs by source domain",
        description = "Retrieves all KPIs from a specific source domain"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "KPIs retrieved successfully")
    })
    public ResponseEntity<java.util.List<KPIResponseDto>> getKPIsBySourceDomain(
        @Parameter(description = "Source domain", required = true)
        @PathVariable String sourceDomain,
        @Parameter(description = "Tenant ID from header", required = true)
        @RequestHeader("X-Tenant-ID") String tenantId
    ) {
        log.info("GET /api/v1/kpis/source-domain/{} - Getting KPIs by source domain", sourceDomain);

        java.util.List<KPIResponseDto> response = queryService.getKPIsBySourceDomain(tenantId, sourceDomain);
        return ResponseEntity.ok(response);
    }

    /**
     * Get real-time KPIs
     */
    @GetMapping(value = "/real-time", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get real-time KPIs",
        description = "Retrieves all KPIs that are configured for real-time updates"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Real-time KPIs retrieved successfully")
    })
    public ResponseEntity<java.util.List<KPIResponseDto>> getRealTimeKPIs(
        @Parameter(description = "Tenant ID from header", required = true)
        @RequestHeader("X-Tenant-ID") String tenantId
    ) {
        log.info("GET /api/v1/kpis/real-time - Getting real-time KPIs");

        java.util.List<KPIResponseDto> response = queryService.getRealTimeKPIs(tenantId);
        return ResponseEntity.ok(response);
    }
}

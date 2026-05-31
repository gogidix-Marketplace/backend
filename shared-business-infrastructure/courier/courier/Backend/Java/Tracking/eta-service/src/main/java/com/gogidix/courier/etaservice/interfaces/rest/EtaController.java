package com.gogidix.courier.etaservice.interfaces.rest;

import com.gogidix.courier.etaservice.application.command.BatchCalculateEtaCommand;
import com.gogidix.courier.etaservice.application.command.CalculateEtaCommand;
import com.gogidix.courier.etaservice.application.dto.*;
import com.gogidix.courier.etaservice.application.mapper.EtaMapper;
import com.gogidix.courier.etaservice.application.service.EtaApplicationService;
import com.gogidix.courier.etaservice.application.query.EtaQuery;
import com.gogidix.courier.etaservice.domain.entity.EtaCalculation;
import com.gogidix.courier.etaservice.domain.entity.EtaHistory;
import com.gogidix.courier.etaservice.shared.context.RequestContext;
import com.gogidix.courier.etaservice.shared.context.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Instant;
import java.util.List;

/**
 * REST controller for ETA operations.
 * Provides endpoints for calculating, updating, and querying ETAs.
 */
@RestController
@RequestMapping("/eta")
@Tag(name = "ETA", description = "APIs for Estimated Time of Arrival calculations")
public class EtaController {

    private final EtaApplicationService etaApplicationService;
    private final EtaMapper etaMapper;

    public EtaController(EtaApplicationService etaApplicationService, EtaMapper etaMapper) {
        this.etaApplicationService = etaApplicationService;
        this.etaMapper = etaMapper;
    }

    // ========================================================================
    // ETA Calculation Endpoints
    // ========================================================================

    @PostMapping("/calculate")
    @Operation(
            summary = "Calculate ETA",
            description = "Calculate estimated time of arrival for a dispatch with traffic-aware algorithms"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "ETA calculated successfully",
                    headers = @Header(name = "X-Correlation-Id", description = "Correlation ID for tracking"),
                    content = @Content(schema = @Schema(implementation = EtaResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "409", description = "ETA already exists")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EtaResponse> calculateEta(
            @Valid @RequestBody EtaRequest request
    ) {
        RequestContext context = RequestContextHolder.getContext();
        EtaResponse response = etaApplicationService.calculateEta(request, context);

        return ResponseEntity
                .created(URI.create("/eta/" + request.dispatchId()))
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping("/{dispatchId}")
    @Operation(
            summary = "Get ETA by dispatch ID",
            description = "Retrieve the current ETA calculation for a specific dispatch"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ETA retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "ETA not found")
    })
    public ResponseEntity<EtaResponse> getEta(
            @Parameter(description = "Dispatch ID", required = true)
            @PathVariable @NotBlank String dispatchId,

            @Parameter(description = "Tenant ID", required = true)
            @RequestParam @NotBlank String tenantId
    ) {
        RequestContext context = RequestContextHolder.getContext();
        EtaCalculation calculation = etaApplicationService.getByDispatchIdAndTenantId(dispatchId, tenantId);
        EtaResponse response = etaMapper.toResponseDto(calculation);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @PutMapping("/{dispatchId}/recalculate")
    @Operation(
            summary = "Recalculate ETA",
            description = "Recalculate ETA for a dispatch with current conditions"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ETA recalculated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "ETA not found"),
            @ApiResponse(responseCode = "409", description = "ETA cannot be recalculated in current state")
    })
    public ResponseEntity<EtaResponse> recalculateEta(
            @Parameter(description = "Dispatch ID", required = true)
            @PathVariable @NotBlank String dispatchId,

            @Valid @RequestBody RecalculateEtaRequest request
    ) {
        RequestContext context = RequestContextHolder.getContext();
        EtaResponse response = etaApplicationService.recalculateEta(dispatchId, request.tenantId(), request, context);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    // ========================================================================
    // History Endpoints
    // ========================================================================

    @GetMapping("/{dispatchId}/history")
    @Operation(
            summary = "Get ETA history",
            description = "Retrieve the history of ETA calculations for a dispatch"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ETA history retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Dispatch not found")
    })
    public ResponseEntity<List<EtaHistoryResponse>> getEtaHistory(
            @Parameter(description = "Dispatch ID", required = true)
            @PathVariable @NotBlank String dispatchId,

            @Parameter(description = "Tenant ID", required = true)
            @RequestParam @NotBlank String tenantId,

            @Parameter(description = "Maximum number of entries to return", example = "50")
            @RequestParam(required = false, defaultValue = "50") int limit
    ) {
        RequestContext context = RequestContextHolder.getContext();
        List<EtaQuery.EtaHistoryDto> history = etaApplicationService.getHistory(dispatchId, tenantId);

        List<EtaHistoryResponse> responses = history.stream()
                .limit(limit)
                .map(this::toHistoryResponse)
                .toList();

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(responses);
    }

    // ========================================================================
    // Statistics Endpoints
    // ========================================================================

    @GetMapping("/stats")
    @Operation(
            summary = "Get ETA statistics",
            description = "Retrieve aggregated statistics for ETA calculations"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    })
    public ResponseEntity<EtaStatisticsResponse> getStatistics(
            @Parameter(description = "Tenant ID", required = true)
            @RequestParam @NotBlank String tenantId,

            @Parameter(description = "Filter from date (ISO format)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fromDate,

            @Parameter(description = "Filter to date (ISO format)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant toDate
    ) {
        RequestContext context = RequestContextHolder.getContext();
        EtaQuery.EtaStatisticsDto stats = etaApplicationService.getStatistics(tenantId, fromDate, toDate);

        EtaStatisticsResponse response = new EtaStatisticsResponse(
                stats.totalCalculations(),
                stats.activeDeliveries(),
                stats.completedDeliveries(),
                stats.averageEtaMinutes(),
                stats.averageAccuracyPercentage(),
                stats.onTimeDeliveries(),
                stats.delayedDeliveries(),
                stats.earlyDeliveries(),
                stats.mostCommonTrafficLevel(),
                new EtaStatisticsResponse.TrafficLevelStats(0, 0, 0, 0) // Would be populated from detailed stats
        );

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    // ========================================================================
    // Batch Operations
    // ========================================================================

    @PostMapping("/batch")
    @Operation(
            summary = "Batch calculate ETA",
            description = "Calculate ETA for multiple dispatches in a single request (max 100)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Batch calculation completed"),
            @ApiResponse(responseCode = "400", description = "Invalid request or too many items")
    })
    public ResponseEntity<BatchEtaResponse> batchCalculateEta(
            @Valid @RequestBody BatchCalculateEtaCommand command
    ) {
        RequestContext context = RequestContextHolder.getContext();
        BatchEtaResponse response = etaApplicationService.batchCalculateEta(command, context);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    // ========================================================================
    // Query Endpoints
    // ========================================================================

    @GetMapping("/active")
    @Operation(
            summary = "Get active deliveries",
            description = "Retrieve all active (in-transit) ETA calculations for a tenant"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active ETAs retrieved successfully")
    })
    public ResponseEntity<List<EtaResponse>> getActiveDeliveries(
            @Parameter(description = "Tenant ID", required = true)
            @RequestParam @NotBlank String tenantId
    ) {
        RequestContext context = RequestContextHolder.getContext();
        List<EtaCalculation> active = etaApplicationService.getActiveByTenantId(tenantId);
        List<EtaResponse> responses = etaMapper.toResponseDtoList(active);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(responses);
    }

    @GetMapping("/arriving-soon")
    @Operation(
            summary = "Get deliveries arriving soon",
            description = "Retrieve deliveries that will arrive within the specified time window"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Arriving soon deliveries retrieved successfully")
    })
    public ResponseEntity<List<EtaResponse>> getArrivingSoon(
            @Parameter(description = "Tenant ID", required = true)
            @RequestParam @NotBlank String tenantId,

            @Parameter(description = "Time window in minutes", example = "30")
            @RequestParam(defaultValue = "30") int withinMinutes
    ) {
        RequestContext context = RequestContextHolder.getContext();
        List<EtaCalculation> arrivingSoon = etaApplicationService.getArrivingSoon(tenantId, withinMinutes);
        List<EtaResponse> responses = etaMapper.toResponseDtoList(arrivingSoon);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(responses);
    }

    // ========================================================================
    // Status Management Endpoints
    // ========================================================================

    @PostMapping("/{dispatchId}/in-transit")
    @Operation(
            summary = "Mark as in-transit",
            description = "Mark a dispatch as in-transit (picking up or on the way)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid state transition"),
            @ApiResponse(responseCode = "404", description = "ETA not found")
    })
    public ResponseEntity<EtaResponse> markAsInTransit(
            @Parameter(description = "Dispatch ID", required = true)
            @PathVariable @NotBlank String dispatchId,

            @Parameter(description = "Tenant ID", required = true)
            @RequestParam @NotBlank String tenantId
    ) {
        RequestContext context = RequestContextHolder.getContext();
        EtaResponse response = etaApplicationService.markAsInTransit(dispatchId, tenantId);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @PostMapping("/{dispatchId}/delivered")
    @Operation(
            summary = "Mark as delivered",
            description = "Mark a dispatch as delivered"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @ApiResponse(responseCode = "404", description = "ETA not found")
    })
    public ResponseEntity<EtaResponse> markAsDelivered(
            @Parameter(description = "Dispatch ID", required = true)
            @PathVariable @NotBlank String dispatchId,

            @Parameter(description = "Tenant ID", required = true)
            @RequestParam @NotBlank String tenantId
    ) {
        RequestContext context = RequestContextHolder.getContext();
        EtaResponse response = etaApplicationService.markAsDelivered(dispatchId, tenantId, context);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @PostMapping("/{dispatchId}/cancel")
    @Operation(
            summary = "Mark as cancelled",
            description = "Mark a dispatch as cancelled"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @ApiResponse(responseCode = "404", description = "ETA not found")
    })
    public ResponseEntity<EtaResponse> markAsCancelled(
            @Parameter(description = "Dispatch ID", required = true)
            @PathVariable @NotBlank String dispatchId,

            @Parameter(description = "Tenant ID", required = true)
            @RequestParam @NotBlank String tenantId,

            @Parameter(description = "Cancellation reason")
            @RequestParam(required = false) String reason
    ) {
        RequestContext context = RequestContextHolder.getContext();
        EtaResponse response = etaApplicationService.markAsCancelled(dispatchId, tenantId, reason, context);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    // ========================================================================
    // Helper Methods
    // ========================================================================

    private EtaHistoryResponse toHistoryResponse(EtaQuery.EtaHistoryDto dto) {
        return new EtaHistoryResponse(
                dto.id(),
                dto.dispatchId(),
                dto.timestamp(),
                dto.etaMinutes(),
                dto.previousEtaMinutes(),
                dto.etaChangeMinutes(),
                dto.distanceKm(),
                null, // currentLocation
                dto.trafficLevel(),
                null, // trafficMultiplier
                null, // vehicleType
                null, // confidenceScore
                dto.changeReason(),
                dto.changeType() != null ? EtaHistory.ChangeType.valueOf(dto.changeType()) : null,
                null, // calculatedBy
                null  // source
        );
    }
}

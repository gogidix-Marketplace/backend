package com.gogidix.aiservices.aisalesforecastingservice.interfaces.rest;

import com.gogidix.aiservices.aisalesforecastingservice.application.command.AddForecastModelsCommand;
import com.gogidix.aiservices.aisalesforecastingservice.application.command.AnalyzeForecastCommand;
import com.gogidix.aiservices.aisalesforecastingservice.application.command.CreateForecastCommand;
import com.gogidix.aiservices.aisalesforecastingservice.application.command.DeleteForecastCommand;
import com.gogidix.aiservices.aisalesforecastingservice.application.command.RemoveForecastModelsCommand;
import com.gogidix.aiservices.aisalesforecastingservice.application.command.UpdateForecastCommand;
import com.gogidix.aiservices.aisalesforecastingservice.application.dto.AnalyzeForecastRequestDto;
import com.gogidix.aiservices.aisalesforecastingservice.application.dto.CreateForecastRequestDto;
import com.gogidix.aiservices.aisalesforecastingservice.application.dto.SalesForecastingResponseDto;
import com.gogidix.aiservices.aisalesforecastingservice.application.dto.ModifyForecastModelsRequestDto;
import com.gogidix.aiservices.aisalesforecastingservice.application.dto.PagedResponseDto;
import com.gogidix.aiservices.aisalesforecastingservice.application.dto.ForecastAnalysisResponseDto;
import com.gogidix.aiservices.aisalesforecastingservice.application.dto.ForecastSearchRequestDto;
import com.gogidix.aiservices.aisalesforecastingservice.application.dto.UpdateForecastRequestDto;
import com.gogidix.aiservices.aisalesforecastingservice.application.service.SalesForecastingApplicationService;
import com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastType;
import com.gogidix.aiservices.aisalesforecastingservice.shared.context.RequestContext;
import com.gogidix.aiservices.aisalesforecastingservice.shared.context.RequestContextHolder;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * REST controller for sales forecast operations.
 */
@RestController
@RequestMapping("/segments")
@Tag(name = "ForecastModel Forecasts", description = "APIs for managing sales forecasts")
public class SalesForecastingController {

    private final SalesForecastingApplicationService service;

    public SalesForecastingController(SalesForecastingApplicationService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create a new segment", description = "Creates a new sales forecast with the provided criteria")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Forecast created successfully",
                    headers = @Header(name = "X-Correlation-Id", description = "Correlation ID for tracking"),
                    content = @Content(schema = @Schema(implementation = SalesForecastingResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "409", description = "Maximum segments per tenant reached")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<SalesForecastingResponseDto> createForecast(
            @Valid @RequestBody CreateForecastRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        CreateForecastCommand command = new CreateForecastCommand(
                request.name(),
                request.description(),
                request.segmentType(),
                request.criteria(),
                context.tenantId(),
                context.userId()
        );

        SalesForecastingResponseDto response = service.createForecast(command);

        return ResponseEntity
                .created(URI.create("/segments/" + response.id()))
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping("/{segmentId}")
    @Operation(summary = "Get a segment by ID", description = "Retrieves a sales forecast by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Forecast retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Forecast not found")
    })
    public ResponseEntity<SalesForecastingResponseDto> getForecast(
            @Parameter(description = "Forecast ID", required = true)
            @PathVariable @NotBlank String segmentId
    ) {
        RequestContext context = RequestContextHolder.getContext();
        SalesForecastingResponseDto response = service.getForecastById(segmentId, context.tenantId());

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping
    @Operation(summary = "List segments", description = "Retrieves a paginated list of segments for the current tenant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Forecasts retrieved successfully")
    })
    public ResponseEntity<PagedResponseDto<SalesForecastingResponseDto>> listForecasts(
            @Parameter(description = "Filter by segment type")
            @RequestParam(required = false) ForecastType segmentType,

            @Parameter(description = "Filter by active status")
            @RequestParam(required = false) Boolean active,

            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(required = false, defaultValue = "0") int page,

            @Parameter(description = "Page size", example = "20")
            @RequestParam(required = false, defaultValue = "20") int size,

            @Parameter(description = "Sort field", example = "createdAt")
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,

            @Parameter(description = "Sort direction (ASC/DESC)", example = "DESC")
            @RequestParam(required = false, defaultValue = "DESC") String sortDirection
    ) {
        RequestContext context = RequestContextHolder.getContext();

        PagedResponseDto<SalesForecastingResponseDto> response = service.getForecastsByTenant(
                context.tenantId(),
                segmentType,
                active,
                page,
                size,
                sortBy,
                sortDirection
        );

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @PutMapping("/{segmentId}")
    @Operation(summary = "Update a segment", description = "Updates an existing sales forecast")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Forecast updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Forecast not found")
    })
    public ResponseEntity<SalesForecastingResponseDto> updateForecast(
            @Parameter(description = "Forecast ID", required = true)
            @PathVariable @NotBlank String segmentId,

            @Valid @RequestBody UpdateForecastRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        UpdateForecastCommand command = new UpdateForecastCommand(
                segmentId,
                request.name(),
                request.description(),
                request.segmentType(),
                request.criteria(),
                request.active(),
                context.tenantId(),
                context.userId()
        );

        SalesForecastingResponseDto response = service.updateForecast(command);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @DeleteMapping("/{segmentId}")
    @Operation(summary = "Delete a segment", description = "Deletes a sales forecast by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Forecast deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Forecast not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteForecast(
            @Parameter(description = "Forecast ID", required = true)
            @PathVariable @NotBlank String segmentId
    ) {
        RequestContext context = RequestContextHolder.getContext();

        service.deleteForecast(segmentId, context.tenantId(), context.userId());

        return ResponseEntity.noContent()
                .header("X-Correlation-Id", context.correlationId())
                .build();
    }

    @PostMapping("/{segmentId}/analyze")
    @Operation(summary = "Analyze a segment", description = "Performs analysis on a sales forecast")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Analysis completed successfully"),
            @ApiResponse(responseCode = "404", description = "Forecast not found"),
            @ApiResponse(responseCode = "400", description = "Forecast is not active")
    })
    public ResponseEntity<ForecastAnalysisResponseDto> analyzeForecast(
            @Parameter(description = "Forecast ID", required = true)
            @PathVariable @NotBlank String segmentId,

            @RequestBody(required = false) AnalyzeForecastRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        AnalyzeForecastRequestDto actualRequest = request != null ? request : AnalyzeForecastRequestDto.create();

        AnalyzeForecastCommand command = new AnalyzeForecastCommand(
                segmentId,
                context.tenantId(),
                context.userId(),
                actualRequest.analysisOptions()
        );

        ForecastAnalysisResponseDto response = service.analyzeForecast(command);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @PostMapping("/{segmentId}/customers")
    @Operation(summary = "Add customers to segment", description = "Adds customers to an existing segment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ForecastModels added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or too many customers"),
            @ApiResponse(responseCode = "404", description = "Forecast not found")
    })
    public ResponseEntity<SalesForecastingResponseDto> addForecastModels(
            @Parameter(description = "Forecast ID", required = true)
            @PathVariable @NotBlank String segmentId,

            @Valid @RequestBody ModifyForecastModelsRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        AddForecastModelsCommand command = new AddForecastModelsCommand(
                segmentId,
                context.tenantId(),
                context.userId(),
                request.customerIds()
        );

        SalesForecastingResponseDto response = service.addForecastModelsToForecast(command);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @DeleteMapping("/{segmentId}/customers")
    @Operation(summary = "Remove customers from segment", description = "Removes customers from an existing segment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ForecastModels removed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Forecast not found")
    })
    public ResponseEntity<SalesForecastingResponseDto> removeForecastModels(
            @Parameter(description = "Forecast ID", required = true)
            @PathVariable @NotBlank String segmentId,

            @Valid @RequestBody ModifyForecastModelsRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        RemoveForecastModelsCommand command = new RemoveForecastModelsCommand(
                segmentId,
                context.tenantId(),
                context.userId(),
                request.customerIds()
        );

        SalesForecastingResponseDto response = service.removeForecastModelsFromForecast(command);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }
}

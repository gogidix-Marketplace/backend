package com.gogidix.aiservices.aichurnpredictionservice.interfaces.rest;

import com.gogidix.aiservices.aichurnpredictionservice.application.command.AddModelsToPredictionCommand;
import com.gogidix.aiservices.aichurnpredictionservice.application.command.AnalyzePredictionCommand;
import com.gogidix.aiservices.aichurnpredictionservice.application.command.CreatePredictionCommand;
import com.gogidix.aiservices.aichurnpredictionservice.application.command.DeletePredictionCommand;
import com.gogidix.aiservices.aichurnpredictionservice.application.command.RemoveModelsFromPredictionCommand;
import com.gogidix.aiservices.aichurnpredictionservice.application.command.UpdatePredictionCommand;
import com.gogidix.aiservices.aichurnpredictionservice.application.dto.AnalyzePredictionRequestDto;
import com.gogidix.aiservices.aichurnpredictionservice.application.dto.CreatePredictionRequestDto;
import com.gogidix.aiservices.aichurnpredictionservice.application.dto.ChurnPredictionResponseDto;
import com.gogidix.aiservices.aichurnpredictionservice.application.dto.ModifyModelsRequestDto;
import com.gogidix.aiservices.aichurnpredictionservice.application.dto.PagedResponseDto;
import com.gogidix.aiservices.aichurnpredictionservice.application.dto.PredictionAnalysisResponseDto;
import com.gogidix.aiservices.aichurnpredictionservice.application.dto.PredictionSearchRequestDto;
import com.gogidix.aiservices.aichurnpredictionservice.application.dto.UpdatePredictionRequestDto;
import com.gogidix.aiservices.aichurnpredictionservice.application.service.ChurnPredictionApplicationService;
import com.gogidix.aiservices.aichurnpredictionservice.domain.model.PredictionType;
import com.gogidix.aiservices.aichurnpredictionservice.shared.context.RequestContext;
import com.gogidix.aiservices.aichurnpredictionservice.shared.context.RequestContextHolder;
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
 * REST controller for churn prediction operations.
 */
@RestController
@RequestMapping("/segments")
@Tag(name = "Customer Predictions", description = "APIs for managing churn predictions")
public class ChurnPredictionController {

    private final ChurnPredictionApplicationService service;

    public ChurnPredictionController(ChurnPredictionApplicationService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create a new segment", description = "Creates a new churn prediction with the provided criteria")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Prediction created successfully",
                    headers = @Header(name = "X-Correlation-Id", description = "Correlation ID for tracking"),
                    content = @Content(schema = @Schema(implementation = ChurnPredictionResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "409", description = "Maximum segments per tenant reached")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ChurnPredictionResponseDto> createPrediction(
            @Valid @RequestBody CreatePredictionRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        CreatePredictionCommand command = new CreatePredictionCommand(
                request.name(),
                request.description(),
                request.segmentType(),
                request.criteria(),
                context.tenantId(),
                context.userId()
        );

        ChurnPredictionResponseDto response = service.createPrediction(command);

        return ResponseEntity
                .created(URI.create("/segments/" + response.id()))
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping("/{segmentId}")
    @Operation(summary = "Get a segment by ID", description = "Retrieves a churn prediction by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prediction retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Prediction not found")
    })
    public ResponseEntity<ChurnPredictionResponseDto> getPrediction(
            @Parameter(description = "Prediction ID", required = true)
            @PathVariable @NotBlank String segmentId
    ) {
        RequestContext context = RequestContextHolder.getContext();
        ChurnPredictionResponseDto response = service.getPredictionById(segmentId, context.tenantId());

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping
    @Operation(summary = "List segments", description = "Retrieves a paginated list of segments for the current tenant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Predictions retrieved successfully")
    })
    public ResponseEntity<PagedResponseDto<ChurnPredictionResponseDto>> listPredictions(
            @Parameter(description = "Filter by segment type")
            @RequestParam(required = false) PredictionType segmentType,

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

        PagedResponseDto<ChurnPredictionResponseDto> response = service.getPredictionsByTenant(
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
    @Operation(summary = "Update a segment", description = "Updates an existing churn prediction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prediction updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Prediction not found")
    })
    public ResponseEntity<ChurnPredictionResponseDto> updatePrediction(
            @Parameter(description = "Prediction ID", required = true)
            @PathVariable @NotBlank String segmentId,

            @Valid @RequestBody UpdatePredictionRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        UpdatePredictionCommand command = new UpdatePredictionCommand(
                segmentId,
                request.name(),
                request.description(),
                request.segmentType(),
                request.criteria(),
                request.active(),
                context.tenantId(),
                context.userId()
        );

        ChurnPredictionResponseDto response = service.updatePrediction(command);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @DeleteMapping("/{segmentId}")
    @Operation(summary = "Delete a segment", description = "Deletes a churn prediction by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Prediction deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Prediction not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deletePrediction(
            @Parameter(description = "Prediction ID", required = true)
            @PathVariable @NotBlank String segmentId
    ) {
        RequestContext context = RequestContextHolder.getContext();

        service.deletePrediction(segmentId, context.tenantId(), context.userId());

        return ResponseEntity.noContent()
                .header("X-Correlation-Id", context.correlationId())
                .build();
    }

    @PostMapping("/{segmentId}/analyze")
    @Operation(summary = "Analyze a segment", description = "Performs analysis on a churn prediction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Analysis completed successfully"),
            @ApiResponse(responseCode = "404", description = "Prediction not found"),
            @ApiResponse(responseCode = "400", description = "Prediction is not active")
    })
    public ResponseEntity<PredictionAnalysisResponseDto> analyzePrediction(
            @Parameter(description = "Prediction ID", required = true)
            @PathVariable @NotBlank String segmentId,

            @RequestBody(required = false) AnalyzePredictionRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        AnalyzePredictionRequestDto actualRequest = request != null ? request : AnalyzePredictionRequestDto.create();

        AnalyzePredictionCommand command = new AnalyzePredictionCommand(
                segmentId,
                context.tenantId(),
                context.userId(),
                actualRequest.analysisOptions()
        );

        PredictionAnalysisResponseDto response = service.analyzePrediction(command);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @PostMapping("/{segmentId}/customers")
    @Operation(summary = "Add customers to segment", description = "Adds customers to an existing segment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customers added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or too many customers"),
            @ApiResponse(responseCode = "404", description = "Prediction not found")
    })
    public ResponseEntity<ChurnPredictionResponseDto> addCustomers(
            @Parameter(description = "Prediction ID", required = true)
            @PathVariable @NotBlank String segmentId,

            @Valid @RequestBody ModifyModelsRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        AddModelsToPredictionCommand command = new AddModelsToPredictionCommand(
                segmentId,
                context.tenantId(),
                context.userId(),
                request.customerIds()
        );

        ChurnPredictionResponseDto response = service.addCustomersToPrediction(command);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @DeleteMapping("/{segmentId}/customers")
    @Operation(summary = "Remove customers from segment", description = "Removes customers from an existing segment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customers removed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Prediction not found")
    })
    public ResponseEntity<ChurnPredictionResponseDto> removeCustomers(
            @Parameter(description = "Prediction ID", required = true)
            @PathVariable @NotBlank String segmentId,

            @Valid @RequestBody ModifyModelsRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        RemoveModelsFromPredictionCommand command = new RemoveModelsFromPredictionCommand(
                segmentId,
                context.tenantId(),
                context.userId(),
                request.customerIds()
        );

        ChurnPredictionResponseDto response = service.removeCustomersFromPrediction(command);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }
}

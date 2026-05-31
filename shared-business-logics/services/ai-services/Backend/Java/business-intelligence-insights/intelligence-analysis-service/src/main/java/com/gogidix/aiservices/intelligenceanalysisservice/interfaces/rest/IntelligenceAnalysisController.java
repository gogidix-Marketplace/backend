package com.gogidix.aiservices.intelligenceanalysisservice.interfaces.rest;

import com.gogidix.aiservices.intelligenceanalysisservice.application.command.AddIntelligenceReportsToAnalysisCommand;
import com.gogidix.aiservices.intelligenceanalysisservice.application.command.AnalyzeAnalysisCommand;
import com.gogidix.aiservices.intelligenceanalysisservice.application.command.CreateAnalysisCommand;
import com.gogidix.aiservices.intelligenceanalysisservice.application.command.DeleteAnalysisCommand;
import com.gogidix.aiservices.intelligenceanalysisservice.application.command.RemoveIntelligenceReportsFromAnalysisCommand;
import com.gogidix.aiservices.intelligenceanalysisservice.application.command.UpdateAnalysisCommand;
import com.gogidix.aiservices.intelligenceanalysisservice.application.dto.AnalyzeAnalysisRequestDto;
import com.gogidix.aiservices.intelligenceanalysisservice.application.dto.CreateAnalysisRequestDto;
import com.gogidix.aiservices.intelligenceanalysisservice.application.dto.IntelligenceAnalysisResponseDto;
import com.gogidix.aiservices.intelligenceanalysisservice.application.dto.ModifyIntelligenceReportsRequestDto;
import com.gogidix.aiservices.intelligenceanalysisservice.application.dto.PagedResponseDto;
import com.gogidix.aiservices.intelligenceanalysisservice.application.dto.AnalysisAnalysisResponseDto;
import com.gogidix.aiservices.intelligenceanalysisservice.application.dto.AnalysisSearchRequestDto;
import com.gogidix.aiservices.intelligenceanalysisservice.application.dto.UpdateAnalysisRequestDto;
import com.gogidix.aiservices.intelligenceanalysisservice.application.service.IntelligenceAnalysisApplicationService;
import com.gogidix.aiservices.intelligenceanalysisservice.domain.model.AnalysisType;
import com.gogidix.aiservices.intelligenceanalysisservice.shared.context.RequestContext;
import com.gogidix.aiservices.intelligenceanalysisservice.shared.context.RequestContextHolder;
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
 * REST controller for customer segment operations.
 */
@RestController
@RequestMapping("/segments")
@Tag(name = "IntelligenceReport Analysiss", description = "APIs for managing customer segments")
public class IntelligenceAnalysisController {

    private final IntelligenceAnalysisApplicationService service;

    public IntelligenceAnalysisController(IntelligenceAnalysisApplicationService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create a new segment", description = "Creates a new customer segment with the provided criteria")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Analysis created successfully",
                    headers = @Header(name = "X-Correlation-Id", description = "Correlation ID for tracking"),
                    content = @Content(schema = @Schema(implementation = IntelligenceAnalysisResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "409", description = "Maximum segments per tenant reached")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<IntelligenceAnalysisResponseDto> createAnalysis(
            @Valid @RequestBody CreateAnalysisRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        CreateAnalysisCommand command = new CreateAnalysisCommand(
                request.name(),
                request.description(),
                request.segmentType(),
                request.criteria(),
                context.tenantId(),
                context.userId()
        );

        IntelligenceAnalysisResponseDto response = service.createAnalysis(command);

        return ResponseEntity
                .created(URI.create("/segments/" + response.id()))
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping("/{segmentId}")
    @Operation(summary = "Get a segment by ID", description = "Retrieves a customer segment by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Analysis retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Analysis not found")
    })
    public ResponseEntity<IntelligenceAnalysisResponseDto> getAnalysis(
            @Parameter(description = "Analysis ID", required = true)
            @PathVariable @NotBlank String segmentId
    ) {
        RequestContext context = RequestContextHolder.getContext();
        IntelligenceAnalysisResponseDto response = service.getAnalysisById(segmentId, context.tenantId());

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping
    @Operation(summary = "List segments", description = "Retrieves a paginated list of segments for the current tenant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Analysiss retrieved successfully")
    })
    public ResponseEntity<PagedResponseDto<IntelligenceAnalysisResponseDto>> listAnalysiss(
            @Parameter(description = "Filter by segment type")
            @RequestParam(required = false) AnalysisType segmentType,

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

        PagedResponseDto<IntelligenceAnalysisResponseDto> response = service.getAnalysissByTenant(
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
    @Operation(summary = "Update a segment", description = "Updates an existing customer segment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Analysis updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Analysis not found")
    })
    public ResponseEntity<IntelligenceAnalysisResponseDto> updateAnalysis(
            @Parameter(description = "Analysis ID", required = true)
            @PathVariable @NotBlank String segmentId,

            @Valid @RequestBody UpdateAnalysisRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        UpdateAnalysisCommand command = new UpdateAnalysisCommand(
                segmentId,
                request.name(),
                request.description(),
                request.segmentType(),
                request.criteria(),
                request.active(),
                context.tenantId(),
                context.userId()
        );

        IntelligenceAnalysisResponseDto response = service.updateAnalysis(command);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @DeleteMapping("/{segmentId}")
    @Operation(summary = "Delete a segment", description = "Deletes a customer segment by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Analysis deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Analysis not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteAnalysis(
            @Parameter(description = "Analysis ID", required = true)
            @PathVariable @NotBlank String segmentId
    ) {
        RequestContext context = RequestContextHolder.getContext();

        service.deleteAnalysis(segmentId, context.tenantId(), context.userId());

        return ResponseEntity.noContent()
                .header("X-Correlation-Id", context.correlationId())
                .build();
    }

    @PostMapping("/{segmentId}/analyze")
    @Operation(summary = "Analyze a segment", description = "Performs analysis on a customer segment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Analysis completed successfully"),
            @ApiResponse(responseCode = "404", description = "Analysis not found"),
            @ApiResponse(responseCode = "400", description = "Analysis is not active")
    })
    public ResponseEntity<AnalysisAnalysisResponseDto> analyzeAnalysis(
            @Parameter(description = "Analysis ID", required = true)
            @PathVariable @NotBlank String segmentId,

            @RequestBody(required = false) AnalyzeAnalysisRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        AnalyzeAnalysisRequestDto actualRequest = request != null ? request : AnalyzeAnalysisRequestDto.create();

        AnalyzeAnalysisCommand command = new AnalyzeAnalysisCommand(
                segmentId,
                context.tenantId(),
                context.userId(),
                actualRequest.analysisOptions()
        );

        AnalysisAnalysisResponseDto response = service.analyzeAnalysis(command);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @PostMapping("/{segmentId}/customers")
    @Operation(summary = "Add customers to segment", description = "Adds customers to an existing segment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "IntelligenceReports added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or too many customers"),
            @ApiResponse(responseCode = "404", description = "Analysis not found")
    })
    public ResponseEntity<IntelligenceAnalysisResponseDto> addIntelligenceReports(
            @Parameter(description = "Analysis ID", required = true)
            @PathVariable @NotBlank String segmentId,

            @Valid @RequestBody ModifyIntelligenceReportsRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        AddIntelligenceReportsToAnalysisCommand command = new AddIntelligenceReportsToAnalysisCommand(
                segmentId,
                context.tenantId(),
                context.userId(),
                request.customerIds()
        );

        IntelligenceAnalysisResponseDto response = service.addIntelligenceReportsToAnalysis(command);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @DeleteMapping("/{segmentId}/customers")
    @Operation(summary = "Remove customers from segment", description = "Removes customers from an existing segment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "IntelligenceReports removed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Analysis not found")
    })
    public ResponseEntity<IntelligenceAnalysisResponseDto> removeIntelligenceReports(
            @Parameter(description = "Analysis ID", required = true)
            @PathVariable @NotBlank String segmentId,

            @Valid @RequestBody ModifyIntelligenceReportsRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        RemoveIntelligenceReportsFromAnalysisCommand command = new RemoveIntelligenceReportsFromAnalysisCommand(
                segmentId,
                context.tenantId(),
                context.userId(),
                request.customerIds()
        );

        IntelligenceAnalysisResponseDto response = service.removeIntelligenceReportsFromAnalysis(command);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }
}

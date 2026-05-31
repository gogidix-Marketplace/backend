package com.gogidix.aiservices.aicustomersegmentationservice.interfaces.rest;

import com.gogidix.aiservices.aicustomersegmentationservice.application.command.AddCustomersToSegmentCommand;
import com.gogidix.aiservices.aicustomersegmentationservice.application.command.AnalyzeSegmentCommand;
import com.gogidix.aiservices.aicustomersegmentationservice.application.command.CreateSegmentCommand;
import com.gogidix.aiservices.aicustomersegmentationservice.application.command.DeleteSegmentCommand;
import com.gogidix.aiservices.aicustomersegmentationservice.application.command.RemoveCustomersFromSegmentCommand;
import com.gogidix.aiservices.aicustomersegmentationservice.application.command.UpdateSegmentCommand;
import com.gogidix.aiservices.aicustomersegmentationservice.application.dto.AnalyzeSegmentRequestDto;
import com.gogidix.aiservices.aicustomersegmentationservice.application.dto.CreateSegmentRequestDto;
import com.gogidix.aiservices.aicustomersegmentationservice.application.dto.CustomerSegmentResponseDto;
import com.gogidix.aiservices.aicustomersegmentationservice.application.dto.ModifyCustomersRequestDto;
import com.gogidix.aiservices.aicustomersegmentationservice.application.dto.PagedResponseDto;
import com.gogidix.aiservices.aicustomersegmentationservice.application.dto.SegmentAnalysisResponseDto;
import com.gogidix.aiservices.aicustomersegmentationservice.application.dto.SegmentSearchRequestDto;
import com.gogidix.aiservices.aicustomersegmentationservice.application.dto.UpdateSegmentRequestDto;
import com.gogidix.aiservices.aicustomersegmentationservice.application.service.CustomerSegmentApplicationService;
import com.gogidix.aiservices.aicustomersegmentationservice.domain.model.SegmentType;
import com.gogidix.aiservices.aicustomersegmentationservice.shared.context.RequestContext;
import com.gogidix.aiservices.aicustomersegmentationservice.shared.context.RequestContextHolder;
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
@Tag(name = "Customer Segments", description = "APIs for managing customer segments")
public class CustomerSegmentController {

    private final CustomerSegmentApplicationService service;

    public CustomerSegmentController(CustomerSegmentApplicationService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create a new segment", description = "Creates a new customer segment with the provided criteria")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Segment created successfully",
                    headers = @Header(name = "X-Correlation-Id", description = "Correlation ID for tracking"),
                    content = @Content(schema = @Schema(implementation = CustomerSegmentResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "409", description = "Maximum segments per tenant reached")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CustomerSegmentResponseDto> createSegment(
            @Valid @RequestBody CreateSegmentRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        CreateSegmentCommand command = new CreateSegmentCommand(
                request.name(),
                request.description(),
                request.segmentType(),
                request.criteria(),
                context.tenantId(),
                context.userId()
        );

        CustomerSegmentResponseDto response = service.createSegment(command);

        return ResponseEntity
                .created(URI.create("/segments/" + response.id()))
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping("/{segmentId}")
    @Operation(summary = "Get a segment by ID", description = "Retrieves a customer segment by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Segment retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Segment not found")
    })
    public ResponseEntity<CustomerSegmentResponseDto> getSegment(
            @Parameter(description = "Segment ID", required = true)
            @PathVariable @NotBlank String segmentId
    ) {
        RequestContext context = RequestContextHolder.getContext();
        CustomerSegmentResponseDto response = service.getSegmentById(segmentId, context.tenantId());

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping
    @Operation(summary = "List segments", description = "Retrieves a paginated list of segments for the current tenant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Segments retrieved successfully")
    })
    public ResponseEntity<PagedResponseDto<CustomerSegmentResponseDto>> listSegments(
            @Parameter(description = "Filter by segment type")
            @RequestParam(required = false) SegmentType segmentType,

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

        PagedResponseDto<CustomerSegmentResponseDto> response = service.getSegmentsByTenant(
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
            @ApiResponse(responseCode = "200", description = "Segment updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Segment not found")
    })
    public ResponseEntity<CustomerSegmentResponseDto> updateSegment(
            @Parameter(description = "Segment ID", required = true)
            @PathVariable @NotBlank String segmentId,

            @Valid @RequestBody UpdateSegmentRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        UpdateSegmentCommand command = new UpdateSegmentCommand(
                segmentId,
                request.name(),
                request.description(),
                request.segmentType(),
                request.criteria(),
                request.active(),
                context.tenantId(),
                context.userId()
        );

        CustomerSegmentResponseDto response = service.updateSegment(command);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @DeleteMapping("/{segmentId}")
    @Operation(summary = "Delete a segment", description = "Deletes a customer segment by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Segment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Segment not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteSegment(
            @Parameter(description = "Segment ID", required = true)
            @PathVariable @NotBlank String segmentId
    ) {
        RequestContext context = RequestContextHolder.getContext();

        service.deleteSegment(segmentId, context.tenantId(), context.userId());

        return ResponseEntity.noContent()
                .header("X-Correlation-Id", context.correlationId())
                .build();
    }

    @PostMapping("/{segmentId}/analyze")
    @Operation(summary = "Analyze a segment", description = "Performs analysis on a customer segment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Analysis completed successfully"),
            @ApiResponse(responseCode = "404", description = "Segment not found"),
            @ApiResponse(responseCode = "400", description = "Segment is not active")
    })
    public ResponseEntity<SegmentAnalysisResponseDto> analyzeSegment(
            @Parameter(description = "Segment ID", required = true)
            @PathVariable @NotBlank String segmentId,

            @RequestBody(required = false) AnalyzeSegmentRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        AnalyzeSegmentRequestDto actualRequest = request != null ? request : AnalyzeSegmentRequestDto.create();

        AnalyzeSegmentCommand command = new AnalyzeSegmentCommand(
                segmentId,
                context.tenantId(),
                context.userId(),
                actualRequest.analysisOptions()
        );

        SegmentAnalysisResponseDto response = service.analyzeSegment(command);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @PostMapping("/{segmentId}/customers")
    @Operation(summary = "Add customers to segment", description = "Adds customers to an existing segment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customers added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or too many customers"),
            @ApiResponse(responseCode = "404", description = "Segment not found")
    })
    public ResponseEntity<CustomerSegmentResponseDto> addCustomers(
            @Parameter(description = "Segment ID", required = true)
            @PathVariable @NotBlank String segmentId,

            @Valid @RequestBody ModifyCustomersRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        AddCustomersToSegmentCommand command = new AddCustomersToSegmentCommand(
                segmentId,
                context.tenantId(),
                context.userId(),
                request.customerIds()
        );

        CustomerSegmentResponseDto response = service.addCustomersToSegment(command);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @DeleteMapping("/{segmentId}/customers")
    @Operation(summary = "Remove customers from segment", description = "Removes customers from an existing segment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customers removed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Segment not found")
    })
    public ResponseEntity<CustomerSegmentResponseDto> removeCustomers(
            @Parameter(description = "Segment ID", required = true)
            @PathVariable @NotBlank String segmentId,

            @Valid @RequestBody ModifyCustomersRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        RemoveCustomersFromSegmentCommand command = new RemoveCustomersFromSegmentCommand(
                segmentId,
                context.tenantId(),
                context.userId(),
                request.customerIds()
        );

        CustomerSegmentResponseDto response = service.removeCustomersFromSegment(command);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }
}

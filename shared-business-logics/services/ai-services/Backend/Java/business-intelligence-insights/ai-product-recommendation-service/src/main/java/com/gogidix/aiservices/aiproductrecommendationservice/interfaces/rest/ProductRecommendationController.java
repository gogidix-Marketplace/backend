package com.gogidix.aiservices.aiproductrecommendationservice.interfaces.rest;

import com.gogidix.aiservices.aiproductrecommendationservice.application.command.AddProductsToRecommendationCommand;
import com.gogidix.aiservices.aiproductrecommendationservice.application.command.AnalyzeRecommendationCommand;
import com.gogidix.aiservices.aiproductrecommendationservice.application.command.CreateRecommendationCommand;
import com.gogidix.aiservices.aiproductrecommendationservice.application.command.DeleteRecommendationCommand;
import com.gogidix.aiservices.aiproductrecommendationservice.application.command.RemoveProductsFromRecommendationCommand;
import com.gogidix.aiservices.aiproductrecommendationservice.application.command.UpdateRecommendationCommand;
import com.gogidix.aiservices.aiproductrecommendationservice.application.dto.AnalyzeRecommendationRequestDto;
import com.gogidix.aiservices.aiproductrecommendationservice.application.dto.CreateRecommendationRequestDto;
import com.gogidix.aiservices.aiproductrecommendationservice.application.dto.ProductRecommendationResponseDto;
import com.gogidix.aiservices.aiproductrecommendationservice.application.dto.ModifyProductsRequestDto;
import com.gogidix.aiservices.aiproductrecommendationservice.application.dto.PagedResponseDto;
import com.gogidix.aiservices.aiproductrecommendationservice.application.dto.RecommendationAnalysisResponseDto;
import com.gogidix.aiservices.aiproductrecommendationservice.application.dto.RecommendationSearchRequestDto;
import com.gogidix.aiservices.aiproductrecommendationservice.application.dto.UpdateRecommendationRequestDto;
import com.gogidix.aiservices.aiproductrecommendationservice.application.service.ProductRecommendationApplicationService;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationType;
import com.gogidix.aiservices.aiproductrecommendationservice.shared.context.RequestContext;
import com.gogidix.aiservices.aiproductrecommendationservice.shared.context.RequestContextHolder;
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
 * REST controller for product recommendation operations.
 */
@RestController
@RequestMapping("/segments")
@Tag(name = "Product Recommendations", description = "APIs for managing product recommendations")
public class ProductRecommendationController {

    private final ProductRecommendationApplicationService service;

    public ProductRecommendationController(ProductRecommendationApplicationService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create a new segment", description = "Creates a new product recommendation with the provided criteria")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Recommendation created successfully",
                    headers = @Header(name = "X-Correlation-Id", description = "Correlation ID for tracking"),
                    content = @Content(schema = @Schema(implementation = ProductRecommendationResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "409", description = "Maximum segments per tenant reached")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductRecommendationResponseDto> createRecommendation(
            @Valid @RequestBody CreateRecommendationRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        CreateRecommendationCommand command = new CreateRecommendationCommand(
                request.name(),
                request.description(),
                request.segmentType(),
                request.criteria(),
                context.tenantId(),
                context.userId()
        );

        ProductRecommendationResponseDto response = service.createRecommendation(command);

        return ResponseEntity
                .created(URI.create("/segments/" + response.id()))
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping("/{segmentId}")
    @Operation(summary = "Get a segment by ID", description = "Retrieves a product recommendation by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recommendation retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Recommendation not found")
    })
    public ResponseEntity<ProductRecommendationResponseDto> getRecommendation(
            @Parameter(description = "Recommendation ID", required = true)
            @PathVariable @NotBlank String segmentId
    ) {
        RequestContext context = RequestContextHolder.getContext();
        ProductRecommendationResponseDto response = service.getRecommendationById(segmentId, context.tenantId());

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping
    @Operation(summary = "List segments", description = "Retrieves a paginated list of segments for the current tenant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recommendations retrieved successfully")
    })
    public ResponseEntity<PagedResponseDto<ProductRecommendationResponseDto>> listRecommendations(
            @Parameter(description = "Filter by segment type")
            @RequestParam(required = false) RecommendationType segmentType,

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

        PagedResponseDto<ProductRecommendationResponseDto> response = service.getRecommendationsByTenant(
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
    @Operation(summary = "Update a segment", description = "Updates an existing product recommendation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recommendation updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Recommendation not found")
    })
    public ResponseEntity<ProductRecommendationResponseDto> updateRecommendation(
            @Parameter(description = "Recommendation ID", required = true)
            @PathVariable @NotBlank String segmentId,

            @Valid @RequestBody UpdateRecommendationRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        UpdateRecommendationCommand command = new UpdateRecommendationCommand(
                segmentId,
                request.name(),
                request.description(),
                request.segmentType(),
                request.criteria(),
                request.active(),
                context.tenantId(),
                context.userId()
        );

        ProductRecommendationResponseDto response = service.updateRecommendation(command);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @DeleteMapping("/{segmentId}")
    @Operation(summary = "Delete a segment", description = "Deletes a product recommendation by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Recommendation deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Recommendation not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteRecommendation(
            @Parameter(description = "Recommendation ID", required = true)
            @PathVariable @NotBlank String segmentId
    ) {
        RequestContext context = RequestContextHolder.getContext();

        service.deleteRecommendation(segmentId, context.tenantId(), context.userId());

        return ResponseEntity.noContent()
                .header("X-Correlation-Id", context.correlationId())
                .build();
    }

    @PostMapping("/{segmentId}/analyze")
    @Operation(summary = "Analyze a segment", description = "Performs analysis on a product recommendation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Analysis completed successfully"),
            @ApiResponse(responseCode = "404", description = "Recommendation not found"),
            @ApiResponse(responseCode = "400", description = "Recommendation is not active")
    })
    public ResponseEntity<RecommendationAnalysisResponseDto> analyzeRecommendation(
            @Parameter(description = "Recommendation ID", required = true)
            @PathVariable @NotBlank String segmentId,

            @RequestBody(required = false) AnalyzeRecommendationRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        AnalyzeRecommendationRequestDto actualRequest = request != null ? request : AnalyzeRecommendationRequestDto.create();

        AnalyzeRecommendationCommand command = new AnalyzeRecommendationCommand(
                segmentId,
                context.tenantId(),
                context.userId(),
                actualRequest.analysisOptions()
        );

        RecommendationAnalysisResponseDto response = service.analyzeRecommendation(command);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @PostMapping("/{segmentId}/customers")
    @Operation(summary = "Add customers to segment", description = "Adds customers to an existing segment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or too many customers"),
            @ApiResponse(responseCode = "404", description = "Recommendation not found")
    })
    public ResponseEntity<ProductRecommendationResponseDto> addProducts(
            @Parameter(description = "Recommendation ID", required = true)
            @PathVariable @NotBlank String segmentId,

            @Valid @RequestBody ModifyProductsRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        AddProductsToRecommendationCommand command = new AddProductsToRecommendationCommand(
                segmentId,
                context.tenantId(),
                context.userId(),
                request.customerIds()
        );

        ProductRecommendationResponseDto response = service.addProductsToRecommendation(command);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @DeleteMapping("/{segmentId}/customers")
    @Operation(summary = "Remove customers from segment", description = "Removes customers from an existing segment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products removed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Recommendation not found")
    })
    public ResponseEntity<ProductRecommendationResponseDto> removeProducts(
            @Parameter(description = "Recommendation ID", required = true)
            @PathVariable @NotBlank String segmentId,

            @Valid @RequestBody ModifyProductsRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        RemoveProductsFromRecommendationCommand command = new RemoveProductsFromRecommendationCommand(
                segmentId,
                context.tenantId(),
                context.userId(),
                request.customerIds()
        );

        ProductRecommendationResponseDto response = service.removeProductsFromRecommendation(command);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }
}

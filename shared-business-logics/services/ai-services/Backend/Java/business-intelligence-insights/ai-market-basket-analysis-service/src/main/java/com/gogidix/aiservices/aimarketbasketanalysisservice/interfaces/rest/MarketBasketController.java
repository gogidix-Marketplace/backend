package com.gogidix.aiservices.aimarketbasketanalysisservice.interfaces.rest;

import com.gogidix.aiservices.aimarketbasketanalysisservice.application.command.AddCustomersToBasketCommand;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.command.AnalyzeBasketCommand;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.command.CreateBasketCommand;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.command.DeleteBasketCommand;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.command.RemoveCustomersFromBasketCommand;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.command.UpdateBasketCommand;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.dto.AnalyzeBasketRequestDto;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.dto.CreateBasketRequestDto;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.dto.MarketBasketResponseDto;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.dto.ModifyCustomersRequestDto;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.dto.PagedResponseDto;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.dto.BasketAnalysisResponseDto;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.dto.BasketSearchRequestDto;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.dto.UpdateBasketRequestDto;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.service.MarketBasketApplicationService;
import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.BasketType;
import com.gogidix.aiservices.aimarketbasketanalysisservice.shared.context.RequestContext;
import com.gogidix.aiservices.aimarketbasketanalysisservice.shared.context.RequestContextHolder;
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
 * REST controller for market basket operations.
 */
@RestController
@RequestMapping("/segments")
@Tag(name = "Customer Baskets", description = "APIs for managing market baskets")
public class MarketBasketController {

    private final MarketBasketApplicationService service;

    public MarketBasketController(MarketBasketApplicationService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create a new segment", description = "Creates a new market basket with the provided criteria")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Basket created successfully",
                    headers = @Header(name = "X-Correlation-Id", description = "Correlation ID for tracking"),
                    content = @Content(schema = @Schema(implementation = MarketBasketResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "409", description = "Maximum segments per tenant reached")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<MarketBasketResponseDto> createBasket(
            @Valid @RequestBody CreateBasketRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        CreateBasketCommand command = new CreateBasketCommand(
                request.name(),
                request.description(),
                request.segmentType(),
                request.criteria(),
                context.tenantId(),
                context.userId()
        );

        MarketBasketResponseDto response = service.createBasket(command);

        return ResponseEntity
                .created(URI.create("/segments/" + response.id()))
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping("/{segmentId}")
    @Operation(summary = "Get a segment by ID", description = "Retrieves a market basket by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Basket retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Basket not found")
    })
    public ResponseEntity<MarketBasketResponseDto> getBasket(
            @Parameter(description = "Basket ID", required = true)
            @PathVariable @NotBlank String segmentId
    ) {
        RequestContext context = RequestContextHolder.getContext();
        MarketBasketResponseDto response = service.getBasketById(segmentId, context.tenantId());

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping
    @Operation(summary = "List segments", description = "Retrieves a paginated list of segments for the current tenant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Baskets retrieved successfully")
    })
    public ResponseEntity<PagedResponseDto<MarketBasketResponseDto>> listBaskets(
            @Parameter(description = "Filter by segment type")
            @RequestParam(required = false) BasketType segmentType,

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

        PagedResponseDto<MarketBasketResponseDto> response = service.getBasketsByTenant(
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
    @Operation(summary = "Update a segment", description = "Updates an existing market basket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Basket updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Basket not found")
    })
    public ResponseEntity<MarketBasketResponseDto> updateBasket(
            @Parameter(description = "Basket ID", required = true)
            @PathVariable @NotBlank String segmentId,

            @Valid @RequestBody UpdateBasketRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        UpdateBasketCommand command = new UpdateBasketCommand(
                segmentId,
                request.name(),
                request.description(),
                request.segmentType(),
                request.criteria(),
                request.active(),
                context.tenantId(),
                context.userId()
        );

        MarketBasketResponseDto response = service.updateBasket(command);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @DeleteMapping("/{segmentId}")
    @Operation(summary = "Delete a segment", description = "Deletes a market basket by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Basket deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Basket not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteBasket(
            @Parameter(description = "Basket ID", required = true)
            @PathVariable @NotBlank String segmentId
    ) {
        RequestContext context = RequestContextHolder.getContext();

        service.deleteBasket(segmentId, context.tenantId(), context.userId());

        return ResponseEntity.noContent()
                .header("X-Correlation-Id", context.correlationId())
                .build();
    }

    @PostMapping("/{segmentId}/analyze")
    @Operation(summary = "Analyze a segment", description = "Performs analysis on a market basket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Analysis completed successfully"),
            @ApiResponse(responseCode = "404", description = "Basket not found"),
            @ApiResponse(responseCode = "400", description = "Basket is not active")
    })
    public ResponseEntity<BasketAnalysisResponseDto> analyzeBasket(
            @Parameter(description = "Basket ID", required = true)
            @PathVariable @NotBlank String segmentId,

            @RequestBody(required = false) AnalyzeBasketRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        AnalyzeBasketRequestDto actualRequest = request != null ? request : AnalyzeBasketRequestDto.create();

        AnalyzeBasketCommand command = new AnalyzeBasketCommand(
                segmentId,
                context.tenantId(),
                context.userId(),
                actualRequest.analysisOptions()
        );

        BasketAnalysisResponseDto response = service.analyzeBasket(command);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @PostMapping("/{segmentId}/customers")
    @Operation(summary = "Add customers to segment", description = "Adds customers to an existing segment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customers added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or too many customers"),
            @ApiResponse(responseCode = "404", description = "Basket not found")
    })
    public ResponseEntity<MarketBasketResponseDto> addCustomers(
            @Parameter(description = "Basket ID", required = true)
            @PathVariable @NotBlank String segmentId,

            @Valid @RequestBody ModifyCustomersRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        AddCustomersToBasketCommand command = new AddCustomersToBasketCommand(
                segmentId,
                context.tenantId(),
                context.userId(),
                request.customerIds()
        );

        MarketBasketResponseDto response = service.addCustomersToBasket(command);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @DeleteMapping("/{segmentId}/customers")
    @Operation(summary = "Remove customers from segment", description = "Removes customers from an existing segment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customers removed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Basket not found")
    })
    public ResponseEntity<MarketBasketResponseDto> removeCustomers(
            @Parameter(description = "Basket ID", required = true)
            @PathVariable @NotBlank String segmentId,

            @Valid @RequestBody ModifyCustomersRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        RemoveCustomersFromBasketCommand command = new RemoveCustomersFromBasketCommand(
                segmentId,
                context.tenantId(),
                context.userId(),
                request.customerIds()
        );

        MarketBasketResponseDto response = service.removeCustomersFromBasket(command);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }
}

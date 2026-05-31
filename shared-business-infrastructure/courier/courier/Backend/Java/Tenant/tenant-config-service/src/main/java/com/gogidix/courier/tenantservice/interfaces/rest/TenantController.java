package com.gogidix.courier.tenantservice.interfaces.rest;

import com.gogidix.courier.tenantservice.application.dto.PagedResponseDto;
import com.gogidix.courier.tenantservice.application.dto.TenantConfigRequest;
import com.gogidix.courier.tenantservice.application.dto.TenantConfigResponse;
import com.gogidix.courier.tenantservice.application.dto.TenantRequest;
import com.gogidix.courier.tenantservice.application.dto.TenantResponse;
import com.gogidix.courier.tenantservice.application.service.TenantApplicationService;
import com.gogidix.courier.tenantservice.domain.entity.Tenant;
import com.gogidix.courier.tenantservice.shared.context.RequestContext;
import com.gogidix.courier.tenantservice.shared.context.RequestContextHolder;
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
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/**
 * REST controller for tenant operations.
 */
@RestController
@RequestMapping("/tenants")
@Tag(name = "Tenants", description = "APIs for managing tenants")
public class TenantController {

    private final TenantApplicationService service;

    public TenantController(TenantApplicationService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create a new tenant", description = "Creates a new tenant with the provided details")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Tenant created successfully",
                    headers = @Header(name = "X-Correlation-Id", description = "Correlation ID for tracking"),
                    content = @Content(schema = @Schema(implementation = TenantResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "409", description = "Tenant already exists")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TenantResponse> createTenant(
            @Valid @RequestBody TenantRequest request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        TenantResponse response = service.createTenant(request, context.userId());

        return ResponseEntity
                .created(URI.create("/tenants/" + response.id()))
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a tenant by ID", description = "Retrieves a tenant by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tenant retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Tenant not found")
    })
    public ResponseEntity<TenantResponse> getTenant(
            @Parameter(description = "Tenant ID", required = true)
            @PathVariable @NotBlank String id
    ) {
        RequestContext context = RequestContextHolder.getContext();
        TenantResponse response = service.getTenantById(id);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping
    @Operation(summary = "List tenants", description = "Retrieves a paginated list of tenants")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tenants retrieved successfully")
    })
    public ResponseEntity<PagedResponseDto<TenantResponse>> listTenants(
            @Parameter(description = "Filter by name (partial match)")
            @RequestParam(required = false) String name,

            @Parameter(description = "Filter by status")
            @RequestParam(required = false) Tenant.TenantStatus status,

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

        PagedResponseDto<TenantResponse> response = service.listTenants(
                name, status, page, size, sortBy, sortDirection
        );

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a tenant", description = "Updates an existing tenant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tenant updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Tenant not found"),
            @ApiResponse(responseCode = "409", description = "Tenant name already exists")
    })
    public ResponseEntity<TenantResponse> updateTenant(
            @Parameter(description = "Tenant ID", required = true)
            @PathVariable @NotBlank String id,

            @Valid @RequestBody TenantRequest request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        TenantResponse response = service.updateTenant(id, request, context.userId());

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a tenant", description = "Deletes a tenant by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tenant deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Tenant not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteTenant(
            @Parameter(description = "Tenant ID", required = true)
            @PathVariable @NotBlank String id
    ) {
        RequestContext context = RequestContextHolder.getContext();

        service.deleteTenant(id);

        return ResponseEntity.noContent()
                .header("X-Correlation-Id", context.correlationId())
                .build();
    }

    @GetMapping("/{id}/config")
    @Operation(summary = "Get tenant configuration", description = "Retrieves the configuration for a tenant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configuration retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Tenant not found")
    })
    public ResponseEntity<TenantConfigResponse> getTenantConfig(
            @Parameter(description = "Tenant ID", required = true)
            @PathVariable @NotBlank String id
    ) {
        RequestContext context = RequestContextHolder.getContext();

        TenantConfigResponse response = service.getTenantConfig(id);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @PutMapping("/{id}/config")
    @Operation(summary = "Update tenant configuration", description = "Updates the configuration for a tenant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configuration updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Tenant not found")
    })
    public ResponseEntity<TenantConfigResponse> updateTenantConfig(
            @Parameter(description = "Tenant ID", required = true)
            @PathVariable @NotBlank String id,

            @Valid @RequestBody TenantConfigRequest request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        TenantConfigResponse response = service.updateTenantConfig(id, request, context.userId());

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @PostMapping("/{id}/activate")
    @Operation(summary = "Activate a tenant", description = "Activates a tenant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tenant activated successfully"),
            @ApiResponse(responseCode = "400", description = "Tenant cannot be activated"),
            @ApiResponse(responseCode = "404", description = "Tenant not found")
    })
    public ResponseEntity<TenantResponse> activateTenant(
            @Parameter(description = "Tenant ID", required = true)
            @PathVariable @NotBlank String id
    ) {
        RequestContext context = RequestContextHolder.getContext();

        TenantResponse response = service.activateTenant(id);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @PostMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate a tenant", description = "Deactivates a tenant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tenant deactivated successfully"),
            @ApiResponse(responseCode = "400", description = "Tenant cannot be deactivated"),
            @ApiResponse(responseCode = "404", description = "Tenant not found")
    })
    public ResponseEntity<TenantResponse> deactivateTenant(
            @Parameter(description = "Tenant ID", required = true)
            @PathVariable @NotBlank String id
    ) {
        RequestContext context = RequestContextHolder.getContext();

        TenantResponse response = service.deactivateTenant(id);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }
}

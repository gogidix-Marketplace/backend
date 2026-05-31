package com.gogidix.courier.assignmentservice.interfaces.rest;

import com.gogidix.courier.assignmentservice.application.dto.*;
import com.gogidix.courier.assignmentservice.application.query.AssignmentQuery;
import com.gogidix.courier.assignmentservice.application.service.AssignmentApplicationService;
import com.gogidix.courier.assignmentservice.domain.entity.DriverAssignment;
import com.gogidix.courier.assignmentservice.shared.context.RequestContext;
import com.gogidix.courier.assignmentservice.shared.context.RequestContextHolder;
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
import java.util.List;

/**
 * REST controller for driver assignment operations.
 */
@RestController
@RequestMapping("/assignments")
@Tag(name = "Driver Assignments", description = "APIs for managing driver assignments and dispatch optimization")
public class AssignmentController {

    private final AssignmentApplicationService service;

    public AssignmentController(AssignmentApplicationService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create a new driver assignment", description = "Creates a new driver assignment for a dispatch order with intelligent optimization")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Assignment created successfully",
                    headers = @Header(name = "X-Correlation-Id", description = "Correlation ID for tracking"),
                    content = @Content(schema = @Schema(implementation = AssignmentResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "409", description = "Active assignment already exists for dispatch")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AssignmentResponse> createAssignment(
            @Valid @RequestBody AssignmentRequest request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        AssignmentResponse response = service.createAssignment(request, context.userId());

        return ResponseEntity
                .created(URI.create("/assignments/" + response.id()))
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an assignment by ID", description = "Retrieves a driver assignment by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assignment retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Assignment not found")
    })
    public ResponseEntity<AssignmentResponse> getAssignment(
            @Parameter(description = "Assignment ID", required = true)
            @PathVariable @NotBlank String id
    ) {
        RequestContext context = RequestContextHolder.getContext();
        AssignmentResponse response = service.getAssignmentById(id);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping
    @Operation(summary = "List assignments", description = "Retrieves a paginated list of driver assignments with filtering")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assignments retrieved successfully")
    })
    public ResponseEntity<PagedResponseDto<AssignmentResponse>> listAssignments(
            @Parameter(description = "Filter by dispatch ID")
            @RequestParam(required = false) String dispatchId,

            @Parameter(description = "Filter by driver ID")
            @RequestParam(required = false) String driverId,

            @Parameter(description = "Filter by status")
            @RequestParam(required = false) DriverAssignment.AssignmentStatus status,

            @Parameter(description = "Filter by priority")
            @RequestParam(required = false) DriverAssignment.AssignmentPriority priority,

            @Parameter(description = "Filter by created after timestamp")
            @RequestParam(required = false) String createdAfter,

            @Parameter(description = "Filter by created before timestamp")
            @RequestParam(required = false) String createdBefore,

            @Parameter(description = "Include only active assignments")
            @RequestParam(required = false, defaultValue = "false") boolean activeOnly,

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

        AssignmentQuery query = AssignmentQuery.create()
                .withDispatchId(dispatchId)
                .withDriverId(driverId)
                .withStatus(status)
                .withPriority(priority)
                .withActiveOnly(activeOnly)
                .withPagination(page, size)
                .withSort(sortBy, sortDirection);

        PagedResponseDto<AssignmentResponse> response = service.listAssignments(query);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @PutMapping("/{id}/assign")
    @Operation(summary = "Assign or reassign a driver", description = "Assigns a new driver to the assignment (reassignment)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Driver assigned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Assignment not found"),
            @ApiResponse(responseCode = "409", description = "Cannot reassign completed assignment")
    })
    public ResponseEntity<AssignmentResponse> assignDriver(
            @Parameter(description = "Assignment ID", required = true)
            @PathVariable @NotBlank String id,

            @Valid @RequestBody AssignDriverRequest request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        var command = new com.gogidix.courier.assignmentservice.application.command.AssignDriverCommand(
                id, request.driverId(), request.reason(), request.assignmentScore(), context.userId()
        );

        AssignmentResponse response = service.assignDriver(command, context.userId());

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @PostMapping("/{id}/accept")
    @Operation(summary = "Accept an assignment", description = "Driver accepts the assignment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assignment accepted successfully"),
            @ApiResponse(responseCode = "400", description = "Assignment cannot be accepted"),
            @ApiResponse(responseCode = "404", description = "Assignment not found")
    })
    public ResponseEntity<AssignmentResponse> acceptAssignment(
            @Parameter(description = "Assignment ID", required = true)
            @PathVariable @NotBlank String id
    ) {
        RequestContext context = RequestContextHolder.getContext();

        var command = new com.gogidix.courier.assignmentservice.application.command.AcceptAssignmentCommand(
                id, context.userId()
        );

        AssignmentResponse response = service.acceptAssignment(command);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @PostMapping("/{id}/start")
    @Operation(summary = "Start an assignment", description = "Driver starts the assignment (en route to pickup)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assignment started successfully"),
            @ApiResponse(responseCode = "400", description = "Assignment cannot be started"),
            @ApiResponse(responseCode = "404", description = "Assignment not found")
    })
    public ResponseEntity<AssignmentResponse> startAssignment(
            @Parameter(description = "Assignment ID", required = true)
            @PathVariable @NotBlank String id
    ) {
        RequestContext context = RequestContextHolder.getContext();

        var command = new com.gogidix.courier.assignmentservice.application.command.StartAssignmentCommand(
                id, context.userId()
        );

        AssignmentResponse response = service.startAssignment(command);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @PutMapping("/{id}/complete")
    @Operation(summary = "Complete an assignment", description = "Marks the assignment as completed with actual metrics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assignment completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Assignment not found")
    })
    public ResponseEntity<AssignmentResponse> completeAssignment(
            @Parameter(description = "Assignment ID", required = true)
            @PathVariable @NotBlank String id,

            @Valid @RequestBody CompleteAssignmentRequest request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        var command = new com.gogidix.courier.assignmentservice.application.command.CompleteAssignmentCommand(
                id, request.actualDistanceKm(), request.actualDurationMinutes(), context.userId()
        );

        AssignmentResponse response = service.completeAssignment(command);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel an assignment", description = "Cancels a driver assignment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assignment cancelled successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Assignment not found")
    })
    public ResponseEntity<AssignmentResponse> cancelAssignment(
            @Parameter(description = "Assignment ID", required = true)
            @PathVariable @NotBlank String id,

            @Valid @RequestBody CancelAssignmentRequest request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        var command = new com.gogidix.courier.assignmentservice.application.command.CancelAssignmentCommand(
                id, request.cancellationReason(), context.userId()
        );

        AssignmentResponse response = service.cancelAssignment(command);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an assignment", description = "Deletes an assignment by its ID (only terminal assignments)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Assignment deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Cannot delete active assignment"),
            @ApiResponse(responseCode = "404", description = "Assignment not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteAssignment(
            @Parameter(description = "Assignment ID", required = true)
            @PathVariable @NotBlank String id
    ) {
        RequestContext context = RequestContextHolder.getContext();

        service.deleteAssignment(id);

        return ResponseEntity.noContent()
                .header("X-Correlation-Id", context.correlationId())
                .build();
    }

    @GetMapping("/driver/{driverId}")
    @Operation(summary = "Get driver's assignments", description = "Retrieves all assignments for a specific driver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assignments retrieved successfully")
    })
    public ResponseEntity<List<AssignmentResponse>> getDriverAssignments(
            @Parameter(description = "Driver ID", required = true)
            @PathVariable @NotBlank String driverId,

            @Parameter(description = "Filter by active only")
            @RequestParam(required = false, defaultValue = "false") boolean activeOnly
    ) {
        RequestContext context = RequestContextHolder.getContext();

        List<AssignmentResponse> response;
        if (activeOnly) {
            response = service.getActiveAssignmentsByDriverId(driverId, context.tenantId());
        } else {
            response = service.getAssignmentsByDriverId(driverId, context.tenantId());
        }

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping("/dispatch/{dispatchId}")
    @Operation(summary = "Get dispatch assignments", description = "Retrieves all assignments for a specific dispatch order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assignments retrieved successfully")
    })
    public ResponseEntity<List<AssignmentResponse>> getDispatchAssignments(
            @Parameter(description = "Dispatch ID", required = true)
            @PathVariable @NotBlank String dispatchId
    ) {
        RequestContext context = RequestContextHolder.getContext();

        List<AssignmentResponse> response = service.getAssignmentsByDispatchId(dispatchId, context.tenantId());

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping("/dispatch/{dispatchId}/active")
    @Operation(summary = "Get active dispatch assignment", description = "Retrieves the active assignment for a specific dispatch order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active assignment retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No active assignment found")
    })
    public ResponseEntity<AssignmentResponse> getActiveDispatchAssignment(
            @Parameter(description = "Dispatch ID", required = true)
            @PathVariable @NotBlank String dispatchId
    ) {
        RequestContext context = RequestContextHolder.getContext();

        AssignmentResponse response = service.getActiveAssignmentByDispatchId(dispatchId, context.tenantId());

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping("/stats")
    @Operation(summary = "Get assignment statistics", description = "Retrieves comprehensive assignment statistics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    })
    public ResponseEntity<AssignmentStats> getAssignmentStats() {
        RequestContext context = RequestContextHolder.getContext();

        AssignmentStats response = service.getAssignmentStats(context.tenantId());

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }
}

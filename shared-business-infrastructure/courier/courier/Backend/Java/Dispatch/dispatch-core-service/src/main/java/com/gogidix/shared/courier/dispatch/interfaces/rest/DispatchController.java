package com.gogidix.shared.courier.dispatch.interfaces.rest;

import com.gogidix.shared.courier.dispatch.application.command.AssignDriverCommand;
import com.gogidix.shared.courier.dispatch.application.command.CancelDispatchCommand;
import com.gogidix.shared.courier.dispatch.application.command.CompleteDispatchCommand;
import com.gogidix.shared.courier.dispatch.application.command.CreateDispatchOrderCommand;
import com.gogidix.shared.courier.dispatch.application.dto.DispatchOrderDTO;
import com.gogidix.shared.courier.dispatch.application.mapper.DispatchOrderDtoMapper;
import com.gogidix.shared.courier.dispatch.application.query.DispatchQuery;
import com.gogidix.shared.courier.dispatch.application.service.DispatchApplicationService;
import com.gogidix.shared.courier.dispatch.domain.entity.DispatchStatus;
import com.gogidix.shared.courier.dispatch.interfaces.rest.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST Controller for Dispatch Management
 * Provides CRUD operations and dispatch lifecycle management
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/dispatch")
@RequiredArgsConstructor
public class DispatchController {

    private final DispatchApplicationService dispatchApplicationService;
    private final DispatchOrderDtoMapper dispatchOrderDtoMapper;

    /**
     * Create a new dispatch order
     * POST /api/v1/dispatch/orders
     */
    @PostMapping("/orders")
    public ResponseEntity<DispatchResponse> createDispatch(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @Valid @RequestBody DispatchRequest request) {
        log.info("POST /api/v1/dispatch/orders - Creating dispatch for tenant: {}", tenantId);

        // Convert request to command
        CreateDispatchOrderCommand command = CreateDispatchOrderCommand.builder()
                .dispatchId(UUID.randomUUID().toString())
                .orderId(request.getOrderId())
                .customerId(request.getCustomerId())
                .pickupLocation(mapToLocationCommand(request.getPickupLocation()))
                .deliveryLocation(mapToLocationCommand(request.getDeliveryLocation()))
                .pickupAddress(request.getPickupAddress())
                .deliveryAddress(request.getDeliveryAddress())
                .priority(request.getPriority() != null ? request.getPriority() : "MEDIUM")
                .estimatedPickupTime(request.getEstimatedPickupTime())
                .estimatedDeliveryTime(request.getEstimatedDeliveryTime())
                .metadata(request.getMetadata())
                .totalAmount(request.getTotalAmount())
                .currency(request.getCurrency())
                .build();

        DispatchOrderDTO dispatchDTO = dispatchApplicationService.createDispatch(tenantId, command);

        return ResponseEntity.status(HttpStatus.CREATED).body(DispatchResponse.fromDTO(dispatchDTO));
    }

    /**
     * Get dispatch order by ID
     * GET /api/v1/dispatch/orders/{id}
     */
    @GetMapping("/orders/{id}")
    public ResponseEntity<DispatchResponse> getDispatch(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @PathVariable String id) {
        log.info("GET /api/v1/dispatch/orders/{} - Fetching dispatch for tenant: {}", id, tenantId);

        DispatchOrderDTO dispatchDTO = dispatchApplicationService.getDispatch(tenantId, id);

        return ResponseEntity.ok(DispatchResponse.fromDTO(dispatchDTO));
    }

    /**
     * List dispatch orders with optional filters
     * GET /api/v1/dispatch/orders?status=PENDING&driverId=xxx&customerId=xxx
     */
    @GetMapping("/orders")
    public ResponseEntity<List<DispatchResponse>> listDispatches(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String driverId,
            @RequestParam(required = false) String customerId) {
        log.info("GET /api/v1/dispatch/orders - Listing dispatches for tenant: {}", tenantId);

        DispatchQuery query = DispatchQuery.builder()
                .status(status)
                .driverId(driverId)
                .customerId(customerId)
                .build();

        List<DispatchOrderDTO> dispatches = dispatchApplicationService.queryDispatches(tenantId, query);

        List<DispatchResponse> response = dispatches.stream()
                .map(DispatchResponse::fromDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Assign driver to dispatch order
     * PUT /api/v1/dispatch/orders/{id}/assign
     */
    @PutMapping("/orders/{id}/assign")
    public ResponseEntity<DispatchResponse> assignDriver(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @PathVariable String id,
            @Valid @RequestBody AssignmentRequest request) {
        log.info("PUT /api/v1/dispatch/orders/{}/assign - Assigning driver: {}", id, request.getDriverId());

        AssignDriverCommand command = AssignDriverCommand.builder()
                .driverId(request.getDriverId())
                .vehicleId(request.getVehicleId())
                .notes(request.getNotes())
                .build();

        DispatchOrderDTO dispatchDTO = dispatchApplicationService.assignDriver(tenantId, id, command);

        return ResponseEntity.ok(DispatchResponse.fromDTO(dispatchDTO));
    }

    /**
     * Cancel dispatch order
     * PUT /api/v1/dispatch/orders/{id}/cancel
     */
    @PutMapping("/orders/{id}/cancel")
    public ResponseEntity<DispatchResponse> cancelDispatch(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @PathVariable String id,
            @Valid @RequestBody CancellationRequest request) {
        log.info("PUT /api/v1/dispatch/orders/{}/cancel - Cancelling dispatch", id);

        CancelDispatchCommand command = CancelDispatchCommand.builder()
                .cancelledBy(request.getCancelledBy())
                .cancellationReason(request.getCancellationReason())
                .build();

        DispatchOrderDTO dispatchDTO = dispatchApplicationService.cancelDispatch(tenantId, id, command);

        return ResponseEntity.ok(DispatchResponse.fromDTO(dispatchDTO));
    }

    /**
     * Complete dispatch order
     * PUT /api/v1/dispatch/orders/{id}/complete
     */
    @PutMapping("/orders/{id}/complete")
    public ResponseEntity<DispatchResponse> completeDispatch(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @PathVariable String id,
            @Valid @RequestBody CompletionRequest request) {
        log.info("PUT /api/v1/dispatch/orders/{}/complete - Completing dispatch", id);

        CompleteDispatchCommand command = CompleteDispatchCommand.builder()
                .actualDistanceMeters(request.getActualDistanceMeters())
                .actualDurationMinutes(request.getActualDurationMinutes())
                .deliveryNotes(request.getDeliveryNotes())
                .signature(request.getSignature())
                .build();

        DispatchOrderDTO dispatchDTO = dispatchApplicationService.completeDispatch(tenantId, id, command);

        return ResponseEntity.ok(DispatchResponse.fromDTO(dispatchDTO));
    }

    /**
     * Get dashboard statistics
     * GET /api/v1/dispatch/dashboard
     */
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard(
            @RequestHeader("X-Tenant-ID") String tenantId) {
        log.info("GET /api/v1/dispatch/dashboard - Fetching dashboard for tenant: {}", tenantId);

        Map<String, Object> dashboard = dispatchApplicationService.getDashboard(tenantId);

        return ResponseEntity.ok(dashboard);
    }

    /**
     * Find nearby pickups (geospatial query)
     * GET /api/v1/dispatch/nearby-pickups?latitude=xx&longitude=xx&radius=5
     */
    @GetMapping("/nearby-pickups")
    public ResponseEntity<List<DispatchResponse>> findNearbyPickups(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "5.0") Double radiusKm) {
        log.info("GET /api/v1/dispatch/nearby-pickups - Finding nearby pickups for tenant: {}", tenantId);

        List<DispatchOrderDTO> nearbyPickups = dispatchApplicationService.findNearbyPickups(
                tenantId, latitude, longitude, radiusKm);

        List<DispatchResponse> response = nearbyPickups.stream()
                .map(DispatchResponse::fromDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Find nearby deliveries (geospatial query)
     * GET /api/v1/dispatch/nearby-deliveries?latitude=xx&longitude=xx&radius=5
     */
    @GetMapping("/nearby-deliveries")
    public ResponseEntity<List<DispatchResponse>> findNearbyDeliveries(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "5.0") Double radiusKm) {
        log.info("GET /api/v1/dispatch/nearby-deliveries - Finding nearby deliveries for tenant: {}", tenantId);

        List<DispatchOrderDTO> nearbyDeliveries = dispatchApplicationService.findNearbyDeliveries(
                tenantId, latitude, longitude, radiusKm);

        List<DispatchResponse> response = nearbyDeliveries.stream()
                .map(DispatchResponse::fromDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Get dispatches by status
     * GET /api/v1/dispatch/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<DispatchResponse>> getDispatchesByStatus(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @PathVariable String status) {
        log.info("GET /api/v1/dispatch/status/{} - Fetching dispatches for tenant: {}", status, tenantId);

        DispatchQuery query = DispatchQuery.builder()
                .status(status)
                .build();

        List<DispatchOrderDTO> dispatches = dispatchApplicationService.queryDispatches(tenantId, query);

        List<DispatchResponse> response = dispatches.stream()
                .map(DispatchResponse::fromDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Get dispatches for a specific driver
     * GET /api/v1/dispatch/drivers/{driverId}
     */
    @GetMapping("/drivers/{driverId}")
    public ResponseEntity<List<DispatchResponse>> getDriverDispatches(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @PathVariable String driverId) {
        log.info("GET /api/v1/dispatch/drivers/{} - Fetching dispatches for tenant: {}", driverId, tenantId);

        DispatchQuery query = DispatchQuery.builder()
                .driverId(driverId)
                .build();

        List<DispatchOrderDTO> dispatches = dispatchApplicationService.queryDispatches(tenantId, query);

        List<DispatchResponse> response = dispatches.stream()
                .map(DispatchResponse::fromDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Get dispatches for a specific customer
     * GET /api/v1/dispatch/customers/{customerId}
     */
    @GetMapping("/customers/{customerId}")
    public ResponseEntity<List<DispatchResponse>> getCustomerDispatches(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @PathVariable String customerId) {
        log.info("GET /api/v1/dispatch/customers/{} - Fetching dispatches for tenant: {}", customerId, tenantId);

        DispatchQuery query = DispatchQuery.builder()
                .customerId(customerId)
                .build();

        List<DispatchOrderDTO> dispatches = dispatchApplicationService.queryDispatches(tenantId, query);

        List<DispatchResponse> response = dispatches.stream()
                .map(DispatchResponse::fromDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Helper method to map LocationDto to LocationCommand
     */
    private CreateDispatchOrderCommand.LocationCommand mapToLocationCommand(DispatchRequest.LocationDto location) {
        if (location == null) {
            return null;
        }
        return CreateDispatchOrderCommand.LocationCommand.builder()
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
    }
}

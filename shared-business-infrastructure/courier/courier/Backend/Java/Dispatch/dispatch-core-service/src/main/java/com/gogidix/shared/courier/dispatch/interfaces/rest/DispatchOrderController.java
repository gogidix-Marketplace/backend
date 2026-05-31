package com.gogidix.shared.courier.dispatch.interfaces.rest;

import com.gogidix.shared.courier.dispatch.application.dto.DispatchOrderDTO;
import com.gogidix.shared.courier.dispatch.application.query.DispatchOrderQuery;
import com.gogidix.shared.courier.dispatch.application.service.DispatchOrderApplicationService;
import com.gogidix.shared.courier.dispatch.domain.entity.DispatchStatus;
import com.gogidix.shared.courier.dispatch.interfaces.rest.dto.CreateDispatchRequest;
import com.gogidix.shared.courier.dispatch.interfaces.rest.dto.DispatchResponse;
import com.gogidix.shared.courier.dispatch.interfaces.rest.dto.UpdateDispatchRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for Dispatch Order Management with geospatial capabilities
 * Provides CRUD operations and geospatial queries for dispatch orders
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/dispatch-orders")
@RequiredArgsConstructor
public class DispatchOrderController {

    private final DispatchOrderApplicationService dispatchOrderApplicationService;

    /**
     * Create a new dispatch order
     * POST /api/v1/dispatch-orders
     */
    @PostMapping
    public ResponseEntity<DispatchResponse> createDispatchOrder(@Valid @RequestBody CreateDispatchRequest request) {
        log.info("POST /api/v1/dispatch-orders - Creating dispatch order: {}", request.getDispatchId());

        DispatchOrderDTO dispatchDTO = dispatchOrderApplicationService.createDispatchOrder(request.toCommand());

        return ResponseEntity.status(HttpStatus.CREATED).body(DispatchResponse.fromDTO(dispatchDTO));
    }

    /**
     * Get dispatch order by tenantId and dispatchId
     * GET /api/v1/dispatch-orders/{tenantId}/{dispatchId}
     */
    @GetMapping("/{tenantId}/{dispatchId}")
    public ResponseEntity<DispatchResponse> getDispatchOrder(
            @PathVariable String tenantId,
            @PathVariable String dispatchId) {

        log.debug("GET /api/v1/dispatch-orders/{}/{} - Fetching dispatch order", tenantId, dispatchId);

        DispatchOrderDTO dispatchDTO = dispatchOrderApplicationService.getDispatchOrder(tenantId, dispatchId);

        return ResponseEntity.ok(DispatchResponse.fromDTO(dispatchDTO));
    }

    /**
     * Get all dispatch orders for tenant with optional filters
     * GET /api/v1/dispatch-orders/{tenantId}?status=PENDING&driverId=xxx
     */
    @GetMapping("/{tenantId}")
    public ResponseEntity<List<DispatchResponse>> getDispatchOrders(
            @PathVariable String tenantId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String driverId,
            @RequestParam(required = false) String customerId) {

        log.debug("GET /api/v1/dispatch-orders/{} - Fetching dispatch orders with filters", tenantId);

        DispatchOrderQuery query = DispatchOrderQuery.builder()
                .tenantId(tenantId)
                .status(status)
                .assignedDriverId(driverId)
                .customerId(customerId)
                .build();

        List<DispatchOrderDTO> dispatchOrders = dispatchOrderApplicationService.queryDispatchOrders(query);

        List<DispatchResponse> response = dispatchOrders.stream()
                .map(DispatchResponse::fromDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Update dispatch order
     * PUT /api/v1/dispatch-orders/{tenantId}/{dispatchId}
     */
    @PutMapping("/{tenantId}/{dispatchId}")
    public ResponseEntity<DispatchResponse> updateDispatchOrder(
            @PathVariable String tenantId,
            @PathVariable String dispatchId,
            @RequestBody UpdateDispatchRequest request) {

        log.info("PUT /api/v1/dispatch-orders/{}/{} - Updating dispatch order", tenantId, dispatchId);

        // Convert UpdateDispatchRequest to UpdateDispatchOrderCommand
        var command = com.gogidix.shared.courier.dispatch.application.command.UpdateDispatchOrderCommand.builder()
                .orderId(request.getOrderId())
                .customerId(request.getCustomerId())
                .status(request.getStatus())
                .assignedDriverId(request.getAssignedDriverId())
                .assignedVehicleId(request.getAssignedVehicleId())
                .estimatedPickupTime(request.getEstimatedPickupTime())
                .estimatedDeliveryTime(request.getEstimatedDeliveryTime())
                .actualPickupTime(request.getActualPickupTime())
                .actualDeliveryTime(request.getActualDeliveryTime())
                .build();

        DispatchOrderDTO dispatchDTO = dispatchOrderApplicationService.updateDispatchOrder(tenantId, dispatchId, command);

        return ResponseEntity.ok(DispatchResponse.fromDTO(dispatchDTO));
    }

    /**
     * Assign driver to dispatch order
     * POST /api/v1/dispatch-orders/{tenantId}/{dispatchId}/assign
     */
    @PostMapping("/{tenantId}/{dispatchId}/assign")
    public ResponseEntity<DispatchResponse> assignDriver(
            @PathVariable String tenantId,
            @PathVariable String dispatchId,
            @RequestBody AssignDriverRequest request) {

        log.info("POST /api/v1/dispatch-orders/{}/{}/assign - Assigning driver: {}", tenantId, dispatchId, request.getDriverId());

        DispatchOrderDTO dispatchDTO = dispatchOrderApplicationService.assignDriver(
                tenantId, dispatchId, request.getDriverId(), request.getVehicleId());

        return ResponseEntity.ok(DispatchResponse.fromDTO(dispatchDTO));
    }

    /**
     * Update dispatch order status
     * PATCH /api/v1/dispatch-orders/{tenantId}/{dispatchId}/status
     */
    @PatchMapping("/{tenantId}/{dispatchId}/status")
    public ResponseEntity<DispatchResponse> updateStatus(
            @PathVariable String tenantId,
            @PathVariable String dispatchId,
            @RequestBody UpdateStatusRequest request) {

        log.info("PATCH /api/v1/dispatch-orders/{}/{}/status - Updating status to: {}", tenantId, dispatchId, request.getStatus());

        DispatchOrderDTO dispatchDTO = dispatchOrderApplicationService.updateStatus(tenantId, dispatchId, request.getStatus());

        return ResponseEntity.ok(DispatchResponse.fromDTO(dispatchDTO));
    }

    /**
     * Find nearby pickups (geospatial)
     * GET /api/v1/dispatch-orders/{tenantId}/nearby-pickups
     */
    @GetMapping("/{tenantId}/nearby-pickups")
    public ResponseEntity<List<DispatchResponse>> findNearbyPickups(
            @PathVariable String tenantId,
            @RequestParam double longitude,
            @RequestParam double latitude,
            @RequestParam(defaultValue = "5.0") double maxDistanceKm) {

        log.debug("GET /api/v1/dispatch-orders/{}/nearby-pickups - Finding nearby pickups", tenantId);

        List<DispatchOrderDTO> nearbyPickups = dispatchOrderApplicationService.findNearbyPickups(
                tenantId, latitude, longitude, maxDistanceKm);

        List<DispatchResponse> response = nearbyPickups.stream()
                .map(DispatchResponse::fromDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Get dispatch orders by status
     * GET /api/v1/dispatch-orders/{tenantId}/status/{status}
     */
    @GetMapping("/{tenantId}/status/{status}")
    public ResponseEntity<List<DispatchResponse>> getDispatchOrdersByStatus(
            @PathVariable String tenantId,
            @PathVariable DispatchStatus status) {

        log.debug("GET /api/v1/dispatch-orders/{}/status/{}", tenantId, status);

        DispatchOrderQuery query = DispatchOrderQuery.builder()
                .tenantId(tenantId)
                .status(status.name())
                .build();

        List<DispatchOrderDTO> dispatchOrders = dispatchOrderApplicationService.queryDispatchOrders(query);

        List<DispatchResponse> response = dispatchOrders.stream()
                .map(DispatchResponse::fromDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Request DTO for driver assignment
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class AssignDriverRequest {

        private String driverId;

        private String vehicleId;
    }

    /**
     * Request DTO for status update
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class UpdateStatusRequest {

        private String status;
    }
}

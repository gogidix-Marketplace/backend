package com.gogidix.shared.warehousing.shipping.interfaces.rest;

import com.gogidix.shared.warehousing.shipping.application.command.CreateShipmentCommand;
import com.gogidix.shared.warehousing.shipping.application.command.UpdateShipmentCommand;
import com.gogidix.shared.warehousing.shipping.application.dto.ShipmentDTO;
import com.gogidix.shared.warehousing.shipping.application.dto.TrackingEventDTO;
import com.gogidix.shared.warehousing.shipping.application.service.ShippingService;
import com.gogidix.shared.warehousing.shipping.domain.entity.Shipment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shipments")
@RequiredArgsConstructor
@Tag(name = "Shipment", description = "Shipment management APIs")
public class ShipmentController {

    private final ShippingService shippingService;

    @PostMapping
    @Operation(summary = "Create a new shipment", description = "Creates a new shipment with carrier integration")
    public ResponseEntity<ShipmentDTO> createShipment(@Valid @RequestBody CreateShipmentCommand command) {
        ShipmentDTO shipment = shippingService.createShipment(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(shipment);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get shipment by ID", description = "Retrieves shipment details by ID")
    public ResponseEntity<ShipmentDTO> getShipment(@Parameter(description = "Shipment ID") @PathVariable String id) {
        ShipmentDTO shipment = shippingService.getShipment(id);
        return ResponseEntity.ok(shipment);
    }

    @GetMapping("/tracking/{trackingNumber}")
    @Operation(summary = "Get shipment by tracking number", description = "Retrieves shipment details by tracking number")
    public ResponseEntity<ShipmentDTO> getShipmentByTrackingNumber(
            @Parameter(description = "Tracking number") @PathVariable String trackingNumber) {
        ShipmentDTO shipment = shippingService.getShipmentByTrackingNumber(trackingNumber);
        return ResponseEntity.ok(shipment);
    }

    @GetMapping
    @Operation(summary = "Get all shipments", description = "Retrieves all shipments for current tenant")
    public ResponseEntity<List<ShipmentDTO>> getAllShipments() {
        List<ShipmentDTO> shipments = shippingService.getAllShipments();
        return ResponseEntity.ok(shipments);
    }

    @GetMapping("/order/{orderNumber}")
    @Operation(summary = "Get shipments by order number", description = "Retrieves all shipments for a specific order")
    public ResponseEntity<List<ShipmentDTO>> getShipmentsByOrderNumber(
            @Parameter(description = "Order number") @PathVariable String orderNumber) {
        List<ShipmentDTO> shipments = shippingService.getShipmentsByOrderNumber(orderNumber);
        return ResponseEntity.ok(shipments);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get shipments by status", description = "Retrieves all shipments with a specific status")
    public ResponseEntity<List<ShipmentDTO>> getShipmentsByStatus(
            @Parameter(description = "Shipment status") @PathVariable Shipment.ShipmentStatus status) {
        List<ShipmentDTO> shipments = shippingService.getShipmentsByStatus(status);
        return ResponseEntity.ok(shipments);
    }

    @GetMapping("/active")
    @Operation(summary = "Get active shipments", description = "Retrieves all active (in-transit) shipments")
    public ResponseEntity<List<ShipmentDTO>> getActiveShipments() {
        List<ShipmentDTO> shipments = shippingService.getActiveShipments();
        return ResponseEntity.ok(shipments);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update shipment", description = "Updates shipment details")
    public ResponseEntity<ShipmentDTO> updateShipment(
            @Parameter(description = "Shipment ID") @PathVariable String id,
            @Valid @RequestBody UpdateShipmentCommand command) {
        ShipmentDTO shipment = shippingService.updateShipment(id, command);
        return ResponseEntity.ok(shipment);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update shipment status", description = "Updates the status of a shipment")
    public ResponseEntity<ShipmentDTO> updateShipmentStatus(
            @Parameter(description = "Shipment ID") @PathVariable String id,
            @Parameter(description = "New status") @RequestParam Shipment.ShipmentStatus status) {
        ShipmentDTO shipment = shippingService.updateShipmentStatus(id, status);
        return ResponseEntity.ok(shipment);
    }

    @GetMapping("/{id}/tracking")
    @Operation(summary = "Get tracking history", description = "Retrieves tracking history for a shipment")
    public ResponseEntity<List<TrackingEventDTO>> getTrackingHistory(
            @Parameter(description = "Shipment ID") @PathVariable String id) {
        List<TrackingEventDTO> events = shippingService.getTrackingHistory(id);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/tracking/{trackingNumber}/history")
    @Operation(summary = "Get tracking history by tracking number", description = "Retrieves tracking history by tracking number")
    public ResponseEntity<List<TrackingEventDTO>> getTrackingHistoryByTrackingNumber(
            @Parameter(description = "Tracking number") @PathVariable String trackingNumber) {
        List<TrackingEventDTO> events = shippingService.getTrackingHistoryByTrackingNumber(trackingNumber);
        return ResponseEntity.ok(events);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete shipment", description = "Deletes a shipment")
    public ResponseEntity<Void> deleteShipment(@Parameter(description = "Shipment ID") @PathVariable String id) {
        shippingService.deleteShipment(id);
        return ResponseEntity.noContent().build();
    }
}

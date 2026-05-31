package com.gogidix.shared.warehousing.shipping.application.service;

import com.gogidix.shared.warehousing.shipping.application.command.CreateShipmentCommand;
import com.gogidix.shared.warehousing.shipping.application.command.UpdateShipmentCommand;
import com.gogidix.shared.warehousing.shipping.application.dto.ShipmentDTO;
import com.gogidix.shared.warehousing.shipping.application.dto.TrackingEventDTO;
import com.gogidix.shared.warehousing.shipping.application.mapper.ShipmentMapper;
import com.gogidix.shared.warehousing.shipping.domain.entity.Shipment;
import com.gogidix.shared.warehousing.shipping.domain.entity.TrackingEvent;
import com.gogidix.shared.warehousing.shipping.domain.events.ShipmentCreatedEvent;
import com.gogidix.shared.warehousing.shipping.domain.events.ShipmentUpdatedEvent;
import com.gogidix.shared.warehousing.shipping.domain.exception.ShipmentNotFoundException;
import com.gogidix.shared.warehousing.shipping.domain.repository.ShipmentRepository;
import com.gogidix.shared.warehousing.shipping.domain.repository.TrackingEventRepository;
import com.gogidix.shared.warehousing.shipping.infrastructure.messaging.ShippingEventPublisher;
import com.gogidix.shared.warehousing.shipping.infrastructure.security.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Shipping Application Service
 *
 * Handles shipping operations with multi-tenant support
 * Publishes domain events for downstream processing
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ShippingService {

    private final ShipmentRepository shipmentRepository;
    private final TrackingEventRepository trackingEventRepository;
    private final ShipmentMapper shipmentMapper;
    private final ShippingEventPublisher eventPublisher;

    /**
     * Create a new shipment
     */
    public ShipmentDTO createShipment(CreateShipmentCommand command) {
        log.info("Creating shipment for order: {}", command.getOrderNumber());

        String tenantId = TenantContext.getCurrentTenantId();

        Shipment shipment = shipmentMapper.toEntity(command);
        shipment.setTenantId(tenantId);
        shipment.setStatus(Shipment.ShipmentStatus.CREATED);
        shipment.setTrackingNumber(generateTrackingNumber(command.getCarrier()));

        Shipment savedShipment = shipmentRepository.save(shipment);

        // Create initial tracking event
        TrackingEvent initialEvent = TrackingEvent.builder()
            .tenantId(tenantId)
            .shipmentId(savedShipment.getId())
            .trackingNumber(savedShipment.getTrackingNumber())
            .statusCode("CREATED")
            .statusDescription("Shipment created")
            .timestamp(LocalDateTime.now())
            .eventType(TrackingEvent.TrackingEventType.PICKUP)
            .build();
        trackingEventRepository.save(initialEvent);

        // Publish domain event
        ShipmentCreatedEvent event = ShipmentCreatedEvent.builder()
            .shipmentId(savedShipment.getId())
            .orderNumber(savedShipment.getOrderNumber())
            .trackingNumber(savedShipment.getTrackingNumber())
            .carrier(savedShipment.getCarrier().name())
            .serviceLevel(savedShipment.getServiceLevel().name())
            .status(savedShipment.getStatus().name())
            .tenantId(savedShipment.getTenantId())
            .build();
        eventPublisher.publishShipmentCreated(event);

        log.info("Shipment created with ID: {}", savedShipment.getId());
        return shipmentMapper.toDTO(savedShipment);
    }

    /**
     * Get shipment by ID
     */
    @Transactional(readOnly = true)
    public ShipmentDTO getShipment(String id) {
        Shipment shipment = shipmentRepository.findById(id)
            .orElseThrow(() -> new ShipmentNotFoundException(id, TenantContext.getCurrentTenantId()));
        return shipmentMapper.toDTO(shipment);
    }

    /**
     * Get shipment by tracking number
     */
    @Transactional(readOnly = true)
    public ShipmentDTO getShipmentByTrackingNumber(String trackingNumber) {
        String tenantId = TenantContext.getCurrentTenantId();
        Shipment shipment = shipmentRepository.findByTenantIdAndTrackingNumber(tenantId, trackingNumber)
            .orElseThrow(() -> new ShipmentNotFoundException("Tracking number: " + trackingNumber, tenantId));
        return shipmentMapper.toDTO(shipment);
    }

    /**
     * Get all shipments for current tenant
     */
    @Transactional(readOnly = true)
    public List<ShipmentDTO> getAllShipments() {
        String tenantId = TenantContext.getCurrentTenantId();
        List<Shipment> shipments = shipmentRepository.findByTenantId(tenantId);
        return shipmentMapper.toDTOList(shipments);
    }

    /**
     * Get shipments by order number
     */
    @Transactional(readOnly = true)
    public List<ShipmentDTO> getShipmentsByOrderNumber(String orderNumber) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<Shipment> shipments = shipmentRepository.findByTenantIdAndOrderNumber(tenantId, orderNumber);
        return shipmentMapper.toDTOList(shipments);
    }

    /**
     * Get shipments by status
     */
    @Transactional(readOnly = true)
    public List<ShipmentDTO> getShipmentsByStatus(Shipment.ShipmentStatus status) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<Shipment> shipments = shipmentRepository.findByTenantIdAndStatus(tenantId, status);
        return shipmentMapper.toDTOList(shipments);
    }

    /**
     * Get active shipments
     */
    @Transactional(readOnly = true)
    public List<ShipmentDTO> getActiveShipments() {
        String tenantId = TenantContext.getCurrentTenantId();
        List<Shipment> shipments = shipmentRepository.findActiveShipments(tenantId);
        return shipmentMapper.toDTOList(shipments);
    }

    /**
     * Update shipment
     */
    public ShipmentDTO updateShipment(String id, UpdateShipmentCommand command) {
        log.info("Updating shipment: {}", id);

        String tenantId = TenantContext.getCurrentTenantId();
        Shipment shipment = shipmentRepository.findById(id)
            .orElseThrow(() -> new ShipmentNotFoundException(id, tenantId));

        shipmentMapper.updateEntity(shipment, command);
        Shipment updatedShipment = shipmentRepository.save(shipment);

        // Publish domain event
        ShipmentUpdatedEvent event = ShipmentUpdatedEvent.builder()
            .shipmentId(updatedShipment.getId())
            .orderNumber(updatedShipment.getOrderNumber())
            .trackingNumber(updatedShipment.getTrackingNumber())
            .status(updatedShipment.getStatus().name())
            .tenantId(updatedShipment.getTenantId())
            .changeType("UPDATE")
            .build();
        eventPublisher.publishShipmentUpdated(event);

        log.info("Shipment updated: {}", id);
        return shipmentMapper.toDTO(updatedShipment);
    }

    /**
     * Update shipment status
     */
    public ShipmentDTO updateShipmentStatus(String id, Shipment.ShipmentStatus status) {
        log.info("Updating shipment status: {} to {}", id, status);

        String tenantId = TenantContext.getCurrentTenantId();
        Shipment shipment = shipmentRepository.findById(id)
            .orElseThrow(() -> new ShipmentNotFoundException(id, tenantId));

        shipment.setStatus(status);

        if (status == Shipment.ShipmentStatus.DELIVERED) {
            shipment.setActualDelivery(LocalDateTime.now());
        }

        Shipment updatedShipment = shipmentRepository.save(shipment);

        // Create tracking event
        TrackingEvent trackingEvent = TrackingEvent.builder()
            .tenantId(tenantId)
            .shipmentId(updatedShipment.getId())
            .trackingNumber(updatedShipment.getTrackingNumber())
            .statusCode(status.name())
            .statusDescription("Status updated to " + status)
            .timestamp(LocalDateTime.now())
            .eventType(mapStatusToEventType(status))
            .build();
        trackingEventRepository.save(trackingEvent);

        // Publish domain event
        ShipmentUpdatedEvent event = ShipmentUpdatedEvent.builder()
            .shipmentId(updatedShipment.getId())
            .orderNumber(updatedShipment.getOrderNumber())
            .trackingNumber(updatedShipment.getTrackingNumber())
            .status(status.name())
            .tenantId(updatedShipment.getTenantId())
            .changeType("STATUS_UPDATE")
            .build();
        eventPublisher.publishShipmentUpdated(event);

        log.info("Shipment status updated: {} to {}", id, status);
        return shipmentMapper.toDTO(updatedShipment);
    }

    /**
     * Get tracking history for a shipment
     */
    @Transactional(readOnly = true)
    public List<TrackingEventDTO> getTrackingHistory(String shipmentId) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<TrackingEvent> events = trackingEventRepository.findByTenantIdAndShipmentIdOrderByTimestampDesc(tenantId, shipmentId);
        return shipmentMapper.toTrackingEventDTOList(events);
    }

    /**
     * Get tracking history by tracking number
     */
    @Transactional(readOnly = true)
    public List<TrackingEventDTO> getTrackingHistoryByTrackingNumber(String trackingNumber) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<TrackingEvent> events = trackingEventRepository.findByTenantIdAndTrackingNumberOrderByTimestampDesc(tenantId, trackingNumber);
        return shipmentMapper.toTrackingEventDTOList(events);
    }

    /**
     * Delete shipment
     */
    public void deleteShipment(String id) {
        log.info("Deleting shipment: {}", id);

        String tenantId = TenantContext.getCurrentTenantId();
        if (!shipmentRepository.findById(id).isPresent()) {
            throw new ShipmentNotFoundException(id, tenantId);
        }

        shipmentRepository.deleteById(id);
        log.info("Shipment deleted: {}", id);
    }

    private String generateTrackingNumber(Shipment.Carrier carrier) {
        return carrier.name() + "-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
    }

    private TrackingEvent.TrackingEventType mapStatusToEventType(Shipment.ShipmentStatus status) {
        return switch (status) {
            case PICKED_UP -> TrackingEvent.TrackingEventType.PICKUP;
            case IN_TRANSIT -> TrackingEvent.TrackingEventType.IN_TRANSIT;
            case OUT_FOR_DELIVERY -> TrackingEvent.TrackingEventType.OUT_FOR_DELIVERY;
            case DELIVERED -> TrackingEvent.TrackingEventType.DELIVERED;
            case EXCEPTION -> TrackingEvent.TrackingEventType.EXCEPTION;
            case CANCELLED -> TrackingEvent.TrackingEventType.CANCELLED;
            case RETURNED -> TrackingEvent.TrackingEventType.RETURNED;
            default -> TrackingEvent.TrackingEventType.IN_TRANSIT;
        };
    }
}

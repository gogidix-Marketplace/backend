package com.gogidix.shared.warehousing.serialization.application.service;

import com.gogidix.shared.warehousing.serialization.application.command.CreateSerializedItemCommand;
import com.gogidix.shared.warehousing.serialization.application.command.UpdateSerializedItemStatusCommand;
import com.gogidix.shared.warehousing.serialization.application.dto.SerializedItemDTO;
import com.gogidix.shared.warehousing.serialization.domain.entity.SerializedItem;
import com.gogidix.shared.warehousing.serialization.domain.events.SerializedItemCreatedEvent;
import com.gogidix.shared.warehousing.serialization.domain.events.SerializedItemStatusChangedEvent;
import com.gogidix.shared.warehousing.serialization.domain.exception.DuplicateSerialNumberException;
import com.gogidix.shared.warehousing.serialization.domain.exception.EntityNotFoundException;
import com.gogidix.shared.warehousing.serialization.domain.repository.SerializedItemRepository;
import com.gogidix.shared.warehousing.serialization.infrastructure.messaging.SerializationEventPublisher;
import com.gogidix.shared.warehousing.serialization.infrastructure.security.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serialized Item Application Service
 *
 * Handles serialized item operations with multi-tenant support
 * Publishes domain events for downstream processing
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SerializedItemService {

    private final SerializedItemRepository serializedItemRepository;
    private final SerializationEventPublisher eventPublisher;

    /**
     * Create a new serialized item
     */
    public SerializedItemDTO createSerializedItem(CreateSerializedItemCommand command) {
        log.info("Creating serialized item with serial number: {}", command.getSerialNumber());

        String tenantId = TenantContext.getCurrentTenantId();

        // Check for duplicate serial number
        if (serializedItemRepository.existsByTenantIdAndSerialNumber(tenantId, command.getSerialNumber())) {
            throw new DuplicateSerialNumberException(command.getSerialNumber());
        }

        SerializedItem item = SerializedItem.builder()
            .tenantId(tenantId)
            .serialNumber(command.getSerialNumber())
            .sku(command.getSku())
            .status(command.getStatus() != null ? command.getStatus() : SerializedItem.SerializedItemStatus.AVAILABLE)
            .batchId(command.getBatchId())
            .expiryDate(command.getExpiryDate())
            .locationId(command.getLocationId())
            .binLocation(command.getBinLocation())
            .costValue(command.getCostValue())
            .attributes(command.getAttributes())
            .notes(command.getNotes())
            .statusChangedAt(LocalDateTime.now())
            .build();

        SerializedItem savedItem = serializedItemRepository.save(item);

        // Publish domain event
        SerializedItemCreatedEvent event = SerializedItemCreatedEvent.builder()
            .serializedItemId(savedItem.getId())
            .serialNumber(savedItem.getSerialNumber())
            .sku(savedItem.getSku())
            .status(savedItem.getStatus().name())
            .batchId(savedItem.getBatchId())
            .expiryDate(savedItem.getExpiryDate())
            .locationId(savedItem.getLocationId())
            .tenantId(savedItem.getTenantId())
            .timestamp(LocalDateTime.now())
            .build();
        eventPublisher.publishSerializedItemCreated(event);

        log.info("Serialized item created with ID: {}", savedItem.getId());
        return toDTO(savedItem);
    }

    /**
     * Get serialized item by ID
     */
    @Transactional(readOnly = true)
    public SerializedItemDTO getSerializedItem(String id) {
        SerializedItem item = serializedItemRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SerializedItem", id));
        return toDTO(item);
    }

    /**
     * Get serialized item by serial number
     */
    @Transactional(readOnly = true)
    public SerializedItemDTO getSerializedItemBySerialNumber(String serialNumber) {
        String tenantId = TenantContext.getCurrentTenantId();
        SerializedItem item = serializedItemRepository.findByTenantIdAndSerialNumber(tenantId, serialNumber)
            .orElseThrow(() -> new EntityNotFoundException("SerializedItem", serialNumber));
        return toDTO(item);
    }

    /**
     * Get all serialized items for current tenant
     */
    @Transactional(readOnly = true)
    public List<SerializedItemDTO> getAllSerializedItems() {
        String tenantId = TenantContext.getCurrentTenantId();
        List<SerializedItem> items = serializedItemRepository.findByTenantId(tenantId);
        return items.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get serialized items by SKU
     */
    @Transactional(readOnly = true)
    public List<SerializedItemDTO> getSerializedItemsBySku(String sku) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<SerializedItem> items = serializedItemRepository.findByTenantIdAndSku(tenantId, sku);
        return items.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get serialized items by batch ID
     */
    @Transactional(readOnly = true)
    public List<SerializedItemDTO> getSerializedItemsByBatch(String batchId) {
        List<SerializedItem> items = serializedItemRepository.findByBatchId(batchId);
        return items.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Update serialized item status
     */
    public SerializedItemDTO updateStatus(String id, UpdateSerializedItemStatusCommand command) {
        log.info("Updating status for serialized item: {}", id);

        SerializedItem item = serializedItemRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SerializedItem", id));

        SerializedItem.SerializedItemStatus oldStatus = item.getStatus();
        item.setStatus(command.getStatus());
        item.setStatusChangedAt(LocalDateTime.now());
        item.setStatusChangedBy(command.getChangedBy());
        if (command.getNotes() != null) {
            item.setNotes(command.getNotes());
        }

        SerializedItem updatedItem = serializedItemRepository.save(item);

        // Publish domain event
        SerializedItemStatusChangedEvent event = SerializedItemStatusChangedEvent.builder()
            .serializedItemId(updatedItem.getId())
            .serialNumber(updatedItem.getSerialNumber())
            .sku(updatedItem.getSku())
            .oldStatus(oldStatus.name())
            .newStatus(updatedItem.getStatus().name())
            .changedBy(command.getChangedBy())
            .tenantId(updatedItem.getTenantId())
            .timestamp(LocalDateTime.now())
            .build();
        eventPublisher.publishStatusChanged(event);

        log.info("Serialized item status updated: {} from {} to {}", id, oldStatus, command.getStatus());
        return toDTO(updatedItem);
    }

    /**
     * Update serialized item location
     */
    public SerializedItemDTO updateLocation(String id, String locationId, String binLocation) {
        log.info("Updating location for serialized item: {}", id);

        SerializedItem item = serializedItemRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SerializedItem", id));

        item.setLocationId(locationId);
        if (binLocation != null) {
            item.setBinLocation(binLocation);
        }

        SerializedItem updatedItem = serializedItemRepository.save(item);
        log.info("Serialized item location updated: {}", id);
        return toDTO(updatedItem);
    }

    /**
     * Delete serialized item
     */
    public void deleteSerializedItem(String id) {
        log.info("Deleting serialized item: {}", id);

        if (!serializedItemRepository.existsById(id)) {
            throw new EntityNotFoundException("SerializedItem", id);
        }

        serializedItemRepository.deleteById(id);
        log.info("Serialized item deleted: {}", id);
    }

    private SerializedItemDTO toDTO(SerializedItem item) {
        return SerializedItemDTO.builder()
            .id(item.getId())
            .tenantId(item.getTenantId())
            .serialNumber(item.getSerialNumber())
            .sku(item.getSku())
            .status(item.getStatus())
            .batchId(item.getBatchId())
            .expiryDate(item.getExpiryDate())
            .locationId(item.getLocationId())
            .binLocation(item.getBinLocation())
            .costValue(item.getCostValue())
            .createdAt(item.getCreatedAt())
            .updatedAt(item.getUpdatedAt())
            .statusChangedAt(item.getStatusChangedAt())
            .statusChangedBy(item.getStatusChangedBy())
            .notes(item.getNotes())
            .build();
    }
}

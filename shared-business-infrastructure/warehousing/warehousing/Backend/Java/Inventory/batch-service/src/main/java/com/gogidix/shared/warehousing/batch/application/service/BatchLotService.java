package com.gogidix.shared.warehousing.batch.application.service;

import com.gogidix.shared.warehousing.batch.application.command.CreateBatchLotCommand;
import com.gogidix.shared.warehousing.batch.application.command.UpdateBatchLotCommand;
import com.gogidix.shared.warehousing.batch.application.dto.BatchLotDTO;
import com.gogidix.shared.warehousing.batch.domain.entity.BatchLot;
import com.gogidix.shared.warehousing.batch.domain.events.BatchLotCreatedEvent;
import com.gogidix.shared.warehousing.batch.domain.exception.BatchNotFoundException;
import com.gogidix.shared.warehousing.batch.domain.repository.BatchLotRepository;
import com.gogidix.shared.warehousing.batch.infrastructure.messaging.BatchEventPublisher;
import com.gogidix.shared.warehousing.batch.infrastructure.security.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Batch Lot Application Service
 *
 * Handles batch lot operations with multi-tenant support
 * Publishes domain events for downstream processing
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BatchLotService {

    private final BatchLotRepository batchLotRepository;
    private final BatchEventPublisher eventPublisher;

    /**
     * Create a new batch lot
     */
    public BatchLotDTO createBatchLot(CreateBatchLotCommand command) {
        log.info("Creating batch lot with number: {}", command.getLotNumber());

        String tenantId = TenantContext.getCurrentTenantId();

        BatchLot lot = BatchLot.builder()
            .tenantId(tenantId)
            .lotNumber(command.getLotNumber())
            .sku(command.getSku())
            .description(command.getDescription())
            .productionDate(command.getProductionDate())
            .expirationDate(command.getExpirationDate())
            .supplierId(command.getSupplierId())
            .supplierName(command.getSupplierName())
            .countryOfOrigin(command.getCountryOfOrigin())
            .manufacturingBatchNumber(command.getManufacturingBatchNumber())
            .totalQuantity(command.getTotalQuantity())
            .availableQuantity(command.getTotalQuantity())
            .reservedQuantity(0)
            .unitOfMeasure(command.getUnitOfMeasure())
            .costPerUnit(command.getCostPerUnit())
            .status(command.getStatus() != null ? command.getStatus() : BatchLot.LotStatus.ACTIVE)
            .locationId(command.getLocationId())
            .qcStatus(command.getQcStatus() != null ? command.getQcStatus() : BatchLot.QCStatus.PENDING)
            .qcNotes(command.getQcNotes())
            .storageRequirements(command.getStorageRequirements())
            .attributes(command.getAttributes())
            .build();

        BatchLot savedLot = batchLotRepository.save(lot);

        // Publish domain event
        BatchLotCreatedEvent event = BatchLotCreatedEvent.builder()
            .lotId(savedLot.getId())
            .lotNumber(savedLot.getLotNumber())
            .sku(savedLot.getSku())
            .totalQuantity(savedLot.getTotalQuantity())
            .expirationDate(savedLot.getExpirationDate())
            .supplierId(savedLot.getSupplierId())
            .tenantId(savedLot.getTenantId())
            .timestamp(LocalDateTime.now())
            .build();
        eventPublisher.publishBatchLotCreated(event);

        log.info("Batch lot created with ID: {}", savedLot.getId());
        return toDTO(savedLot);
    }

    /**
     * Get batch lot by ID
     */
    @Transactional(readOnly = true)
    public BatchLotDTO getBatchLot(String id) {
        BatchLot lot = batchLotRepository.findById(id)
            .orElseThrow(() -> new BatchNotFoundException("BatchLot", id));
        return toDTO(lot);
    }

    /**
     * Get batch lot by lot number
     */
    @Transactional(readOnly = true)
    public BatchLotDTO getBatchLotByNumber(String lotNumber) {
        String tenantId = TenantContext.getCurrentTenantId();
        BatchLot lot = batchLotRepository.findByTenantIdAndLotNumber(tenantId, lotNumber)
            .orElseThrow(() -> new BatchNotFoundException("BatchLot", lotNumber));
        return toDTO(lot);
    }

    /**
     * Get all batch lots for current tenant
     */
    @Transactional(readOnly = true)
    public List<BatchLotDTO> getAllBatchLots() {
        String tenantId = TenantContext.getCurrentTenantId();
        List<BatchLot> lots = batchLotRepository.findByTenantId(tenantId);
        return lots.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get batch lots by SKU
     */
    @Transactional(readOnly = true)
    public List<BatchLotDTO> getBatchLotsBySku(String sku) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<BatchLot> lots = batchLotRepository.findByTenantIdAndSku(tenantId, sku);
        return lots.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get batch lots by SKU ordered by expiration date (FEFO)
     */
    @Transactional(readOnly = true)
    public List<BatchLotDTO> getBatchLotsBySkuOrderedByExpiry(String sku) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<BatchLot> lots = batchLotRepository.findByTenantIdAndSkuAndStatusOrderByExpirationDateAsc(
            tenantId, sku, BatchLot.LotStatus.ACTIVE);
        return lots.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get batch lots expiring soon
     */
    @Transactional(readOnly = true)
    public List<BatchLotDTO> getBatchLotsExpiringBefore(LocalDate date) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<BatchLot> lots = batchLotRepository.findByTenantIdAndExpirationDateBefore(tenantId, date);
        return lots.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Update batch lot
     */
    public BatchLotDTO updateBatchLot(String id, UpdateBatchLotCommand command) {
        log.info("Updating batch lot: {}", id);

        BatchLot lot = batchLotRepository.findById(id)
            .orElseThrow(() -> new BatchNotFoundException("BatchLot", id));

        if (command.getDescription() != null) {
            lot.setDescription(command.getDescription());
        }
        if (command.getExpirationDate() != null) {
            lot.setExpirationDate(command.getExpirationDate());
        }
        if (command.getTotalQuantity() != null) {
            lot.setTotalQuantity(command.getTotalQuantity());
        }
        if (command.getAvailableQuantity() != null) {
            lot.setAvailableQuantity(command.getAvailableQuantity());
        }
        if (command.getReservedQuantity() != null) {
            lot.setReservedQuantity(command.getReservedQuantity());
        }
        if (command.getStatus() != null) {
            lot.setStatus(command.getStatus());
        }
        if (command.getLocationId() != null) {
            lot.setLocationId(command.getLocationId());
        }
        if (command.getQcStatus() != null) {
            lot.setQcStatus(command.getQcStatus());
        }
        if (command.getQcNotes() != null) {
            lot.setQcNotes(command.getQcNotes());
        }
        if (command.getStorageRequirements() != null) {
            lot.setStorageRequirements(command.getStorageRequirements());
        }
        if (command.getAttributes() != null) {
            lot.setAttributes(command.getAttributes());
        }

        BatchLot updatedLot = batchLotRepository.save(lot);
        log.info("Batch lot updated: {}", id);
        return toDTO(updatedLot);
    }

    /**
     * Allocate quantity from batch lot
     */
    public BatchLotDTO allocateQuantity(String lotId, Integer quantity) {
        log.info("Allocating {} from lot: {}", quantity, lotId);

        BatchLot lot = batchLotRepository.findById(lotId)
            .orElseThrow(() -> new BatchNotFoundException("BatchLot", lotId));

        if (lot.getAvailableQuantity() < quantity) {
            throw new IllegalArgumentException(
                String.format("Insufficient available quantity: requested=%d, available=%d",
                    quantity, lot.getAvailableQuantity()));
        }

        lot.setAvailableQuantity(lot.getAvailableQuantity() - quantity);
        lot.setReservedQuantity(lot.getReservedQuantity() + quantity);

        if (lot.getAvailableQuantity() == 0) {
            lot.setStatus(BatchLot.LotStatus.FULLY_ALLOCATED);
        } else if (lot.getReservedQuantity() > 0) {
            lot.setStatus(BatchLot.LotStatus.PARTIALLY_ALLOCATED);
        }

        BatchLot updatedLot = batchLotRepository.save(lot);
        log.info("Allocated {} from lot: {}, remaining: {}", quantity, lotId, updatedLot.getAvailableQuantity());
        return toDTO(updatedLot);
    }

    /**
     * Deallocate quantity from batch lot
     */
    public BatchLotDTO deallocateQuantity(String lotId, Integer quantity) {
        log.info("Deallocating {} from lot: {}", quantity, lotId);

        BatchLot lot = batchLotRepository.findById(lotId)
            .orElseThrow(() -> new BatchNotFoundException("BatchLot", lotId));

        if (lot.getReservedQuantity() < quantity) {
            throw new IllegalArgumentException(
                String.format("Cannot deallocate more than reserved: requested=%d, reserved=%d",
                    quantity, lot.getReservedQuantity()));
        }

        lot.setReservedQuantity(lot.getReservedQuantity() - quantity);
        lot.setAvailableQuantity(lot.getAvailableQuantity() + quantity);

        if (lot.getReservedQuantity() == 0 && lot.getAvailableQuantity() > 0) {
            lot.setStatus(BatchLot.LotStatus.ACTIVE);
        }

        BatchLot updatedLot = batchLotRepository.save(lot);
        log.info("Deallocated {} from lot: {}, available: {}", quantity, lotId, updatedLot.getAvailableQuantity());
        return toDTO(updatedLot);
    }

    /**
     * Delete batch lot
     */
    public void deleteBatchLot(String id) {
        log.info("Deleting batch lot: {}", id);

        if (!batchLotRepository.existsById(id)) {
            throw new BatchNotFoundException("BatchLot", id);
        }

        batchLotRepository.deleteById(id);
        log.info("Batch lot deleted: {}", id);
    }

    private BatchLotDTO toDTO(BatchLot lot) {
        return BatchLotDTO.builder()
            .id(lot.getId())
            .tenantId(lot.getTenantId())
            .lotNumber(lot.getLotNumber())
            .sku(lot.getSku())
            .description(lot.getDescription())
            .productionDate(lot.getProductionDate())
            .expirationDate(lot.getExpirationDate())
            .supplierId(lot.getSupplierId())
            .supplierName(lot.getSupplierName())
            .countryOfOrigin(lot.getCountryOfOrigin())
            .manufacturingBatchNumber(lot.getManufacturingBatchNumber())
            .totalQuantity(lot.getTotalQuantity())
            .availableQuantity(lot.getAvailableQuantity())
            .reservedQuantity(lot.getReservedQuantity())
            .unitOfMeasure(lot.getUnitOfMeasure())
            .costPerUnit(lot.getCostPerUnit())
            .status(lot.getStatus())
            .locationId(lot.getLocationId())
            .qcStatus(lot.getQcStatus())
            .qcNotes(lot.getQcNotes())
            .storageRequirements(lot.getStorageRequirements())
            .createdAt(lot.getCreatedAt())
            .updatedAt(lot.getUpdatedAt())
            .build();
    }
}

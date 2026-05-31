package com.gogidix.shared.warehousing.serialization.application.service;

import com.gogidix.shared.warehousing.serialization.application.command.CreateBatchCommand;
import com.gogidix.shared.warehousing.serialization.application.dto.BatchDTO;
import com.gogidix.shared.warehousing.serialization.domain.entity.Batch;
import com.gogidix.shared.warehousing.serialization.domain.events.BatchCreatedEvent;
import com.gogidix.shared.warehousing.serialization.domain.exception.EntityNotFoundException;
import com.gogidix.shared.warehousing.serialization.domain.repository.BatchRepository;
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
 * Batch Application Service
 *
 * Handles batch operations with multi-tenant support
 * Publishes domain events for downstream processing
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BatchService {

    private final BatchRepository batchRepository;
    private final SerializationEventPublisher eventPublisher;

    /**
     * Create a new batch
     */
    public BatchDTO createBatch(CreateBatchCommand command) {
        log.info("Creating batch with number: {}", command.getBatchNumber());

        String tenantId = TenantContext.getCurrentTenantId();

        Batch batch = Batch.builder()
            .tenantId(tenantId)
            .batchNumber(command.getBatchNumber())
            .sku(command.getSku())
            .description(command.getDescription())
            .manufacturingDate(command.getManufacturingDate())
            .expirationDate(command.getExpirationDate())
            .supplierId(command.getSupplierId())
            .supplierName(command.getSupplierName())
            .countryOfOrigin(command.getCountryOfOrigin())
            .totalQuantity(command.getTotalQuantity())
            .availableQuantity(command.getTotalQuantity())
            .unitOfMeasure(command.getUnitOfMeasure())
            .status(command.getStatus() != null ? command.getStatus() : Batch.BatchStatus.ACTIVE)
            .locationId(command.getLocationId())
            .qcStatus(command.getQcStatus() != null ? command.getQcStatus() : Batch.QCStatus.PENDING)
            .qcNotes(command.getQcNotes())
            .attributes(command.getAttributes())
            .build();

        Batch savedBatch = batchRepository.save(batch);

        // Publish domain event
        BatchCreatedEvent event = BatchCreatedEvent.builder()
            .batchId(savedBatch.getId())
            .batchNumber(savedBatch.getBatchNumber())
            .sku(savedBatch.getSku())
            .totalQuantity(savedBatch.getTotalQuantity())
            .tenantId(savedBatch.getTenantId())
            .timestamp(LocalDateTime.now())
            .build();
        eventPublisher.publishBatchCreated(event);

        log.info("Batch created with ID: {}", savedBatch.getId());
        return toDTO(savedBatch);
    }

    /**
     * Get batch by ID
     */
    @Transactional(readOnly = true)
    public BatchDTO getBatch(String id) {
        Batch batch = batchRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Batch", id));
        return toDTO(batch);
    }

    /**
     * Get batch by batch number
     */
    @Transactional(readOnly = true)
    public BatchDTO getBatchByNumber(String batchNumber) {
        String tenantId = TenantContext.getCurrentTenantId();
        Batch batch = batchRepository.findByTenantIdAndBatchNumber(tenantId, batchNumber)
            .orElseThrow(() -> new EntityNotFoundException("Batch", batchNumber));
        return toDTO(batch);
    }

    /**
     * Get all batches for current tenant
     */
    @Transactional(readOnly = true)
    public List<BatchDTO> getAllBatches() {
        String tenantId = TenantContext.getCurrentTenantId();
        List<Batch> batches = batchRepository.findByTenantId(tenantId);
        return batches.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get batches by SKU
     */
    @Transactional(readOnly = true)
    public List<BatchDTO> getBatchesBySku(String sku) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<Batch> batches = batchRepository.findByTenantIdAndSku(tenantId, sku);
        return batches.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Update batch available quantity
     */
    public BatchDTO updateAvailableQuantity(String batchId, Integer adjustment) {
        log.info("Updating available quantity for batch: {} by {}", batchId, adjustment);

        Batch batch = batchRepository.findById(batchId)
            .orElseThrow(() -> new EntityNotFoundException("Batch", batchId));

        int newQuantity = batch.getAvailableQuantity() + adjustment;
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Available quantity cannot be negative");
        }
        if (newQuantity > batch.getTotalQuantity()) {
            throw new IllegalArgumentException("Available quantity cannot exceed total quantity");
        }

        batch.setAvailableQuantity(newQuantity);

        // Update status if fully allocated
        if (newQuantity == 0 && batch.getAvailableQuantity() == 0) {
            batch.setStatus(Batch.BatchStatus.FULLY_ALLOCATED);
        }

        Batch updatedBatch = batchRepository.save(batch);
        log.info("Batch available quantity updated: {}", batchId);
        return toDTO(updatedBatch);
    }

    /**
     * Update batch QC status
     */
    public BatchDTO updateQCStatus(String batchId, Batch.QCStatus qcStatus, String notes) {
        log.info("Updating QC status for batch: {}", batchId);

        Batch batch = batchRepository.findById(batchId)
            .orElseThrow(() -> new EntityNotFoundException("Batch", batchId));

        batch.setQcStatus(qcStatus);
        if (notes != null) {
            batch.setQcNotes(notes);
        }

        Batch updatedBatch = batchRepository.save(batch);
        log.info("Batch QC status updated: {}", batchId);
        return toDTO(updatedBatch);
    }

    /**
     * Delete batch
     */
    public void deleteBatch(String id) {
        log.info("Deleting batch: {}", id);

        if (!batchRepository.existsById(id)) {
            throw new EntityNotFoundException("Batch", id);
        }

        batchRepository.deleteById(id);
        log.info("Batch deleted: {}", id);
    }

    private BatchDTO toDTO(Batch batch) {
        return BatchDTO.builder()
            .id(batch.getId())
            .tenantId(batch.getTenantId())
            .batchNumber(batch.getBatchNumber())
            .sku(batch.getSku())
            .description(batch.getDescription())
            .manufacturingDate(batch.getManufacturingDate())
            .expirationDate(batch.getExpirationDate())
            .supplierId(batch.getSupplierId())
            .supplierName(batch.getSupplierName())
            .countryOfOrigin(batch.getCountryOfOrigin())
            .totalQuantity(batch.getTotalQuantity())
            .availableQuantity(batch.getAvailableQuantity())
            .unitOfMeasure(batch.getUnitOfMeasure())
            .status(batch.getStatus())
            .locationId(batch.getLocationId())
            .qcStatus(batch.getQcStatus())
            .qcNotes(batch.getQcNotes())
            .createdAt(batch.getCreatedAt())
            .updatedAt(batch.getUpdatedAt())
            .build();
    }
}

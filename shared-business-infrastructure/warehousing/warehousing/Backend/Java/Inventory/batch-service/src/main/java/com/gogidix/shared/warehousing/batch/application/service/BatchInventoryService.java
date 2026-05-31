package com.gogidix.shared.warehousing.batch.application.service;

import com.gogidix.shared.warehousing.batch.application.dto.BatchInventoryDTO;
import com.gogidix.shared.warehousing.batch.domain.entity.BatchInventory;
import com.gogidix.shared.warehousing.batch.domain.exception.BatchNotFoundException;
import com.gogidix.shared.warehousing.batch.domain.repository.BatchInventoryRepository;
import com.gogidix.shared.warehousing.batch.infrastructure.security.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Batch Inventory Application Service
 *
 * Handles batch inventory operations with multi-tenant support
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BatchInventoryService {

    private final BatchInventoryRepository batchInventoryRepository;

    /**
     * Get batch inventory by ID
     */
    @Transactional(readOnly = true)
    public BatchInventoryDTO getBatchInventory(String id) {
        BatchInventory inventory = batchInventoryRepository.findById(id)
            .orElseThrow(() -> new BatchNotFoundException("BatchInventory", id));
        return toDTO(inventory);
    }

    /**
     * Get batch inventory by lot ID and SKU
     */
    @Transactional(readOnly = true)
    public Optional<BatchInventoryDTO> getBatchInventoryByLotAndSku(String lotId, String sku) {
        String tenantId = TenantContext.getCurrentTenantId();
        return batchInventoryRepository.findByTenantIdAndLotIdAndSku(tenantId, lotId, sku)
            .map(this::toDTO);
    }

    /**
     * Get all batch inventory for a lot
     */
    @Transactional(readOnly = true)
    public List<BatchInventoryDTO> getBatchInventoryByLot(String lotId) {
        List<BatchInventory> inventory = batchInventoryRepository.findByLotId(lotId);
        return inventory.stream()
            .map(this::toDTO)
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Get batch inventory by SKU
     */
    @Transactional(readOnly = true)
    public List<BatchInventoryDTO> getBatchInventoryBySku(String sku) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<BatchInventory> inventory = batchInventoryRepository.findByTenantIdAndSku(tenantId, sku);
        return inventory.stream()
            .map(this::toDTO)
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Get all batch inventory for current tenant
     */
    @Transactional(readOnly = true)
    public List<BatchInventoryDTO> getAllBatchInventory() {
        String tenantId = TenantContext.getCurrentTenantId();
        List<BatchInventory> inventory = batchInventoryRepository.findByTenantId(tenantId);
        return inventory.stream()
            .map(this::toDTO)
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Get batch inventory by location
     */
    @Transactional(readOnly = true)
    public List<BatchInventoryDTO> getBatchInventoryByLocation(String locationId) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<BatchInventory> inventory = batchInventoryRepository.findByTenantIdAndLocationId(tenantId, locationId);
        return inventory.stream()
            .map(this::toDTO)
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Adjust batch inventory quantity
     */
    public BatchInventoryDTO adjustQuantity(String inventoryId, Integer quantityOnHand, Double unitCost) {
        log.info("Adjusting inventory quantity for: {}", inventoryId);

        BatchInventory inventory = batchInventoryRepository.findById(inventoryId)
            .orElseThrow(() -> new BatchNotFoundException("BatchInventory", inventoryId));

        inventory.setQuantityOnHand(quantityOnHand);
        inventory.setLastCountedDate(LocalDateTime.now());

        if (unitCost != null) {
            inventory.setUnitCost(unitCost);
        }

        inventory.calculateAvailableQuantity();
        inventory.calculateTotalValue();

        BatchInventory updated = batchInventoryRepository.save(inventory);
        log.info("Inventory quantity adjusted for: {}", inventoryId);
        return toDTO(updated);
    }

    /**
     * Allocate inventory quantity
     */
    public BatchInventoryDTO allocateQuantity(String inventoryId, Integer quantity) {
        log.info("Allocating {} from inventory: {}", quantity, inventoryId);

        BatchInventory inventory = batchInventoryRepository.findById(inventoryId)
            .orElseThrow(() -> new BatchNotFoundException("BatchInventory", inventoryId));

        if (inventory.getQuantityAvailable() < quantity) {
            throw new IllegalArgumentException(
                String.format("Insufficient available quantity: requested=%d, available=%d",
                    quantity, inventory.getQuantityAvailable()));
        }

        inventory.setQuantityAllocated(inventory.getQuantityAllocated() + quantity);
        inventory.calculateAvailableQuantity();

        if (inventory.getQuantityAvailable() == 0) {
            inventory.setStatus(BatchInventory.InventoryStatus.ALLOCATED);
        }

        BatchInventory updated = batchInventoryRepository.save(inventory);
        log.info("Allocated {} from inventory: {}, remaining: {}", quantity, inventoryId, updated.getQuantityAvailable());
        return toDTO(updated);
    }

    /**
     * Deallocate inventory quantity
     */
    public BatchInventoryDTO deallocateQuantity(String inventoryId, Integer quantity) {
        log.info("Deallocating {} from inventory: {}", quantity, inventoryId);

        BatchInventory inventory = batchInventoryRepository.findById(inventoryId)
            .orElseThrow(() -> new BatchNotFoundException("BatchInventory", inventoryId));

        if (inventory.getQuantityAllocated() < quantity) {
            throw new IllegalArgumentException(
                String.format("Cannot deallocate more than allocated: requested=%d, allocated=%d",
                    quantity, inventory.getQuantityAllocated()));
        }

        inventory.setQuantityAllocated(inventory.getQuantityAllocated() - quantity);
        inventory.calculateAvailableQuantity();

        if (inventory.getQuantityAllocated() == 0 && inventory.getQuantityAvailable() > 0) {
            inventory.setStatus(BatchInventory.InventoryStatus.AVAILABLE);
        }

        BatchInventory updated = batchInventoryRepository.save(inventory);
        log.info("Deallocated {} from inventory: {}, available: {}", quantity, inventoryId, updated.getQuantityAvailable());
        return toDTO(updated);
    }

    private BatchInventoryDTO toDTO(BatchInventory inventory) {
        return BatchInventoryDTO.builder()
            .id(inventory.getId())
            .tenantId(inventory.getTenantId())
            .lotId(inventory.getLotId())
            .sku(inventory.getSku())
            .locationId(inventory.getLocationId())
            .binLocation(inventory.getBinLocation())
            .quantityOnHand(inventory.getQuantityOnHand())
            .quantityAllocated(inventory.getQuantityAllocated())
            .quantityAvailable(inventory.getQuantityAvailable())
            .quantityInTransit(inventory.getQuantityInTransit())
            .quantityBackordered(inventory.getQuantityBackordered())
            .unitCost(inventory.getUnitCost())
            .totalValue(inventory.getTotalValue())
            .lastCountedDate(inventory.getLastCountedDate())
            .lastCycleCountVariance(inventory.getLastCycleCountVariance())
            .status(inventory.getStatus())
            .createdAt(inventory.getCreatedAt())
            .updatedAt(inventory.getUpdatedAt())
            .build();
    }
}

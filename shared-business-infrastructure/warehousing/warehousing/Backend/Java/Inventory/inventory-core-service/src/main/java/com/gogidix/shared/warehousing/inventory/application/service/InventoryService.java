package com.gogidix.shared.warehousing.inventory.application.service;

import com.gogidix.shared.warehousing.inventory.application.command.CreateInventoryCommand;
import com.gogidix.shared.warehousing.inventory.application.command.UpdateInventoryCommand;
import com.gogidix.shared.warehousing.inventory.application.dto.InventoryDTO;
import com.gogidix.shared.warehousing.inventory.application.mapper.InventoryMapper;
import com.gogidix.shared.warehousing.inventory.domain.entity.Inventory;
import com.gogidix.shared.warehousing.inventory.domain.events.InventoryCreatedEvent;
import com.gogidix.shared.warehousing.inventory.domain.events.InventoryUpdatedEvent;
import com.gogidix.shared.warehousing.inventory.domain.repository.InventoryRepository;
import com.gogidix.shared.warehousing.inventory.infrastructure.messaging.InventoryEventPublisher;
import com.gogidix.shared.warehousing.inventory.infrastructure.security.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Inventory Application Service
 *
 * Handles inventory operations with multi-tenant support
 * Publishes domain events for downstream processing
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;
    private final InventoryEventPublisher eventPublisher;

    /**
     * Create a new inventory item
     */
    public InventoryDTO createInventory(CreateInventoryCommand command) {
        log.info("Creating inventory for SKU: {} in location: {}", command.getSku(), command.getLocationId());

        String tenantId = TenantContext.getCurrentTenantId();

        Inventory inventory = inventoryMapper.toEntity(command);
        inventory.setTenantId(tenantId);

        Inventory savedInventory = inventoryRepository.save(inventory);

        // Publish domain event
        InventoryCreatedEvent event = InventoryCreatedEvent.builder()
            .inventoryId(savedInventory.getId())
            .sku(savedInventory.getSku())
            .quantity(savedInventory.getQuantity())
            .locationId(savedInventory.getLocationId())
            .tenantId(savedInventory.getTenantId())
            .build();
        eventPublisher.publishInventoryCreated(event);

        log.info("Inventory created with ID: {}", savedInventory.getId());
        return inventoryMapper.toDTO(savedInventory);
    }

    /**
     * Get inventory by ID
     */
    @Transactional(readOnly = true)
    public InventoryDTO getInventory(String id) {
        Inventory inventory = inventoryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Inventory not found: " + id));
        return inventoryMapper.toDTO(inventory);
    }

    /**
     * Get all inventory for current tenant
     */
    @Transactional(readOnly = true)
    public List<InventoryDTO> getAllInventory() {
        String tenantId = TenantContext.getCurrentTenantId();
        List<Inventory> inventories = inventoryRepository.findByTenantId(tenantId);
        return inventoryMapper.toDTOList(inventories);
    }

    /**
     * Get inventory by SKU for current tenant
     */
    @Transactional(readOnly = true)
    public List<InventoryDTO> getInventoryBySku(String sku) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<Inventory> inventories = inventoryRepository.findByTenantIdAndSku(tenantId, sku);
        return inventoryMapper.toDTOList(inventories);
    }

    /**
     * Get available inventory (quantity > 0) for current tenant
     */
    @Transactional(readOnly = true)
    public List<InventoryDTO> getAvailableInventory() {
        String tenantId = TenantContext.getCurrentTenantId();
        List<Inventory> inventories = inventoryRepository.findByTenantIdAndQuantityGreaterThan(tenantId, 0);
        return inventoryMapper.toDTOList(inventories);
    }

    /**
     * Update inventory
     */
    public InventoryDTO updateInventory(String id, UpdateInventoryCommand command) {
        log.info("Updating inventory: {}", id);

        Inventory inventory = inventoryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Inventory not found: " + id));

        inventoryMapper.updateEntity(inventory, command);
        Inventory updatedInventory = inventoryRepository.save(inventory);

        // Publish domain event
        InventoryUpdatedEvent event = InventoryUpdatedEvent.builder()
            .inventoryId(updatedInventory.getId())
            .sku(updatedInventory.getSku())
            .quantity(updatedInventory.getQuantity())
            .locationId(updatedInventory.getLocationId())
            .tenantId(updatedInventory.getTenantId())
            .build();
        eventPublisher.publishInventoryUpdated(event);

        log.info("Inventory updated: {}", id);
        return inventoryMapper.toDTO(updatedInventory);
    }

    /**
     * Adjust inventory quantity
     */
    public InventoryDTO adjustQuantity(String id, Integer adjustment) {
        log.info("Adjusting inventory quantity for {} by {}", id, adjustment);

        Inventory inventory = inventoryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Inventory not found: " + id));

        int newQuantity = inventory.getQuantity() + adjustment;
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Inventory quantity cannot be negative");
        }

        inventory.setQuantity(newQuantity);
        Inventory updatedInventory = inventoryRepository.save(inventory);

        // Publish domain event
        InventoryUpdatedEvent event = InventoryUpdatedEvent.builder()
            .inventoryId(updatedInventory.getId())
            .sku(updatedInventory.getSku())
            .quantity(updatedInventory.getQuantity())
            .locationId(updatedInventory.getLocationId())
            .tenantId(updatedInventory.getTenantId())
            .adjustment(adjustment)
            .build();
        eventPublisher.publishInventoryUpdated(event);

        log.info("Inventory quantity adjusted for {}: new quantity = {}", id, newQuantity);
        return inventoryMapper.toDTO(updatedInventory);
    }

    /**
     * Delete inventory
     */
    public void deleteInventory(String id) {
        log.info("Deleting inventory: {}", id);

        if (!inventoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Inventory not found: " + id);
        }

        inventoryRepository.deleteById(id);
        log.info("Inventory deleted: {}", id);
    }

    /**
     * Check if inventory is available for current tenant
     */
    @Transactional(readOnly = true)
    public boolean checkAvailability(String sku, Integer quantity) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<Inventory> inventories = inventoryRepository.findByTenantIdAndSku(tenantId, sku);
        int totalAvailable = inventories.stream()
            .mapToInt(Inventory::getQuantity)
            .sum();
        return totalAvailable >= quantity;
    }
}

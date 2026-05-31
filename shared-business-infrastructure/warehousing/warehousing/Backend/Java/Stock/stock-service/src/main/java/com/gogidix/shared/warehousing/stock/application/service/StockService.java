package com.gogidix.shared.warehousing.stock.application.service;

import com.gogidix.shared.warehousing.stock.application.command.AdjustStockCommand;
import com.gogidix.shared.warehousing.stock.application.command.CreateStockCommand;
import com.gogidix.shared.warehousing.stock.application.command.ReserveStockCommand;
import com.gogidix.shared.warehousing.stock.application.dto.StockLevelDTO;
import com.gogidix.shared.warehousing.stock.application.dto.StockMovementDTO;
import com.gogidix.shared.warehousing.stock.application.mapper.StockMapper;
import com.gogidix.shared.warehousing.stock.domain.entity.StockLevel;
import com.gogidix.shared.warehousing.stock.domain.entity.StockMovement;
import com.gogidix.shared.warehousing.stock.domain.repository.StockLevelRepository;
import com.gogidix.shared.warehousing.stock.domain.repository.StockMovementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Stock Service - Core business logic for stock management
 * 
 * Handles stock operations including:
 * - Creating stock entries
 * - Adjusting stock quantities
 * - Reserving/releasing stock
 * - Tracking stock movements
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockService {

    private final StockLevelRepository stockLevelRepository;
    private final StockMovementRepository stockMovementRepository;
    private final StockMapper stockMapper;

    /**
     * Create initial stock for a SKU at a location
     */
    @Transactional
    public StockLevelDTO createStock(CreateStockCommand command, String tenantId) {
        log.info("Creating stock for SKU: {} at location: {} for tenant: {}", 
            command.getSku(), command.getLocationId(), tenantId);

        // Check if stock already exists
        Optional<StockLevel> existingStock = stockLevelRepository
            .findBySkuAndLocationId(command.getSku(), command.getLocationId());

        if (existingStock.isPresent()) {
            throw new IllegalStateException(
                String.format("Stock already exists for SKU %s at location %s", 
                    command.getSku(), command.getLocationId()));
        }

        // Create new stock level
        StockLevel stockLevel = stockMapper.toEntity(command);
        stockLevel.setTenantId(tenantId);

        StockLevel savedStock = stockLevelRepository.save(stockLevel);

        // Record initial movement
        recordMovement(
            savedStock.getSku(),
            savedStock.getLocationId(),
            tenantId,
            StockMovement.MovementType.INBOUND_ADJUSTMENT,
            command.getQuantity(),
            null,
            null,
            "Initial stock creation",
            "system"
        );

        log.info("Stock created successfully with ID: {}", savedStock.getId());
        return stockMapper.toDTO(savedStock);
    }

    /**
     * Get stock level by SKU and location
     */
    public Optional<StockLevelDTO> getStock(String sku, String locationId) {
        return stockLevelRepository.findBySkuAndLocationId(sku, locationId)
            .map(stockMapper::toDTO);
    }

    /**
     * Get all stock levels for a SKU
     */
    public List<StockLevelDTO> getStockBySku(String sku) {
        return stockMapper.toDTOList(stockLevelRepository.findBySku(sku));
    }

    /**
     * Get all stock levels at a location
     */
    public List<StockLevelDTO> getStockByLocation(String locationId) {
        return stockMapper.toDTOList(stockLevelRepository.findByLocationId(locationId));
    }

    /**
     * Get all stock levels
     */
    public List<StockLevelDTO> getAllStock() {
        return stockMapper.toDTOList(stockLevelRepository.findAll());
    }

    /**
     * Adjust stock quantity (add or remove)
     */
    @Transactional
    public StockLevelDTO adjustStock(AdjustStockCommand command, String tenantId) {
        log.info("Adjusting stock for SKU: {} at location: {} - Type: {}", 
            command.getSku(), command.getLocationId(), command.getMovementType());

        StockLevel stockLevel = stockLevelRepository
            .findBySkuAndLocationId(command.getSku(), command.getLocationId())
            .orElseThrow(() -> new IllegalStateException(
                String.format("Stock not found for SKU %s at location %s", 
                    command.getSku(), command.getLocationId())));

        Integer previousQuantity = stockLevel.getTotalQuantity();
        Integer newQuantity;

        switch (command.getMovementType()) {
            case INBOUND_RECEIPT:
            case INBOUND_RETURN:
            case INBOUND_ADJUSTMENT:
                stockLevel.addStock(command.getQuantity());
                newQuantity = stockLevel.getTotalQuantity();
                break;

            case OUTBOUND_SHIPMENT:
            case OUTBOUND_TRANSFER:
            case OUTBOUND_ADJUSTMENT:
                if (!stockLevel.removeStock(command.getQuantity())) {
                    throw new IllegalStateException(
                        String.format("Insufficient stock for SKU %s at location %s. Available: %d, Requested: %d",
                            command.getSku(), command.getLocationId(), 
                            stockLevel.getAvailableQuantity(), command.getQuantity()));
                }
                newQuantity = stockLevel.getTotalQuantity();
                break;

            case CYCLE_COUNT:
                // For cycle count, quantity represents the actual count
                // Adjust available quantity to match
                int difference = command.getQuantity() - stockLevel.getTotalQuantity();
                if (difference > 0) {
                    stockLevel.addStock(difference);
                } else if (difference < 0) {
                    stockLevel.removeStock(Math.abs(difference));
                }
                newQuantity = stockLevel.getTotalQuantity();
                break;

            default:
                throw new IllegalArgumentException("Unsupported movement type: " + command.getMovementType());
        }

        StockLevel updatedStock = stockLevelRepository.save(stockLevel);

        // Record movement
        recordMovement(
            stockLevel.getSku(),
            stockLevel.getLocationId(),
            tenantId,
            command.getMovementType(),
            command.getQuantity(),
            previousQuantity,
            newQuantity,
            command.getReason(),
            command.getPerformedBy()
        );

        log.info("Stock adjusted successfully. Previous: {}, New: {}", previousQuantity, newQuantity);
        return stockMapper.toDTO(updatedStock);
    }

    /**
     * Reserve stock for an order
     */
    @Transactional
    public StockLevelDTO reserveStock(ReserveStockCommand command, String tenantId) {
        log.info("Reserving {} units of SKU: {} at location: {} for order: {}", 
            command.getQuantity(), command.getSku(), command.getLocationId(), command.getOrderId());

        StockLevel stockLevel = stockLevelRepository
            .findBySkuAndLocationId(command.getSku(), command.getLocationId())
            .orElseThrow(() -> new IllegalStateException(
                String.format("Stock not found for SKU %s at location %s", 
                    command.getSku(), command.getLocationId())));

        Integer previousQuantity = stockLevel.getAvailableQuantity();

        if (!stockLevel.reserve(command.getQuantity())) {
            throw new IllegalStateException(
                String.format("Insufficient available stock for SKU %s at location %s. Available: %d, Requested: %d",
                    command.getSku(), command.getLocationId(), 
                    stockLevel.getAvailableQuantity(), command.getQuantity()));
        }

        StockLevel updatedStock = stockLevelRepository.save(stockLevel);

        // Record movement
        recordMovement(
            stockLevel.getSku(),
            stockLevel.getLocationId(),
            tenantId,
            StockMovement.MovementType.RESERVE,
            command.getQuantity(),
            previousQuantity,
            stockLevel.getAvailableQuantity(),
            "Stock reserved for order: " + command.getOrderId(),
            command.getReservedBy()
        );

        log.info("Stock reserved successfully. Available now: {}", stockLevel.getAvailableQuantity());
        return stockMapper.toDTO(updatedStock);
    }

    /**
     * Release reserved stock back to available
     */
    @Transactional
    public StockLevelDTO releaseStock(String sku, String locationId, Integer quantity, 
                                       String orderId, String tenantId, String releasedBy) {
        log.info("Releasing {} units of SKU: {} at location: {} from order: {}", 
            quantity, sku, locationId, orderId);

        StockLevel stockLevel = stockLevelRepository
            .findBySkuAndLocationId(sku, locationId)
            .orElseThrow(() -> new IllegalStateException(
                String.format("Stock not found for SKU %s at location %s", sku, locationId)));

        Integer previousReserved = stockLevel.getReservedQuantity();

        if (!stockLevel.release(quantity)) {
            throw new IllegalStateException(
                String.format("Cannot release %d units. Only %d units reserved",
                    quantity, stockLevel.getReservedQuantity()));
        }

        StockLevel updatedStock = stockLevelRepository.save(stockLevel);

        // Record movement
        recordMovement(
            stockLevel.getSku(),
            stockLevel.getLocationId(),
            tenantId,
            StockMovement.MovementType.RELEASE,
            quantity,
            previousReserved,
            stockLevel.getReservedQuantity(),
            "Stock released from order: " + orderId,
            releasedBy
        );

        log.info("Stock released successfully. Reserved now: {}", stockLevel.getReservedQuantity());
        return stockMapper.toDTO(updatedStock);
    }

    /**
     * Allocate reserved stock (move from reserved to allocated)
     */
    @Transactional
    public StockLevelDTO allocateStock(String sku, String locationId, Integer quantity, 
                                        String orderId, String tenantId, String allocatedBy) {
        log.info("Allocating {} units of SKU: {} at location: {} for order: {}", 
            quantity, sku, locationId, orderId);

        StockLevel stockLevel = stockLevelRepository
            .findBySkuAndLocationId(sku, locationId)
            .orElseThrow(() -> new IllegalStateException(
                String.format("Stock not found for SKU %s at location %s", sku, locationId)));

        if (!stockLevel.allocate(quantity)) {
            throw new IllegalStateException(
                String.format("Cannot allocate %d units. Only %d units reserved",
                    quantity, stockLevel.getReservedQuantity()));
        }

        StockLevel updatedStock = stockLevelRepository.save(stockLevel);

        // Record movement
        recordMovement(
            stockLevel.getSku(),
            stockLevel.getLocationId(),
            tenantId,
            StockMovement.MovementType.ALLOCATE,
            quantity,
            null,
            null,
            "Stock allocated for order: " + orderId,
            allocatedBy
        );

        log.info("Stock allocated successfully. Allocated: {}", stockLevel.getAllocatedQuantity());
        return stockMapper.toDTO(updatedStock);
    }

    /**
     * Confirm allocation (stock shipped)
     */
    @Transactional
    public StockLevelDTO confirmAllocation(String sku, String locationId, Integer quantity, 
                                            String orderId, String tenantId, String confirmedBy) {
        log.info("Confirming allocation of {} units of SKU: {} at location: {} for order: {}", 
            quantity, sku, locationId, orderId);

        StockLevel stockLevel = stockLevelRepository
            .findBySkuAndLocationId(sku, locationId)
            .orElseThrow(() -> new IllegalStateException(
                String.format("Stock not found for SKU %s at location %s", sku, locationId)));

        if (!stockLevel.confirmAllocation(quantity)) {
            throw new IllegalStateException(
                String.format("Cannot confirm %d units. Only %d units allocated",
                    quantity, stockLevel.getAllocatedQuantity()));
        }

        StockLevel updatedStock = stockLevelRepository.save(stockLevel);

        // Record movement
        recordMovement(
            stockLevel.getSku(),
            stockLevel.getLocationId(),
            tenantId,
            StockMovement.MovementType.CONFIRM_ALLOCATION,
            quantity,
            null,
            null,
            "Allocation confirmed for order: " + orderId,
            confirmedBy
        );

        log.info("Allocation confirmed successfully. Allocated now: {}", stockLevel.getAllocatedQuantity());
        return stockMapper.toDTO(updatedStock);
    }

    /**
     * Get stock movements for a SKU
     */
    public List<StockMovementDTO> getStockMovements(String sku) {
        return stockMapper.toMovementDTOList(stockMovementRepository.findBySkuOrderByTimestampDesc(sku));
    }

    /**
     * Get stock movements for a SKU with pagination
     */
    public Page<StockMovementDTO> getStockMovements(String sku, Pageable pageable) {
        return stockMovementRepository.findBySkuOrderByTimestampDesc(sku, pageable)
            .map(stockMapper::toMovementDTO);
    }

    /**
     * Get stock movements for a SKU at a location
     */
    public List<StockMovementDTO> getStockMovements(String sku, String locationId) {
        return stockMapper.toMovementDTOList(
            stockMovementRepository.findBySkuAndLocationIdOrderByTimestampDesc(sku, locationId));
    }

    /**
     * Get stock movements by reference ID
     */
    public List<StockMovementDTO> getStockMovementsByReference(String referenceId) {
        return stockMapper.toMovementDTOList(
            stockMovementRepository.findByReferenceId(referenceId));
    }

    /**
     * Get stock levels below reorder point
     */
    public List<StockLevelDTO> getStockBelowReorderPoint() {
        return stockMapper.toDTOList(stockLevelRepository.findBelowReorderPoint());
    }

    /**
     * Get total available quantity for SKU across all locations
     */
    public Integer getTotalAvailableQuantity(String sku) {
        return stockLevelRepository.getTotalAvailableQuantity(sku);
    }

    /**
     * Delete stock (with validation)
     */
    @Transactional
    public void deleteStock(String sku, String locationId) {
        log.info("Deleting stock for SKU: {} at location: {}", sku, locationId);

        StockLevel stockLevel = stockLevelRepository
            .findBySkuAndLocationId(sku, locationId)
            .orElseThrow(() -> new IllegalStateException(
                String.format("Stock not found for SKU %s at location %s", sku, locationId)));

        // Validate no reserved or allocated stock
        if (stockLevel.getReservedQuantity() > 0 || stockLevel.getAllocatedQuantity() > 0) {
            throw new IllegalStateException(
                String.format("Cannot delete stock with reserved (%d) or allocated (%d) quantities",
                    stockLevel.getReservedQuantity(), stockLevel.getAllocatedQuantity()));
        }

        stockLevelRepository.delete(stockLevel);
        log.info("Stock deleted successfully");
    }

    /**
     * Helper method to record stock movements
     */
    private void recordMovement(String sku, String locationId, String tenantId,
                                StockMovement.MovementType movementType, Integer quantity,
                                Integer previousQuantity, Integer newQuantity,
                                String reason, String performedBy) {
        
        StockMovement movement = StockMovement.builder()
            .sku(sku)
            .locationId(locationId)
            .tenantId(tenantId)
            .movementType(movementType)
            .quantity(quantity)
            .previousQuantity(previousQuantity)
            .newQuantity(newQuantity)
            .reason(reason)
            .performedBy(performedBy != null ? performedBy : "system")
            .timestamp(LocalDateTime.now())
            .build();

        stockMovementRepository.save(movement);
    }
}
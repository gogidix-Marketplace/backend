package com.gogidix.shared.warehouse.inventory.stock.application.service;

import com.gogidix.shared.warehouse.inventory.stock.application.command.AdjustStockCommand;
import com.gogidix.shared.warehouse.inventory.stock.application.command.RecordStockMovementCommand;
import com.gogidix.shared.warehouse.inventory.stock.application.dto.StockItemDTO;
import com.gogidix.shared.warehouse.inventory.stock.application.dto.StockMovementDTO;
import com.gogidix.shared.warehouse.inventory.stock.application.mapper.StockDtoMapper;
import com.gogidix.shared.warehouse.inventory.stock.domain.entity.StockItem;
import com.gogidix.shared.warehouse.inventory.stock.domain.entity.StockMovement;
import com.gogidix.shared.warehouse.inventory.stock.domain.events.LowStockEvent;
import com.gogidix.shared.warehouse.inventory.stock.domain.events.StockAdjustedEvent;
import com.gogidix.shared.warehouse.inventory.stock.domain.repository.StockItemRepository;
import com.gogidix.shared.warehouse.inventory.stock.domain.repository.StockMovementRepository;
import com.gogidix.shared.warehouse.inventory.stock.domain.service.StockDomainService;
import com.gogidix.shared.warehouse.inventory.stock.infrastructure.messaging.StockEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Application Service for Stock Operations
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StockApplicationService {

    private final StockItemRepository stockItemRepository;
    private final StockMovementRepository movementRepository;
    private final StockDomainService domainService;
    private final StockDtoMapper dtoMapper;
    private final StockEventPublisher eventPublisher;

    @Transactional
    public StockItemDTO adjustStock(AdjustStockCommand command) {
        log.info("Adjusting stock for SKU: {} by quantity: {}",
                command.getSku(), command.getQuantity());

        StockItem stockItem = stockItemRepository
                .findByTenantIdAndSku(command.getTenantId(), command.getSku())
                .orElseGet(() -> createNewStockItem(command));

        int previousQuantity = stockItem.getQuantity();
        stockItem.setQuantity(stockItem.getQuantity() + command.getQuantity());
        stockItem.setLastUpdated(java.time.LocalDateTime.now());
        stockItem = stockItemRepository.save(stockItem);

        // Record movement
        StockMovement movement = dtoMapper.toMovementEntity(command, stockItem);
        movementRepository.save(movement);

        StockAdjustedEvent event = StockAdjustedEvent.builder()
                .tenantId(stockItem.getTenantId())
                .stockItemId(stockItem.getStockItemId())
                .sku(stockItem.getSku())
                .previousQuantity(previousQuantity)
                .newQuantity(stockItem.getQuantity())
                .adjustment(command.getQuantity())
                .reason(command.getReason())
                .build();
        eventPublisher.publishStockAdjusted(event);

        // Check for low stock
        if (domainService.isLowStock(stockItem)) {
            LowStockEvent lowStockEvent = LowStockEvent.builder()
                    .tenantId(stockItem.getTenantId())
                    .stockItemId(stockItem.getStockItemId())
                    .sku(stockItem.getSku())
                    .currentQuantity(stockItem.getQuantity())
                    .minimumThreshold(stockItem.getMinThreshold())
                    .build();
            eventPublisher.publishLowStock(lowStockEvent);
        }

        return dtoMapper.toDTO(stockItem);
    }

    public Optional<StockItemDTO> getStockItem(String tenantId, String sku) {
        return stockItemRepository.findByTenantIdAndSku(tenantId, sku)
                .map(dtoMapper::toDTO);
    }

    public List<StockMovementDTO> getStockMovements(String tenantId, String sku) {
        return movementRepository.findByTenantIdAndSkuOrderByCreatedAtDesc(
                tenantId, sku)
                .stream()
                .map(dtoMapper::toMovementDTO)
                .toList();
    }

    public List<StockItemDTO> getLowStockItems(String tenantId) {
        return stockItemRepository.findByTenantId(tenantId)
                .stream()
                .filter(domainService::isLowStock)
                .map(dtoMapper::toDTO)
                .toList();
    }

    private StockItem createNewStockItem(AdjustStockCommand command) {
        return StockItem.builder()
                .stockItemId(java.util.UUID.randomUUID().toString())
                .tenantId(command.getTenantId())
                .sku(command.getSku())
                .quantity(0)
                .minThreshold(10)
                .maxThreshold(1000)
                .location(command.getLocationId())
                .createdAt(java.time.LocalDateTime.now())
                .lastUpdated(java.time.LocalDateTime.now())
                .build();
    }
}

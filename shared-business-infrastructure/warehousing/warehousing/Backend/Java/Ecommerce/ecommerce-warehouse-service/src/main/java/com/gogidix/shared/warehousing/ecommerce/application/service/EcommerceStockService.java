package com.gogidix.shared.warehousing.ecommerce.application.service;

import com.gogidix.shared.warehousing.ecommerce.domain.entity.VendorStockRegistration;
import com.gogidix.shared.warehousing.ecommerce.domain.entity.WarehouseStockLevel;
import com.gogidix.shared.warehousing.ecommerce.domain.events.WarehouseStockRegisteredEvent;
import com.gogidix.shared.warehousing.ecommerce.domain.repository.VendorStockRegistrationRepository;
import com.gogidix.shared.warehousing.ecommerce.domain.repository.WarehouseStockLevelRepository;
import com.gogidix.shared.warehousing.ecommerce.infrastructure.messaging.producers.EcommerceWarehouseEventProducer;
import com.gogidix.shared.warehousing.ecommerce.interfaces.rest.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EcommerceStockService {

    private final VendorStockRegistrationRepository registrationRepository;
    private final WarehouseStockLevelRepository stockLevelRepository;
    private final EcommerceWarehouseEventProducer eventProducer;

    public StockRegistrationResponse registerStock(StockRegistrationRequest request) {
        log.info("Registering stock for vendor {} at warehouse {}", request.getVendorId(), request.getWarehouseId());

        List<VendorStockRegistration.StockItem> stockItems = request.getItems().stream()
                .map(item -> {
                    String location = generateLocation(request.getWarehouseId(), item.getSku());
                    return VendorStockRegistration.StockItem.builder()
                            .sku(item.getSku())
                            .productName(item.getProductName())
                            .quantity(item.getQuantity())
                            .unitOfMeasure(item.getUnitOfMeasure() != null ? item.getUnitOfMeasure() : "EA")
                            .category(item.getCategory())
                            .weight(item.getWeight())
                            .weightUnit(item.getWeightUnit())
                            .dimensions(item.getDimensions() != null ? VendorStockRegistration.Dimensions.builder()
                                    .length(item.getDimensions().getLength())
                                    .width(item.getDimensions().getWidth())
                                    .height(item.getDimensions().getHeight())
                                    .build() : null)
                            .attributes(item.getAttributes())
                            .allocatedLocation(location)
                            .status("REGISTERED")
                            .build();
                })
                .collect(Collectors.toList());

        VendorStockRegistration registration = VendorStockRegistration.builder()
                .vendorId(request.getVendorId())
                .warehouseId(request.getWarehouseId())
                .zoneId(request.getZoneId())
                .sellingRadius(request.getSellingRadius() != null ? request.getSellingRadius() : "NATIONWIDE")
                .items(stockItems)
                .status("REGISTERED")
                .estimatedReceivingDate(LocalDateTime.now().plusDays(1))
                .build();

        VendorStockRegistration saved = registrationRepository.save(registration);

        for (var item : request.getItems()) {
            upsertStockLevel(request.getVendorId(), request.getWarehouseId(), request.getZoneId(),
                    item.getSku(), item.getProductName(), item.getCategory(),
                    item.getQuantity(), request.getSellingRadius());
        }

        List<WarehouseStockRegisteredEvent.StockItemInfo> eventItems = stockItems.stream()
                .map(si -> WarehouseStockRegisteredEvent.StockItemInfo.builder()
                        .sku(si.getSku())
                        .quantity(si.getQuantity())
                        .location(si.getAllocatedLocation())
                        .build())
                .collect(Collectors.toList());
        eventProducer.publishStockRegistered(request.getVendorId(), request.getWarehouseId(),
                request.getZoneId(), eventItems);

        List<StockRegistrationResponse.RegisteredItem> responseItems = stockItems.stream()
                .map(si -> StockRegistrationResponse.RegisteredItem.builder()
                        .sku(si.getSku())
                        .allocatedLocation(si.getAllocatedLocation())
                        .quantity(si.getQuantity())
                        .status(si.getStatus())
                        .build())
                .collect(Collectors.toList());

        return StockRegistrationResponse.builder()
                .registrationId(saved.getId())
                .vendorId(saved.getVendorId())
                .warehouseId(saved.getWarehouseId())
                .zoneId(saved.getZoneId())
                .items(responseItems)
                .estimatedReceivingDate(saved.getEstimatedReceivingDate())
                .build();
    }

    public WarehouseAvailabilityResponse getWarehouseAvailability(String zoneId, String sku) {
        log.info("Querying warehouse availability for SKU {} in zone {}", sku, zoneId);

        List<WarehouseStockLevel> stockLevels;
        if (zoneId != null && sku != null) {
            stockLevels = stockLevelRepository.findBySkuAndZoneId(sku, zoneId);
        } else if (sku != null) {
            stockLevels = stockLevelRepository.findBySku(sku);
        } else if (zoneId != null) {
            stockLevels = stockLevelRepository.findByZoneId(zoneId);
        } else {
            stockLevels = stockLevelRepository.findAll();
        }

        Map<String, List<WarehouseStockLevel>> byWarehouse = stockLevels.stream()
                .collect(Collectors.groupingBy(WarehouseStockLevel::getWarehouseId));

        int totalAvailable = stockLevels.stream()
                .mapToInt(WarehouseStockLevel::getAvailable)
                .sum();

        List<WarehouseAvailabilityResponse.WarehouseStockInfo> warehouseInfos = byWarehouse.entrySet().stream()
                .map(entry -> {
                    List<WarehouseStockLevel> levels = entry.getValue();
                    WarehouseStockLevel first = levels.get(0);
                    int available = levels.stream().mapToInt(WarehouseStockLevel::getAvailable).sum();
                    int reserved = levels.stream().mapToInt(sl -> sl.getReserved() != null ? sl.getReserved() : 0).sum();
                    int incoming = levels.stream().mapToInt(sl -> sl.getIncoming() != null ? sl.getIncoming() : 0).sum();
                    return WarehouseAvailabilityResponse.WarehouseStockInfo.builder()
                            .warehouseId(entry.getKey())
                            .warehouseName(formatWarehouseName(entry.getKey()))
                            .zoneId(first.getZoneId())
                            .available(available)
                            .reserved(reserved)
                            .incoming(incoming)
                            .estimatedPickTime(first.getEstimatedPickTime() != null ? first.getEstimatedPickTime() : "2 hours")
                            .build();
                })
                .collect(Collectors.toList());

        return WarehouseAvailabilityResponse.builder()
                .sku(sku)
                .totalAvailable(totalAvailable)
                .warehouses(warehouseInfos)
                .build();
    }

    public VendorStockOverviewResponse getVendorStockOverview(String vendorId) {
        log.info("Getting stock overview for vendor {}", vendorId);

        List<WarehouseStockLevel> vendorStock = stockLevelRepository.findByVendorId(vendorId);

        Map<String, List<WarehouseStockLevel>> byWarehouse = vendorStock.stream()
                .collect(Collectors.groupingBy(WarehouseStockLevel::getWarehouseId));

        String sellingRadius = vendorStock.isEmpty() ? "NATIONWIDE" :
                vendorStock.get(0).getSellingRadius() != null ? vendorStock.get(0).getSellingRadius() : "NATIONWIDE";

        long totalSKUs = vendorStock.stream()
                .map(WarehouseStockLevel::getSku)
                .distinct()
                .count();

        List<VendorStockOverviewResponse.WarehouseStockSummary> summaries = byWarehouse.entrySet().stream()
                .map(entry -> {
                    List<WarehouseStockLevel> levels = entry.getValue();
                    WarehouseStockLevel first = levels.get(0);
                    return VendorStockOverviewResponse.WarehouseStockSummary.builder()
                            .warehouseId(entry.getKey())
                            .zoneId(first.getZoneId())
                            .warehouseName(formatWarehouseName(entry.getKey()))
                            .skuCount((int) levels.stream().map(WarehouseStockLevel::getSku).distinct().count())
                            .totalUnits(levels.stream().mapToInt(WarehouseStockLevel::getQuantity).sum())
                            .build();
                })
                .collect(Collectors.toList());

        return VendorStockOverviewResponse.builder()
                .vendorId(vendorId)
                .sellingRadius(sellingRadius)
                .totalSKUs((int) totalSKUs)
                .warehouses(summaries)
                .build();
    }

    public void updateStockLevel(String vendorId, String warehouseId, String sku, Integer quantity) {
        log.info("Updating stock level: vendor={}, warehouse={}, sku={}, qty={}", vendorId, warehouseId, sku, quantity);
        Optional<WarehouseStockLevel> existing = stockLevelRepository.findBySkuAndWarehouseId(sku, warehouseId);
        if (existing.isPresent()) {
            WarehouseStockLevel level = existing.get();
            level.setQuantity(quantity);
            stockLevelRepository.save(level);
            checkStockThresholds(level);
        } else {
            log.warn("Stock level not found for sku {} at warehouse {}", sku, warehouseId);
        }
    }

    public boolean reserveStock(String warehouseId, String sku, Integer quantity) {
        Optional<WarehouseStockLevel> existing = stockLevelRepository.findBySkuAndWarehouseId(sku, warehouseId);
        if (existing.isPresent()) {
            WarehouseStockLevel level = existing.get();
            if (level.getAvailable() >= quantity) {
                level.setReserved(level.getReserved() + quantity);
                stockLevelRepository.save(level);
                checkStockThresholds(level);
                return true;
            }
        }
        return false;
    }

    public void releaseStock(String warehouseId, String sku, Integer quantity) {
        Optional<WarehouseStockLevel> existing = stockLevelRepository.findBySkuAndWarehouseId(sku, warehouseId);
        if (existing.isPresent()) {
            WarehouseStockLevel level = existing.get();
            level.setReserved(Math.max(0, level.getReserved() - quantity));
            stockLevelRepository.save(level);
        }
    }

    private void upsertStockLevel(String vendorId, String warehouseId, String zoneId,
                                   String sku, String productName, String category,
                                   Integer quantity, String sellingRadius) {
        Optional<WarehouseStockLevel> existing = stockLevelRepository.findBySkuAndWarehouseId(sku, warehouseId);
        if (existing.isPresent()) {
            WarehouseStockLevel level = existing.get();
            level.setQuantity(level.getQuantity() + quantity);
            stockLevelRepository.save(level);
            checkStockThresholds(level);
        } else {
            WarehouseStockLevel newLevel = WarehouseStockLevel.builder()
                    .vendorId(vendorId)
                    .warehouseId(warehouseId)
                    .zoneId(zoneId)
                    .sku(sku)
                    .productName(productName)
                    .category(category)
                    .quantity(quantity)
                    .reserved(0)
                    .incoming(0)
                    .sellingRadius(sellingRadius != null ? sellingRadius : "NATIONWIDE")
                    .estimatedPickTime("2 hours")
                    .reorderThreshold(10)
                    .build();
            stockLevelRepository.save(newLevel);
        }
    }

    private void checkStockThresholds(WarehouseStockLevel level) {
        if (level.isDepleted()) {
            eventProducer.publishStockDepleted(level.getVendorId(), level.getWarehouseId(), level.getSku());
        } else if (level.isLowStock()) {
            eventProducer.publishStockLow(level.getVendorId(), level.getWarehouseId(),
                    level.getSku(), level.getAvailable(), level.getReorderThreshold());
        }
    }

    private String generateLocation(String warehouseId, String sku) {
        Random random = new Random(sku.hashCode());
        char zone = (char) ('A' + random.nextInt(6));
        int aisle = random.nextInt(10) + 1;
        int shelf = random.nextInt(5) + 1;
        int bin = random.nextInt(20) + 1;
        return String.format("ZONE-%c-AISLE-%02d-SHELF-%d-BIN-%d", zone, aisle, shelf, bin);
    }

    private String formatWarehouseName(String warehouseId) {
        if (warehouseId == null) return "Unknown Warehouse";
        if (warehouseId.toLowerCase().contains("lag")) return "Lagos Fulfillment Center";
        if (warehouseId.toLowerCase().contains("abj")) return "Abuja Storage Hub";
        return warehouseId.replace("-", " ");
    }
}

package com.gogidix.shared.warehousing.fulfillment.packing.application.service;

import com.gogidix.shared.warehousing.fulfillment.packing.domain.entity.PackOrder;
import com.gogidix.shared.warehousing.fulfillment.packing.domain.repository.PackOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Application Service for Pack Order Management
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PackOrderService {

    private final PackOrderRepository packOrderRepository;

    public PackOrder createPackOrder(PackOrder packOrder) {
        log.info("Creating pack order for pick order: {}", packOrder.getPickOrderId());

        packOrder.setId(UUID.randomUUID().toString());
        packOrder.setPackNumber(generatePackNumber());
        packOrder.setStatus(PackOrder.PackStatus.PENDING);
        packOrder.setCreatedAt(LocalDateTime.now());
        packOrder.setUpdatedAt(LocalDateTime.now());

        if (packOrder.getItems() != null) {
            packOrder.setTotalItems(packOrder.getItems().size());
        }

        return packOrderRepository.save(packOrder);
    }

    public PackOrder getByPackNumber(String tenantId, String packNumber) {
        return packOrderRepository.findByTenantIdAndPackNumber(tenantId, packNumber)
                .orElseThrow(() -> new IllegalArgumentException("Pack order not found: " + packNumber));
    }

    public PackOrder getByPickOrderId(String tenantId, String pickOrderId) {
        return packOrderRepository.findByTenantIdAndPickOrderId(tenantId, pickOrderId)
                .orElseThrow(() -> new IllegalArgumentException("Pack order not found for pick: " + pickOrderId));
    }

    public List<PackOrder> getPendingOrders(String tenantId, String warehouseId) {
        return packOrderRepository.findPendingOrdersByWarehouse(tenantId, warehouseId);
    }

    public List<PackOrder> getByStatus(String tenantId, String status) {
        return packOrderRepository.findByTenantIdAndStatus(
                tenantId, PackOrder.PackStatus.valueOf(status));
    }

    public List<PackOrder> getCompletedWithoutTracking(String tenantId) {
        return packOrderRepository.findCompletedWithoutTracking(tenantId);
    }

    public PackOrder assignPacker(String packOrderId, String packerId, String packerName) {
        PackOrder packOrder = packOrderRepository.findById(packOrderId)
                .orElseThrow(() -> new IllegalArgumentException("Pack order not found: " + packOrderId));

        packOrder.setStatus(PackOrder.PackStatus.ASSIGNED);
        packOrder.setPackerId(packerId);
        packOrder.setPackerName(packerName);
        packOrder.setUpdatedAt(LocalDateTime.now());

        return packOrderRepository.save(packOrder);
    }

    public PackOrder startPacking(String packOrderId, String packerId, String packerName) {
        PackOrder packOrder = packOrderRepository.findById(packOrderId)
                .orElseThrow(() -> new IllegalArgumentException("Pack order not found: " + packOrderId));

        packOrder.startPacking(packerId, packerName);
        return packOrderRepository.save(packOrder);
    }

    public PackOrder markItemPacked(String packOrderId, String itemId, Double quantity, String boxNumber, String packedBy) {
        PackOrder packOrder = packOrderRepository.findById(packOrderId)
                .orElseThrow(() -> new IllegalArgumentException("Pack order not found: " + packOrderId));

        packOrder.markItemPacked(itemId, quantity, boxNumber, packedBy);
        return packOrderRepository.save(packOrder);
    }

    public PackOrder completePacking(String packOrderId) {
        PackOrder packOrder = packOrderRepository.findById(packOrderId)
                .orElseThrow(() -> new IllegalArgumentException("Pack order not found: " + packOrderId));

        packOrder.completePacking();
        return packOrderRepository.save(packOrder);
    }

    public PackOrder markAsShipped(String packOrderId, String trackingNumber) {
        PackOrder packOrder = packOrderRepository.findById(packOrderId)
                .orElseThrow(() -> new IllegalArgumentException("Pack order not found: " + packOrderId));

        packOrder.markAsShipped(trackingNumber);
        return packOrderRepository.save(packOrder);
    }

    private String generatePackNumber() {
        return "PACK-" + System.currentTimeMillis();
    }
}

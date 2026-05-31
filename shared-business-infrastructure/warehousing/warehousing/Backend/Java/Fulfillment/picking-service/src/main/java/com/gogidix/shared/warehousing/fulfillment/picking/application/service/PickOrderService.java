package com.gogidix.shared.warehousing.fulfillment.picking.application.service;

import com.gogidix.shared.warehousing.fulfillment.picking.domain.entity.PickOrder;
import com.gogidix.shared.warehousing.fulfillment.picking.domain.repository.PickOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Application Service for Pick Order Management
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PickOrderService {

    private final PickOrderRepository pickOrderRepository;

    public PickOrder createPickOrder(PickOrder pickOrder) {
        log.info("Creating pick order for order: {}", pickOrder.getOrderNumber());

        pickOrder.setId(UUID.randomUUID().toString());
        pickOrder.setPickNumber(generatePickNumber());
        pickOrder.setStatus(PickOrder.PickStatus.PENDING);
        pickOrder.setCreatedAt(LocalDateTime.now());
        pickOrder.setUpdatedAt(LocalDateTime.now());

        if (pickOrder.getItems() != null) {
            pickOrder.setTotalItems(pickOrder.getItems().size());
            pickOrder.setTotalQuantity(pickOrder.getItems().stream()
                    .mapToDouble(PickOrder.PickItem::getQuantity).sum());
        }

        return pickOrderRepository.save(pickOrder);
    }

    public PickOrder getByPickNumber(String tenantId, String pickNumber) {
        return pickOrderRepository.findByTenantIdAndPickNumber(tenantId, pickNumber)
                .orElseThrow(() -> new IllegalArgumentException("Pick order not found: " + pickNumber));
    }

    public PickOrder getByOrderNumber(String tenantId, String orderNumber) {
        return pickOrderRepository.findByTenantIdAndOrderNumber(tenantId, orderNumber)
                .orElseThrow(() -> new IllegalArgumentException("Pick order not found for order: " + orderNumber));
    }

    public List<PickOrder> getPendingOrders(String tenantId, String warehouseId) {
        return pickOrderRepository.findPendingOrdersByWarehouse(tenantId, warehouseId);
    }

    public List<PickOrder> getByStatus(String tenantId, String status) {
        return pickOrderRepository.findByTenantIdAndStatus(
                tenantId, PickOrder.PickStatus.valueOf(status));
    }

    public List<PickOrder> getOverdueOrders(String tenantId) {
        return pickOrderRepository.findByTenantIdAndStatusAndDueDateBefore(
                tenantId, PickOrder.PickStatus.PENDING, LocalDateTime.now());
    }

    public PickOrder assignPicker(String pickOrderId, String pickerId, String pickerName) {
        PickOrder pickOrder = pickOrderRepository.findById(pickOrderId)
                .orElseThrow(() -> new IllegalArgumentException("Pick order not found: " + pickOrderId));

        pickOrder.setStatus(PickOrder.PickStatus.ASSIGNED);
        pickOrder.setPickerId(pickerId);
        pickOrder.setPickerName(pickerName);
        pickOrder.setUpdatedAt(LocalDateTime.now());

        return pickOrderRepository.save(pickOrder);
    }

    public PickOrder startPicking(String pickOrderId, String pickerId, String pickerName) {
        PickOrder pickOrder = pickOrderRepository.findById(pickOrderId)
                .orElseThrow(() -> new IllegalArgumentException("Pick order not found: " + pickOrderId));

        pickOrder.startPicking(pickerId, pickerName);
        return pickOrderRepository.save(pickOrder);
    }

    public PickOrder markItemPicked(String pickOrderId, String itemId, Double quantity, String pickedBy) {
        PickOrder pickOrder = pickOrderRepository.findById(pickOrderId)
                .orElseThrow(() -> new IllegalArgumentException("Pick order not found: " + pickOrderId));

        pickOrder.markItemPicked(itemId, quantity, pickedBy);
        return pickOrderRepository.save(pickOrder);
    }

    public PickOrder completePicking(String pickOrderId) {
        PickOrder pickOrder = pickOrderRepository.findById(pickOrderId)
                .orElseThrow(() -> new IllegalArgumentException("Pick order not found: " + pickOrderId));

        pickOrder.completePicking();
        return pickOrderRepository.save(pickOrder);
    }

    private String generatePickNumber() {
        return "PICK-" + System.currentTimeMillis();
    }
}

package com.gogidix.shared.warehousing.ecommerce.application.service;

import com.gogidix.shared.warehousing.ecommerce.domain.entity.EcommerceFulfillmentOrder;
import com.gogidix.shared.warehousing.ecommerce.domain.events.ReadyForPickupEvent;
import com.gogidix.shared.warehousing.ecommerce.domain.repository.EcommerceFulfillmentOrderRepository;
import com.gogidix.shared.warehousing.ecommerce.infrastructure.messaging.producers.EcommerceWarehouseEventProducer;
import com.gogidix.shared.warehousing.ecommerce.interfaces.rest.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EcommerceFulfillmentService {

    private final EcommerceFulfillmentOrderRepository repository;
    private final EcommerceStockService stockService;
    private final EcommerceWarehouseEventProducer eventProducer;

    public EcommerceFulfillmentResponse createFulfillment(EcommerceFulfillmentRequest request) {
        log.info("Creating fulfillment for order {} at warehouse {}", request.getOrderId(), request.getWarehouseId());

        for (EcommerceFulfillmentRequest.OrderItem item : request.getItems()) {
            boolean reserved = stockService.reserveStock(request.getWarehouseId(), item.getSku(), item.getQuantity());
            if (!reserved) {
                throw new IllegalArgumentException("Insufficient stock for SKU: " + item.getSku() + " at warehouse: " + request.getWarehouseId());
            }
        }

        String fulfillmentId = "ful-" + UUID.randomUUID().toString();

        List<EcommerceFulfillmentOrder.FulfillmentItem> items = request.getItems().stream()
                .map(item -> EcommerceFulfillmentOrder.FulfillmentItem.builder()
                        .sku(item.getSku())
                        .productName(item.getProductName())
                        .quantity(item.getQuantity())
                        .build())
                .collect(Collectors.toList());

        EcommerceFulfillmentOrder.CustomerAddress address = null;
        if (request.getCustomerAddress() != null) {
            address = EcommerceFulfillmentOrder.CustomerAddress.builder()
                    .address(request.getCustomerAddress().getAddress())
                    .latitude(request.getCustomerAddress().getLatitude())
                    .longitude(request.getCustomerAddress().getLongitude())
                    .build();
        }

        EcommerceFulfillmentOrder order = EcommerceFulfillmentOrder.builder()
                .fulfillmentId(fulfillmentId)
                .orderId(request.getOrderId())
                .subOrderId(request.getSubOrderId())
                .vendorId(request.getVendorId())
                .warehouseId(request.getWarehouseId())
                .status("RECEIVED")
                .priority(request.getPriority() != null ? request.getPriority() : "NORMAL")
                .deliveryDeadline(request.getDeliveryDeadline())
                .deliveryType(request.getDeliveryType())
                .items(items)
                .customerAddress(address)
                .specialInstructions(request.getSpecialInstructions())
                .estimatedCompletion(LocalDateTime.now().plusHours(2))
                .build();

        order.initializeStages();

        EcommerceFulfillmentOrder saved = repository.save(order);

        eventProducer.publishFulfillmentReceived(fulfillmentId, request.getOrderId(),
                request.getSubOrderId(), request.getWarehouseId(), null);

        return mapToResponse(saved);
    }

    public FulfillmentStatusResponse getFulfillmentStatus(String fulfillmentId) {
        log.info("Querying fulfillment status for {}", fulfillmentId);
        EcommerceFulfillmentOrder order = repository.findByFulfillmentId(fulfillmentId)
                .orElseThrow(() -> new IllegalArgumentException("Fulfillment not found: " + fulfillmentId));
        return mapToStatusResponse(order);
    }

    public FulfillmentStatusResponse startPicking(String fulfillmentId, String staffId, String staffName) {
        log.info("Starting picking for fulfillment {}", fulfillmentId);
        EcommerceFulfillmentOrder order = repository.findByFulfillmentId(fulfillmentId)
                .orElseThrow(() -> new IllegalArgumentException("Fulfillment not found: " + fulfillmentId));

        if (!"RECEIVED".equals(order.getStatus())) {
            throw new IllegalArgumentException("Fulfillment must be in RECEIVED status to start picking. Current: " + order.getStatus());
        }

        order.transitionTo("PICKING");
        order.setAssignedStaff(EcommerceFulfillmentOrder.AssignedStaff.builder()
                .name(staffName)
                .role("Picker/Packer")
                .startedAt(LocalDateTime.now())
                .build());
        order.setProgress(EcommerceFulfillmentOrder.FulfillmentProgress.builder()
                .itemsPicked(0)
                .totalItems(order.getItems().stream().mapToInt(EcommerceFulfillmentOrder.FulfillmentItem::getQuantity).sum())
                .percentageComplete(0)
                .build());

        EcommerceFulfillmentOrder saved = repository.save(order);
        eventProducer.publishFulfillmentPicking(fulfillmentId, order.getOrderId(),
                order.getSubOrderId(), staffId, staffName);
        return mapToStatusResponse(saved);
    }

    public FulfillmentStatusResponse completePicking(String fulfillmentId) {
        log.info("Completing picking for fulfillment {}", fulfillmentId);
        EcommerceFulfillmentOrder order = repository.findByFulfillmentId(fulfillmentId)
                .orElseThrow(() -> new IllegalArgumentException("Fulfillment not found: " + fulfillmentId));

        if (!"PICKING".equals(order.getStatus())) {
            throw new IllegalArgumentException("Fulfillment must be in PICKING status. Current: " + order.getStatus());
        }

        order.transitionTo("PACKING");
        if (order.getProgress() != null) {
            order.setProgress(EcommerceFulfillmentOrder.FulfillmentProgress.builder()
                    .itemsPicked(order.getProgress().getTotalItems())
                    .totalItems(order.getProgress().getTotalItems())
                    .percentageComplete(100)
                    .build());
        }

        EcommerceFulfillmentOrder saved = repository.save(order);
        eventProducer.publishFulfillmentPacked(fulfillmentId, order.getOrderId(),
                order.getSubOrderId(), 0.0, null);
        return mapToStatusResponse(saved);
    }

    public FulfillmentStatusResponse completePacking(String fulfillmentId) {
        log.info("Completing packing for fulfillment {}", fulfillmentId);
        EcommerceFulfillmentOrder order = repository.findByFulfillmentId(fulfillmentId)
                .orElseThrow(() -> new IllegalArgumentException("Fulfillment not found: " + fulfillmentId));

        if (!"PACKING".equals(order.getStatus())) {
            throw new IllegalArgumentException("Fulfillment must be in PACKING status. Current: " + order.getStatus());
        }

        order.transitionTo("READY_FOR_PICKUP");

        EcommerceFulfillmentOrder saved = repository.save(order);

        ReadyForPickupEvent event = ReadyForPickupEvent.builder()
                .fulfillmentId(fulfillmentId)
                .orderId(order.getOrderId())
                .subOrderId(order.getSubOrderId())
                .warehouseId(order.getWarehouseId())
                .zoneId(order.getZoneId())
                .warehouseAddress("Warehouse Address - " + order.getWarehouseId())
                .warehouseLatitude(6.5244)
                .warehouseLongitude(3.3792)
                .contactName("Warehouse Manager")
                .contactPhone("+234-800-000-0000")
                .packageCount(1)
                .readyAt(LocalDateTime.now())
                .deliveryType(order.getDeliveryType())
                .build();
        eventProducer.publishReadyForPickup(event);

        return mapToStatusResponse(saved);
    }

    public void markHandedToCourier(String fulfillmentId) {
        log.info("Marking fulfillment {} as handed to courier", fulfillmentId);
        EcommerceFulfillmentOrder order = repository.findByFulfillmentId(fulfillmentId)
                .orElseThrow(() -> new IllegalArgumentException("Fulfillment not found: " + fulfillmentId));
        order.setStatus("HANDED_TO_COURIER");
        order.setUpdatedAt(LocalDateTime.now());
        repository.save(order);
    }

    @SuppressWarnings("unchecked")
    public void createFulfillmentFromOrder(String orderId, String subOrderId, String vendorId,
                                            String warehouseId, Map<String, Object> event) {
        log.info("Auto-creating fulfillment from order.confirmed event for order {}", orderId);

        List<Map<String, Object>> eventItems = (List<Map<String, Object>>) event.get("items");
        if (eventItems == null || eventItems.isEmpty()) {
            log.warn("No items in order.confirmed event for order {}", orderId);
            return;
        }

        List<EcommerceFulfillmentRequest.OrderItem> items = eventItems.stream()
                .map(item -> EcommerceFulfillmentRequest.OrderItem.builder()
                        .sku((String) item.get("sku"))
                        .productName((String) item.get("productName"))
                        .quantity(item.get("quantity") instanceof Number ?
                                ((Number) item.get("quantity")).intValue() : 1)
                        .build())
                .collect(Collectors.toList());

        EcommerceFulfillmentRequest fulfillmentRequest = EcommerceFulfillmentRequest.builder()
                .orderId(orderId)
                .subOrderId(subOrderId)
                .vendorId(vendorId)
                .warehouseId(warehouseId)
                .items(items)
                .priority((String) event.getOrDefault("priority", "NORMAL"))
                .deliveryType((String) event.get("deliveryType"))
                .specialInstructions((String) event.get("specialInstructions"))
                .build();

        createFulfillment(fulfillmentRequest);
    }

    public void cancelFulfillmentByOrder(String orderId, String subOrderId) {
        log.info("Cancelling fulfillment for order {}", orderId);
        repository.findByOrderIdAndSubOrderId(orderId, subOrderId)
                .ifPresent(order -> {
                    if ("RECEIVED".equals(order.getStatus()) || "PENDING".equals(order.getStatus())) {
                        for (EcommerceFulfillmentOrder.FulfillmentItem item : order.getItems()) {
                            stockService.releaseStock(order.getWarehouseId(), item.getSku(), item.getQuantity());
                        }
                        order.setStatus("CANCELLED");
                        order.setUpdatedAt(LocalDateTime.now());
                        repository.save(order);
                    } else {
                        log.warn("Cannot cancel fulfillment {} - already in progress", order.getFulfillmentId());
                    }
                });
    }

    private EcommerceFulfillmentResponse mapToResponse(EcommerceFulfillmentOrder order) {
        List<EcommerceFulfillmentResponse.StageInfo> stages = order.getStages() != null ?
                order.getStages().stream()
                        .map(s -> EcommerceFulfillmentResponse.StageInfo.builder()
                                .stage(s.getStage())
                                .status(s.getStatus())
                                .completedAt(s.getCompletedAt())
                                .build())
                        .collect(Collectors.toList()) : List.of();

        return EcommerceFulfillmentResponse.builder()
                .fulfillmentId(order.getFulfillmentId())
                .orderId(order.getOrderId())
                .subOrderId(order.getSubOrderId())
                .warehouseId(order.getWarehouseId())
                .status(order.getStatus())
                .estimatedCompletion(order.getEstimatedCompletion())
                .stages(stages)
                .build();
    }

    private FulfillmentStatusResponse mapToStatusResponse(EcommerceFulfillmentOrder order) {
        FulfillmentStatusResponse.AssignedStaffInfo staffInfo = null;
        if (order.getAssignedStaff() != null) {
            staffInfo = FulfillmentStatusResponse.AssignedStaffInfo.builder()
                    .name(order.getAssignedStaff().getName())
                    .role(order.getAssignedStaff().getRole())
                    .startedAt(order.getAssignedStaff().getStartedAt())
                    .build();
        }

        FulfillmentStatusResponse.ProgressInfo progressInfo = null;
        if (order.getProgress() != null) {
            progressInfo = FulfillmentStatusResponse.ProgressInfo.builder()
                    .itemsPicked(order.getProgress().getItemsPicked())
                    .totalItems(order.getProgress().getTotalItems())
                    .percentageComplete(order.getProgress().getPercentageComplete())
                    .build();
        }

        List<FulfillmentStatusResponse.StageDetail> stageDetails = order.getStages() != null ?
                order.getStages().stream()
                        .map(s -> FulfillmentStatusResponse.StageDetail.builder()
                                .stage(s.getStage())
                                .status(s.getStatus())
                                .completedAt(s.getCompletedAt())
                                .startedAt(s.getStartedAt())
                                .build())
                        .collect(Collectors.toList()) : List.of();

        return FulfillmentStatusResponse.builder()
                .fulfillmentId(order.getFulfillmentId())
                .orderId(order.getOrderId())
                .status(order.getStatus())
                .currentStage(order.getStatus())
                .assignedStaff(staffInfo)
                .progress(progressInfo)
                .stages(stageDetails)
                .build();
    }
}

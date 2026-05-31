package com.gogidix.shared.warehousing.stock.application.service;

import com.gogidix.shared.warehousing.stock.application.command.AllocateStockCommand;
import com.gogidix.shared.warehousing.stock.application.command.ReserveStockCommand;
import com.gogidix.shared.warehousing.stock.application.dto.StockAllocationDTO;
import com.gogidix.shared.warehousing.stock.application.dto.StockReservationDTO;
import com.gogidix.shared.warehousing.stock.domain.entity.StockAllocation;
import com.gogidix.shared.warehousing.stock.domain.entity.StockReservation;
import com.gogidix.shared.warehousing.stock.domain.repository.StockAllocationRepository;
import com.gogidix.shared.warehousing.stock.domain.repository.StockReservationRepository;
import com.gogidix.shared.warehousing.stock.infrastructure.messaging.StockEventPublisher;
import com.gogidix.shared.warehousing.stock.infrastructure.security.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class StockAllocationService {

    private static final int DEFAULT_RESERVATION_DURATION_MINUTES = 30;

    private final StockAllocationRepository allocationRepository;
    private final StockReservationRepository reservationRepository;
    private final StockEventPublisher eventPublisher;

    public List<StockAllocationDTO> allocateStock(AllocateStockCommand command) {
        log.info("Allocating stock for order: {}, items: {}", command.getOrderId(), command.getItems().size());

        String tenantId = TenantContext.getCurrentTenant();
        List<StockAllocationDTO> allocations = new ArrayList<>();

        for (AllocateStockCommand.OrderItem item : command.getItems()) {
            List<StockAllocation> itemAllocations = findAvailableStockForAllocation(
                tenantId, item.getSku(), item.getQuantity(), command.getWarehouseId()
            );

            if (itemAllocations.isEmpty()) {
                log.warn("Insufficient stock for SKU: {}, requested: {}", item.getSku(), item.getQuantity());
                throw new InsufficientStockException(
                    "Insufficient stock for SKU: " + item.getSku() + ". Requested: " + item.getQuantity()
                );
            }

            for (StockAllocation allocation : itemAllocations) {
                allocation.setOrderId(command.getOrderId());
                allocation.setAllocationTime(LocalDateTime.now());
                allocation.setAllocationId(UUID.randomUUID().toString());
                StockAllocation saved = allocationRepository.save(allocation);
                allocations.add(mapToAllocationDTO(saved));
            }

            int totalAllocated = itemAllocations.stream().mapToInt(StockAllocation::getQuantity).sum();
            eventPublisher.publishStockAllocated(tenantId, command.getOrderId(), item.getSku(), totalAllocated);
        }

        log.info("Stock allocation completed for order: {}, total allocations: {}", command.getOrderId(), allocations.size());
        return allocations;
    }

    public StockReservationDTO reserveStock(ReserveStockCommand command) {
        log.info("Reserving stock for order: {}, SKU: {}", command.getOrderId(), command.getSku());

        String tenantId = TenantContext.getCurrentTenant();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(DEFAULT_RESERVATION_DURATION_MINUTES);

        AllocateStockCommand.OrderItem orderItem = AllocateStockCommand.OrderItem.builder()
            .sku(command.getSku())
            .quantity(command.getQuantity())
            .build();

        StockReservation reservation = StockReservation.builder()
            .reservationId(UUID.randomUUID().toString())
            .tenantId(tenantId)
            .orderId(command.getOrderId())
            .items(Collections.singletonList(orderItem))
            .createdAt(LocalDateTime.now())
            .expiresAt(expiresAt)
            .status(StockReservation.ReservationStatus.ACTIVE)
            .build();

        StockReservation saved = reservationRepository.save(reservation);

        eventPublisher.publishStockReserved(
            command.getSku(), command.getLocationId(), tenantId,
            command.getQuantity(), command.getOrderId()
        );

        log.info("Stock reserved with ID: {}, expires at: {}", saved.getReservationId(), expiresAt);
        return mapToReservationDTO(saved);
    }

    public void releaseReservation(String reservationId, boolean convertToAllocation) {
        log.info("Releasing reservation: {}, convert to allocation: {}", reservationId, convertToAllocation);

        StockReservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new ReservationNotFoundException("Reservation not found: " + reservationId));

        if (convertToAllocation && reservation.getItems() != null) {
            for (AllocateStockCommand.OrderItem item : reservation.getItems()) {
                StockAllocation allocation = StockAllocation.builder()
                    .tenantId(reservation.getTenantId())
                    .orderId(reservation.getOrderId())
                    .sku(item.getSku())
                    .quantity(item.getQuantity())
                    .status("ALLOCATED")
                    .allocationId(UUID.randomUUID().toString())
                    .allocationTime(LocalDateTime.now())
                    .build();
                allocationRepository.save(allocation);

                eventPublisher.publishStockAllocated(
                    reservation.getTenantId(), reservation.getOrderId(),
                    item.getSku(), item.getQuantity()
                );
            }
        }

        reservation.setStatus(StockReservation.ReservationStatus.RELEASED);
        reservationRepository.save(reservation);

        log.info("Reservation released: {}", reservationId);
    }

    private List<StockAllocation> findAvailableStockForAllocation(
            String tenantId, String sku, int requestedQuantity, String warehouseId) {

        List<StockAllocation> allocations = new ArrayList<>();
        int remainingQuantity = requestedQuantity;

        List<StockAllocation> availableStock = allocationRepository
            .findByTenantIdAndSkuAndWarehouseIdAndQuantityGreaterThanOrderByExpirationDateAsc(
                tenantId, sku, warehouseId, 0
            );

        for (StockAllocation stock : availableStock) {
            if (remainingQuantity <= 0) break;

            int availableQuantity = stock.getQuantity();
            int allocatedQuantity = Math.min(availableQuantity, remainingQuantity);

            StockAllocation allocation = StockAllocation.builder()
                .tenantId(tenantId)
                .sku(sku)
                .locationId(stock.getLocationId())
                .quantity(allocatedQuantity)
                .warehouseId(warehouseId)
                .batchNumber(stock.getBatchNumber())
                .expirationDate(stock.getExpirationDate())
                .build();

            allocations.add(allocation);
            remainingQuantity -= allocatedQuantity;
        }

        return remainingQuantity > 0 ? new ArrayList<>() : allocations;
    }

    @Transactional
    public void cleanupExpiredReservations() {
        log.info("Cleaning up expired reservations");
        List<StockReservation> expiredReservations = reservationRepository
            .findByStatusAndExpiresAtBefore(StockReservation.ReservationStatus.ACTIVE, LocalDateTime.now());

        for (StockReservation reservation : expiredReservations) {
            reservation.setStatus(StockReservation.ReservationStatus.EXPIRED);
            reservationRepository.save(reservation);
            eventPublisher.publishReservationExpired(reservation.getTenantId(), reservation.getReservationId());
        }

        log.info("Cleaned up {} expired reservations", expiredReservations.size());
    }

    private StockAllocationDTO mapToAllocationDTO(StockAllocation allocation) {
        return StockAllocationDTO.builder()
            .allocationId(allocation.getAllocationId())
            .orderId(allocation.getOrderId())
            .sku(allocation.getSku())
            .quantity(allocation.getQuantity())
            .locationId(allocation.getLocationId())
            .warehouseId(allocation.getWarehouseId())
            .batchNumber(allocation.getBatchNumber())
            .build();
    }

    private StockReservationDTO mapToReservationDTO(StockReservation reservation) {
        return StockReservationDTO.builder()
            .reservationId(reservation.getReservationId())
            .orderId(reservation.getOrderId())
            .expiresAt(reservation.getExpiresAt())
            .status(reservation.getStatus().name())
            .build();
    }

    public static class InsufficientStockException extends RuntimeException {
        public InsufficientStockException(String message) {
            super(message);
        }
    }

    public static class ReservationNotFoundException extends RuntimeException {
        public ReservationNotFoundException(String message) {
            super(message);
        }
    }
}

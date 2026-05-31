package com.gogidix.shared.warehousing.stock.application;

import com.gogidix.shared.warehousing.stock.application.command.AllocateStockCommand;
import com.gogidix.shared.warehousing.stock.application.command.ReserveStockCommand;
import com.gogidix.shared.warehousing.stock.application.dto.StockAllocationDTO;
import com.gogidix.shared.warehousing.stock.application.dto.StockReservationDTO;
import com.gogidix.shared.warehousing.stock.application.service.StockAllocationService;
import com.gogidix.shared.warehousing.stock.domain.entity.StockAllocation;
import com.gogidix.shared.warehousing.stock.domain.entity.StockReservation;
import com.gogidix.shared.warehousing.stock.domain.repository.StockAllocationRepository;
import com.gogidix.shared.warehousing.stock.domain.repository.StockReservationRepository;
import com.gogidix.shared.warehousing.stock.infrastructure.messaging.StockEventPublisher;
import com.gogidix.shared.warehousing.stock.infrastructure.security.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockAllocationServiceTest {

    @Mock
    private StockAllocationRepository allocationRepository;

    @Mock
    private StockReservationRepository reservationRepository;

    @Mock
    private StockEventPublisher eventPublisher;

    @InjectMocks
    private StockAllocationService stockAllocationService;

    private static final String TEST_TENANT_ID = "test-tenant-123";
    private static final String TEST_ORDER_ID = "order-001";
    private static final String TEST_WAREHOUSE_ID = "wh-001";
    private static final String TEST_SKU = "SKU-001";
    private static final String TEST_LOCATION_ID = "LOC-001";

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant(TEST_TENANT_ID);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void allocateStock_SufficientAvailable_SuccessfullyAllocates() {
        AllocateStockCommand command = AllocateStockCommand.builder()
            .orderId(TEST_ORDER_ID)
            .warehouseId(TEST_WAREHOUSE_ID)
            .items(Arrays.asList(
                AllocateStockCommand.OrderItem.builder()
                    .sku(TEST_SKU)
                    .quantity(50)
                    .build()
            ))
            .build();

        StockAllocation availableStock = StockAllocation.builder()
            .sku(TEST_SKU)
            .locationId(TEST_LOCATION_ID)
            .quantity(100)
            .warehouseId(TEST_WAREHOUSE_ID)
            .build();

        when(allocationRepository.findByTenantIdAndSkuAndWarehouseIdAndQuantityGreaterThanOrderByExpirationDateAsc(
            eq(TEST_TENANT_ID), eq(TEST_SKU), eq(TEST_WAREHOUSE_ID), eq(0)
        )).thenReturn(Arrays.asList(availableStock));

        when(allocationRepository.save(any(StockAllocation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<StockAllocationDTO> result = stockAllocationService.allocateStock(command);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(50, result.get(0).getQuantity());
        verify(eventPublisher).publishStockAllocated(eq(TEST_TENANT_ID), eq(TEST_ORDER_ID), eq(TEST_SKU), eq(50));
    }

    @Test
    void allocateStock_InsufficientStock_ThrowsException() {
        AllocateStockCommand command = AllocateStockCommand.builder()
            .orderId(TEST_ORDER_ID)
            .warehouseId(TEST_WAREHOUSE_ID)
            .items(Arrays.asList(
                AllocateStockCommand.OrderItem.builder()
                    .sku(TEST_SKU)
                    .quantity(200)
                    .build()
            ))
            .build();

        StockAllocation availableStock = StockAllocation.builder()
            .sku(TEST_SKU)
            .locationId(TEST_LOCATION_ID)
            .quantity(50)
            .warehouseId(TEST_WAREHOUSE_ID)
            .build();

        when(allocationRepository.findByTenantIdAndSkuAndWarehouseIdAndQuantityGreaterThanOrderByExpirationDateAsc(
            any(), any(), any(), any()
        )).thenReturn(Arrays.asList(availableStock));

        assertThrows(StockAllocationService.InsufficientStockException.class,
            () -> stockAllocationService.allocateStock(command));
    }

    @Test
    void reserveStock_ValidCommand_CreatesReservation() {
        ReserveStockCommand command = ReserveStockCommand.builder()
            .orderId(TEST_ORDER_ID)
            .sku(TEST_SKU)
            .locationId(TEST_LOCATION_ID)
            .quantity(10)
            .build();

        when(reservationRepository.save(any(StockReservation.class))).thenAnswer(invocation -> {
            StockReservation r = invocation.getArgument(0);
            r.setReservationId("res-001");
            return r;
        });

        StockReservationDTO result = stockAllocationService.reserveStock(command);

        assertNotNull(result);
        assertNotNull(result.getReservationId());
        assertEquals("ACTIVE", result.getStatus());
        verify(eventPublisher).publishStockReserved(
            eq(TEST_SKU), eq(TEST_LOCATION_ID), eq(TEST_TENANT_ID), eq(10), eq(TEST_ORDER_ID)
        );
    }

    @Test
    void releaseReservation_ConvertToTrue_ConvertsToAllocation() {
        String reservationId = "res-001";
        List<AllocateStockCommand.OrderItem> items = Arrays.asList(
            AllocateStockCommand.OrderItem.builder().sku(TEST_SKU).quantity(10).build()
        );

        StockReservation reservation = StockReservation.builder()
            .reservationId(reservationId)
            .tenantId(TEST_TENANT_ID)
            .orderId(TEST_ORDER_ID)
            .items(items)
            .status(StockReservation.ReservationStatus.ACTIVE)
            .build();

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(allocationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(reservationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        stockAllocationService.releaseReservation(reservationId, true);

        assertEquals(StockReservation.ReservationStatus.RELEASED, reservation.getStatus());
        verify(eventPublisher).publishStockAllocated(eq(TEST_TENANT_ID), eq(TEST_ORDER_ID), eq(TEST_SKU), eq(10));
    }

    @Test
    void releaseReservation_ConvertToFalse_JustReleases() {
        String reservationId = "res-001";
        List<AllocateStockCommand.OrderItem> items = Arrays.asList(
            AllocateStockCommand.OrderItem.builder().sku(TEST_SKU).quantity(10).build()
        );

        StockReservation reservation = StockReservation.builder()
            .reservationId(reservationId)
            .tenantId(TEST_TENANT_ID)
            .orderId(TEST_ORDER_ID)
            .items(items)
            .status(StockReservation.ReservationStatus.ACTIVE)
            .build();

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        stockAllocationService.releaseReservation(reservationId, false);

        assertEquals(StockReservation.ReservationStatus.RELEASED, reservation.getStatus());
        verify(allocationRepository, never()).save(any());
    }

    @Test
    void cleanupExpiredReservations_ExpiredReservations_MarksAsExpired() {
        List<StockReservation> expiredReservations = Arrays.asList(
            StockReservation.builder()
                .reservationId("res-001")
                .tenantId(TEST_TENANT_ID)
                .status(StockReservation.ReservationStatus.ACTIVE)
                .expiresAt(LocalDateTime.now().minusMinutes(10))
                .build(),
            StockReservation.builder()
                .reservationId("res-002")
                .tenantId(TEST_TENANT_ID)
                .status(StockReservation.ReservationStatus.ACTIVE)
                .expiresAt(LocalDateTime.now().minusMinutes(5))
                .build()
        );

        when(reservationRepository.findByStatusAndExpiresAtBefore(
            eq(StockReservation.ReservationStatus.ACTIVE), any(LocalDateTime.class)
        )).thenReturn(expiredReservations);
        when(reservationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        stockAllocationService.cleanupExpiredReservations();

        verify(reservationRepository, times(2)).save(any(StockReservation.class));
        verify(eventPublisher, times(2)).publishReservationExpired(eq(TEST_TENANT_ID), any());
    }

    @Test
    void allocateStock_FEFOAlgorithm_AllocatesOldestBatchFirst() {
        AllocateStockCommand command = AllocateStockCommand.builder()
            .orderId(TEST_ORDER_ID)
            .warehouseId(TEST_WAREHOUSE_ID)
            .items(Arrays.asList(
                AllocateStockCommand.OrderItem.builder()
                    .sku(TEST_SKU)
                    .quantity(100)
                    .build()
            ))
            .build();

        LocalDateTime now = LocalDateTime.now();
        StockAllocation oldBatch = StockAllocation.builder()
            .sku(TEST_SKU)
            .locationId("LOC-001")
            .quantity(30)
            .warehouseId(TEST_WAREHOUSE_ID)
            .batchNumber("BATCH-001")
            .expirationDate(now.plusDays(10))
            .build();

        StockAllocation newBatch = StockAllocation.builder()
            .sku(TEST_SKU)
            .locationId("LOC-002")
            .quantity(100)
            .warehouseId(TEST_WAREHOUSE_ID)
            .batchNumber("BATCH-002")
            .expirationDate(now.plusDays(30))
            .build();

        when(allocationRepository.findByTenantIdAndSkuAndWarehouseIdAndQuantityGreaterThanOrderByExpirationDateAsc(
            eq(TEST_TENANT_ID), eq(TEST_SKU), eq(TEST_WAREHOUSE_ID), eq(0)
        )).thenReturn(Arrays.asList(oldBatch, newBatch));

        when(allocationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        List<StockAllocationDTO> result = stockAllocationService.allocateStock(command);

        assertEquals(2, result.size());
        assertEquals(30, result.get(0).getQuantity());
        assertEquals("BATCH-001", result.get(0).getBatchNumber());
        assertEquals(70, result.get(1).getQuantity());
        assertEquals("BATCH-002", result.get(1).getBatchNumber());
    }
}

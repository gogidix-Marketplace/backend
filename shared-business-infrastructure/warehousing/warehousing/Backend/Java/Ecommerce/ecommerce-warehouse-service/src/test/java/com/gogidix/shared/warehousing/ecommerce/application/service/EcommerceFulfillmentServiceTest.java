package com.gogidix.shared.warehousing.ecommerce.application.service;

import com.gogidix.shared.warehousing.ecommerce.domain.entity.EcommerceFulfillmentOrder;
import com.gogidix.shared.warehousing.ecommerce.domain.repository.EcommerceFulfillmentOrderRepository;
import com.gogidix.shared.warehousing.ecommerce.infrastructure.messaging.producers.EcommerceWarehouseEventProducer;
import com.gogidix.shared.warehousing.ecommerce.interfaces.rest.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EcommerceFulfillmentServiceTest {

    @Mock
    private EcommerceFulfillmentOrderRepository repository;

    @Mock
    private EcommerceStockService stockService;

    @Mock
    private EcommerceWarehouseEventProducer eventProducer;

    @InjectMocks
    private EcommerceFulfillmentService fulfillmentService;

    private EcommerceFulfillmentRequest fulfillmentRequest;

    @BeforeEach
    void setUp() {
        fulfillmentRequest = EcommerceFulfillmentRequest.builder()
                .orderId("GO-2026-04723")
                .subOrderId("SO-2026-04723-V1")
                .vendorId("vendor-uuid-001")
                .warehouseId("wh-lagos-01")
                .items(List.of(
                        EcommerceFulfillmentRequest.OrderItem.builder()
                                .sku("TECH-001-BT").productName("Wireless Bluetooth Headphones").quantity(1).build(),
                        EcommerceFulfillmentRequest.OrderItem.builder()
                                .sku("TECH-002-USB").productName("USB-C Charging Cable").quantity(2).build()
                ))
                .priority("HIGH")
                .deliveryDeadline(LocalDateTime.now().plusHours(4))
                .deliveryType("TYPE_A")
                .customerAddress(EcommerceFulfillmentRequest.CustomerAddress.builder()
                        .address("45 Allen Avenue, Ikeja")
                        .latitude(6.5963)
                        .longitude(3.3420)
                        .build())
                .specialInstructions("Fragile items - handle with care")
                .build();
    }

    @Test
    void createFulfillment_success() {
        when(stockService.reserveStock(anyString(), anyString(), anyInt())).thenReturn(true);
        when(repository.save(any(EcommerceFulfillmentOrder.class)))
                .thenAnswer(invocation -> {
                    EcommerceFulfillmentOrder order = invocation.getArgument(0);
                    order.setId("order-uuid-001");
                    return order;
                });

        EcommerceFulfillmentResponse response = fulfillmentService.createFulfillment(fulfillmentRequest);

        assertNotNull(response);
        assertNotNull(response.getFulfillmentId());
        assertTrue(response.getFulfillmentId().startsWith("ful-"));
        assertEquals("GO-2026-04723", response.getOrderId());
        assertEquals("wh-lagos-01", response.getWarehouseId());
        assertEquals("RECEIVED", response.getStatus());
        assertEquals(4, response.getStages().size());

        verify(stockService, times(2)).reserveStock(eq("wh-lagos-01"), anyString(), anyInt());
        verify(eventProducer).publishFulfillmentReceived(anyString(), eq("GO-2026-04723"),
                eq("SO-2026-04723-V1"), eq("wh-lagos-01"), isNull());
    }

    @Test
    void createFulfillment_insufficientStock_throws() {
        when(stockService.reserveStock("wh-lagos-01", "TECH-001-BT", 1)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () ->
                fulfillmentService.createFulfillment(fulfillmentRequest));

        verify(repository, never()).save(any());
    }

    @Test
    void getFulfillmentStatus_found() {
        EcommerceFulfillmentOrder order = buildTestOrder();
        when(repository.findByFulfillmentId("ful-001")).thenReturn(Optional.of(order));

        FulfillmentStatusResponse response = fulfillmentService.getFulfillmentStatus("ful-001");

        assertNotNull(response);
        assertEquals("ful-001", response.getFulfillmentId());
        assertEquals("RECEIVED", response.getStatus());
        assertEquals(4, response.getStages().size());
    }

    @Test
    void getFulfillmentStatus_notFound_throws() {
        when(repository.findByFulfillmentId("ful-999")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> fulfillmentService.getFulfillmentStatus("ful-999"));
    }

    @Test
    void startPicking_success() {
        EcommerceFulfillmentOrder order = buildTestOrder();
        when(repository.findByFulfillmentId("ful-001")).thenReturn(Optional.of(order));
        when(repository.save(any())).thenReturn(order);

        FulfillmentStatusResponse response = fulfillmentService.startPicking("ful-001", "staff-1", "Emmanuel Adebayo");

        assertEquals("PICKING", response.getStatus());
        assertNotNull(response.getAssignedStaff());
        assertEquals("Emmanuel Adebayo", response.getAssignedStaff().getName());
        verify(eventProducer).publishFulfillmentPicking(eq("ful-001"), anyString(), anyString(),
                eq("staff-1"), eq("Emmanuel Adebayo"));
    }

    @Test
    void startPicking_wrongStatus_throws() {
        EcommerceFulfillmentOrder order = buildTestOrder();
        order.setStatus("PICKING");
        when(repository.findByFulfillmentId("ful-001")).thenReturn(Optional.of(order));

        assertThrows(IllegalArgumentException.class,
                () -> fulfillmentService.startPicking("ful-001", "staff-1", "Test"));
    }

    @Test
    void completePicking_success() {
        EcommerceFulfillmentOrder order = buildTestOrder();
        order.setStatus("PICKING");
        order.setProgress(EcommerceFulfillmentOrder.FulfillmentProgress.builder()
                .itemsPicked(0).totalItems(3).percentageComplete(0).build());
        when(repository.findByFulfillmentId("ful-001")).thenReturn(Optional.of(order));
        when(repository.save(any())).thenReturn(order);

        FulfillmentStatusResponse response = fulfillmentService.completePicking("ful-001");

        assertEquals("PACKING", response.getStatus());
        verify(eventProducer).publishFulfillmentPacked(eq("ful-001"), anyString(), anyString(),
                anyDouble(), isNull());
    }

    @Test
    void completePacking_success() {
        EcommerceFulfillmentOrder order = buildTestOrder();
        order.setStatus("PACKING");
        when(repository.findByFulfillmentId("ful-001")).thenReturn(Optional.of(order));
        when(repository.save(any())).thenReturn(order);

        FulfillmentStatusResponse response = fulfillmentService.completePacking("ful-001");

        assertEquals("READY_FOR_PICKUP", response.getStatus());
        verify(eventProducer).publishReadyForPickup(any());
    }

    @Test
    void cancelFulfillment_receivedOrder_cancelsAndReleasesStock() {
        EcommerceFulfillmentOrder order = buildTestOrder();
        order.setStatus("RECEIVED");
        when(repository.findByOrderIdAndSubOrderId("GO-001", "SO-001"))
                .thenReturn(Optional.of(order));

        fulfillmentService.cancelFulfillmentByOrder("GO-001", "SO-001");

        verify(stockService, times(2)).releaseStock(eq("wh-lagos-01"), anyString(), anyInt());
        verify(repository).save(argThat(o -> "CANCELLED".equals(o.getStatus())));
    }

    @Test
    void cancelFulfillment_inProgress_doesNotCancel() {
        EcommerceFulfillmentOrder order = buildTestOrder();
        order.setStatus("PICKING");
        when(repository.findByOrderIdAndSubOrderId("GO-001", "SO-001"))
                .thenReturn(Optional.of(order));

        fulfillmentService.cancelFulfillmentByOrder("GO-001", "SO-001");

        verify(stockService, never()).releaseStock(anyString(), anyString(), anyInt());
        verify(repository, never()).save(any());
    }

    @Test
    void markHandedToCourier_success() {
        EcommerceFulfillmentOrder order = buildTestOrder();
        when(repository.findByFulfillmentId("ful-001")).thenReturn(Optional.of(order));

        fulfillmentService.markHandedToCourier("ful-001");

        verify(repository).save(argThat(o -> "HANDED_TO_COURIER".equals(o.getStatus())));
    }

    @Test
    void createFulfillmentFromOrder_success() {
        when(stockService.reserveStock(anyString(), anyString(), anyInt())).thenReturn(true);
        when(repository.save(any(EcommerceFulfillmentOrder.class)))
                .thenAnswer(invocation -> {
                    EcommerceFulfillmentOrder order = invocation.getArgument(0);
                    order.setId("auto-001");
                    return order;
                });

        Map<String, Object> event = new HashMap<>();
        event.put("orderId", "GO-AUTO-001");
        event.put("subOrderId", "SO-AUTO-001");
        event.put("vendorId", "vendor-1");
        event.put("warehouseId", "wh-01");
        event.put("priority", "HIGH");
        event.put("items", List.of(
                Map.of("sku", "SKU-1", "productName", "Product 1", "quantity", 2)
        ));

        fulfillmentService.createFulfillmentFromOrder("GO-AUTO-001", "SO-AUTO-001",
                "vendor-1", "wh-01", event);

        verify(repository).save(any(EcommerceFulfillmentOrder.class));
    }

    private EcommerceFulfillmentOrder buildTestOrder() {
        EcommerceFulfillmentOrder order = EcommerceFulfillmentOrder.builder()
                .id("order-uuid-001")
                .fulfillmentId("ful-001")
                .orderId("GO-2026-04723")
                .subOrderId("SO-2026-04723-V1")
                .vendorId("vendor-uuid-001")
                .warehouseId("wh-lagos-01")
                .status("RECEIVED")
                .priority("HIGH")
                .items(List.of(
                        EcommerceFulfillmentOrder.FulfillmentItem.builder()
                                .sku("TECH-001-BT").productName("Headphones").quantity(1).build(),
                        EcommerceFulfillmentOrder.FulfillmentItem.builder()
                                .sku("TECH-002-USB").productName("USB Cable").quantity(2).build()
                ))
                .estimatedCompletion(LocalDateTime.now().plusHours(2))
                .build();
        order.initializeStages();
        return order;
    }
}

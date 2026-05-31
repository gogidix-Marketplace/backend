package com.gogidix.shared.warehousing.ecommerce.infrastructure.messaging.consumers;

import com.gogidix.shared.warehousing.ecommerce.application.service.EcommerceFulfillmentService;
import com.gogidix.shared.warehousing.ecommerce.application.service.EcommerceStockService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EcommerceOrderEventConsumerTest {

    @Mock
    private EcommerceFulfillmentService fulfillmentService;

    @Mock
    private EcommerceStockService stockService;

    @InjectMocks
    private EcommerceOrderEventConsumer consumer;

    @Test
    void handleOrderConfirmed_createsFulfillment() {
        Map<String, Object> event = new HashMap<>();
        event.put("orderId", "GO-001");
        event.put("subOrderId", "SO-001");
        event.put("vendorId", "vendor-1");
        event.put("warehouseId", "wh-01");
        event.put("items", List.of(
                Map.of("sku", "SKU-1", "productName", "Product 1", "quantity", 2)
        ));

        consumer.handleOrderConfirmed(event);

        verify(fulfillmentService).createFulfillmentFromOrder(eq("GO-001"), eq("SO-001"),
                eq("vendor-1"), eq("wh-01"), eq(event));
    }

    @Test
    void handleOrderConfirmed_errorInProcessing_doesNotThrow() {
        Map<String, Object> event = new HashMap<>();
        event.put("orderId", "GO-001");
        event.put("subOrderId", "SO-001");

        doThrow(new RuntimeException("Test error"))
                .when(fulfillmentService).createFulfillmentFromOrder(anyString(), anyString(),
                        anyString(), anyString(), anyMap());

        consumer.handleOrderConfirmed(event);
    }

    @Test
    void handleOrderCancelled_cancelsFulfillment() {
        Map<String, Object> event = new HashMap<>();
        event.put("orderId", "GO-001");
        event.put("subOrderId", "SO-001");

        consumer.handleOrderCancelled(event);

        verify(fulfillmentService).cancelFulfillmentByOrder("GO-001", "SO-001");
    }

    @Test
    void handleVendorStockUpdate_updatesStock() {
        Map<String, Object> event = new HashMap<>();
        event.put("vendorId", "vendor-1");
        event.put("warehouseId", "wh-01");
        event.put("sku", "SKU-1");
        event.put("quantity", 100);

        consumer.handleVendorStockUpdate(event);

        verify(stockService).updateStockLevel("vendor-1", "wh-01", "SKU-1", 100);
    }

    @Test
    void handleCourierPickedUp_marksHandedToCourier() {
        Map<String, Object> event = new HashMap<>();
        event.put("fulfillmentId", "ful-001");
        event.put("orderId", "GO-001");

        consumer.handleCourierPickedUp(event);

        verify(fulfillmentService).markHandedToCourier("ful-001");
    }
}

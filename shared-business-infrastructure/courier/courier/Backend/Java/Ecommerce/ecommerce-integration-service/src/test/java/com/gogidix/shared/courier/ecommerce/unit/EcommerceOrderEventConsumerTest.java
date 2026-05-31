package com.gogidix.shared.courier.ecommerce.unit;

import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import com.gogidix.shared.courier.ecommerce.application.service.EcommerceCourierAssignmentService;
import com.gogidix.shared.courier.ecommerce.infrastructure.messaging.consumers.EcommerceOrderEventConsumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EcommerceOrderEventConsumerTest {

    @Mock private EcommerceCourierAssignmentService assignmentService;
    @InjectMocks private EcommerceOrderEventConsumer consumer;

    @Test
    void shouldHandleReadyForPickup() {
        Map<String, Object> event = new HashMap<>();
        event.put("orderId", "GO-001");
        event.put("subOrderId", "SO-001");
        event.put("vendorId", "vendor-001");
        event.put("deliveryType", "TYPE_A");

        consumer.handleReadyForPickup(event);
        verify(assignmentService).assignCourier(any());
    }

    @Test
    void shouldHandleReadyForPickupWithDefaultDeliveryType() {
        Map<String, Object> event = new HashMap<>();
        event.put("orderId", "GO-002");
        event.put("subOrderId", "SO-002");
        event.put("vendorId", "v1");

        consumer.handleReadyForPickup(event);
        verify(assignmentService).assignCourier(any());
    }

    @Test
    void shouldHandleHubReadyForDispatch() {
        Map<String, Object> event = new HashMap<>();
        event.put("orderId", "GO-003");
        event.put("subOrderId", "SO-003");

        consumer.handleHubReadyForDispatch(event);
        verify(assignmentService).assignHubLeg(any());
    }

    @Test
    void shouldHandleOrderCancelled() {
        Map<String, Object> event = new HashMap<>();
        event.put("orderId", "GO-004");
        event.put("subOrderId", "SO-004");
        event.put("reason", "Customer changed mind");

        consumer.handleOrderCancelled(event);
        verify(assignmentService).cancelAssignment("GO-004", "SO-004", "Customer changed mind");
    }

    @Test
    void shouldHandleOrderCancelledWithDefaultReason() {
        Map<String, Object> event = new HashMap<>();
        event.put("orderId", "GO-005");
        event.put("subOrderId", "SO-005");

        consumer.handleOrderCancelled(event);
        verify(assignmentService).cancelAssignment("GO-005", "SO-005", "Order cancelled");
    }

    @Test
    void shouldHandleDeliveryRescheduled() {
        Map<String, Object> event = new HashMap<>();
        event.put("orderId", "GO-006");
        event.put("subOrderId", "SO-006");
        event.put("newScheduledTime", "2026-05-07T10:00:00Z");

        consumer.handleDeliveryRescheduled(event);
        verify(assignmentService).rescheduleAssignment(eq("GO-006"), eq("SO-006"), any());
    }

    @Test
    void shouldHandleDeliveryRescheduledWithNullTime() {
        Map<String, Object> event = new HashMap<>();
        event.put("orderId", "GO-007");
        event.put("subOrderId", "SO-007");

        consumer.handleDeliveryRescheduled(event);
        verify(assignmentService).rescheduleAssignment(eq("GO-007"), eq("SO-007"), any());
    }

    @Test
    void shouldHandleReadyForPickupError() {
        Map<String, Object> event = new HashMap<>();
        event.put("orderId", "GO-ERR");
        event.put("subOrderId", "SO-ERR");

        when(assignmentService.assignCourier(any())).thenThrow(new RuntimeException("DB error"));

        consumer.handleReadyForPickup(event);
    }

    @Test
    void shouldHandleHubReadyError() {
        Map<String, Object> event = new HashMap<>();
        event.put("orderId", "GO-ERR2");
        event.put("subOrderId", "SO-ERR2");

        when(assignmentService.assignHubLeg(any())).thenThrow(new RuntimeException("Error"));

        consumer.handleHubReadyForDispatch(event);
    }

    @Test
    void shouldHandleCancelError() {
        Map<String, Object> event = new HashMap<>();
        event.put("orderId", "GO-ERR3");
        event.put("subOrderId", "SO-ERR3");

        doThrow(new IllegalArgumentException("Not found")).when(assignmentService).cancelAssignment(any(), any(), any());

        consumer.handleOrderCancelled(event);
    }

    @Test
    void shouldHandleRescheduleError() {
        Map<String, Object> event = new HashMap<>();
        event.put("orderId", "GO-ERR4");
        event.put("subOrderId", "SO-ERR4");

        doThrow(new IllegalArgumentException("Not found")).when(assignmentService).rescheduleAssignment(any(), any(), any());

        consumer.handleDeliveryRescheduled(event);
    }
}

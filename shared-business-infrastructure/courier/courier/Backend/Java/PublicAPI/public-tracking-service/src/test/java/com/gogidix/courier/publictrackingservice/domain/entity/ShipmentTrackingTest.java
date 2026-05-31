package com.gogidix.courier.publictrackingservice.domain.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ShipmentTracking entity.
 */
@DisplayName("ShipmentTracking Entity Tests")
class ShipmentTrackingTest {

    @Test
    @DisplayName("Should create shipment tracking with valid parameters")
    void shouldCreateShipmentTrackingWithValidParameters() {
        // Given
        String tenantId = "tenant-001";
        String trackingNumber = "TRK-123456789";
        String orderId = "ORDER-001";

        // When
        ShipmentTracking tracking = new ShipmentTracking(tenantId, trackingNumber, orderId);

        // Then
        assertNotNull(tracking.getId());
        assertEquals(tenantId, tracking.getTenantId());
        assertEquals(trackingNumber, tracking.getTrackingNumber());
        assertEquals(orderId, tracking.getOrderId());
        assertEquals(ShipmentTracking.TrackingStatus.ORDER_PLACED, tracking.getStatus());
        assertTrue(tracking.getEvents().isEmpty());
        assertNotNull(tracking.getCreatedAt());
        assertNotNull(tracking.getUpdatedAt());
    }

    @Test
    @DisplayName("Should throw when tenantId is null")
    void shouldThrowWhenTenantIdIsNull() {
        assertThrows(NullPointerException.class, () ->
            new ShipmentTracking(null, "TRK-123456789", "ORDER-001")
        );
    }

    @Test
    @DisplayName("Should throw when trackingNumber is null")
    void shouldThrowWhenTrackingNumberIsNull() {
        assertThrows(NullPointerException.class, () ->
            new ShipmentTracking("tenant-001", null, "ORDER-001")
        );
    }

    @Test
    @DisplayName("Should allow null orderId")
    void shouldAllowNullOrderId() {
        // Given
        String tenantId = "tenant-001";
        String trackingNumber = "TRK-123456789";

        // When
        ShipmentTracking tracking = new ShipmentTracking(tenantId, trackingNumber, null);

        // Then
        assertNotNull(tracking.getId());
        assertEquals(tenantId, tracking.getTenantId());
        assertEquals(trackingNumber, tracking.getTrackingNumber());
        assertNull(tracking.getOrderId());
    }

    @Test
    @DisplayName("Should add tracking event")
    void shouldAddTrackingEvent() {
        // Given
        ShipmentTracking tracking = new ShipmentTracking("tenant-001", "TRK-123456789", "ORDER-001");
        TrackingEvent event = new TrackingEvent("TRK-123456789",
            TrackingEvent.EventType.PICKED_UP, "Package picked up", "New York, NY");

        // When
        tracking.addEvent(event);

        // Then
        assertEquals(1, tracking.getEvents().size());
        assertEquals("New York, NY", tracking.getCurrentLocation());
        assertEquals(ShipmentTracking.TrackingStatus.IN_TRANSIT, tracking.getStatus());
    }

    @Test
    @DisplayName("Should update status to delivered when delivered event added")
    void shouldUpdateStatusToDeliveredWhenDeliveredEventAdded() {
        // Given
        ShipmentTracking tracking = new ShipmentTracking("tenant-001", "TRK-123456789", "ORDER-001");
        TrackingEvent event = new TrackingEvent("TRK-123456789",
            TrackingEvent.EventType.DELIVERED, "Package delivered", "Los Angeles, CA");

        // When
        tracking.addEvent(event);

        // Then
        assertEquals(ShipmentTracking.TrackingStatus.DELIVERED, tracking.getStatus());
        assertEquals("Los Angeles, CA", tracking.getCurrentLocation());
        assertNotNull(tracking.getActualDelivery());
    }

    @Test
    @DisplayName("Should update status to out for delivery")
    void shouldUpdateStatusToOutForDelivery() {
        // Given
        ShipmentTracking tracking = new ShipmentTracking("tenant-001", "TRK-123456789", "ORDER-001");
        TrackingEvent event = new TrackingEvent("TRK-123456789",
            TrackingEvent.EventType.OUT_FOR_DELIVERY, "Out for delivery", "Los Angeles, CA");

        // When
        tracking.addEvent(event);

        // Then
        assertEquals(ShipmentTracking.TrackingStatus.OUT_FOR_DELIVERY, tracking.getStatus());
    }

    @Test
    @DisplayName("Should update status to exception when exception event added")
    void shouldUpdateStatusToExceptionWhenExceptionEventAdded() {
        // Given
        ShipmentTracking tracking = new ShipmentTracking("tenant-001", "TRK-123456789", "ORDER-001");
        TrackingEvent event = new TrackingEvent("TRK-123456789",
            TrackingEvent.EventType.EXCEPTION, "Delivery attempted - no access", "Los Angeles, CA");

        // When
        tracking.addEvent(event);

        // Then
        assertEquals(ShipmentTracking.TrackingStatus.EXCEPTION, tracking.getStatus());
    }

    @Test
    @DisplayName("Should update status to cancelled when cancelled event added")
    void shouldUpdateStatusToCancelledWhenCancelledEventAdded() {
        // Given
        ShipmentTracking tracking = new ShipmentTracking("tenant-001", "TRK-123456789", "ORDER-001");
        TrackingEvent event = new TrackingEvent("TRK-123456789",
            TrackingEvent.EventType.CANCELLED, "Order cancelled by customer", null);

        // When
        tracking.addEvent(event);

        // Then
        assertEquals(ShipmentTracking.TrackingStatus.CANCELLED, tracking.getStatus());
    }

    @Test
    @DisplayName("Should update status to pickup scheduled")
    void shouldUpdateStatusToPickupScheduled() {
        // Given
        ShipmentTracking tracking = new ShipmentTracking("tenant-001", "TRK-123456789", "ORDER-001");
        TrackingEvent event = new TrackingEvent("TRK-123456789",
            TrackingEvent.EventType.OUT_FOR_PICKUP, "Driver assigned for pickup", "New York, NY");

        // When
        tracking.addEvent(event);

        // Then
        assertEquals(ShipmentTracking.TrackingStatus.PICKUP_SCHEDULED, tracking.getStatus());
    }

    @Test
    @DisplayName("Should handle multiple tracking events")
    void shouldHandleMultipleTrackingEvents() {
        // Given
        ShipmentTracking tracking = new ShipmentTracking("tenant-001", "TRK-123456789", "ORDER-001");

        // When - adding events in sequence
        tracking.addEvent(new TrackingEvent("TRK-123456789",
            TrackingEvent.EventType.ORDER_CONFIRMED, "Order confirmed", null));
        tracking.addEvent(new TrackingEvent("TRK-123456789",
            TrackingEvent.EventType.PICKED_UP, "Package picked up", "New York, NY"));
        tracking.addEvent(new TrackingEvent("TRK-123456789",
            TrackingEvent.EventType.IN_TRANSIT, "In transit to destination", "Chicago, IL"));
        tracking.addEvent(new TrackingEvent("TRK-123456789",
            TrackingEvent.EventType.OUT_FOR_DELIVERY, "Out for delivery", "Los Angeles, CA"));
        tracking.addEvent(new TrackingEvent("TRK-123456789",
            TrackingEvent.EventType.DELIVERED, "Package delivered", "Los Angeles, CA"));

        // Then
        assertEquals(5, tracking.getEvents().size());
        assertEquals(ShipmentTracking.TrackingStatus.DELIVERED, tracking.getStatus());
        assertEquals("Los Angeles, CA", tracking.getCurrentLocation());
        assertNotNull(tracking.getActualDelivery());
    }

    @Test
    @DisplayName("Should set customer id")
    void shouldSetCustomerId() {
        // Given
        ShipmentTracking tracking = new ShipmentTracking("tenant-001", "TRK-123456789", "ORDER-001");

        // When
        tracking.setCustomerId("customer-001");

        // Then
        assertEquals("customer-001", tracking.getCustomerId());
    }

    @Test
    @DisplayName("Should set addresses")
    void shouldSetAddresses() {
        // Given
        ShipmentTracking tracking = new ShipmentTracking("tenant-001", "TRK-123456789", "ORDER-001");

        // When
        tracking.setOriginAddress("123 Main St, New York, NY 10001");
        tracking.setDestinationAddress("456 Oak Ave, Los Angeles, CA 90001");

        // Then
        assertEquals("123 Main St, New York, NY 10001", tracking.getOriginAddress());
        assertEquals("456 Oak Ave, Los Angeles, CA 90001", tracking.getDestinationAddress());
    }

    @Test
    @DisplayName("Should set estimated delivery")
    void shouldSetEstimatedDelivery() {
        // Given
        ShipmentTracking tracking = new ShipmentTracking("tenant-001", "TRK-123456789", "ORDER-001");
        Instant estimatedDelivery = Instant.now().plusSeconds(86400 * 3); // 3 days

        // When
        tracking.setEstimatedDelivery(estimatedDelivery);

        // Then
        assertEquals(estimatedDelivery, tracking.getEstimatedDelivery());
    }

    @Test
    @DisplayName("Should handle all tracking statuses")
    void shouldHandleAllTrackingStatuses() {
        assertNotNull(ShipmentTracking.TrackingStatus.ORDER_PLACED);
        assertNotNull(ShipmentTracking.TrackingStatus.ORDER_CONFIRMED);
        assertNotNull(ShipmentTracking.TrackingStatus.PICKUP_SCHEDULED);
        assertNotNull(ShipmentTracking.TrackingStatus.IN_TRANSIT);
        assertNotNull(ShipmentTracking.TrackingStatus.OUT_FOR_DELIVERY);
        assertNotNull(ShipmentTracking.TrackingStatus.DELIVERED);
        assertNotNull(ShipmentTracking.TrackingStatus.EXCEPTION);
        assertNotNull(ShipmentTracking.TrackingStatus.CANCELLED);
        assertNotNull(ShipmentTracking.TrackingStatus.RETURNED);
    }

    @Test
    @DisplayName("Should update timestamp on event addition")
    void shouldUpdateTimestampOnEventAddition() {
        // Given
        ShipmentTracking tracking = new ShipmentTracking("tenant-001", "TRK-123456789", "ORDER-001");
        var initialUpdatedAt = tracking.getUpdatedAt();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            fail("Sleep interrupted");
        }

        // When
        tracking.addEvent(new TrackingEvent("TRK-123456789",
            TrackingEvent.EventType.ORDER_CONFIRMED, "Order confirmed", null));

        // Then
        assertTrue(tracking.getUpdatedAt().isAfter(initialUpdatedAt));
    }

    @Test
    @DisplayName("Should maintain status when unknown event type added")
    void shouldMaintainStatusWhenUnknownEventTypeAdded() {
        // Given
        ShipmentTracking tracking = new ShipmentTracking("tenant-001", "TRK-123456789", "ORDER-001");
        ShipmentTracking.TrackingStatus initialStatus = tracking.getStatus();

        // When - DELIVERY_ATTEMPTED is not in the mapping, so status should remain unchanged
        tracking.addEvent(new TrackingEvent("TRK-123456789",
            TrackingEvent.EventType.DELIVERY_ATTEMPTED, "Delivery attempted", "Los Angeles, CA"));

        // Then
        assertEquals(initialStatus, tracking.getStatus());
    }

    @Test
    @DisplayName("Should handle full shipment lifecycle")
    void shouldHandleFullShipmentLifecycle() {
        // Given
        ShipmentTracking tracking = new ShipmentTracking("tenant-001", "TRK-123456789", "ORDER-001");

        // Initial state
        assertEquals(ShipmentTracking.TrackingStatus.ORDER_PLACED, tracking.getStatus());

        // Confirm
        tracking.addEvent(new TrackingEvent("TRK-123456789",
            TrackingEvent.EventType.ORDER_CONFIRMED, "Order confirmed", null));
        assertEquals(ShipmentTracking.TrackingStatus.ORDER_PLACED, tracking.getStatus());

        // Pickup scheduled
        tracking.addEvent(new TrackingEvent("TRK-123456789",
            TrackingEvent.EventType.OUT_FOR_PICKUP, "Driver assigned", "New York, NY"));
        assertEquals(ShipmentTracking.TrackingStatus.PICKUP_SCHEDULED, tracking.getStatus());

        // Picked up
        tracking.addEvent(new TrackingEvent("TRK-123456789",
            TrackingEvent.EventType.PICKED_UP, "Package picked up", "New York, NY"));
        assertEquals(ShipmentTracking.TrackingStatus.IN_TRANSIT, tracking.getStatus());

        // In transit
        tracking.addEvent(new TrackingEvent("TRK-123456789",
            TrackingEvent.EventType.IN_TRANSIT, "Arrived at sort facility", "Chicago, IL"));

        // Out for delivery
        tracking.addEvent(new TrackingEvent("TRK-123456789",
            TrackingEvent.EventType.OUT_FOR_DELIVERY, "Out for delivery", "Los Angeles, CA"));
        assertEquals(ShipmentTracking.TrackingStatus.OUT_FOR_DELIVERY, tracking.getStatus());

        // Delivered
        tracking.addEvent(new TrackingEvent("TRK-123456789",
            TrackingEvent.EventType.DELIVERED, "Delivered to front desk", "Los Angeles, CA"));
        assertEquals(ShipmentTracking.TrackingStatus.DELIVERED, tracking.getStatus());
        assertNotNull(tracking.getActualDelivery());
    }
}

package com.gogidix.ecommerce.tracking.domain.model;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.ArrayList;
import static org.assertj.core.api.Assertions.assertThat;

class OrderTrackingLombokTest {
    private static final Instant FIXED = Instant.parse("2026-01-01T00:00:00Z");
    private OrderTracking createFull() {
        Instant now = FIXED;
        OrderTracking t = new OrderTracking();
        t.setId("id1"); t.setTenantId("t1"); t.setTrackingId("trk1"); t.setOrderId("ord1");
        t.setOrderNumber("ON-1"); t.setStatus(OrderTracking.TrackingStatus.IN_TRANSIT);
        t.setCurrentLocation("NYC"); t.setEstimatedDeliveryDate("2025-01-01");
        t.setCarrier("FedEx"); t.setCarrierTrackingNumber("FX123");
        t.setShipmentType(OrderTracking.ShipmentType.EXPRESS);
        t.setOrigin("NYC"); t.setDestination("LA");
        t.setTrackingEvents(new ArrayList<>());
        t.setShippedAt(now); t.setInTransitAt(now);
        t.setDeliveredAt(now); t.setFailedAt(now);
        t.setNotes("notes"); t.setIsActive(true); t.setCreatedAt(now); t.setUpdatedAt(now);
        return t;
    }

    @Test void equals_same() { assertThat(createFull()).isEqualTo(createFull()); }
    @Test void equals_different() {
        OrderTracking t1 = createFull(); OrderTracking t2 = createFull(); t2.setTrackingId("other");
        assertThat(t1).isNotEqualTo(t2);
    }
    @Test void equals_null() { assertThat(createFull()).isNotEqualTo(null); }
    @Test void hashCode_consistency() {
        OrderTracking t = createFull();
        assertThat(t.hashCode()).isEqualTo(t.hashCode());
    }
    @Test void toString_notNull() { assertThat(createFull().toString()).contains("OrderTracking"); }
    @Test void canEqual() { assertThat(createFull().canEqual(new OrderTracking())).isTrue(); }
    @Test void trackingEvent_equals() {
        Instant now = Instant.now();
        OrderTracking.TrackingEvent e1 = new OrderTracking.TrackingEvent();
        e1.setEventId("e1"); e1.setStatus(OrderTracking.TrackingStatus.SHIPPED);
        e1.setDescription("shipped"); e1.setLocation("NYC"); e1.setTimestamp(now); e1.setSource("api");
        OrderTracking.TrackingEvent e2 = new OrderTracking.TrackingEvent();
        e2.setEventId("e1"); e2.setStatus(OrderTracking.TrackingStatus.SHIPPED);
        e2.setDescription("shipped"); e2.setLocation("NYC"); e2.setTimestamp(now); e2.setSource("api");
        assertThat(e1).isEqualTo(e2);
        e2.setDescription("other");
        assertThat(e1).isNotEqualTo(e2);
    }
}
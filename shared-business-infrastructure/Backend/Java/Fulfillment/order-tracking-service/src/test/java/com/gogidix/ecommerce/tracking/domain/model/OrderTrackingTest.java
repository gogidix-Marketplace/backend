package com.gogidix.ecommerce.tracking.domain.model;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class OrderTrackingTest {

    private OrderTracking createFull() {
        OrderTracking t = new OrderTracking();
        t.setId("id1"); t.setTenantId("t1"); t.setTrackingId("trk-1");
        t.setOrderId("ord-1"); t.setOrderNumber("ON-100");
        t.setStatus(OrderTracking.TrackingStatus.IN_TRANSIT);
        t.setCurrentLocation("Warehouse A");
        t.setEstimatedDeliveryDate("2024-06-01");
        t.setCarrier("FedEx"); t.setCarrierTrackingNumber("FX-123");
        t.setShipmentType(OrderTracking.ShipmentType.EXPRESS);
        t.setOrigin("NYC"); t.setDestination("LA");
        t.setShippedAt(Instant.parse("2024-05-20T10:00:00Z"));
        t.setInTransitAt(Instant.parse("2024-05-21T08:00:00Z"));
        t.setNotes("Handle with care");
        t.setIsActive(true);
        t.setCreatedAt(Instant.now()); t.setUpdatedAt(Instant.now());
        return t;
    }

    @Test void allFields() {
        OrderTracking t = createFull();
        assertThat(t.getId()).isEqualTo("id1");
        assertThat(t.getTenantId()).isEqualTo("t1");
        assertThat(t.getTrackingId()).isEqualTo("trk-1");
        assertThat(t.getOrderId()).isEqualTo("ord-1");
        assertThat(t.getOrderNumber()).isEqualTo("ON-100");
        assertThat(t.getStatus()).isEqualTo(OrderTracking.TrackingStatus.IN_TRANSIT);
        assertThat(t.getCurrentLocation()).isEqualTo("Warehouse A");
        assertThat(t.getCarrier()).isEqualTo("FedEx");
        assertThat(t.getCarrierTrackingNumber()).isEqualTo("FX-123");
        assertThat(t.getShipmentType()).isEqualTo(OrderTracking.ShipmentType.EXPRESS);
        assertThat(t.getOrigin()).isEqualTo("NYC");
        assertThat(t.getDestination()).isEqualTo("LA");
        assertThat(t.getNotes()).isEqualTo("Handle with care");
        assertThat(t.getIsActive()).isTrue();
    }

    @Test void trackingEvent() {
        OrderTracking.TrackingEvent e = new OrderTracking.TrackingEvent();
        e.setEventId("ev1"); e.setStatus(OrderTracking.TrackingStatus.SHIPPED);
        e.setDescription("Picked up"); e.setLocation("NYC");
        e.setTimestamp(Instant.now()); e.setSource("carrier");
        assertThat(e.getEventId()).isEqualTo("ev1");
        assertThat(e.getStatus()).isEqualTo(OrderTracking.TrackingStatus.SHIPPED);
        assertThat(e.getDescription()).isEqualTo("Picked up");
        assertThat(e.getLocation()).isEqualTo("NYC");
        assertThat(e.getSource()).isEqualTo("carrier");
    }

    @Test void trackingEventsList() {
        OrderTracking t = createFull();
        assertThat(t.getTrackingEvents()).isEmpty();
        OrderTracking.TrackingEvent e = new OrderTracking.TrackingEvent();
        e.setDescription("test");
        t.getTrackingEvents().add(e);
        assertThat(t.getTrackingEvents()).hasSize(1);
    }

    @Test void statusEnum() {
        assertThat(OrderTracking.TrackingStatus.values()).hasSize(9);
        assertThat(OrderTracking.TrackingStatus.valueOf("DELIVERED")).isEqualTo(OrderTracking.TrackingStatus.DELIVERED);
    }

    @Test void shipmentTypeEnum() {
        assertThat(OrderTracking.ShipmentType.values()).hasSize(5);
        assertThat(OrderTracking.ShipmentType.valueOf("SAME_DAY")).isEqualTo(OrderTracking.ShipmentType.SAME_DAY);
    }

    @Test void nullDefaults() {
        OrderTracking t = new OrderTracking();
        assertThat(t.getId()).isNull();
        assertThat(t.getStatus()).isNull();
        assertThat(t.getDeliveredAt()).isNull();
        assertThat(t.getFailedAt()).isNull();
        assertThat(t.getTrackingEvents()).isNotNull().isEmpty();
    }
}
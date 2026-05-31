package com.gogidix.ecommerce.tracking.application.dto;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class TrackingDtoTest {

    @Test void trackingEventDto_allFields() {
        Instant now = Instant.now();
        TrackingUpdateDto upd = new TrackingUpdateDto("DELIVERED", "LA", "Delivered", now);
        TrackingEventDto dto = new TrackingEventDto("id1", "t1", "ship1", "ord1",
            "FedEx", "FX-123", List.of(upd), "DELIVERED", now, now);
        assertThat(dto.id()).isEqualTo("id1");
        assertThat(dto.tenantId()).isEqualTo("t1");
        assertThat(dto.shipmentId()).isEqualTo("ship1");
        assertThat(dto.orderId()).isEqualTo("ord1");
        assertThat(dto.carrier()).isEqualTo("FedEx");
        assertThat(dto.trackingNumber()).isEqualTo("FX-123");
        assertThat(dto.updates()).hasSize(1);
        assertThat(dto.status()).isEqualTo("DELIVERED");
    }

    @Test void trackingEventDto_nulls() {
        TrackingEventDto dto = new TrackingEventDto(null, null, null, null, null, null, null, null, null, null);
        assertThat(dto.id()).isNull(); assertThat(dto.status()).isNull();
    }

    @Test void trackingResponse_fromDto() {
        TrackingUpdateDto upd = new TrackingUpdateDto("SHIPPED", "NYC", "Shipped", null);
        TrackingEventDto dto = new TrackingEventDto("id1", "t1", "s1", "o1",
            "UPS", "UP-456", List.of(upd), "SHIPPED", null, null);
        TrackingResponse r = TrackingResponse.from(dto);
        assertThat(r.trackingNumber()).isEqualTo("UP-456");
        assertThat(r.carrier()).isEqualTo("UPS");
        assertThat(r.status()).isEqualTo("SHIPPED");
        assertThat(r.updates()).hasSize(1);
    }

    @Test void trackingResponse_direct() {
        TrackingResponse r = new TrackingResponse("TN-1", "FedEx", "PENDING", null);
        assertThat(r.trackingNumber()).isEqualTo("TN-1");
        assertThat(r.carrier()).isEqualTo("FedEx");
        assertThat(r.updates()).isNull();
    }

    @Test void trackingUpdateDto_allFields() {
        Instant now = Instant.now();
        TrackingUpdateDto upd = new TrackingUpdateDto("IN_TRANSIT", "Chicago", "In transit", now);
        assertThat(upd.status()).isEqualTo("IN_TRANSIT");
        assertThat(upd.location()).isEqualTo("Chicago");
        assertThat(upd.description()).isEqualTo("In transit");
        assertThat(upd.timestamp()).isEqualTo(now);
    }

    @Test void trackingUpdateDto_nulls() {
        TrackingUpdateDto upd = new TrackingUpdateDto(null, null, null, null);
        assertThat(upd.status()).isNull(); assertThat(upd.location()).isNull();
    }

    @Test void dto_equality() {
        TrackingUpdateDto a = new TrackingUpdateDto("s", "l", "d", null);
        TrackingUpdateDto b = new TrackingUpdateDto("s", "l", "d", null);
        assertThat(a).isEqualTo(b); assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
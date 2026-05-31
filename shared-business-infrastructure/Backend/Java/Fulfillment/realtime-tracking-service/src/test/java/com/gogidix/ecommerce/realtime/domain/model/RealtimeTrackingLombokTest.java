package com.gogidix.ecommerce.realtime.domain.model;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

class RealtimeTrackingLombokTest {
    private RealtimeTracking createFull() { return createAt(Instant.now()); }
    private RealtimeTracking createAt(Instant now) {
        RealtimeTracking t = new RealtimeTracking();
        t.setId("id1"); t.setTenantId("t1"); t.setTrackingId("rtk1"); t.setOrderId("ord1");
        t.setSpeed(65.0); t.setHeading(180.0); t.setAltitude(500.0);
        t.setDeviceId("dev1"); t.setVehicleId("veh1");
        t.setBatteryStatus(RealtimeTracking.BatteryStatus.FULL);
        t.setSignalStrength(RealtimeTracking.SignalStrength.EXCELLENT);
        t.setIsActive(true); t.setLastUpdateAt(now); t.setCreatedAt(now); t.setUpdatedAt(now);

        RealtimeTracking.Location loc = new RealtimeTracking.Location();
        loc.setLatitude(40.7); loc.setLongitude(-74.0);
        loc.setAddress("NYC"); loc.setCity("New York"); loc.setCountry("US"); loc.setTimestamp(now);
        t.setCurrentLocation(loc);
        return t;
    }

    @Test void equals_same() {
        Instant fixed = Instant.parse("2026-01-01T00:00:00Z");
        assertThat(createAt(fixed)).isEqualTo(createAt(fixed));
    }
    @Test void equals_different() {
        RealtimeTracking t1 = createFull(); RealtimeTracking t2 = createFull(); t2.setSpeed(99.0);
        assertThat(t1).isNotEqualTo(t2);
    }
    @Test void equals_null() { assertThat(createFull()).isNotEqualTo(null); }
    @Test void hashCode_consistency() {
        Instant fixed = Instant.parse("2026-01-01T00:00:00Z");
        assertThat(createAt(fixed).hashCode()).isEqualTo(createAt(fixed).hashCode());
    }
    @Test void toString_notNull() { assertThat(createFull().toString()).contains("RealtimeTracking"); }
    @Test void canEqual() { assertThat(createFull().canEqual(new RealtimeTracking())).isTrue(); }
    @Test void location_equals() {
        Instant now = Instant.now();
        RealtimeTracking.Location l1 = new RealtimeTracking.Location();
        l1.setLatitude(40.7); l1.setLongitude(-74.0); l1.setAddress("NYC");
        l1.setCity("New York"); l1.setCountry("US"); l1.setTimestamp(now);
        RealtimeTracking.Location l2 = new RealtimeTracking.Location();
        l2.setLatitude(40.7); l2.setLongitude(-74.0); l2.setAddress("NYC");
        l2.setCity("New York"); l2.setCountry("US"); l2.setTimestamp(now);
        assertThat(l1).isEqualTo(l2);
        l2.setLatitude(34.0);
        assertThat(l1).isNotEqualTo(l2);
    }
}
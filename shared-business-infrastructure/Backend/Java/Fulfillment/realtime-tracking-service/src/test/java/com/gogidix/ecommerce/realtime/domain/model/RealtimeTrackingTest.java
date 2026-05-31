package com.gogidix.ecommerce.realtime.domain.model;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

class RealtimeTrackingTest {

    private RealtimeTracking createFull() {
        RealtimeTracking t = new RealtimeTracking();
        t.setId("id1"); t.setTenantId("t1"); t.setTrackingId("rtk-1");
        t.setOrderId("ord-1"); t.setSpeed(65.5); t.setHeading(180.0); t.setAltitude(500.0);
        t.setDeviceId("dev-1"); t.setVehicleId("veh-1");
        t.setBatteryStatus(RealtimeTracking.BatteryStatus.FULL);
        t.setSignalStrength(RealtimeTracking.SignalStrength.EXCELLENT);
        t.setIsActive(true); t.setLastUpdateAt(Instant.now());
        t.setCreatedAt(Instant.now()); t.setUpdatedAt(Instant.now());
        RealtimeTracking.Location loc = new RealtimeTracking.Location();
        loc.setLatitude(40.7128); loc.setLongitude(-74.006); loc.setAddress("123 Main");
        loc.setCity("NYC"); loc.setCountry("US"); loc.setTimestamp(Instant.now());
        t.setCurrentLocation(loc);
        return t;
    }

    @Test void allFields() {
        RealtimeTracking t = createFull();
        assertThat(t.getId()).isEqualTo("id1");
        assertThat(t.getTenantId()).isEqualTo("t1");
        assertThat(t.getTrackingId()).isEqualTo("rtk-1");
        assertThat(t.getOrderId()).isEqualTo("ord-1");
        assertThat(t.getSpeed()).isEqualTo(65.5);
        assertThat(t.getHeading()).isEqualTo(180.0);
        assertThat(t.getAltitude()).isEqualTo(500.0);
        assertThat(t.getDeviceId()).isEqualTo("dev-1");
        assertThat(t.getVehicleId()).isEqualTo("veh-1");
        assertThat(t.getBatteryStatus()).isEqualTo(RealtimeTracking.BatteryStatus.FULL);
        assertThat(t.getSignalStrength()).isEqualTo(RealtimeTracking.SignalStrength.EXCELLENT);
        assertThat(t.getIsActive()).isTrue();
        assertThat(t.getLastUpdateAt()).isNotNull();
    }

    @Test void location() {
        RealtimeTracking.Location loc = new RealtimeTracking.Location();
        loc.setLatitude(34.0522); loc.setLongitude(-118.2437);
        loc.setAddress("456 Oak"); loc.setCity("LA");
        loc.setCountry("US"); loc.setTimestamp(Instant.now());
        assertThat(loc.getLatitude()).isEqualTo(34.0522);
        assertThat(loc.getLongitude()).isEqualTo(-118.2437);
        assertThat(loc.getAddress()).isEqualTo("456 Oak");
        assertThat(loc.getCity()).isEqualTo("LA");
        assertThat(loc.getCountry()).isEqualTo("US");
    }

    @Test void batteryStatusEnum() {
        assertThat(RealtimeTracking.BatteryStatus.values()).hasSize(4);
        assertThat(RealtimeTracking.BatteryStatus.valueOf("CRITICAL")).isEqualTo(RealtimeTracking.BatteryStatus.CRITICAL);
    }

    @Test void signalStrengthEnum() {
        assertThat(RealtimeTracking.SignalStrength.values()).hasSize(5);
        assertThat(RealtimeTracking.SignalStrength.valueOf("NO_SIGNAL")).isEqualTo(RealtimeTracking.SignalStrength.NO_SIGNAL);
    }

    @Test void nullDefaults() {
        RealtimeTracking t = new RealtimeTracking();
        assertThat(t.getId()).isNull(); assertThat(t.getSpeed()).isNull();
        assertThat(t.getCurrentLocation()).isNull(); assertThat(t.getDestination()).isNull();
        assertThat(t.getIsActive()).isNull();
    }
}
package com.gogidix.ecommerce.realtime.domain.service;

import com.gogidix.ecommerce.realtime.domain.model.RealtimeTracking;
import com.gogidix.ecommerce.realtime.domain.model.RealtimeTracking.Location;
import com.gogidix.ecommerce.realtime.domain.repository.RealtimeTrackingRepository;
import com.gogidix.ecommerce.realtime.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.realtime.shared.requestcontext.RequestContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RealtimeTrackingServiceTest {
    @Mock private RealtimeTrackingRepository repository;
    @InjectMocks private RealtimeTrackingService service;
    @BeforeEach void setUp() { RequestContextHolder.set(RequestContext.builder().tenantId("t1").build()); }
    @AfterEach void tearDown() { RequestContextHolder.clear(); }

    private RealtimeTracking createTracking() {
        RealtimeTracking t = new RealtimeTracking(); t.setId("id1"); t.setTenantId("t1"); t.setTrackingId("rtk-1");
        return t;
    }

    @Test void testCreateTracking() {
        when(repository.save(any(RealtimeTracking.class))).thenAnswer(inv -> inv.getArgument(0));
        RealtimeTracking result = service.createTracking(new RealtimeTracking());
        assertThat(result.getTrackingId()).isNotNull();
        assertThat(result.getIsActive()).isTrue();
        assertThat(result.getLastUpdateAt()).isNotNull();
    }
    @Test void getTracking_found() {
        when(repository.findByTenantIdAndTrackingId("t1", "rtk-1")).thenReturn(Optional.of(createTracking()));
        assertThat(service.getTracking("t1", "rtk-1")).isNotNull();
    }
    @Test void getTracking_notFound() {
        when(repository.findByTenantIdAndTrackingId("t1", "x")).thenReturn(Optional.empty());
        assertThat(service.getTracking("t1", "x")).isNull();
    }
    @Test void updateLocation() {
        RealtimeTracking t = createTracking();
        when(repository.findByTenantIdAndTrackingId("t1", "rtk-1")).thenReturn(Optional.of(t));
        when(repository.save(any(RealtimeTracking.class))).thenAnswer(inv -> inv.getArgument(0));
        Location loc = new Location(); loc.setLatitude(40.7); loc.setLongitude(-74.0);
        RealtimeTracking result = service.updateLocation("t1", "rtk-1", loc);
        assertThat(result.getCurrentLocation().getLatitude()).isEqualTo(40.7);
        assertThat(result.getCurrentLocation().getTimestamp()).isNotNull();
    }
    @Test void updateLocation_notFound() {
        when(repository.findByTenantIdAndTrackingId("t1", "x")).thenReturn(Optional.empty());
        assertThat(service.updateLocation("t1", "x", new Location())).isNull();
    }
    @Test void updateMetrics() {
        RealtimeTracking t = createTracking();
        when(repository.findByTenantIdAndTrackingId("t1", "rtk-1")).thenReturn(Optional.of(t));
        when(repository.save(any(RealtimeTracking.class))).thenAnswer(inv -> inv.getArgument(0));
        RealtimeTracking result = service.updateMetrics("t1", "rtk-1", 65.0, 180.0, 500.0);
        assertThat(result.getSpeed()).isEqualTo(65.0);
        assertThat(result.getHeading()).isEqualTo(180.0);
        assertThat(result.getAltitude()).isEqualTo(500.0);
    }
    @Test void updateDeviceStatus() {
        RealtimeTracking t = createTracking();
        when(repository.findByTenantIdAndTrackingId("t1", "rtk-1")).thenReturn(Optional.of(t));
        when(repository.save(any(RealtimeTracking.class))).thenAnswer(inv -> inv.getArgument(0));
        RealtimeTracking result = service.updateDeviceStatus("t1", "rtk-1",
            RealtimeTracking.BatteryStatus.LOW, RealtimeTracking.SignalStrength.POOR);
        assertThat(result.getBatteryStatus()).isEqualTo(RealtimeTracking.BatteryStatus.LOW);
        assertThat(result.getSignalStrength()).isEqualTo(RealtimeTracking.SignalStrength.POOR);
    }
    @Test void deactivateTracking() {
        RealtimeTracking t = createTracking(); t.setIsActive(true);
        when(repository.findByTenantIdAndTrackingId("t1", "rtk-1")).thenReturn(Optional.of(t));
        service.deactivateTracking("t1", "rtk-1");
        verify(repository).save(argThat(tr -> !tr.getIsActive()));
    }
    @Test void deactivateTracking_notFound() {
        when(repository.findByTenantIdAndTrackingId("t1", "x")).thenReturn(Optional.empty());
        service.deactivateTracking("t1", "x");
        verify(repository, never()).save(any());
    }
}
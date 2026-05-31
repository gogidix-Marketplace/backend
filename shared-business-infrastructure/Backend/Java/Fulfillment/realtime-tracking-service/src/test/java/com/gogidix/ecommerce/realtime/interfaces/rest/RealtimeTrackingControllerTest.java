package com.gogidix.ecommerce.realtime.interfaces.rest;

import com.gogidix.ecommerce.realtime.domain.model.RealtimeTracking;
import com.gogidix.ecommerce.realtime.domain.model.RealtimeTracking.Location;
import com.gogidix.ecommerce.realtime.domain.service.RealtimeTrackingService;
import com.gogidix.ecommerce.realtime.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.realtime.shared.requestcontext.RequestContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RealtimeTrackingControllerTest {
    @Mock private RealtimeTrackingService service;
    @InjectMocks private RealtimeTrackingController controller;
    @BeforeEach void setUp() { RequestContextHolder.set(RequestContext.builder().tenantId("t1").build()); }
    @AfterEach void tearDown() { RequestContextHolder.clear(); }

    private RealtimeTracking createTrk() { RealtimeTracking t = new RealtimeTracking(); t.setId("id1"); return t; }

    @Test void getTracking_found() {
        when(service.getTracking("t1","rtk1")).thenReturn(createTrk());
        assertThat(controller.getTracking("rtk1").getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void getTracking_notFound() {
        when(service.getTracking("t1","x")).thenReturn(null);
        assertThat(controller.getTracking("x").getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test void getTrackingByOrderId_found() {
        when(service.getTrackingByOrderId("t1","ord1")).thenReturn(createTrk());
        assertThat(controller.getTrackingByOrderId("ord1").getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void createTracking() {
        when(service.createTracking(any())).thenAnswer(inv -> inv.getArgument(0));
        assertThat(controller.createTracking(new RealtimeTracking()).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void updateLocation_found() {
        when(service.updateLocation(eq("t1"),eq("rtk1"),any())).thenReturn(createTrk());
        assertThat(controller.updateLocation("rtk1",new Location()).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void updateLocation_notFound() {
        when(service.updateLocation(eq("t1"),eq("x"),any())).thenReturn(null);
        assertThat(controller.updateLocation("x",new Location()).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test void updateMetrics_found() {
        when(service.updateMetrics("t1","rtk1",65.0,180.0,500.0)).thenReturn(createTrk());
        assertThat(controller.updateMetrics("rtk1",65.0,180.0,500.0).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void updateDeviceStatus_found() {
        when(service.updateDeviceStatus("t1","rtk1",
            RealtimeTracking.BatteryStatus.FULL, RealtimeTracking.SignalStrength.EXCELLENT)).thenReturn(createTrk());
        assertThat(controller.updateDeviceStatus("rtk1",
            RealtimeTracking.BatteryStatus.FULL, RealtimeTracking.SignalStrength.EXCELLENT).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void deactivateTracking() {
        assertThat(controller.deactivateTracking("rtk1").getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(service).deactivateTracking("t1","rtk1");
    }
}
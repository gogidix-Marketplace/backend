package com.gogidix.ecommerce.tracking.interfaces.rest;

import com.gogidix.ecommerce.tracking.domain.model.OrderTracking;
import com.gogidix.ecommerce.tracking.domain.model.OrderTracking.TrackingEvent;
import com.gogidix.ecommerce.tracking.domain.service.OrderTrackingService;
import com.gogidix.ecommerce.tracking.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.tracking.shared.requestcontext.RequestContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderTrackingControllerTest {
    @Mock private OrderTrackingService service;
    @InjectMocks private OrderTrackingController controller;
    @BeforeEach void setUp() { RequestContextHolder.set(RequestContext.builder().tenantId("t1").build()); }
    @AfterEach void tearDown() { RequestContextHolder.clear(); }

    private OrderTracking createTracking() {
        OrderTracking t = new OrderTracking(); t.setId("id1"); return t;
    }

    @Test void getTracking_found() {
        when(service.getTracking("t1","trk1")).thenReturn(createTracking());
        assertThat(controller.getTracking("trk1").getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void getTracking_notFound() {
        when(service.getTracking("t1","x")).thenReturn(null);
        assertThat(controller.getTracking("x").getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test void getTrackingByOrderId_found() {
        when(service.getTrackingByOrderId("t1","ord1")).thenReturn(createTracking());
        assertThat(controller.getTrackingByOrderId("ord1").getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void getTrackingByOrderId_notFound() {
        when(service.getTrackingByOrderId("t1","x")).thenReturn(null);
        assertThat(controller.getTrackingByOrderId("x").getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test void getTrackingByStatus() {
        when(service.getTrackingByStatus("t1", OrderTracking.TrackingStatus.PENDING)).thenReturn(List.of(createTracking()));
        assertThat(controller.getTrackingByStatus(OrderTracking.TrackingStatus.PENDING).getBody()).hasSize(1);
    }
    @Test void testCreateTracking() {
        when(service.createTracking(any(OrderTracking.class))).thenAnswer(inv -> inv.getArgument(0));
        assertThat(controller.createTracking(new OrderTracking()).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void addTrackingEvent_found() {
        when(service.addTrackingEvent(eq("t1"),eq("trk1"),any())).thenReturn(createTracking());
        assertThat(controller.addTrackingEvent("trk1",new TrackingEvent()).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void addTrackingEvent_notFound() {
        when(service.addTrackingEvent(eq("t1"),eq("x"),any())).thenReturn(null);
        assertThat(controller.addTrackingEvent("x",new TrackingEvent()).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test void updateStatus_found() {
        when(service.updateStatus("t1","trk1",OrderTracking.TrackingStatus.SHIPPED)).thenReturn(createTracking());
        assertThat(controller.updateStatus("trk1",OrderTracking.TrackingStatus.SHIPPED).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void updateCarrierInfo_found() {
        when(service.updateCarrierInfo("t1","trk1","FedEx","FX1")).thenReturn(createTracking());
        assertThat(controller.updateCarrierInfo("trk1","FedEx","FX1").getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}

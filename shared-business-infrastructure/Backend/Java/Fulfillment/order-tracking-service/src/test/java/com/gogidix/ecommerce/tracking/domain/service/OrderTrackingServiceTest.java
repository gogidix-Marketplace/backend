package com.gogidix.ecommerce.tracking.domain.service;

import com.gogidix.ecommerce.tracking.domain.model.OrderTracking;
import com.gogidix.ecommerce.tracking.domain.model.OrderTracking.TrackingEvent;
import com.gogidix.ecommerce.tracking.domain.repository.OrderTrackingRepository;
import com.gogidix.ecommerce.tracking.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.tracking.shared.requestcontext.RequestContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderTrackingServiceTest {
    @Mock private OrderTrackingRepository repository;
    @InjectMocks private OrderTrackingService service;
    @BeforeEach void setUp() { RequestContextHolder.set(RequestContext.builder().tenantId("t1").build()); }
    @AfterEach void tearDown() { RequestContextHolder.clear(); }

    private OrderTracking createTracking() {
        OrderTracking t = new OrderTracking(); t.setId("id1"); t.setTenantId("t1"); t.setTrackingId("trk-1");
        return t;
    }

    @Test void testCreateTracking() {
        when(repository.save(any(OrderTracking.class))).thenAnswer(inv -> inv.getArgument(0));
        OrderTracking result = service.createTracking(new OrderTracking());
        assertThat(result.getTrackingId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(OrderTracking.TrackingStatus.PENDING);
        assertThat(result.getCreatedAt()).isNotNull();
    }
    @Test void getTracking_found() {
        when(repository.findByTenantIdAndTrackingId("t1", "trk-1")).thenReturn(Optional.of(createTracking()));
        assertThat(service.getTracking("t1", "trk-1")).isNotNull();
    }
    @Test void getTracking_notFound() {
        when(repository.findByTenantIdAndTrackingId("t1", "x")).thenReturn(Optional.empty());
        assertThat(service.getTracking("t1", "x")).isNull();
    }
    @Test void addTrackingEvent_shipped() {
        OrderTracking t = createTracking();
        when(repository.findByTenantIdAndTrackingId("t1", "trk-1")).thenReturn(Optional.of(t));
        when(repository.save(any(OrderTracking.class))).thenAnswer(inv -> inv.getArgument(0));
        TrackingEvent event = new TrackingEvent(); event.setStatus(OrderTracking.TrackingStatus.SHIPPED);
        OrderTracking result = service.addTrackingEvent("t1", "trk-1", event);
        assertThat(result.getStatus()).isEqualTo(OrderTracking.TrackingStatus.SHIPPED);
        assertThat(result.getShippedAt()).isNotNull();
        assertThat(result.getTrackingEvents()).hasSize(1);
    }
    @Test void addTrackingEvent_delivered() {
        OrderTracking t = createTracking();
        when(repository.findByTenantIdAndTrackingId("t1", "trk-1")).thenReturn(Optional.of(t));
        when(repository.save(any(OrderTracking.class))).thenAnswer(inv -> inv.getArgument(0));
        TrackingEvent event = new TrackingEvent(); event.setStatus(OrderTracking.TrackingStatus.DELIVERED);
        OrderTracking result = service.addTrackingEvent("t1", "trk-1", event);
        assertThat(result.getDeliveredAt()).isNotNull();
    }
    @Test void addTrackingEvent_notFound() {
        when(repository.findByTenantIdAndTrackingId("t1", "x")).thenReturn(Optional.empty());
        assertThat(service.addTrackingEvent("t1", "x", new TrackingEvent())).isNull();
    }
    @Test void updateStatus() {
        OrderTracking t = createTracking();
        when(repository.findByTenantIdAndTrackingId("t1", "trk-1")).thenReturn(Optional.of(t));
        when(repository.save(any(OrderTracking.class))).thenAnswer(inv -> inv.getArgument(0));
        OrderTracking result = service.updateStatus("t1", "trk-1", OrderTracking.TrackingStatus.IN_TRANSIT);
        assertThat(result.getStatus()).isEqualTo(OrderTracking.TrackingStatus.IN_TRANSIT);
        assertThat(result.getTrackingEvents()).hasSize(1);
    }
    @Test void updateCarrierInfo() {
        OrderTracking t = createTracking();
        when(repository.findByTenantIdAndTrackingId("t1", "trk-1")).thenReturn(Optional.of(t));
        when(repository.save(any(OrderTracking.class))).thenAnswer(inv -> inv.getArgument(0));
        OrderTracking result = service.updateCarrierInfo("t1", "trk-1", "FedEx", "FX-123");
        assertThat(result.getCarrier()).isEqualTo("FedEx");
        assertThat(result.getCarrierTrackingNumber()).isEqualTo("FX-123");
    }
    @Test void getTrackingByOrderId() {
        when(repository.findByTenantIdAndOrderId("t1", "ord-1")).thenReturn(Optional.of(createTracking()));
        assertThat(service.getTrackingByOrderId("t1", "ord-1")).isNotNull();
    }
    @Test void getTrackingByStatus() {
        when(repository.findByTenantIdAndStatus("t1", OrderTracking.TrackingStatus.PENDING)).thenReturn(List.of(createTracking()));
        assertThat(service.getTrackingByStatus("t1", OrderTracking.TrackingStatus.PENDING)).hasSize(1);
    }
}
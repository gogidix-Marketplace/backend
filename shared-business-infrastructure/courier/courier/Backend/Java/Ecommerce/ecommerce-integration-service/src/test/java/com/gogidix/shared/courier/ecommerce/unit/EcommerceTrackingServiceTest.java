package com.gogidix.shared.courier.ecommerce.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Optional;

import com.gogidix.shared.courier.ecommerce.application.service.EcommerceTrackingService;
import com.gogidix.shared.courier.ecommerce.domain.entity.EcommerceDeliveryTracking;
import com.gogidix.shared.courier.ecommerce.domain.repository.EcommerceTrackingRepository;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.DeliveryType;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.HubStageStatus;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.StageCompletionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EcommerceTrackingServiceTest {

    @Mock private EcommerceTrackingRepository trackingRepository;
    private EcommerceTrackingService service;

    @BeforeEach
    void setUp() {
        service = new EcommerceTrackingService(trackingRepository);
    }

    @Test
    void shouldGetTrackingByOrderId() {
        EcommerceDeliveryTracking tracking = new EcommerceDeliveryTracking("GO-001", "SO-001", DeliveryType.TYPE_A);
        when(trackingRepository.findByOrderId("GO-001")).thenReturn(Optional.of(tracking));

        EcommerceDeliveryTracking result = service.getTrackingByOrderId("GO-001");

        assertEquals("GO-001", result.getOrderId());
    }

    @Test
    void shouldThrowWhenTrackingNotFound() {
        when(trackingRepository.findByOrderId("MISSING")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.getTrackingByOrderId("MISSING"));
    }

    @Test
    void shouldUpdateStage() {
        EcommerceDeliveryTracking tracking = new EcommerceDeliveryTracking("GO-002", "SO-002", DeliveryType.TYPE_B);
        when(trackingRepository.findByOrderId("GO-002")).thenReturn(Optional.of(tracking));
        when(trackingRepository.save(any())).thenReturn(tracking);

        service.updateStage("GO-002", HubStageStatus.PICKED_UP, StageCompletionStatus.COMPLETED);

        assertEquals(HubStageStatus.PICKED_UP, tracking.getCurrentStage());
        assertEquals(1, tracking.getStages().size());
        verify(trackingRepository).save(tracking);
    }

    @Test
    void shouldMarkHubArrival() {
        EcommerceDeliveryTracking tracking = new EcommerceDeliveryTracking("GO-003", "SO-003", DeliveryType.TYPE_B);
        when(trackingRepository.findByOrderId("GO-003")).thenReturn(Optional.of(tracking));
        when(trackingRepository.save(any())).thenReturn(tracking);

        service.markHubArrival("GO-003", "Lagos Hub");

        assertEquals(HubStageStatus.AT_ORIGIN_HUB, tracking.getCurrentStage());
        assertEquals("Lagos Hub", tracking.getStages().get(0).getHubName());
    }

    @Test
    void shouldMarkDelivered() {
        EcommerceDeliveryTracking tracking = new EcommerceDeliveryTracking("GO-004", "SO-004", DeliveryType.TYPE_A);
        when(trackingRepository.findByOrderId("GO-004")).thenReturn(Optional.of(tracking));
        when(trackingRepository.save(any())).thenReturn(tracking);

        service.markDelivered("GO-004");

        assertEquals(HubStageStatus.DELIVERED, tracking.getCurrentStage());
        assertEquals(StageCompletionStatus.COMPLETED, tracking.getStages().get(0).getStatus());
    }
}

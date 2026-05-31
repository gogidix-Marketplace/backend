package com.gogidix.shared.courier.ecommerce.unit;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;

import com.gogidix.shared.courier.ecommerce.domain.entity.EcommerceDeliveryTracking;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.DeliveryType;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.HubStageStatus;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.StageCompletionStatus;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.dto.TrackingResponse;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.mappers.EcommerceTrackingMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EcommerceTrackingMapperTest {

    private EcommerceTrackingMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new EcommerceTrackingMapper();
    }

    @Test
    void shouldMapTrackingToResponse() {
        EcommerceDeliveryTracking tracking = new EcommerceDeliveryTracking("GO-001", "SO-001", DeliveryType.TYPE_B);
        tracking.setTrackingId("TRK-001");
        tracking.updateCourierInfo("Rider 1", "+234");

        EcommerceDeliveryTracking.HubStage stage1 = new EcommerceDeliveryTracking.HubStage(
            HubStageStatus.PICKUP_ASSIGNED, StageCompletionStatus.COMPLETED
        );
        stage1.setCourierName("Rider 1");
        stage1.setCourierPhone("+234");
        stage1.setCompletedAt(Instant.now());
        tracking.addStage(stage1);

        tracking.updateCurrentStage(HubStageStatus.AT_ORIGIN_HUB);

        TrackingResponse response = mapper.toResponse(tracking);

        assertEquals("GO-001", response.orderId());
        assertEquals("SO-001", response.subOrderId());
        assertEquals("TYPE_B", response.deliveryType());
        assertEquals("AT_ORIGIN_HUB", response.currentStage());
        assertEquals(1, response.stages().size());
        assertEquals("PICKUP_ASSIGNED", response.stages().get(0).stage());
        assertEquals("COMPLETED", response.stages().get(0).status());
    }

    @Test
    void shouldHandleEmptyStages() {
        EcommerceDeliveryTracking tracking = new EcommerceDeliveryTracking("GO-002", "SO-002", DeliveryType.TYPE_A);
        TrackingResponse response = mapper.toResponse(tracking);

        assertNotNull(response.stages());
        assertTrue(response.stages().isEmpty());
    }
}

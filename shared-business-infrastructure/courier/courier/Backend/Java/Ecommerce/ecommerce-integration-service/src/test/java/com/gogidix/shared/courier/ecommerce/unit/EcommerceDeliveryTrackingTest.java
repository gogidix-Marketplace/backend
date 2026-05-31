package com.gogidix.shared.courier.ecommerce.unit;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;

import com.gogidix.shared.courier.ecommerce.domain.entity.EcommerceDeliveryTracking;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.DeliveryType;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.HubStageStatus;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.StageCompletionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EcommerceDeliveryTrackingTest {

    private EcommerceDeliveryTracking tracking;

    @BeforeEach
    void setUp() {
        tracking = new EcommerceDeliveryTracking("GO-001", "SO-001", DeliveryType.TYPE_B);
    }

    @Test
    void shouldCreateTrackingWithCorrectFields() {
        assertNotNull(tracking.getId());
        assertEquals("GO-001", tracking.getOrderId());
        assertEquals("SO-001", tracking.getSubOrderId());
        assertEquals(DeliveryType.TYPE_B, tracking.getDeliveryType());
        assertEquals(HubStageStatus.PICKUP_ASSIGNED, tracking.getCurrentStage());
        assertNotNull(tracking.getStages());
        assertTrue(tracking.getStages().isEmpty());
    }

    @Test
    void shouldAddStage() {
        EcommerceDeliveryTracking.HubStage stage = new EcommerceDeliveryTracking.HubStage(
            HubStageStatus.PICKED_UP, StageCompletionStatus.COMPLETED
        );
        tracking.addStage(stage);

        assertEquals(1, tracking.getStages().size());
        assertEquals(HubStageStatus.PICKED_UP, tracking.getStages().get(0).getStage());
    }

    @Test
    void shouldUpdateCurrentStage() {
        tracking.updateCurrentStage(HubStageStatus.IN_TRANSIT_BETWEEN_HUBS);
        assertEquals(HubStageStatus.IN_TRANSIT_BETWEEN_HUBS, tracking.getCurrentStage());
    }

    @Test
    void shouldUpdateEta() {
        Instant eta = Instant.now().plusSeconds(3600);
        tracking.updateEta(eta);
        assertEquals(eta, tracking.getEta());
    }

    @Test
    void shouldUpdateCourierInfo() {
        tracking.updateCourierInfo("Rider 1", "+2348011111111");
        assertEquals("Rider 1", tracking.getCourierName());
        assertEquals("+2348011111111", tracking.getCourierPhone());
    }

    @Test
    void shouldSetTrackingId() {
        tracking.setTrackingId("TRK-001");
        assertEquals("TRK-001", tracking.getTrackingId());
    }

    @Test
    void shouldTestHubStageProperties() {
        EcommerceDeliveryTracking.HubStage stage = new EcommerceDeliveryTracking.HubStage(
            HubStageStatus.AT_ORIGIN_HUB, StageCompletionStatus.IN_PROGRESS
        );
        stage.setHubName("Lagos Hub");
        stage.setCourierName("Rider 1");
        stage.setCourierPhone("+234");
        stage.setCompletedAt(Instant.now());
        stage.setArrivedAt(Instant.now());

        assertEquals("Lagos Hub", stage.getHubName());
        assertEquals("Rider 1", stage.getCourierName());
        assertNotNull(stage.getCompletedAt());
        assertNotNull(stage.getArrivedAt());
    }

    @Test
    void shouldTestEquality() {
        EcommerceDeliveryTracking other = new EcommerceDeliveryTracking("o", "s", DeliveryType.TYPE_A);
        assertNotEquals(tracking, other);
        assertEquals(tracking, tracking);
    }
}

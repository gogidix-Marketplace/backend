package com.gogidix.shared.courier.ecommerce.unit;

import static org.mockito.Mockito.*;

import com.gogidix.shared.courier.ecommerce.domain.events.*;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.DeliveryLeg;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.DeliveryType;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.VehicleType;
import com.gogidix.shared.courier.ecommerce.infrastructure.messaging.producers.EcommerceCourierEventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Instant;

@ExtendWith(MockitoExtension.class)
class EcommerceCourierEventPublisherTest {

    @Mock private KafkaTemplate<String, Object> kafkaTemplate;
    @InjectMocks private EcommerceCourierEventPublisher publisher;

    @Test
    void shouldPublishCourierAssigned() {
        CourierAssignedEvent event = new CourierAssignedEvent(
            "agg-1", "GO-001", "SO-001", "c1", "Rider", "+234",
            VehicleType.MOTORCYCLE, "TRK-001", Instant.now(), Instant.now(),
            DeliveryType.TYPE_A, DeliveryLeg.FULL
        );

        publisher.publishCourierAssigned(event);
        verify(kafkaTemplate).send("ecommerce.courier.assigned", "GO-001", event);
    }

    @Test
    void shouldPublishCourierPickedUp() {
        CourierPickedUpEvent event = new CourierPickedUpEvent(
            "agg-1", "GO-001", "SO-001", "c1", "TRK-001", 2
        );

        publisher.publishCourierPickedUp(event);
        verify(kafkaTemplate).send("ecommerce.courier.picked_up", "GO-001", event);
    }

    @Test
    void shouldPublishCourierDelivered() {
        CourierDeliveredEvent event = new CourierDeliveredEvent(
            "agg-1", "GO-001", "SO-001", "c1", "TRK-001", "photo.jpg", "John"
        );

        publisher.publishCourierDelivered(event);
        verify(kafkaTemplate).send("ecommerce.courier.delivered", "GO-001", event);
    }

    @Test
    void shouldPublishCourierAtHub() {
        CourierAtHubEvent event = new CourierAtHubEvent(
            "agg-1", "GO-001", "SO-001", "c1", "hub-1", "Lagos Hub"
        );

        publisher.publishCourierAtHub(event);
        verify(kafkaTemplate).send("ecommerce.courier.at_hub", "GO-001", event);
    }

    @Test
    void shouldPublishStatusChanged() {
        CourierStatusChangedEvent event = new CourierStatusChangedEvent(
            "agg-1", "GO-001", "SO-001", "c1", "TRK-001",
            "ASSIGNED", "PICKED_UP", null
        );

        publisher.publishStatusChanged(event);
        verify(kafkaTemplate).send("ecommerce.courier.status_changed", "GO-001", event);
    }
}

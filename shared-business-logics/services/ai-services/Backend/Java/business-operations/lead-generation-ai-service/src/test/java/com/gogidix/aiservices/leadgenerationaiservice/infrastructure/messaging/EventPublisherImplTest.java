package com.gogidix.aiservices.leadgenerationaiservice.infrastructure.messaging;
import org.junit.jupiter.api.*; import java.util.Map; import static org.assertj.core.api.Assertions.*;
class EventPublisherImplTest {
    private final EventPublisherImpl pub = new EventPublisherImpl();
    @Test void publish() { assertThatCode(() -> pub.publish("test.event", Map.of("k","v"))).doesNotThrowAnyException(); }
}

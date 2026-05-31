package com.gogidix.aiservices.supplychainoptimizationservice.domain.event;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;
import java.util.*;
import java.util.UUID;

class OptimizationEventTest {
    @Test void create() {
        var e = OptimizationEvent.builder()
            .requestId(UUID.randomUUID())
            .eventType(OptimizationEventType.REQUEST_CREATED)
            .description("d").build();
        assertThat(e.getEventId()).isNotNull();
    }
    @Test void metadata() {
        var e = OptimizationEvent.builder()
            .requestId(UUID.randomUUID())
            .eventType(OptimizationEventType.COMPLETED)
            .metadata(Map.of("k","v")).build();
        assertThat(e.getMetadata()).containsEntry("k","v");
    }
    @Test void enumValues() {
        assertThat(OptimizationEventType.values()).isNotEmpty();
    }
}

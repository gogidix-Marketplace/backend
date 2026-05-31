package com.gogidix.cargo.eventdriven.shared.id;

import java.util.UUID;

public final class EventIdGenerator {
    private EventIdGenerator() {}

    public static String generate() {
        return UUID.randomUUID().toString();
    }

    public static String generateCorrelationId() {
        return "corr-" + UUID.randomUUID().toString();
    }

    public static String generateSagaId(String sagaType) {
        return "saga-" + sagaType + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
}

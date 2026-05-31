package com.gogidix.courier.assignmentservice.shared.util;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public final class IdGenerator {

    private IdGenerator() {
    }

    public static String generateId() {
        return UUID.randomUUID().toString();
    }

    public static String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }

    public static String generateTimeBasedId() {
        long timestamp = Instant.now().toEpochMilli();
        String random = UUID.randomUUID().toString().substring(0, 8);
        return timestamp + "-" + random;
    }

    public static boolean isValidUUID(String value) {
        if (value == null || value.isBlank()) {
            return false;
        }
        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}

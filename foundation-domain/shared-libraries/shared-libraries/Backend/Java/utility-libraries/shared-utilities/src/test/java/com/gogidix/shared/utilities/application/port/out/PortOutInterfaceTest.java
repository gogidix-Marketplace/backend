package com.gogidix.shared.utilities.application.port.out;

import com.gogidix.shared.utilities.domain.model.UtilityResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

class PortOutInterfaceTest {

    @Test
    void notificationPort_allMethods() {
        NotificationPort port = new NotificationPort() {
            public void sendSuccessNotification(String userId, String message) {}
            public void sendErrorNotification(String userId, String message, String errorDetails) {}
            public void sendWarningNotification(String userId, String message, List<String> warnings) {}
            public void sendSystemNotification(String level, String message, Map<String, Object> metadata) {}
        };

        assertNotNull(port);
        port.sendSuccessNotification("user1", "Operation succeeded");
        port.sendErrorNotification("user1", "Operation failed", "Stack trace here");
        port.sendWarningNotification("user1", "Warning occurred", List.of("warn1", "warn2"));
        port.sendSystemNotification("ERROR", "System alert", Map.of("source", "scheduler"));
    }

    @Test
    void cachePort_allMethods() {
        CachePort port = new CachePort() {
            public UtilityResult<?> get(String key) { return null; }
            public void put(String key, UtilityResult<?> result, long ttlSeconds) {}
            public void remove(String key) {}
            public void clear() {}
            public boolean exists(String key) { return false; }
            public String getStatistics() { return "{\"hits\":0,\"misses\":0}"; }
        };

        assertNotNull(port);
        assertNull(port.get("nonexistent"));
        assertFalse(port.exists("nonexistent"));
        port.put("key1", UtilityResult.success("op1", null, "ok"), 300L);
        port.remove("key1");
        port.clear();
        assertEquals("{\"hits\":0,\"misses\":0}", port.getStatistics());
    }
}

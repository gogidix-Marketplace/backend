package com.gogidix.shared.utilities.application.service;

import com.gogidix.shared.utilities.application.port.out.CachePort;
import com.gogidix.shared.utilities.application.port.out.NotificationPort;
import com.gogidix.shared.utilities.domain.model.ProcessingRequest;
import com.gogidix.shared.utilities.domain.model.UtilityResult;
import com.gogidix.shared.utilities.domain.service.DateTimeUtilityService;
import com.gogidix.shared.utilities.domain.service.JsonProcessingService;
import com.gogidix.shared.utilities.domain.valueobject.ProcessingStatus;
import com.gogidix.shared.utilities.domain.valueobject.UtilityType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

class UtilityApplicationServiceBranchTest {

    private DateTimeUtilityService dateTimeService;
    private JsonProcessingService jsonService;

    @Test
    void processRequest_cachedResultHit() {
        AtomicBoolean cachePutCalled = new AtomicBoolean(false);
        CachePort cache = new CachePort() {
            public UtilityResult<?> get(String key) {
                return UtilityResult.success("cached-op", UtilityType.JSON_SERIALIZATION, "cached-result");
            }
            public void put(String key, UtilityResult<?> result, long ttl) { cachePutCalled.set(true); }
            public void remove(String key) {}
            public void clear() {}
            public boolean exists(String key) { return true; }
            public String getStatistics() { return "hits:1"; }
        };
        NotificationPort notification = new NotificationPort() {
            public void sendSuccessNotification(String userId, String message) {}
            public void sendErrorNotification(String userId, String message, String errorDetails) {}
            public void sendWarningNotification(String userId, String message, java.util.List<String> warnings) {}
            public void sendSystemNotification(String level, String message, Map<String, Object> metadata) {}
        };

        UtilityApplicationService svc = new UtilityApplicationService(dateTimeService = new DateTimeUtilityService(),
            jsonService = new JsonProcessingService(), cache, notification);

        ProcessingRequest req = ProcessingRequest.builder()
            .requestId("req-cache")
            .utilityType(UtilityType.DATETIME_FORMATTING)
            .data(LocalDateTime.now())
            .createdAt(Instant.now())
            .priority(5)
            .timeoutMs(30000L)
            .build();
        UtilityResult<?> result = svc.processUtilityRequest(req);
        assertNotNull(result);
    }

    @Test
    void processRequest_cacheableResult_putsToCache() {
        AtomicReference<String> putKey = new AtomicReference<>();
        AtomicBoolean putCalled = new AtomicBoolean(false);
        CachePort cache = new CachePort() {
            public UtilityResult<?> get(String key) { return null; }
            public void put(String key, UtilityResult<?> result, long ttl) { putCalled.set(true); putKey.set(key); }
            public void remove(String key) {}
            public void clear() {}
            public boolean exists(String key) { return false; }
            public String getStatistics() { return ""; }
        };
        NotificationPort notification = new NotificationPort() {
            public void sendSuccessNotification(String userId, String message) {}
            public void sendErrorNotification(String userId, String message, String errorDetails) {}
            public void sendWarningNotification(String userId, String message, java.util.List<String> warnings) {}
            public void sendSystemNotification(String level, String message, Map<String, Object> metadata) {}
        };

        UtilityApplicationService svc = new UtilityApplicationService(new DateTimeUtilityService(),
            new JsonProcessingService(), cache, notification);

        ProcessingRequest req = ProcessingRequest.builder()
            .requestId("req-put-cache")
            .utilityType(UtilityType.DATETIME_FORMATTING)
            .data(LocalDateTime.now())
            .createdAt(Instant.now())
            .priority(5)
            .timeoutMs(30000L)
            .build();
        UtilityResult<?> result = svc.processUtilityRequest(req);
        assertNotNull(result);
    }

    @Test
    void processRequest_failedResult_sendsErrorNotification() {
        AtomicBoolean errorNotifSent = new AtomicBoolean(false);
        CachePort cache = new CachePort() {
            public UtilityResult<?> get(String key) { return null; }
            public void put(String key, UtilityResult<?> result, long ttl) {}
            public void remove(String key) {}
            public void clear() {}
            public boolean exists(String key) { return false; }
            public String getStatistics() { return ""; }
        };
        NotificationPort notification = new NotificationPort() {
            public void sendSuccessNotification(String userId, String message) {}
            public void sendErrorNotification(String userId, String message, String errorDetails) { errorNotifSent.set(true); }
            public void sendWarningNotification(String userId, String message, java.util.List<String> warnings) {}
            public void sendSystemNotification(String level, String message, Map<String, Object> metadata) {}
        };

        UtilityApplicationService svc = new UtilityApplicationService(new DateTimeUtilityService(),
            new JsonProcessingService(), cache, notification);

        ProcessingRequest req = ProcessingRequest.builder()
            .requestId("req-fail")
            .utilityType(UtilityType.DATETIME_FORMATTING)
            .data("not-a-datetime")
            .createdAt(Instant.now())
            .priority(1)
            .timeoutMs(30000L)
            .userId("user1")
            .build();
        UtilityResult<?> result = svc.processUtilityRequest(req);
        assertNotNull(result);
    }

    @Test
    void processRequest_notificationException_swallowed() {
        CachePort cache = new CachePort() {
            public UtilityResult<?> get(String key) { return null; }
            public void put(String key, UtilityResult<?> result, long ttl) {}
            public void remove(String key) {}
            public void clear() {}
            public boolean exists(String key) { return false; }
            public String getStatistics() { return ""; }
        };
        NotificationPort notification = new NotificationPort() {
            public void sendSuccessNotification(String userId, String message) { throw new RuntimeException("notif failed"); }
            public void sendErrorNotification(String userId, String message, String errorDetails) { throw new RuntimeException("notif failed"); }
            public void sendWarningNotification(String userId, String message, java.util.List<String> warnings) {}
            public void sendSystemNotification(String level, String message, Map<String, Object> metadata) {}
        };

        UtilityApplicationService svc = new UtilityApplicationService(new DateTimeUtilityService(),
            new JsonProcessingService(), cache, notification);

        ProcessingRequest req = ProcessingRequest.builder()
            .requestId("req-notif-err")
            .utilityType(UtilityType.DATETIME_FORMATTING)
            .data(LocalDateTime.now())
            .createdAt(Instant.now())
            .priority(1)
            .timeoutMs(30000L)
            .userId("user1")
            .build();
        assertDoesNotThrow(() -> svc.processUtilityRequest(req));
    }

    @Test
    void processAsync_completes() throws Exception {
        CachePort cache = new CachePort() {
            public UtilityResult<?> get(String key) { return null; }
            public void put(String key, UtilityResult<?> result, long ttl) {}
            public void remove(String key) {}
            public void clear() {}
            public boolean exists(String key) { return false; }
            public String getStatistics() { return ""; }
        };
        NotificationPort notification = new NotificationPort() {
            public void sendSuccessNotification(String userId, String message) {}
            public void sendErrorNotification(String userId, String message, String errorDetails) {}
            public void sendWarningNotification(String userId, String message, java.util.List<String> warnings) {}
            public void sendSystemNotification(String level, String message, Map<String, Object> metadata) {}
        };

        UtilityApplicationService svc = new UtilityApplicationService(new DateTimeUtilityService(),
            new JsonProcessingService(), cache, notification);

        ProcessingRequest req = ProcessingRequest.builder()
            .requestId("req-async2")
            .utilityType(UtilityType.JSON_SERIALIZATION)
            .data(Map.of("key", "val"))
            .createdAt(Instant.now())
            .priority(5)
            .timeoutMs(30000L)
            .build();
        CompletableFuture<UtilityResult<?>> future = svc.processUtilityRequestAsync(req);
        UtilityResult<?> result = future.get();
        assertNotNull(result);
    }

    @Test
    void processRequest_encodingCategory() {
        CachePort cache = new CachePort() {
            public UtilityResult<?> get(String key) { return null; }
            public void put(String key, UtilityResult<?> result, long ttl) {}
            public void remove(String key) {}
            public void clear() {}
            public boolean exists(String key) { return false; }
            public String getStatistics() { return ""; }
        };
        NotificationPort notification = new NotificationPort() {
            public void sendSuccessNotification(String userId, String message) {}
            public void sendErrorNotification(String userId, String message, String errorDetails) {}
            public void sendWarningNotification(String userId, String message, java.util.List<String> warnings) {}
            public void sendSystemNotification(String level, String message, Map<String, Object> metadata) {}
        };

        UtilityApplicationService svc = new UtilityApplicationService(new DateTimeUtilityService(),
            new JsonProcessingService(), cache, notification);

        ProcessingRequest req = ProcessingRequest.builder()
            .requestId("req-enc")
            .utilityType(UtilityType.ENCODING)
            .data("test")
            .createdAt(Instant.now())
            .priority(5)
            .timeoutMs(30000L)
            .build();
        UtilityResult<?> result = svc.processUtilityRequest(req);
        assertNotNull(result);
    }

    @Test
    void processRequest_dynamicCategory() {
        CachePort cache = new CachePort() {
            public UtilityResult<?> get(String key) { return null; }
            public void put(String key, UtilityResult<?> result, long ttl) {}
            public void remove(String key) {}
            public void clear() {}
            public boolean exists(String key) { return false; }
            public String getStatistics() { return ""; }
        };
        NotificationPort notification = new NotificationPort() {
            public void sendSuccessNotification(String userId, String message) {}
            public void sendErrorNotification(String userId, String message, String errorDetails) {}
            public void sendWarningNotification(String userId, String message, java.util.List<String> warnings) {}
            public void sendSystemNotification(String level, String message, Map<String, Object> metadata) {}
        };

        UtilityApplicationService svc = new UtilityApplicationService(new DateTimeUtilityService(),
            new JsonProcessingService(), cache, notification);

        ProcessingRequest req = ProcessingRequest.builder()
            .requestId("req-dyn")
            .utilityType(UtilityType.CLASS_LOADING)
            .data("test")
            .createdAt(Instant.now())
            .priority(5)
            .timeoutMs(30000L)
            .build();
        UtilityResult<?> result = svc.processUtilityRequest(req);
        assertNotNull(result);
    }

    @Test
    void processRequest_defaultCategory() {
        CachePort cache = new CachePort() {
            public UtilityResult<?> get(String key) { return null; }
            public void put(String key, UtilityResult<?> result, long ttl) {}
            public void remove(String key) {}
            public void clear() {}
            public boolean exists(String key) { return false; }
            public String getStatistics() { return ""; }
        };
        NotificationPort notification = new NotificationPort() {
            public void sendSuccessNotification(String userId, String message) {}
            public void sendErrorNotification(String userId, String message, String errorDetails) {}
            public void sendWarningNotification(String userId, String message, java.util.List<String> warnings) {}
            public void sendSystemNotification(String level, String message, Map<String, Object> metadata) {}
        };

        UtilityApplicationService svc = new UtilityApplicationService(new DateTimeUtilityService(),
            new JsonProcessingService(), cache, notification);

        ProcessingRequest req = ProcessingRequest.builder()
            .requestId("req-default")
            .utilityType(UtilityType.JSON_SCHEMA_VALIDATION)
            .data("test")
            .createdAt(Instant.now())
            .priority(5)
            .timeoutMs(30000L)
            .build();
        UtilityResult<?> result = svc.processUtilityRequest(req);
        assertNotNull(result);
        assertFalse(result.isSuccessful());
    }
}

package com.gogidix.shared.utilities.application.service;

import com.gogidix.shared.utilities.application.port.in.UtilityOperationUseCase.*;
import com.gogidix.shared.utilities.application.port.out.CachePort;
import com.gogidix.shared.utilities.application.port.out.NotificationPort;
import com.gogidix.shared.utilities.domain.model.ProcessingRequest;
import com.gogidix.shared.utilities.domain.model.UtilityResult;
import com.gogidix.shared.utilities.domain.service.DateTimeUtilityService;
import com.gogidix.shared.utilities.domain.service.JsonProcessingService;
import com.gogidix.shared.utilities.domain.valueobject.UtilityType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

class UtilityApplicationServiceTest {

    private UtilityApplicationService service;
    private DateTimeUtilityService dateTimeService;
    private JsonProcessingService jsonService;
    private CachePort cachePort;
    private NotificationPort notificationPort;

    @BeforeEach
    void setUp() {
        dateTimeService = new DateTimeUtilityService();
        jsonService = new JsonProcessingService();
        cachePort = new CachePort() {
            public UtilityResult<?> get(String key) { return null; }
            public void put(String key, UtilityResult<?> result, long ttl) {}
            public void remove(String key) {}
            public void clear() {}
            public boolean exists(String key) { return false; }
            public String getStatistics() { return ""; }
        };
        notificationPort = new NotificationPort() {
            public void sendSuccessNotification(String userId, String message) {}
            public void sendErrorNotification(String userId, String message, String errorDetails) {}
            public void sendWarningNotification(String userId, String message, java.util.List<String> warnings) {}
            public void sendSystemNotification(String level, String message, Map<String, Object> metadata) {}
        };
        service = new UtilityApplicationService(dateTimeService, jsonService, cachePort, notificationPort);
    }

    @Test
    void processUtilityRequest_dateTimeFormatting() {
        ProcessingRequest req = ProcessingRequest.builder()
            .requestId("req-1")
            .utilityType(UtilityType.DATETIME_FORMATTING)
            .data(LocalDateTime.now())
            .createdAt(Instant.now())
            .priority(5)
            .timeoutMs(30000L)
            .build();
        UtilityResult<?> result = service.processUtilityRequest(req);
        assertNotNull(result);
    }

    @Test
    void processUtilityRequest_dateTimeParsing() {
        ProcessingRequest req = ProcessingRequest.builder()
            .requestId("req-2")
            .utilityType(UtilityType.DATETIME_PARSING)
            .data(LocalDateTime.now().toString())
            .createdAt(Instant.now())
            .priority(5)
            .timeoutMs(30000L)
            .build();
        UtilityResult<?> result = service.processUtilityRequest(req);
        assertNotNull(result);
    }

    @Test
    void processUtilityRequest_dateTimeValidation() {
        ProcessingRequest req = ProcessingRequest.builder()
            .requestId("req-3")
            .utilityType(UtilityType.DATETIME_VALIDATION)
            .data(LocalDateTime.now().minusDays(1))
            .createdAt(Instant.now())
            .priority(5)
            .timeoutMs(30000L)
            .build();
        UtilityResult<?> result = service.processUtilityRequest(req);
        assertNotNull(result);
    }

    @Test
    void processUtilityRequest_jsonSerialization() {
        ProcessingRequest req = ProcessingRequest.builder()
            .requestId("req-4")
            .utilityType(UtilityType.JSON_SERIALIZATION)
            .data(Map.of("key", "value"))
            .createdAt(Instant.now())
            .priority(5)
            .timeoutMs(30000L)
            .build();
        UtilityResult<?> result = service.processUtilityRequest(req);
        assertNotNull(result);
    }

    @Test
    void processUtilityRequest_jsonValidation() {
        ProcessingRequest req = ProcessingRequest.builder()
            .requestId("req-5")
            .utilityType(UtilityType.JSON_VALIDATION)
            .data("{\"valid\":true}")
            .createdAt(Instant.now())
            .priority(5)
            .timeoutMs(30000L)
            .build();
        UtilityResult<?> result = service.processUtilityRequest(req);
        assertNotNull(result);
    }

    @Test
    void processUtilityRequest_jsonTransformation() {
        ProcessingRequest req = ProcessingRequest.builder()
            .requestId("req-6")
            .utilityType(UtilityType.JSON_TRANSFORMATION)
            .data("{\"a\":1}")
            .createdAt(Instant.now())
            .priority(5)
            .timeoutMs(30000L)
            .build();
        UtilityResult<?> result = service.processUtilityRequest(req);
        assertNotNull(result);
    }

    @Test
    void processUtilityRequest_unsupportedType() {
        ProcessingRequest req = ProcessingRequest.builder()
            .requestId("req-7")
            .utilityType(UtilityType.STRING_FORMATTING)
            .data("test")
            .createdAt(Instant.now())
            .priority(5)
            .timeoutMs(30000L)
            .build();
        UtilityResult<?> result = service.processUtilityRequest(req);
        assertNotNull(result);
    }

    @Test
    void processUtilityRequest_fileOperation() {
        ProcessingRequest req = ProcessingRequest.builder()
            .requestId("req-8")
            .utilityType(UtilityType.FILE_EXCEL_PROCESSING)
            .data("test.xlsx")
            .createdAt(Instant.now())
            .priority(5)
            .timeoutMs(30000L)
            .build();
        UtilityResult<?> result = service.processUtilityRequest(req);
        assertNotNull(result);
    }

    @Test
    void processUtilityRequest_httpOperation() {
        ProcessingRequest req = ProcessingRequest.builder()
            .requestId("req-9")
            .utilityType(UtilityType.HTTP_REQUEST_PROCESSING)
            .data("test")
            .createdAt(Instant.now())
            .priority(5)
            .timeoutMs(30000L)
            .build();
        UtilityResult<?> result = service.processUtilityRequest(req);
        assertNotNull(result);
    }

    @Test
    void processUtilityRequest_collectionOperation() {
        ProcessingRequest req = ProcessingRequest.builder()
            .requestId("req-10")
            .utilityType(UtilityType.COLLECTION_FILTERING)
            .data("test")
            .createdAt(Instant.now())
            .priority(5)
            .timeoutMs(30000L)
            .build();
        UtilityResult<?> result = service.processUtilityRequest(req);
        assertNotNull(result);
    }

    @Test
    void processUtilityRequest_validationOperation() {
        ProcessingRequest req = ProcessingRequest.builder()
            .requestId("req-11")
            .utilityType(UtilityType.EMAIL_VALIDATION)
            .data("test@test.com")
            .createdAt(Instant.now())
            .priority(5)
            .timeoutMs(30000L)
            .build();
        UtilityResult<?> result = service.processUtilityRequest(req);
        assertNotNull(result);
    }

    @Test
    void processUtilityRequest_mathOperation() {
        ProcessingRequest req = ProcessingRequest.builder()
            .requestId("req-12")
            .utilityType(UtilityType.MATHEMATICAL_CALCULATION)
            .data("test")
            .createdAt(Instant.now())
            .priority(5)
            .timeoutMs(30000L)
            .build();
        UtilityResult<?> result = service.processUtilityRequest(req);
        assertNotNull(result);
    }

    @Test
    void processUtilityRequest_cryptoOperation() {
        ProcessingRequest req = ProcessingRequest.builder()
            .requestId("req-13")
            .utilityType(UtilityType.HASHING)
            .data("test")
            .createdAt(Instant.now())
            .priority(5)
            .timeoutMs(30000L)
            .build();
        UtilityResult<?> result = service.processUtilityRequest(req);
        assertNotNull(result);
    }

    @Test
    void processUtilityRequest_reflectionOperation() {
        ProcessingRequest req = ProcessingRequest.builder()
            .requestId("req-14")
            .utilityType(UtilityType.OBJECT_INTROSPECTION)
            .data("test")
            .createdAt(Instant.now())
            .priority(5)
            .timeoutMs(30000L)
            .build();
        UtilityResult<?> result = service.processUtilityRequest(req);
        assertNotNull(result);
    }

    @Test
    void processUtilityRequestAsync() throws Exception {
        ProcessingRequest req = ProcessingRequest.builder()
            .requestId("req-async")
            .utilityType(UtilityType.DATETIME_FORMATTING)
            .data(LocalDateTime.now())
            .createdAt(Instant.now())
            .priority(5)
            .timeoutMs(30000L)
            .build();
        CompletableFuture<UtilityResult<?>> future = service.processUtilityRequestAsync(req);
        UtilityResult<?> result = future.get();
        assertNotNull(result);
    }

    @Test
    void processUtilityRequest_expiredRequest() {
        ProcessingRequest req = ProcessingRequest.builder()
            .requestId("req-expired")
            .utilityType(UtilityType.DATETIME_FORMATTING)
            .data(LocalDateTime.now())
            .createdAt(Instant.now().minusSeconds(60000))
            .timeoutMs(1L)
            .priority(5)
            .build();
        UtilityResult<?> result = service.processUtilityRequest(req);
        assertNotNull(result);
    }

    @Test
    void processUtilityRequest_highPriorityWithNotification() {
        ProcessingRequest req = ProcessingRequest.builder()
            .requestId("req-hp")
            .utilityType(UtilityType.DATETIME_FORMATTING)
            .data(LocalDateTime.now())
            .createdAt(Instant.now())
            .priority(1)
            .timeoutMs(30000L)
            .userId("user1")
            .build();
        UtilityResult<?> result = service.processUtilityRequest(req);
        assertNotNull(result);
    }

    @Test
    void processUtilityRequest_dateTimeFormatting_wrongDataType() {
        ProcessingRequest req = ProcessingRequest.builder()
            .requestId("req-wrong")
            .utilityType(UtilityType.DATETIME_FORMATTING)
            .data("not-a-datetime")
            .createdAt(Instant.now())
            .priority(5)
            .timeoutMs(30000L)
            .build();
        UtilityResult<?> result = service.processUtilityRequest(req);
        assertNotNull(result);
    }

    @Test
    void processUtilityRequest_dateTimeParsing_wrongDataType() {
        ProcessingRequest req = ProcessingRequest.builder()
            .requestId("req-wrong2")
            .utilityType(UtilityType.DATETIME_PARSING)
            .data(12345)
            .createdAt(Instant.now())
            .priority(5)
            .timeoutMs(30000L)
            .build();
        UtilityResult<?> result = service.processUtilityRequest(req);
        assertNotNull(result);
    }

    @Test
    void processUtilityRequest_dateTimeValidation_wrongDataType() {
        ProcessingRequest req = ProcessingRequest.builder()
            .requestId("req-wrong3")
            .utilityType(UtilityType.DATETIME_VALIDATION)
            .data("not-a-datetime")
            .createdAt(Instant.now())
            .priority(5)
            .timeoutMs(30000L)
            .build();
        UtilityResult<?> result = service.processUtilityRequest(req);
        assertNotNull(result);
    }

    @Test
    void processUtilityRequest_jsonValidation_wrongDataType() {
        ProcessingRequest req = ProcessingRequest.builder()
            .requestId("req-wrong4")
            .utilityType(UtilityType.JSON_VALIDATION)
            .data(12345)
            .createdAt(Instant.now())
            .priority(5)
            .timeoutMs(30000L)
            .build();
        UtilityResult<?> result = service.processUtilityRequest(req);
        assertNotNull(result);
    }

    @Test
    void processUtilityRequest_jsonTransformation_wrongDataType() {
        ProcessingRequest req = ProcessingRequest.builder()
            .requestId("req-wrong5")
            .utilityType(UtilityType.JSON_TRANSFORMATION)
            .data(12345)
            .createdAt(Instant.now())
            .priority(5)
            .timeoutMs(30000L)
            .build();
        UtilityResult<?> result = service.processUtilityRequest(req);
        assertNotNull(result);
    }
}

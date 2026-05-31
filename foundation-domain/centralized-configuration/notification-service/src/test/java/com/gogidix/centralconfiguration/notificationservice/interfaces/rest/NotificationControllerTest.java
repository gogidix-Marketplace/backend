package com.gogidix.centralconfiguration.notificationservice.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import com.gogidix.centralconfiguration.notificationservice.application.service.NotificationService;
import com.gogidix.centralconfiguration.notificationservice.domain.model.Notification;
import com.gogidix.centralconfiguration.notificationservice.domain.model.NotificationChannel;
import com.gogidix.centralconfiguration.notificationservice.domain.model.NotificationStatus;
import com.gogidix.centralconfiguration.notificationservice.domain.model.NotificationType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationController REST API Tests")
class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    private MockMvc mockMvc;
    private NotificationController controller;
    private ObjectMapper objectMapper;

    private static final String TENANT_ID = "tenant-001";
    private static final Long NOTIFICATION_ID = 1L;

    @BeforeEach
    void setUp() {
        controller = new NotificationController(notificationService);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(objectMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setMessageConverters(converter)
                .build();
    }

    @AfterEach
    void tearDown() {
        reset(notificationService);
    }

    @Nested
    @DisplayName("POST /api/v1/notifications")
    class SendNotificationTests {

        @Test
        @DisplayName("Should send notification successfully")
        void shouldSendNotificationSuccessfully() throws Exception {
            Notification notification = createMockNotification();
            when(notificationService.sendNotification(eq(TENANT_ID), any(), any(), any(), any(), any(), any()))
                    .thenReturn(notification);

            Map<String, Object> request = Map.of(
                    "type", "CONFIG_CREATED",
                    "channel", "EMAIL",
                    "recipient", "admin@example.com",
                    "subject", "Configuration Created",
                    "message", "A new configuration has been created",
                    "metadata", "{\"source\":\"system\"}"
            );

            mockMvc.perform(post("/api/v1/notifications")
                            .header("X-Tenant-ID", TENANT_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.recipient").value("admin@example.com"));

            verify(notificationService).sendNotification(eq(TENANT_ID), eq(NotificationType.CONFIG_CREATED),
                    eq(NotificationChannel.EMAIL), eq("admin@example.com"), eq("Configuration Created"),
                    eq("A new configuration has been created"), eq("{\"source\":\"system\"}"));
        }

        @Test
        @DisplayName("Should use default type")
        void shouldUseDefaultType() throws Exception {
            Notification notification = createMockNotification();
            when(notificationService.sendNotification(eq(TENANT_ID), eq(NotificationType.SYSTEM_ALERT),
                    any(), any(), any(), any(), any())).thenReturn(notification);

            Map<String, Object> request = Map.of(
                    "recipient", "user@example.com",
                    "subject", "Test"
            );

            mockMvc.perform(post("/api/v1/notifications")
                            .header("X-Tenant-ID", TENANT_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());

            verify(notificationService).sendNotification(eq(TENANT_ID), eq(NotificationType.SYSTEM_ALERT),
                    any(), any(), any(), any(), any());
        }

        @Test
        @DisplayName("Should use default channel")
        void shouldUseDefaultChannel() throws Exception {
            Notification notification = createMockNotification();
            when(notificationService.sendNotification(eq(TENANT_ID), any(), eq(NotificationChannel.EMAIL),
                    any(), any(), any(), any())).thenReturn(notification);

            Map<String, Object> request = Map.of(
                    "type", "SYSTEM_ALERT",
                    "recipient", "user@example.com"
            );

            mockMvc.perform(post("/api/v1/notifications")
                            .header("X-Tenant-ID", TENANT_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());

            verify(notificationService).sendNotification(eq(TENANT_ID), any(), eq(NotificationChannel.EMAIL),
                    any(), any(), any(), any());
        }

        @ParameterizedTest
        @ValueSource(strings = {"EMAIL", "WEBHOOK", "SLACK", "SMS", "TEAMS"})
        @DisplayName("Should accept all channel types")
        void shouldAcceptAllChannelTypes(String channel) throws Exception {
            Notification notification = createMockNotification();
            when(notificationService.sendNotification(eq(TENANT_ID), any(), any(), any(), any(), any(), any()))
                    .thenReturn(notification);

            Map<String, Object> request = Map.of(
                    "channel", channel,
                    "recipient", "user@example.com"
            );

            mockMvc.perform(post("/api/v1/notifications")
                            .header("X-Tenant-ID", TENANT_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should use default tenant ID")
        void shouldUseDefaultTenantId() throws Exception {
            Notification notification = createMockNotification();
            when(notificationService.sendNotification(eq("default"), any(), any(), any(), any(), any(), any()))
                    .thenReturn(notification);

            Map<String, Object> request = Map.of(
                    "recipient", "user@example.com"
            );

            mockMvc.perform(post("/api/v1/notifications")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());

            verify(notificationService).sendNotification(eq("default"), any(), any(), any(), any(), any(), any());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/notifications")
    class GetNotificationsTests {

        @Test
        @DisplayName("Should get all notifications")
        void shouldGetAllNotifications() throws Exception {
            List<Notification> notifications = List.of(
                    createMockNotification(1L, "user1@example.com"),
                    createMockNotification(2L, "user2@example.com")
            );
            when(notificationService.getNotifications(eq(TENANT_ID))).thenReturn(notifications);

            mockMvc.perform(get("/api/v1/notifications")
                            .header("X-Tenant-ID", TENANT_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[1].id").value(2));

            verify(notificationService).getNotifications(eq(TENANT_ID));
        }

        @Test
        @DisplayName("Should return empty list when no notifications")
        void shouldReturnEmptyListWhenNoNotifications() throws Exception {
            when(notificationService.getNotifications(anyString())).thenReturn(List.of());

            mockMvc.perform(get("/api/v1/notifications")
                            .header("X-Tenant-ID", TENANT_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());
        }

        @Test
        @DisplayName("Should use default tenant ID")
        void shouldUseDefaultTenantId() throws Exception {
            when(notificationService.getNotifications(eq("default"))).thenReturn(List.of());

            mockMvc.perform(get("/api/v1/notifications"))
                    .andExpect(status().isOk());

            verify(notificationService).getNotifications(eq("default"));
        }
    }

    @Nested
    @DisplayName("POST /api/v1/notifications/retry")
    class RetryFailedNotificationsTests {

        @Test
        @DisplayName("Should retry failed notifications")
        void shouldRetryFailedNotifications() throws Exception {
            doNothing().when(notificationService).retryFailedNotifications(eq(TENANT_ID));

            mockMvc.perform(post("/api/v1/notifications/retry")
                            .header("X-Tenant-ID", TENANT_ID))
                    .andExpect(status().isOk())
                    .andExpect(content().string("\"Retry initiated\""));

            verify(notificationService).retryFailedNotifications(eq(TENANT_ID));
        }

        @Test
        @DisplayName("Should use default tenant ID")
        void shouldUseDefaultTenantId() throws Exception {
            doNothing().when(notificationService).retryFailedNotifications(eq("default"));

            mockMvc.perform(post("/api/v1/notifications/retry"))
                    .andExpect(status().isOk());

            verify(notificationService).retryFailedNotifications(eq("default"));
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should handle send then retrieve workflow")
        void shouldHandleSendThenRetrieveWorkflow() throws Exception {
            Notification notification = createMockNotification();
            List<Notification> notifications = List.of(notification);

            when(notificationService.sendNotification(eq(TENANT_ID), any(), any(), any(), any(), any(), any()))
                    .thenReturn(notification);
            when(notificationService.getNotifications(eq(TENANT_ID))).thenReturn(notifications);

            Map<String, Object> sendRequest = Map.of(
                    "type", "CONFIG_CREATED",
                    "channel", "EMAIL",
                    "recipient", "admin@example.com",
                    "subject", "Test",
                    "message", "Test message"
            );

            mockMvc.perform(post("/api/v1/notifications")
                            .header("X-Tenant-ID", TENANT_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(sendRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1));

            mockMvc.perform(get("/api/v1/notifications")
                            .header("X-Tenant-ID", TENANT_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[0].id").value(1));

            verify(notificationService).sendNotification(any(), any(), any(), any(), any(), any(), any());
            verify(notificationService).getNotifications(eq(TENANT_ID));
        }

        @Test
        @DisplayName("Should handle send then retry workflow")
        void shouldHandleSendThenRetryWorkflow() throws Exception {
            Notification notification = createMockNotification();
            notification.setStatus(NotificationStatus.FAILED);

            when(notificationService.sendNotification(eq(TENANT_ID), any(), any(), any(), any(), any(), any()))
                    .thenReturn(notification);
            doNothing().when(notificationService).retryFailedNotifications(eq(TENANT_ID));

            Map<String, Object> sendRequest = Map.of(
                    "type", "SYSTEM_ALERT",
                    "channel", "WEBHOOK",
                    "recipient", "https://example.com/hook",
                    "subject", "Alert"
            );

            mockMvc.perform(post("/api/v1/notifications")
                            .header("X-Tenant-ID", TENANT_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(sendRequest)))
                    .andExpect(status().isOk());

            mockMvc.perform(post("/api/v1/notifications/retry")
                            .header("X-Tenant-ID", TENANT_ID))
                    .andExpect(status().isOk())
                    .andExpect(content().string("\"Retry initiated\""));

            verify(notificationService).sendNotification(any(), any(), any(), any(), any(), any(), any());
            verify(notificationService).retryFailedNotifications(eq(TENANT_ID));
        }
    }

    @Nested
    @DisplayName("Content Type Tests")
    class ContentTypeTests {

        @Test
        @DisplayName("Should accept application/json")
        void shouldAcceptApplicationJson() throws Exception {
            Notification notification = createMockNotification();
            when(notificationService.sendNotification(any(), any(), any(), any(), any(), any(), any()))
                    .thenReturn(notification);

            Map<String, Object> request = Map.of("recipient", "user@example.com");

            mockMvc.perform(post("/api/v1/notifications")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should return application/json")
        void shouldReturnApplicationJson() throws Exception {
            List<Notification> notifications = List.of(createMockNotification());
            when(notificationService.getNotifications(anyString())).thenReturn(notifications);

            mockMvc.perform(get("/api/v1/notifications"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle null subject")
        void shouldHandleNullSubject() throws Exception {
            Notification notification = createMockNotification();
            when(notificationService.sendNotification(any(), any(), any(), any(), isNull(), any(), isNull()))
                    .thenReturn(notification);

            Map<String, Object> request = new HashMap<>();
            request.put("recipient", "user@example.com");
            request.put("subject", null);
            request.put("message", "Test");
            request.put("metadata", null);

            mockMvc.perform(post("/api/v1/notifications")
                            .header("X-Tenant-ID", TENANT_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should handle very long recipient")
        void shouldHandleVeryLongRecipient() throws Exception {
            Notification notification = createMockNotification();
            when(notificationService.sendNotification(any(), any(), any(), any(), any(), any(), any()))
                    .thenReturn(notification);

            String longRecipient = "x".repeat(400) + "@example.com";
            Map<String, Object> request = Map.of(
                    "recipient", longRecipient,
                    "subject", "Test"
            );

            mockMvc.perform(post("/api/v1/notifications")
                            .header("X-Tenant-ID", TENANT_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should handle webhook URL recipient")
        void shouldHandleWebhookUrlRecipient() throws Exception {
            Notification notification = createMockNotification();
            when(notificationService.sendNotification(any(), any(), any(), any(), any(), any(), any()))
                    .thenReturn(notification);

            Map<String, Object> request = Map.of(
                    "channel", "WEBHOOK",
                    "recipient", "https://api.example.com/webhooks/notifications/abc123",
                    "message", "{\"event\":\"config.updated\"}"
            );

            mockMvc.perform(post("/api/v1/notifications")
                            .header("X-Tenant-ID", TENANT_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }
    }

    // Helper methods
    private Notification createMockNotification() {
        return createMockNotification(NOTIFICATION_ID, "admin@example.com");
    }

    private Notification createMockNotification(Long id, String recipient) {
        return Notification.builder()
                .id(id)
                .tenantId(TENANT_ID)
                .notificationType(NotificationType.CONFIG_CREATED)
                .channel(NotificationChannel.EMAIL)
                .recipient(recipient)
                .subject("Configuration Created")
                .message("A new configuration has been created")
                .metadata("{\"source\":\"system\"}")
                .status(NotificationStatus.SENT)
                .retryCount(0)
                .maxRetries(3)
                .sentAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}

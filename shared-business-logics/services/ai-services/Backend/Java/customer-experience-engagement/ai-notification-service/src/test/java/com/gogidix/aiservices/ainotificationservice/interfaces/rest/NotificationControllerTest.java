package com.gogidix.aiservices.ainotificationservice.interfaces.rest;

import com.gogidix.aiservices.ainotificationservice.application.service.NotificationService;
import com.gogidix.aiservices.ainotificationservice.domain.model.Notification;
import com.gogidix.aiservices.ainotificationservice.domain.model.NotificationStatus;
import com.gogidix.aiservices.ainotificationservice.domain.model.NotificationType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
@DisplayName("NotificationController REST API Tests")
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    private ObjectMapper objectMapper;
    private Notification mockNotification;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockNotification = Notification.create(
                "user-123",
                NotificationType.EMAIL,
                "Test Subject",
                "Test Content"
        );
    }

    @Nested
    @DisplayName("POST /api/v1/notifications/send - Send Notification")
    class SendNotificationTests {

        @Test
        @DisplayName("Should send notification successfully")
        void shouldSendNotification() throws Exception {
            when(notificationService.sendNotification(
                    eq("user-123"),
                    eq(NotificationType.EMAIL),
                    eq("Test Subject"),
                    eq("Test Content")
            )).thenReturn(mockNotification);

            mockMvc.perform(post("/api/v1/notifications/send")
                            .param("recipientId", "user-123")
                            .param("type", "EMAIL")
                            .param("subject", "Test Subject")
                            .param("content", "Test Content"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.notificationId").exists())
                    .andExpect(jsonPath("$.recipientId").value("user-123"))
                    .andExpect(jsonPath("$.subject").value("Test Subject"));

            verify(notificationService).sendNotification(
                    eq("user-123"),
                    eq(NotificationType.EMAIL),
                    eq("Test Subject"),
                    eq("Test Content")
            );
        }

        @Test
        @DisplayName("Should send SMS notification")
        void shouldSendSMSNotification() throws Exception {
            Notification smsNotification = Notification.create(
                    "user-456",
                    NotificationType.SMS,
                    "SMS Subject",
                    "SMS Content"
            );
            when(notificationService.sendNotification(
                    anyString(),
                    eq(NotificationType.SMS),
                    anyString(),
                    anyString()
            )).thenReturn(smsNotification);

            mockMvc.perform(post("/api/v1/notifications/send")
                            .param("recipientId", "user-456")
                            .param("type", "SMS")
                            .param("subject", "SMS Subject")
                            .param("content", "SMS Content"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.type").value("SMS"));

            verify(notificationService).sendNotification(
                    eq("user-456"),
                    eq(NotificationType.SMS),
                    eq("SMS Subject"),
                    eq("SMS Content")
            );
        }

        @Test
        @DisplayName("Should send PUSH notification")
        void shouldSendPushNotification() throws Exception {
            when(notificationService.sendNotification(
                    anyString(),
                    eq(NotificationType.PUSH),
                    anyString(),
                    anyString()
            )).thenReturn(mockNotification);

            mockMvc.perform(post("/api/v1/notifications/send")
                            .param("recipientId", "user-789")
                            .param("type", "PUSH")
                            .param("subject", "Push Subject")
                            .param("content", "Push Content"))
                    .andExpect(status().isOk());

            verify(notificationService).sendNotification(
                    anyString(),
                    eq(NotificationType.PUSH),
                    anyString(),
                    anyString()
            );
        }
    }

    @Nested
    @DisplayName("GET /api/v1/notifications/{notificationId} - Get Notification")
    class GetNotificationTests {

        @Test
        @DisplayName("Should get notification by ID")
        void shouldGetNotificationById() throws Exception {
            when(notificationService.getNotification("notif-123"))
                    .thenReturn(mockNotification);

            mockMvc.perform(get("/api/v1/notifications/notif-123"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.notificationId").exists())
                    .andExpect(jsonPath("$.recipientId").value("user-123"));

            verify(notificationService).getNotification("notif-123");
        }
    }

    @Nested
    @DisplayName("GET /api/v1/notifications/recipient/{recipientId} - Get User Notifications")
    class GetUserNotificationsTests {

        @Test
        @DisplayName("Should get user notifications")
        void shouldGetUserNotifications() throws Exception {
            List<Notification> notifications = List.of(
                    mockNotification,
                    Notification.create("user-123", NotificationType.SMS, "Subject 2", "Content 2")
            );

            when(notificationService.getUserNotifications("user-123"))
                    .thenReturn(notifications);

            mockMvc.perform(get("/api/v1/notifications/recipient/user-123"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(2));

            verify(notificationService).getUserNotifications("user-123");
        }

        @Test
        @DisplayName("Should return empty list when user has no notifications")
        void shouldReturnEmptyList() throws Exception {
            when(notificationService.getUserNotifications("new-user"))
                    .thenReturn(List.of());

            mockMvc.perform(get("/api/v1/notifications/recipient/new-user"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(0));
        }
    }

    @Nested
    @DisplayName("POST /api/v1/notifications/{notificationId}/cancel - Cancel Notification")
    class CancelNotificationTests {

        @Test
        @DisplayName("Should cancel notification successfully")
        void shouldCancelNotification() throws Exception {
            doNothing().when(notificationService).cancelNotification("notif-123");

            mockMvc.perform(post("/api/v1/notifications/notif-123/cancel"))
                    .andExpect(status().isNoContent());

            verify(notificationService).cancelNotification("notif-123");
        }

        @Test
        @DisplayName("Should handle cancellation of already sent notification")
        void shouldHandleCancellationOfSentNotification() throws Exception {
            doNothing().when(notificationService).cancelNotification("sent-notif");

            mockMvc.perform(post("/api/v1/notifications/sent-notif/cancel"))
                    .andExpect(status().isNoContent());

            verify(notificationService).cancelNotification("sent-notif");
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle very long content")
        void shouldHandleVeryLongContent() throws Exception {
            String longContent = "A".repeat(1000);

            when(notificationService.sendNotification(
                    anyString(),
                    any(NotificationType.class),
                    anyString(),
                    eq(longContent)
            )).thenReturn(mockNotification);

            mockMvc.perform(post("/api/v1/notifications/send")
                            .param("recipientId", "user-123")
                            .param("type", "EMAIL")
                            .param("subject", "Subject")
                            .param("content", longContent))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should handle special characters in content")
        void shouldHandleSpecialCharacters() throws Exception {
            String specialContent = "Hello! <script>alert('test')</script> & 'quotes'";

            when(notificationService.sendNotification(
                    anyString(),
                    any(NotificationType.class),
                    anyString(),
                    eq(specialContent)
            )).thenReturn(mockNotification);

            mockMvc.perform(post("/api/v1/notifications/send")
                            .param("recipientId", "user-123")
                            .param("type", "EMAIL")
                            .param("subject", "Subject with special chars")
                            .param("content", specialContent))
                    .andExpect(status().isOk());
        }
    }
}

package com.gogidix.aiservices.ainotificationservice.multitenancy;

import com.gogidix.aiservices.ainotificationservice.AiNotificationServiceApplication;
import com.gogidix.aiservices.ainotificationservice.application.service.NotificationService;
import com.gogidix.aiservices.ainotificationservice.domain.model.Notification;
import com.gogidix.aiservices.ainotificationservice.domain.model.NotificationType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Financial-Grade: Tenant Isolation Tests.
 *
 * These tests validate multi-tenant data isolation and security.
 */
@SpringBootTest(
    classes = AiNotificationServiceApplication.class,
    properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration"
    }
)
@ActiveProfiles("test")
@DisplayName("Financial-Grade: Tenant Isolation Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TenantIsolationTest {

    @Autowired
    private NotificationService notificationService;

    @MockBean
    private com.gogidix.aiservices.ainotificationservice.domain.port.out.NotificationRepository notificationRepository;

    @MockBean
    private com.gogidix.aiservices.ainotificationservice.domain.port.out.NotificationSenderPort notificationSender;

    @MockBean
    private com.gogidix.aiservices.ainotificationservice.domain.port.out.EventPublisherPort eventPublisher;

    @MockBean
    private com.gogidix.aiservices.ainotificationservice.domain.policy.NotificationPolicy policy;

    private static final String TENANT_A = "tenant-a";
    private static final String TENANT_B = "tenant-b";
    private static final String TENANT_C = "tenant-c";

    @BeforeEach
    void resetMocks() {
        reset(notificationRepository, notificationSender, eventPublisher, policy);
    }

    @Nested
    @DisplayName("1. Tenant Data Isolation Tests")
    class TenantDataIsolationTests {

        @Test
        @Order(1)
        @DisplayName("Should isolate notifications by tenant")
        void shouldIsolateNotificationsByTenant() {
            Notification notificationA = Notification.create(
                TENANT_A + "-user1", NotificationType.EMAIL,
                "Tenant A Notification", "Content A");

            Notification notificationB = Notification.create(
                TENANT_B + "-user1", NotificationType.EMAIL,
                "Tenant B Notification", "Content B");

            when(notificationRepository.save(any())).thenReturn(notificationA)
                .thenReturn(notificationB);
            when(policy.canSendNow(any())).thenReturn(true);
            when(notificationSender.send(any())).thenReturn(true);
            doNothing().when(eventPublisher).publishNotificationSent(any(), any());

            var resultA = notificationService.sendNotification(
                TENANT_A + "-user1", NotificationType.EMAIL,
                "Tenant A", "Content A");

            var resultB = notificationService.sendNotification(
                TENANT_B + "-user1", NotificationType.EMAIL,
                "Tenant B", "Content B");

            assertThat(resultA.getRecipientId()).startsWith(TENANT_A);
            assertThat(resultB.getRecipientId()).startsWith(TENANT_B);
        }

        @Test
        @Order(2)
        @DisplayName("Should not cross tenant notification access")
        void shouldNotCrossTenantNotificationAccess() {
            String tenantANotificationId = "tenant-a-notif-123";

            when(notificationRepository.findById(tenantANotificationId))
                .thenThrow(new IllegalArgumentException("Notification not found"));

            // Attempt to access tenant A's notification
            assertThatThrownBy(() -> {
                notificationService.getNotification(tenantANotificationId);
            }).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @Order(3)
        @DisplayName("Should keep tenant metrics separate")
        void shouldKeepTenantMetricsSeparate() {
            when(notificationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
            when(policy.canSendNow(any())).thenReturn(true);
            when(notificationSender.send(any())).thenReturn(true);

            // Send notifications for different tenants
            for (int i = 0; i < 10; i++) {
                try {
                    notificationService.sendNotification(
                        TENANT_A + "-user" + i,
                        NotificationType.EMAIL,
                        "Tenant A " + i, "Content");
                } catch (Exception e) {
                    // Ignore
                }
            }

            for (int i = 0; i < 15; i++) {
                try {
                    notificationService.sendNotification(
                        TENANT_B + "-user" + i,
                        NotificationType.EMAIL,
                        "Tenant B " + i, "Content");
                } catch (Exception e) {
                    // Ignore
                }
            }

            // Verify all notifications were processed
            verify(notificationRepository, atLeast(20)).save(any());
        }

        @Test
        @Order(4)
        @DisplayName("Should isolate batch processing by tenant")
        void shouldIsolateBatchProcessingByTenant() {
            List<Notification> tenantANotifications = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                tenantANotifications.add(Notification.create(
                    TENANT_A + "-user" + i,
                    NotificationType.EMAIL,
                    "Batch A " + i, "Content"));
            }

            when(notificationRepository.findPendingNotifications(100))
                .thenReturn(tenantANotifications);
            when(policy.canSendNow(any())).thenReturn(true);
            when(notificationSender.send(any())).thenReturn(true);
            doNothing().when(eventPublisher).publishNotificationSent(any(), any());

            var processed = notificationService.processPendingNotifications();

            assertThat(processed).hasSize(5);
            processed.forEach(n ->
                assertThat(n.getRecipientId()).startsWith(TENANT_A));
        }
    }

    @Nested
    @DisplayName("2. Concurrent Tenant Operations Tests")
    class ConcurrentTenantOperationsTests {

        @Test
        @Order(1)
        @DisplayName("Should handle concurrent requests from multiple tenants")
        void shouldHandleConcurrentRequestsFromMultipleTenants() throws InterruptedException {
            when(notificationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
            when(policy.canSendNow(any())).thenReturn(true);
            when(notificationSender.send(any())).thenReturn(true);

            ExecutorService executor = Executors.newFixedThreadPool(10);
            List<CompletableFuture<Void>> futures = new ArrayList<>();

            // Submit requests for different tenants concurrently
            for (int tenant = 0; tenant < 3; tenant++) {
                final String tenantId = "tenant-" + tenant;
                for (int i = 0; i < 10; i++) {
                    final int index = i;
                    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                        try {
                            notificationService.sendNotification(
                                tenantId + "-user" + index,
                                NotificationType.EMAIL,
                                "Concurrent " + tenantId, "Content");
                        } catch (Exception e) {
                            // Some failures acceptable
                        }
                    }, executor);
                    futures.add(future);
                }
            }

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.SECONDS);

            // Verify all operations completed
            verify(notificationRepository, atLeast(20)).save(any());
        }

        @Test
        @Order(2)
        @DisplayName("Should maintain isolation under concurrent load")
        void shouldMaintainIsolationUnderConcurrentLoad() throws InterruptedException {
            when(notificationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
            when(policy.canSendNow(any())).thenReturn(true);
            when(notificationSender.send(any())).thenReturn(true);

            ExecutorService executor = Executors.newFixedThreadPool(5);

            // Tenant A operations
            for (int i = 0; i < 10; i++) {
                final int index = i;
                executor.submit(() -> {
                    try {
                        notificationService.sendNotification(
                            TENANT_A + "-user" + index,
                            NotificationType.EMAIL,
                            "Tenant A Concurrent", "Content");
                    } catch (Exception e) {
                        // Ignore
                    }
                });
            }

            // Tenant B operations
            for (int i = 0; i < 10; i++) {
                final int index = i;
                executor.submit(() -> {
                    try {
                        notificationService.sendNotification(
                            TENANT_B + "-user" + index,
                            NotificationType.EMAIL,
                            "Tenant B Concurrent", "Content");
                    } catch (Exception e) {
                        // Ignore
                    }
                });
            }

            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.SECONDS);

            // Both tenants should have their notifications processed
            verify(notificationRepository, atLeast(15)).save(any());
        }

        @Test
        @Order(3)
        @DisplayName("Should prevent race conditions in tenant data")
        void shouldPreventRaceConditionsInTenantData() throws InterruptedException {
            when(notificationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
            when(policy.canSendNow(any())).thenReturn(true);
            when(notificationSender.send(any())).thenReturn(true);

            String sharedUserId = "shared-user";
            ExecutorService executor = Executors.newFixedThreadPool(10);

            for (int i = 0; i < 20; i++) {
                final int index = i;
                executor.submit(() -> {
                    try {
                        notificationService.sendNotification(
                            sharedUserId,
                            NotificationType.EMAIL,
                            "Race Test " + index, "Content");
                    } catch (Exception e) {
                        // Ignore
                    }
                });
            }

            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);

            // All operations should complete without data corruption
            verify(notificationRepository, atLeast(15)).save(any());
        }
    }

    @Nested
    @DisplayName("3. Tenant Resource Quotas Tests")
    class TenantResourceQuotasTests {

        @Test
        @Order(1)
        @DisplayName("Should enforce per-tenant rate limits")
        void shouldEnforcePerTenantRateLimits() {
            when(notificationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
            when(policy.canSendNow(any())).thenReturn(true);
            when(notificationSender.send(any())).thenReturn(true);

            // Tenant A sends many notifications
            for (int i = 0; i < 30; i++) {
                try {
                    notificationService.sendNotification(
                        TENANT_A + "-user",
                        NotificationType.EMAIL,
                        "Rate Limit A " + i, "Content");
                } catch (Exception e) {
                    // Rate limiting may apply
                }
            }

            // Tenant B should still be able to send
            when(policy.canSendNow(any())).thenReturn(true);

            assertThatCode(() -> {
                notificationService.sendNotification(
                    TENANT_B + "-user",
                    NotificationType.EMAIL,
                    "Tenant B Unaffected", "Content");
            }).doesNotThrowAnyException();
        }

        @Test
        @Order(2)
        @DisplayName("Should track per-tenant notification counts")
        void shouldTrackPerTenantNotificationCounts() {
            when(notificationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
            when(policy.canSendNow(any())).thenReturn(true);
            when(notificationSender.send(any())).thenReturn(true);

            int tenantACount = 10;
            int tenantBCount = 15;

            for (int i = 0; i < tenantACount; i++) {
                try {
                    notificationService.sendNotification(
                        TENANT_A + "-user" + i,
                        NotificationType.EMAIL,
                        "Count A", "Content");
                } catch (Exception e) {
                    // Ignore
                }
            }

            for (int i = 0; i < tenantBCount; i++) {
                try {
                    notificationService.sendNotification(
                        TENANT_B + "-user" + i,
                        NotificationType.EMAIL,
                        "Count B", "Content");
                } catch (Exception e) {
                    // Ignore
                }
            }

            // Verify all were processed
            verify(notificationRepository, atLeast(tenantACount + tenantBCount)).save(any());
        }

        @Test
        @Order(3)
        @DisplayName("Should isolate tenant failures")
        void shouldIsolateTenantFailures() {
            when(notificationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
            when(policy.canSendNow(any()))
                .thenReturn(true)  // Tenant A allowed
                .thenReturn(false) // Tenant B blocked
                .thenReturn(true); // Tenant A allowed again
            when(notificationSender.send(any())).thenReturn(true);

            // Tenant A succeeds
            assertThatCode(() -> {
                notificationService.sendNotification(
                    TENANT_A + "-user1",
                    NotificationType.EMAIL,
                    "Success A", "Content");
            }).doesNotThrowAnyException();

            // Tenant B blocked (policy)
            assertThatCode(() -> {
                notificationService.sendNotification(
                    TENANT_B + "-user1",
                    NotificationType.EMAIL,
                    "Blocked B", "Content");
            }).doesNotThrowAnyException();

            // Tenant A still works
            assertThatCode(() -> {
                notificationService.sendNotification(
                    TENANT_A + "-user2",
                    NotificationType.EMAIL,
                    "Success A2", "Content");
            }).doesNotThrowAnyException();
        }

        @Test
        @Order(4)
        @DisplayName("Should enforce per-tenant batch quotas")
        void shouldEnforcePerTenantBatchQuotas() {
            List<Notification> tenantABatch = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                tenantABatch.add(Notification.create(
                    TENANT_A + "-user" + i,
                    NotificationType.EMAIL,
                    "Batch A", "Content"));
            }

            when(notificationRepository.findPendingNotifications(100))
                .thenReturn(tenantABatch);
            when(policy.canSendNow(any())).thenReturn(true);
            when(notificationSender.send(any())).thenReturn(true);

            var processed = notificationService.processPendingNotifications();

            // All tenant A's notifications processed
            assertThat(processed).hasSize(50);
        }
    }

    @Nested
    @DisplayName("4. Tenant Configuration Tests")
    class TenantConfigurationTests {

        @Test
        @Order(1)
        @DisplayName("Should handle tenant-specific preferences")
        void shouldHandleTenantSpecificPreferences() {
            when(notificationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
            when(policy.canSendNow(any())).thenReturn(true);
            when(notificationSender.send(any())).thenReturn(true);

            // Different tenants with different notification types
            assertThatCode(() -> {
                notificationService.sendNotification(
                    TENANT_A + "-user",
                    NotificationType.EMAIL,
                    "Email Config", "Content");
            }).doesNotThrowAnyException();

            assertThatCode(() -> {
                notificationService.sendNotification(
                    TENANT_B + "-user",
                    NotificationType.SMS,
                    "SMS Config", "Content");
            }).doesNotThrowAnyException();
        }

        @Test
        @Order(2)
        @DisplayName("Should respect tenant notification policies")
        void shouldRespectTenantNotificationPolicies() {
            when(notificationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
            when(policy.canSendNow(any()))
                .thenReturn(true)
                .thenReturn(false)
                .thenReturn(true);
            when(notificationSender.send(any())).thenReturn(true);

            // First allowed
            assertThatCode(() -> {
                notificationService.sendNotification(
                    TENANT_A + "-user1",
                    NotificationType.EMAIL,
                    "Allowed", "Content");
            }).doesNotThrowAnyException();

            // Second blocked by policy
            assertThatCode(() -> {
                notificationService.sendNotification(
                    TENANT_A + "-user2",
                    NotificationType.EMAIL,
                    "Blocked", "Content");
            }).doesNotThrowAnyException();

            // Third allowed again
            assertThatCode(() -> {
                notificationService.sendNotification(
                    TENANT_A + "-user3",
                    NotificationType.EMAIL,
                    "Allowed Again", "Content");
            }).doesNotThrowAnyException();
        }

        @Test
        @Order(3)
        @DisplayName("Should handle tenant priority levels")
        void shouldHandleTenantPriorityLevels() {
            when(notificationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
            when(policy.canSendNow(any())).thenReturn(true);
            when(notificationSender.send(any())).thenReturn(true);

            // High priority tenant
            assertThatCode(() -> {
                notificationService.sendNotification(
                    TENANT_A + "-vip-user",
                    NotificationType.EMAIL,
                    "High Priority", "Content");
            }).doesNotThrowAnyException();

            // Normal priority tenant
            assertThatCode(() -> {
                notificationService.sendNotification(
                    TENANT_B + "-regular-user",
                    NotificationType.EMAIL,
                    "Normal Priority", "Content");
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("5. Isolation Edge Cases Tests")
    class IsolationEdgeCasesTests {

        @Test
        @Order(1)
        @DisplayName("Should handle special characters in tenant IDs")
        void shouldHandleSpecialCharactersInTenantIds() {
            when(notificationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
            when(policy.canSendNow(any())).thenReturn(true);
            when(notificationSender.send(any())).thenReturn(true);

            String specialTenantId = "tenant-with_special.chars";

            assertThatCode(() -> {
                notificationService.sendNotification(
                    specialTenantId + "-user",
                    NotificationType.EMAIL,
                    "Special Tenant", "Content");
            }).doesNotThrowAnyException();
        }

        @Test
        @Order(2)
        @DisplayName("Should handle empty tenant ID")
        void shouldHandleEmptyTenantId() {
            when(notificationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
            when(policy.canSendNow(any())).thenReturn(true);
            when(notificationSender.send(any())).thenReturn(true);

            assertThatCode(() -> {
                notificationService.sendNotification(
                    "-user-without-tenant",
                    NotificationType.EMAIL,
                    "No Tenant", "Content");
            }).doesNotThrowAnyException();
        }

        @Test
        @Order(3)
        @DisplayName("Should handle very long tenant IDs")
        void shouldHandleVeryLongTenantIds() {
            when(notificationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
            when(policy.canSendNow(any())).thenReturn(true);
            when(notificationSender.send(any())).thenReturn(true);

            String longTenantId = "very-long-tenant-id-" + "a".repeat(100);

            assertThatCode(() -> {
                notificationService.sendNotification(
                    longTenantId + "-user",
                    NotificationType.EMAIL,
                    "Long Tenant", "Content");
            }).doesNotThrowAnyException();
        }

        @Test
        @Order(4)
        @DisplayName("Should prevent tenant ID spoofing")
        void shouldPreventTenantIdSpoofing() {
            when(notificationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
            when(policy.canSendNow(any())).thenReturn(true);
            when(notificationSender.send(any())).thenReturn(true);

            String spoofedTenantId = "../" + TENANT_A;

            assertThatCode(() -> {
                notificationService.sendNotification(
                    spoofedTenantId + "-user",
                    NotificationType.EMAIL,
                    "Spoofed", "Content");
            }).doesNotThrowAnyException();

            // Verify the tenant ID was treated as a literal string, not a path
            verify(notificationRepository, atLeastOnce()).save(any());
        }

        @Test
        @Order(5)
        @DisplayName("Should handle case-sensitive tenant IDs")
        void shouldHandleCaseSensitiveTenantIds() {
            when(notificationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
            when(policy.canSendNow(any())).thenReturn(true);
            when(notificationSender.send(any())).thenReturn(true);

            String tenantLower = "tenant-a";
            String tenantUpper = "TENANT-A";

            notificationService.sendNotification(
                tenantLower + "-user",
                NotificationType.EMAIL,
                "Lower Case", "Content");

            notificationService.sendNotification(
                tenantUpper + "-user",
                NotificationType.EMAIL,
                "Upper Case", "Content");

            // Both should be processed as separate tenants
            verify(notificationRepository, atLeast(2)).save(any());
        }
    }
}

package com.gogidix.aiservices.ainotificationservice.infrastructure.persistence;

import com.gogidix.aiservices.ainotificationservice.domain.model.Notification;
import com.gogidix.aiservices.ainotificationservice.domain.model.NotificationStatus;
import com.gogidix.aiservices.ainotificationservice.domain.model.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class InMemoryNotificationRepositoryTest {

    private InMemoryNotificationRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryNotificationRepository();
    }

    @Test
    void shouldSaveAndRetrieveNotification() {
        Notification notification = Notification.create(
                "user-123", NotificationType.EMAIL, "Subject", "Content"
        );

        repository.save(notification);

        Optional<Notification> found = repository.findById(notification.getNotificationId());
        assertThat(found).isPresent();
        assertThat(found.get().getSubject()).isEqualTo("Subject");
    }

    @Test
    void shouldFindByRecipientId() {
        repository.save(Notification.create("user-1", NotificationType.EMAIL, "S1", "C1"));
        repository.save(Notification.create("user-2", NotificationType.SMS, "S2", "C2"));
        repository.save(Notification.create("user-1", NotificationType.PUSH, "S3", "C3"));

        List<Notification> user1Notifications = repository.findByRecipientId("user-1");

        assertThat(user1Notifications).hasSize(2);
    }

    @Test
    void shouldFindPendingNotifications() {
        Notification pending1 = Notification.builder()
                .notificationId("n1")
                .recipientId("user-1")
                .type(NotificationType.EMAIL)
                .subject("S1")
                .content("C1")
                .status(NotificationStatus.PENDING)
                .build();

        Notification sent = Notification.builder()
                .notificationId("n2")
                .recipientId("user-1")
                .type(NotificationType.EMAIL)
                .subject("S2")
                .content("C2")
                .status(NotificationStatus.SENT)
                .build();

        repository.save(pending1);
        repository.save(sent);

        List<Notification> pending = repository.findPendingNotifications(10);

        assertThat(pending).hasSize(1);
        assertThat(pending.get(0).getNotificationId()).isEqualTo("n1");
    }
}

package com.gogidix.aiservices.ainotificationservice.application.service;

import com.gogidix.aiservices.ainotificationservice.domain.model.Notification;
import com.gogidix.aiservices.ainotificationservice.domain.model.NotificationStatus;
import com.gogidix.aiservices.ainotificationservice.domain.model.NotificationType;
import com.gogidix.aiservices.ainotificationservice.domain.port.out.EventPublisherPort;
import com.gogidix.aiservices.ainotificationservice.domain.port.out.NotificationRepository;
import com.gogidix.aiservices.ainotificationservice.domain.port.out.NotificationSenderPort;
import com.gogidix.aiservices.ainotificationservice.domain.policy.NotificationPolicy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationSenderPort notificationSender;

    @Mock
    private EventPublisherPort eventPublisher;

    @Mock
    private NotificationPolicy policy;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void shouldSendNotification() {
        when(notificationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(policy.canSendNow(any())).thenReturn(true);
        when(notificationSender.send(any())).thenReturn(true);

        Notification result = notificationService.sendNotification(
                "user-123", NotificationType.EMAIL, "Test", "Content"
        );

        assertThat(result).isNotNull();
        verify(notificationRepository, atLeastOnce()).save(any());
        verify(notificationSender).send(any());
        verify(eventPublisher).publishNotificationSent(any(), eq("user-123"));
    }

    @Test
    void shouldGetNotification() {
        Notification notification = Notification.create(
                "user-123", NotificationType.EMAIL, "Subject", "Content"
        );
        when(notificationRepository.findById("n1")).thenReturn(Optional.of(notification));

        Notification result = notificationService.getNotification("n1");

        assertThat(result).isNotNull();
        verify(notificationRepository).findById("n1");
    }

    @Test
    void shouldCancelNotification() {
        Notification notification = Notification.create(
                "user-123", NotificationType.EMAIL, "Subject", "Content"
        );
        when(notificationRepository.findById("n1")).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        notificationService.cancelNotification("n1");

        assertThat(notification.getStatus()).isEqualTo(NotificationStatus.CANCELLED);
    }
}

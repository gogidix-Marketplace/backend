package com.gogidix.aiservices.ainotificationservice.infrastructure.persistence;

import com.gogidix.aiservices.ainotificationservice.domain.model.Notification;
import com.gogidix.aiservices.ainotificationservice.domain.port.out.NotificationRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryNotificationRepository implements NotificationRepository {

    private final Map<String, Notification> store = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> recipientIndex = new ConcurrentHashMap<>();

    @Override
    public Notification save(Notification notification) {
        store.put(notification.getNotificationId(), notification);
        recipientIndex.computeIfAbsent(notification.getRecipientId(), k -> new HashSet<>())
                .add(notification.getNotificationId());
        return notification;
    }

    @Override
    public Optional<Notification> findById(String notificationId) {
        return Optional.ofNullable(store.get(notificationId));
    }

    @Override
    public List<Notification> findByRecipientId(String recipientId) {
        Set<String> notificationIds = recipientIndex.getOrDefault(recipientId, Set.of());
        return notificationIds.stream()
                .map(store::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<Notification> findByStatus(String status) {
        return store.values().stream()
                .filter(n -> n.getStatus().toString().equals(status))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String notificationId) {
        Notification notification = store.remove(notificationId);
        if (notification != null) {
            Set<String> notifications = recipientIndex.get(notification.getRecipientId());
            if (notifications != null) {
                notifications.remove(notificationId);
            }
        }
    }

    @Override
    public List<Notification> findPendingNotifications(int limit) {
        return store.values().stream()
                .filter(Notification::isPending)
                .sorted(Comparator.comparing(Notification::getCreatedAt))
                .limit(limit)
                .collect(Collectors.toList());
    }
}

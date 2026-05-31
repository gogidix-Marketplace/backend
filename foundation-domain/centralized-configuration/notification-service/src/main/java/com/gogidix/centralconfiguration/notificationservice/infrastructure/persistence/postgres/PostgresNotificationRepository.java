package com.gogidix.centralconfiguration.notificationservice.infrastructure.persistence.postgres;

import com.gogidix.centralconfiguration.notificationservice.domain.model.Notification;
import com.gogidix.centralconfiguration.notificationservice.domain.model.NotificationStatus;
import com.gogidix.centralconfiguration.notificationservice.domain.repository.NotificationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * PostgreSQL implementation of NotificationRepository.
 */
@Repository
public class PostgresNotificationRepository implements NotificationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Notification save(Notification notification) {
        if (notification.getId() == null) {
            entityManager.persist(notification);
            return notification;
        } else {
            return entityManager.merge(notification);
        }
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Notification.class, id));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Notification> findByTenantId(String tenantId) {
        return entityManager.createQuery(
                "SELECT n FROM Notification n WHERE n.tenantId = :tenantId ORDER BY n.createdAt DESC", Notification.class)
                .setParameter("tenantId", tenantId)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Notification> findByTenantIdAndStatus(String tenantId, NotificationStatus status) {
        return entityManager.createQuery(
                "SELECT n FROM Notification n WHERE n.tenantId = :tenantId AND n.status = :status ORDER BY n.createdAt DESC", Notification.class)
                .setParameter("tenantId", tenantId)
                .setParameter("status", status)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Notification> findByRecipient(String recipient) {
        return entityManager.createQuery(
                "SELECT n FROM Notification n WHERE n.recipient = :recipient ORDER BY n.createdAt DESC", Notification.class)
                .setParameter("recipient", recipient)
                .getResultList();
    }

    @Override
    public void delete(Notification notification) {
        if (entityManager.contains(notification)) {
            entityManager.remove(notification);
        } else {
            entityManager.remove(entityManager.merge(notification));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Notification> findAll(int page, int size) {
        return entityManager.createQuery(
                "SELECT n FROM Notification n ORDER BY n.createdAt DESC", Notification.class)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }
}

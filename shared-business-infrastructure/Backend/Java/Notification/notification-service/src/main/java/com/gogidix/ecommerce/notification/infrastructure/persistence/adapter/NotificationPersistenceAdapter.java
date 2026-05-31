package com.gogidix.ecommerce.notification.infrastructure.persistence.adapter;

import com.gogidix.ecommerce.notification.domain.model.Notification;
import com.gogidix.ecommerce.notification.domain.port.out.NotificationRepositoryPort;
import com.gogidix.ecommerce.notification.infrastructure.persistence.repository.NotificationMongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class NotificationPersistenceAdapter implements NotificationRepositoryPort {

    private final NotificationMongoRepository mongoRepository;

    public NotificationPersistenceAdapter(NotificationMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Notification save(Notification entity) { return mongoRepository.save(entity); }

    @Override
    public Optional<Notification> findById(String id) { return mongoRepository.findById(id); }

    @Override
    public List<Notification> findByTenantIdAndIsActive(String tenantId, Boolean isActive) {
        return mongoRepository.findByTenantIdAndIsActive(tenantId, isActive);
    }

    @Override
    public void deleteById(String id) { mongoRepository.deleteById(id); }
}

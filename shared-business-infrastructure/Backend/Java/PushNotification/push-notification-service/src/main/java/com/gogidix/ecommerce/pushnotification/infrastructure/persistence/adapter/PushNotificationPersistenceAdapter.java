package com.gogidix.ecommerce.pushnotification.infrastructure.persistence.adapter;

import com.gogidix.ecommerce.pushnotification.domain.model.PushNotification;
import com.gogidix.ecommerce.pushnotification.domain.port.out.PushNotificationRepositoryPort;
import com.gogidix.ecommerce.pushnotification.infrastructure.persistence.repository.PushNotificationMongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PushNotificationPersistenceAdapter implements PushNotificationRepositoryPort {

    private final PushNotificationMongoRepository mongoRepository;

    public PushNotificationPersistenceAdapter(PushNotificationMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public PushNotification save(PushNotification entity) {
        return mongoRepository.save(entity);
    }

    @Override
    public Optional<PushNotification> findByIdAndTenantId(String id, String tenantId) {
        return mongoRepository.findByIdAndTenantId(id, tenantId);
    }

    @Override
    public Page<PushNotification> findByTenantId(String tenantId, Pageable pageable) {
        return mongoRepository.findByTenantId(tenantId, pageable);
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }
}
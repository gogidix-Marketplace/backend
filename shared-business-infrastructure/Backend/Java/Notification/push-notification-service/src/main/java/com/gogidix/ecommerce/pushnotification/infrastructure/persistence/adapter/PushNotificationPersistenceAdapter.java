package com.gogidix.ecommerce.pushnotification.infrastructure.persistence.adapter;

import com.gogidix.ecommerce.pushnotification.domain.model.PushNotification;
import com.gogidix.ecommerce.pushnotification.domain.port.out.PushNotificationRepositoryPort;
import com.gogidix.ecommerce.pushnotification.infrastructure.persistence.repository.PushNotificationMongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PushNotificationPersistenceAdapter implements PushNotificationRepositoryPort {

    private final PushNotificationMongoRepository mongoRepository;

    public PushNotificationPersistenceAdapter(PushNotificationMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public PushNotification save(PushNotification entity) { return mongoRepository.save(entity); }

    @Override
    public Optional<PushNotification> findById(String id) { return mongoRepository.findById(id); }

    @Override
    public List<PushNotification> findByTenantIdAndIsActive(String tenantId, Boolean isActive) {
        return mongoRepository.findByTenantIdAndIsActive(tenantId, isActive);
    }

    @Override
    public void deleteById(String id) { mongoRepository.deleteById(id); }
}

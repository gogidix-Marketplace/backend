package com.gogidix.ecommerce.sms.infrastructure.persistence.adapter;

import com.gogidix.ecommerce.sms.domain.model.Sms;
import com.gogidix.ecommerce.sms.domain.port.out.SmsRepositoryPort;
import com.gogidix.ecommerce.sms.infrastructure.persistence.repository.SmsMongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SmsPersistenceAdapter implements SmsRepositoryPort {

    private final SmsMongoRepository mongoRepository;

    public SmsPersistenceAdapter(SmsMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Sms save(Sms entity) { return mongoRepository.save(entity); }

    @Override
    public Optional<Sms> findById(String id) { return mongoRepository.findById(id); }

    @Override
    public List<Sms> findByTenantIdAndIsActive(String tenantId, Boolean isActive) {
        return mongoRepository.findByTenantIdAndIsActive(tenantId, isActive);
    }

    @Override
    public void deleteById(String id) { mongoRepository.deleteById(id); }
}

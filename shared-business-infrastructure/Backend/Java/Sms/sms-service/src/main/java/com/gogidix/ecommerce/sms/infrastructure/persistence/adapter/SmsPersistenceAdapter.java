package com.gogidix.ecommerce.sms.infrastructure.persistence.adapter;

import com.gogidix.ecommerce.sms.domain.model.Sms;
import com.gogidix.ecommerce.sms.domain.port.out.SmsRepositoryPort;
import com.gogidix.ecommerce.sms.infrastructure.persistence.repository.SmsMongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SmsPersistenceAdapter implements SmsRepositoryPort {

    private final SmsMongoRepository mongoRepository;

    public SmsPersistenceAdapter(SmsMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Sms save(Sms entity) {
        return mongoRepository.save(entity);
    }

    @Override
    public Optional<Sms> findByIdAndTenantId(String id, String tenantId) {
        return mongoRepository.findByIdAndTenantId(id, tenantId);
    }

    @Override
    public Page<Sms> findByTenantId(String tenantId, Pageable pageable) {
        return mongoRepository.findByTenantId(tenantId, pageable);
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }
}
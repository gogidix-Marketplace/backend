package com.gogidix.ecommerce.email.infrastructure.persistence.adapter;

import com.gogidix.ecommerce.email.domain.model.Email;
import com.gogidix.ecommerce.email.domain.port.out.EmailRepositoryPort;
import com.gogidix.ecommerce.email.infrastructure.persistence.repository.EmailMongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EmailPersistenceAdapter implements EmailRepositoryPort {

    private final EmailMongoRepository mongoRepository;

    public EmailPersistenceAdapter(EmailMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Email save(Email entity) {
        return mongoRepository.save(entity);
    }

    @Override
    public Optional<Email> findByIdAndTenantId(String id, String tenantId) {
        return mongoRepository.findByIdAndTenantId(id, tenantId);
    }

    @Override
    public Page<Email> findByTenantId(String tenantId, Pageable pageable) {
        return mongoRepository.findByTenantId(tenantId, pageable);
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }
}
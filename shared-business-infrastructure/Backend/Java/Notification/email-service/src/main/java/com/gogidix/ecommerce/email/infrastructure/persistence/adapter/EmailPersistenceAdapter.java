package com.gogidix.ecommerce.email.infrastructure.persistence.adapter;

import com.gogidix.ecommerce.email.domain.model.Email;
import com.gogidix.ecommerce.email.domain.port.out.EmailRepositoryPort;
import com.gogidix.ecommerce.email.infrastructure.persistence.repository.EmailMongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EmailPersistenceAdapter implements EmailRepositoryPort {

    private final EmailMongoRepository mongoRepository;

    public EmailPersistenceAdapter(EmailMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Email save(Email entity) { return mongoRepository.save(entity); }

    @Override
    public Optional<Email> findById(String id) { return mongoRepository.findById(id); }

    @Override
    public List<Email> findByTenantIdAndIsActive(String tenantId, Boolean isActive) {
        return mongoRepository.findByTenantIdAndIsActive(tenantId, isActive);
    }

    @Override
    public void deleteById(String id) { mongoRepository.deleteById(id); }
}

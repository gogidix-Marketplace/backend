package com.gogidix.ecommerce.communication.infrastructure.persistence.adapter;

import com.gogidix.ecommerce.communication.domain.model.Communication;
import com.gogidix.ecommerce.communication.domain.port.out.CommunicationRepositoryPort;
import com.gogidix.ecommerce.communication.infrastructure.persistence.repository.CommunicationMongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CommunicationPersistenceAdapter implements CommunicationRepositoryPort {

    private final CommunicationMongoRepository mongoRepository;

    public CommunicationPersistenceAdapter(CommunicationMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Communication save(Communication entity) { return mongoRepository.save(entity); }

    @Override
    public Optional<Communication> findById(String id) { return mongoRepository.findById(id); }

    @Override
    public List<Communication> findByTenantIdAndIsActive(String tenantId, Boolean isActive) {
        return mongoRepository.findByTenantIdAndIsActive(tenantId, isActive);
    }

    @Override
    public void deleteById(String id) { mongoRepository.deleteById(id); }
}

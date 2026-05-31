package com.gogidix.ecommerce.airfreight.infrastructure.persistence.adapter;

import com.gogidix.ecommerce.airfreight.domain.model.AirFreight;
import com.gogidix.ecommerce.airfreight.domain.port.out.AirFreightRepositoryPort;
import com.gogidix.ecommerce.airfreight.infrastructure.persistence.repository.AirFreightMongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AirFreightPersistenceAdapter implements AirFreightRepositoryPort {

    private final AirFreightMongoRepository mongoRepository;

    public AirFreightPersistenceAdapter(AirFreightMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public AirFreight save(AirFreight entity) { return mongoRepository.save(entity); }

    @Override
    public Optional<AirFreight> findById(String id) { return mongoRepository.findById(id); }

    @Override
    public List<AirFreight> findByTenantIdAndIsActive(String tenantId, Boolean isActive) {
        return mongoRepository.findByTenantIdAndIsActive(tenantId, isActive);
    }

    @Override
    public void deleteById(String id) { mongoRepository.deleteById(id); }
}

package com.gogidix.ecommerce.oceanshipping.infrastructure.persistence.adapter;

import com.gogidix.ecommerce.oceanshipping.domain.model.OceanShipping;
import com.gogidix.ecommerce.oceanshipping.domain.port.out.OceanShippingRepositoryPort;
import com.gogidix.ecommerce.oceanshipping.infrastructure.persistence.repository.OceanShippingMongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OceanShippingPersistenceAdapter implements OceanShippingRepositoryPort {

    private final OceanShippingMongoRepository mongoRepository;

    public OceanShippingPersistenceAdapter(OceanShippingMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public OceanShipping save(OceanShipping entity) { return mongoRepository.save(entity); }

    @Override
    public Optional<OceanShipping> findById(String id) { return mongoRepository.findById(id); }

    @Override
    public List<OceanShipping> findByTenantIdAndIsActive(String tenantId, Boolean isActive) {
        return mongoRepository.findByTenantIdAndIsActive(tenantId, isActive);
    }

    @Override
    public void deleteById(String id) { mongoRepository.deleteById(id); }
}
